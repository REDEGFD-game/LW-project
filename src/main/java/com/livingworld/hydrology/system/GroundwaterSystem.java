package com.livingworld.hydrology.system;

import com.livingworld.hydrology.data.HydrologyWorldData;
import net.minecraft.server.level.ServerLevel;

/**
 * Система грунтовых вод B1.
 * Сторона: server.
 * Грунтовые воды накапливаются от осадков и постепенно убывают.
 */
public class GroundwaterSystem {

    private static final float INFILTRATION = 0.1f;   // доля осадков, уходящих в землю
    private static final float DRAIN = 0.01f;         // естественный сток

    public static void tick(ServerLevel level, HydrologyWorldData data) {
        for (var entry : data.rainfall.entrySet()) {
            long key = entry.getKey();
            float rain = entry.getValue();
            float current = data.groundwater.getOrDefault(key, 0.3f); // стартовое значение

            float next = current + rain * INFILTRATION - DRAIN;
            data.groundwater.put(key, Math.max(0f, Math.min(1f, next)));
        }
        data.setDirty();
    }
}