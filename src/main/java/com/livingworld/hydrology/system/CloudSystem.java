package com.livingworld.hydrology.system;

import com.livingworld.hydrology.data.HydrologyWorldData;
import net.minecraft.server.level.ServerLevel;

/**
 * Система облаков B1.
 * Сторона: server.
 * Облака движутся по ветру и переносят влажность.
 */
public class CloudSystem {

    public static void tick(ServerLevel level, HydrologyWorldData data) {
        // Простая модель: влажность плавно «перетекает» из более влажных чанков в менее влажные.
        // Настоящая модель с облаками будет в следующих итерациях.
        // Сейчас просто сглаживаем влажность, чтобы не было резких скачков.
        for (var entry : data.humidity.entrySet()) {
            long key = entry.getKey();
            float hum = entry.getValue();
            int cx = HydrologyWorldData.keyX(key);
            int cz = HydrologyWorldData.keyZ(key);

            // Соседи по +X
            long eastKey = HydrologyWorldData.chunkKey(cx + 1, cz);
            Float eastHum = data.humidity.get(eastKey);

            if (eastHum != null) {
                float diff = (hum - eastHum) * 0.05f;
                data.humidity.put(key, Math.max(0f, Math.min(1f, hum - diff)));
                data.humidity.put(eastKey, Math.max(0f, Math.min(1f, eastHum + diff)));
            }
        }
    }
}