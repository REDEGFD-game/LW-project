package com.livingworld.core.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

/**
 * Поставщик климата (температура/влажность в точке).
 * Сторона: server.
 */
public interface IClimateProvider {
    /** Температура в °C. */
    double getTemperature(ServerLevel level, BlockPos pos);

    /** Влажность 0..1. */
    double getHumidity(ServerLevel level, BlockPos pos);
}