package com.livingworld.geology.system;

import com.livingworld.geology.data.GeologyWorldData;
import com.livingworld.hydrology.data.HydrologyWorldData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class ErosionSystem {
    public static void tick(ServerLevel level, GeologyWorldData geoData) {
        HydrologyWorldData hydro = level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(HydrologyWorldData::create, HydrologyWorldData::load, null),
                HydrologyWorldData.DATA_NAME
        );

        // Проходим по чанкам, где есть вода (реки) или сильный дождь
        for (var entry : hydro.rainfall.entrySet()) {
            long key = entry.getKey();
            float rain = entry.getValue();
            
            if (rain > 0.5f) { // Если сильный дождь
                float currentStability = geoData.stability.getOrDefault(key, 1.0f);
                // Эрозия снижает стабильность
                float newStability = Math.max(0.0f, currentStability - (rain * 0.001f));
                geoData.stability.put(key, newStability);
            }
        }
        geoData.setDirty();
    }
}