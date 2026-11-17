package com.livingworld.hydrology.system;

import com.livingworld.hydrology.config.HydrologyConfig;
import com.livingworld.hydrology.data.HydrologyWorldData;
import com.livingworld.timeevents.calendar.Calendar;
import com.livingworld.timeevents.data.TimeEventsData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;

public class ClimateGrid {

    private static final int RADIUS_CHUNKS = 8;

    public static void tick(ServerLevel level, HydrologyWorldData data) {
        // Читаем сезон
        TimeEventsData timeData = level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(TimeEventsData::create, TimeEventsData::load, null),
                TimeEventsData.DATA_NAME
        );
        Calendar.Season season = timeData.getCalendar().getSeason();

        float seasonOffset = switch (season) {
            case SPRING -> 5f;
            case SUMMER -> 15f;
            case AUTUMN -> 0f;
            case WINTER -> -15f;
        };

        level.players().forEach(player -> {
            int pcx = player.blockPosition().getX() >> 4;
            int pcz = player.blockPosition().getZ() >> 4;

            for (int dx = -RADIUS_CHUNKS; dx <= RADIUS_CHUNKS; dx++) {
                for (int dz = -RADIUS_CHUNKS; dz <= RADIUS_CHUNKS; dz++) {
                    int cx = pcx + dx;
                    int cz = pcz + dz;
                    long key = HydrologyWorldData.chunkKey(cx, cz);

                    int blockX = (cx << 4) + 8;
                    int blockZ = (cz << 4) + 8;

                    // Высота поверхности
                    int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, blockX, blockZ);

                    // Биом
                    var biomeHolder = level.getBiome(new net.minecraft.core.BlockPos(blockX, surfaceY, blockZ));
                    Biome biome = biomeHolder.value();

                    // === ФИЗИЧЕСКАЯ МОДЕЛЬ ТЕМПЕРАТУРЫ ===
                    // 1. Базовая температура (конфиг)
                    double baseTemp = HydrologyConfig.TEMPERATURE_BASE.get(); // 15°C
                    // 2. Поправка биома
                    float biomeTemp = biome.getBaseTemperature(); // 0.0..2.0
                    double biomeOffset = (biomeTemp - 0.8) * 20.0;
                    // 3. Градиент высоты: -6.5°C на 1000 м (0.0065°C на блок)
                    double altitudeOffset = -(surfaceY - 63) * 0.0065;
                    // 4. Сезон
                    double temp = baseTemp + biomeOffset + altitudeOffset + seasonOffset;

                    // Влажность: базовая + поправка биома
                    double baseHum = HydrologyConfig.HUMIDITY_BASE.get();
                    float hum = (float) (biome.hasPrecipitation() ? baseHum : baseHum * 0.3);

                    data.temperature.put(key, (float) temp);
                    data.humidity.put(key, Math.max(0f, Math.min(1f, hum)));
                }
            }
        });

        data.setDirty();
    }
}