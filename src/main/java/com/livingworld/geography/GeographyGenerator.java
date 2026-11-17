package com.livingworld.geography;

import com.livingworld.geography.country.CountryBorderGenerator;
import com.livingworld.geography.data.GeographyWorldData;
import com.livingworld.geography.river.RiverGenerator;
import com.livingworld.geography.road.RoadGraph;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

/**
 * Оркестратор генерации LW-Geography.
 * Вызывается один раз при первом запуске мира.
 */
public class GeographyGenerator {

    public static final int WORLD_SIZE = 10000;
    public static final int CELL_SIZE = 1000;
    public static final int COUNTRY_COUNT = 6;
    public static final int RIVER_COUNT = 12;
    public static final int TRADE_ROUTES = 4;

    public static GeographyWorldData generateIfNeeded(ServerLevel overworld, GeographyWorldData data) {
        if (data.isGenerated()) return data;

        long seed = overworld.getSeed();
        Random random = new Random(seed);

        // 1. Страны
        CountryBorderGenerator cg = new CountryBorderGenerator(WORLD_SIZE, CELL_SIZE, COUNTRY_COUNT);
        cg.generate(random);

        // 2. Дороги: узлы — центры стран (для простоты), плюс случайные точки
        RoadGraph rg = new RoadGraph();
        for (CountryBorderGenerator.Country c : cg.getCountries()) {
            rg.addNode(new RoadGraph.Node(c.id, c.seedX, c.seedZ));
        }
        rg.build(TRADE_ROUTES, random);

        // 3. Реки: используем простой heightmap на основе шума
        RiverGenerator rgen = new RiverGenerator(WORLD_SIZE, 50);
        rgen.generate(random, RIVER_COUNT, (x, z) -> {
            // Простой шум высоты (детерминированный по координатам)
            double n1 = Math.sin(x * 0.001 + seed * 0.0001) * 0.5 + 0.5;
            double n2 = Math.cos(z * 0.001 + seed * 0.0002) * 0.5 + 0.5;
            return (n1 + n2) / 2.0;
        });

        // 4. Сохраняем в data
        data.setFromGenerators(cg, rg, rgen);
        data.setGenerated(true);

        return data;
    }
}