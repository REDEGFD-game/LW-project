package com.livingworld.settlements;

import com.livingworld.settlements.data.Settlement;
import com.livingworld.settlements.data.SettlementWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SettlementSpawner {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-Settlements");
    private static final List<BlockPos> manualCities = new ArrayList<>();
    private static final List<BlockPos> manualVillages = new ArrayList<>();

    public static void addCity(int x, int z) {
        manualCities.add(new BlockPos(x, 0, z));
        LOGGER.info("[LW-DEBUG] Добавлена точка города: X={}, Z={}", x, z);
    }

    public static void addVillage(int x, int z) {
        manualVillages.add(new BlockPos(x, 0, z));
        LOGGER.info("[LW-DEBUG] Добавлена точка деревни: X={}, Z={}", x, z);
    }

    public static void placeAllManual(ServerLevel level) {
        SettlementWorldData setData = level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(SettlementWorldData::create, SettlementWorldData::load, null),
                SettlementWorldData.DATA_NAME
        );

        if (!setData.all().isEmpty()) {
            LOGGER.info("[LW-DEBUG] Поселения уже существуют, пропускаем");
            return;
        }

        Random random = new Random(level.getSeed());
        int count = 0;

        for (int i = 0; i < manualCities.size(); i++) {
            BlockPos raw = manualCities.get(i);
            // Привязка к сетке чанков: округление вниз до кратных 16
            int chunkX = (raw.getX() >> 4) << 4;
            int chunkZ = (raw.getZ() >> 4) << 4;
            int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, chunkX, chunkZ);
            if (y < 0) {
                LOGGER.warn("[LW-DEBUG] Город {}: Y<0, чанк не загружен, пропуск", raw);
                continue;
            }
            BlockPos pos = new BlockPos(chunkX, y, chunkZ);
            String name = (i == 0) ? "Capital" : "City_" + i;
            Settlement s = setData.getOrCreate(name + "@" + pos.asLong(), pos, name);
            s.population = 500 + random.nextInt(300);
            s.housingCapacity = s.population + 100;
            s.foodReserve = s.population * 2;
            s.budget = 500;
            s.stage = Settlement.Stage.CITY;
            SettlementBuilder.placeStructure(level, pos, "village_house");
            LOGGER.info("[LW-DEBUG] Создан город {} в {}", name, pos);
            count++;
        }

        for (int i = 0; i < manualVillages.size(); i++) {
            BlockPos raw = manualVillages.get(i);
            int chunkX = (raw.getX() >> 4) << 4;
            int chunkZ = (raw.getZ() >> 4) << 4;
            int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, chunkX, chunkZ);
            if (y < 0) {
                LOGGER.warn("[LW-DEBUG] Деревня {}: Y<0, чанк не загружен, пропуск", raw);
                continue;
            }
            BlockPos pos = new BlockPos(chunkX, y, chunkZ);
            String name = "Village_" + i;
            Settlement s = setData.getOrCreate(name + "@" + pos.asLong(), pos, name);
            s.population = 50 + random.nextInt(50);
            s.housingCapacity = s.population + 20;
            s.foodReserve = s.population * 2;
            s.budget = 100;
            s.stage = Settlement.Stage.VILLAGE;
            SettlementBuilder.placeStructure(level, pos, "village_house");
            LOGGER.info("[LW-DEBUG] Создана деревня {} в {}", name, pos);
            count++;
        }

        setData.setDirty();
        LOGGER.info("[LW-DEBUG] Создано поселений из ручных точек: {}", count);
    }
}