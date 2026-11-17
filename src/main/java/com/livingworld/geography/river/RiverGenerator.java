package com.livingworld.geography.river;

import java.util.*;

/**
 * Генерация рек на основе seed.
 * Реки текут от горных регионов (высокий шум) к морю, используя
 * алгоритм "спуска по градиенту" с добавлением шума для извилистости.
 * Не зависит от API Minecraft.
 */
public class RiverGenerator {

    public static class River {
        public final List<int[]> points; // [x, z]
        public final int width;

        public River(List<int[]> points, int width) {
            this.points = points;
            this.width = width;
        }
    }

    private final int worldSize;
    private final int stepSize;
    private final List<River> rivers = new ArrayList<>();

    public RiverGenerator(int worldSize, int stepSize) {
        this.worldSize = worldSize;
        this.stepSize = stepSize;
    }

    /**
     * Генерация рек.
     * @param random seed
     * @param riverCount количество рек
     * @param heightMap функция высоты: (x, z) -> высота (0..1)
     */
    public void generate(Random random, int riverCount,
                         java.util.function.BiFunction<Integer, Integer, Double> heightMap) {
        rivers.clear();

        for (int i = 0; i < riverCount; i++) {
            // Старт: случайная точка в "горах" (высота > 0.7)
            int startX = 0, startZ = 0;
            boolean found = false;
            for (int attempt = 0; attempt < 100; attempt++) {
                int x = random.nextInt(worldSize);
                int z = random.nextInt(worldSize);
                if (heightMap.apply(x, z) > 0.7) {
                    startX = x;
                    startZ = z;
                    found = true;
                    break;
                }
            }
            if (!found) continue;

            List<int[]> points = new ArrayList<>();
            int cx = startX, cz = startZ;
            points.add(new int[]{cx, cz});

            // Спуск по градиенту
            for (int step = 0; step < 500; step++) {
                double h = heightMap.apply(cx, cz);
                if (h < 0.2) break; // достигли моря/низменности

                // Направление: наименьшая высота среди соседей
                int bestX = cx, bestZ = cz;
                double bestH = h;
                for (int dx = -stepSize; dx <= stepSize; dx += stepSize) {
                    for (int dz = -stepSize; dz <= stepSize; dz += stepSize) {
                        if (dx == 0 && dz == 0) continue;
                        int nx = cx + dx, nz = cz + dz;
                        if (nx < 0 || nx >= worldSize || nz < 0 || nz >= worldSize) continue;
                        double nh = heightMap.apply(nx, nz);
                        // Добавляем шум для извилистости
                        nh += (random.nextDouble() - 0.5) * 0.05;
                        if (nh < bestH) {
                            bestH = nh;
                            bestX = nx;
                            bestZ = nz;
                        }
                    }
                }

                if (bestX == cx && bestZ == cz) break; // локальный минимум
                cx = bestX;
                cz = bestZ;
                points.add(new int[]{cx, cz});
            }

            if (points.size() > 3) {
                rivers.add(new River(points, 1 + random.nextInt(3)));
            }
        }
    }

    public List<River> getRivers() { return Collections.unmodifiableList(rivers); }
}