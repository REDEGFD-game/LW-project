package com.livingworld.hydrology.system;

import com.livingworld.hydrology.config.HydrologyConfig;
import com.livingworld.hydrology.data.HydrologyWorldData;
import net.minecraft.server.level.ServerLevel;

/**
 * Система рек B1.
 * Сторона: server.
 * Расход реки = суммарные осадки по чанку * множитель.
 */
public class RiverNetwork {

    public static void tick(ServerLevel level, HydrologyWorldData data) {
        double multiplier = HydrologyConfig.RIVER_FLOW_MULTIPLIER.get();

        for (var entry : data.rainfall.entrySet()) {
            long key = entry.getKey();
            float rain = entry.getValue();

            // Пока простая модель: расход = осадки * множитель
            float flow = (float) (rain * multiplier * 10.0);
            data.riverFlow.put(key, flow);
        }
        data.setDirty();
    }
}