package com.livingworld.geography.country;

import java.util.*;

/**
 * Генерация границ стран на основе seed.
 * Мир 10000×10000 разбивается на регионы (грубая сетка),
 * каждый регион получает "семя страны", затем выполняется
 * распространение влияния (Voronoi-подобное) с учётом шума.
 * Не зависит от API Minecraft.
 */
public class CountryBorderGenerator {

    public static class Country {
        public final int id;
        public final int seedX, seedZ; // центр влияния
        public final String name;

        public Country(int id, int seedX, int seedZ, String name) {
            this.id = id;
            this.seedX = seedX;
            this.seedZ = seedZ;
            this.name = name;
        }
    }

    private final int worldSize;
    private final int cellSize;
    private final int countryCount;
    private final List<Country> countries = new ArrayList<>();
    private int[][] grid; // grid[x][z] = id страны

    public CountryBorderGenerator(int worldSize, int cellSize, int countryCount) {
        this.worldSize = worldSize;
        this.cellSize = cellSize;
        this.countryCount = countryCount;
    }

    /**
     * Генерация: размещение семян стран и распространение влияния.
     * @param random seed для воспроизводимости
     */
    public void generate(Random random) {
        int cells = worldSize / cellSize;
        grid = new int[cells][cells];

        // 1. Размещение семян стран (случайные ячейки, без дублирования)
        Set<Integer> used = new HashSet<>();
        for (int i = 0; i < countryCount; i++) {
            int cx, cz;
            do {
                cx = random.nextInt(cells);
                cz = random.nextInt(cells);
            } while (!used.add(cx * cells + cz));

            int worldX = cx * cellSize + cellSize / 2;
            int worldZ = cz * cellSize + cellSize / 2;
            countries.add(new Country(i, worldX, worldZ, "Country_" + i));
        }

        // 2. Voronoi: каждая ячейка принадлежит ближайшему семени
        // с небольшим шумом для естественности границ
        for (int x = 0; x < cells; x++) {
            for (int z = 0; z < cells; z++) {
                double wx = x * cellSize + cellSize / 2.0;
                double wz = z * cellSize + cellSize / 2.0;

                // Шум для смещения границ
                double noiseX = (random.nextDouble() - 0.5) * cellSize * 0.8;
                double noiseZ = (random.nextDouble() - 0.5) * cellSize * 0.8;
                wx += noiseX;
                wz += noiseZ;

                int bestId = -1;
                double bestDist = Double.MAX_VALUE;
                for (Country c : countries) {
                    double dx = wx - c.seedX;
                    double dz = wz - c.seedZ;
                    double d = dx * dx + dz * dz;
                    if (d < bestDist) {
                        bestDist = d;
                        bestId = c.id;
                    }
                }
                grid[x][z] = bestId;
            }
        }
    }

    public List<Country> getCountries() { return Collections.unmodifiableList(countries); }

    /** ID страны в мировых координатах, или -1 если вне мира. */
    public int getCountryAt(int worldX, int worldZ) {
        int cells = worldSize / cellSize;
        int cx = worldX / cellSize;
        int cz = worldZ / cellSize;
        if (cx < 0 || cx >= cells || cz < 0 || cz >= cells) return -1;
        return grid[cx][cz];
    }

    /** Для отладки: текстовое представление сетки. */
    public String describeGrid() {
        StringBuilder sb = new StringBuilder();
        int cells = worldSize / cellSize;
        for (int z = 0; z < cells; z++) {
            for (int x = 0; x < cells; x++) {
                sb.append(String.format("%2d ", grid[x][z]));
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}