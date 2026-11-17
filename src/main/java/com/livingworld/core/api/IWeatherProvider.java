package com.livingworld.core.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface IWeatherProvider {
    /** Текущая погода в точке (0 = ясно, 1 = шторм). */
    float getStormIntensity(ServerLevel level, BlockPos pos);
}