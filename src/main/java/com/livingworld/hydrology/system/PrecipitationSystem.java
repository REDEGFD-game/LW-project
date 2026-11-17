package com.livingworld.hydrology.system;

import com.livingworld.hydrology.config.HydrologyConfig;
import com.livingworld.hydrology.data.HydrologyWorldData;
import com.livingworld.timeevents.calendar.Calendar;
import com.livingworld.timeevents.data.TimeEventsData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class PrecipitationSystem {

    public static void tick(ServerLevel level, HydrologyWorldData data) {
        double multiplier = HydrologyConfig.RAIN_MULTIPLIER.get();

        TimeEventsData timeData = level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(TimeEventsData::create, TimeEventsData::load, null),
                TimeEventsData.DATA_NAME
        );
        Calendar.Season season = timeData.getCalendar().getSeason();
        float seasonFactor = 1.0f;
        if (season == Calendar.Season.SUMMER) seasonFactor = 0.5f;
        else if (season == Calendar.Season.WINTER) seasonFactor = 1.5f;

        for (var entry : data.humidity.entrySet()) {
            long key = entry.getKey();
            float hum = entry.getValue();

            float rain = (float) Math.max(0, (hum - 0.5) * multiplier * 2.0 * seasonFactor);
            rain = Math.min(1f, rain);

            data.rainfall.put(key, rain);
        }
        data.setDirty();
    }
}