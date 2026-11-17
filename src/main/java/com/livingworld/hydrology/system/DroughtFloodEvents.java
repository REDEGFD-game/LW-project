package com.livingworld.hydrology.system;

import com.livingworld.hydrology.data.HydrologyWorldData;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * События засух и наводнений B1.
 * Сторона: server.
 */
public class DroughtFloodEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B1");

    private static final float DROUGHT_HUMIDITY = 0.15f;
    private static final float FLOOD_HUMIDITY   = 0.9f;

    private static int logCooldown = 0;

    public static void tick(ServerLevel level, HydrologyWorldData data) {
        if (logCooldown > 0) {
            logCooldown--;
            return;
        }

        int droughtCount = 0;
        int floodCount = 0;

        for (var entry : data.humidity.entrySet()) {
            float hum = entry.getValue();
            if (hum < DROUGHT_HUMIDITY) droughtCount++;
            else if (hum > FLOOD_HUMIDITY) floodCount++;
        }

        if (droughtCount > 0 || floodCount > 0) {
            LOGGER.info("[LW-B1-DEBUG] Засух: {}, наводнений: {}", droughtCount, floodCount);
            logCooldown = 40; // не спамить в лог
        }
    }
}