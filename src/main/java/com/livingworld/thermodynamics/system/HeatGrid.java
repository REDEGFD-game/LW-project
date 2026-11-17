package com.livingworld.thermodynamics.system;

import com.livingworld.hydrology.data.HydrologyWorldData;
import com.livingworld.thermodynamics.config.ThermodynamicsConfig;
import com.livingworld.thermodynamics.data.ThermodynamicsWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Сетка тепла B2.
 * Сторона: server.
 * Читает климат из B1, распространяет тепло по блокам.
 */
public class HeatGrid {

    private static final int RADIUS_CHUNKS = 4; // 64 блока вокруг игрока

    public static void tick(ServerLevel level, ThermodynamicsWorldData data) {
        double ambient = ThermodynamicsConfig.AMBIENT_TEMP.get();
        double transferRate = ThermodynamicsConfig.HEAT_TRANSFER_RATE.get();

        // Получаем климат из B1
        HydrologyWorldData hydro = level.getDataStorage().computeIfAbsent(
                new net.minecraft.world.level.saveddata.SavedData.Factory<>(
                        HydrologyWorldData::create, HydrologyWorldData::load, null),
                HydrologyWorldData.DATA_NAME
        );

        level.players().forEach(player -> {
            BlockPos pPos = player.blockPosition();
            int pcx = pPos.getX() >> 4;
            int pcz = pPos.getZ() >> 4;

            for (int dx = -RADIUS_CHUNKS; dx <= RADIUS_CHUNKS; dx++) {
                for (int dz = -RADIUS_CHUNKS; dz <= RADIUS_CHUNKS; dz++) {
                    int cx = pcx + dx;
                    int cz = pcz + dz;
                    long key = ThermodynamicsWorldData.chunkKey(cx, cz);

                    // 1. Базовая температура = температура воздуха из B1 (или ambient)
                    float airTemp = hydro.temperature.getOrDefault(
                            HydrologyWorldData.chunkKey(cx, cz),
                            (float) ambient
                    );

                    float currentTemp = data.temperature.getOrDefault(key, airTemp);

                    // 2. Теплообмен с воздухом (температура стремится к воздуху)
                    currentTemp += (airTemp - currentTemp) * transferRate;

                    // 3. Проверка на лёд/воду (фазовые переходы)
                    if (currentTemp > 0f) {
                        BlockPos center = new BlockPos((cx << 4) + 8, 64, (cz << 4) + 8);
                        Block block = level.getBlockState(center).getBlock();
                        
                        // Если рядом лёд и температура > 0 — тает
                        if (block == Blocks.ICE) {
                            level.setBlockAndUpdate(center, Blocks.WATER.defaultBlockState());
                        }
                    } else if (currentTemp < -5f) {
                        BlockPos center = new BlockPos((cx << 4) + 8, 64, (cz << 4) + 8);
                        Block block = level.getBlockState(center).getBlock();
                        if (block == Blocks.WATER) {
                            level.setBlockAndUpdate(center, Blocks.ICE.defaultBlockState());
                        }
                    }

                    data.temperature.put(key, currentTemp);
                }
            }
        });

        data.setDirty();
    }
}