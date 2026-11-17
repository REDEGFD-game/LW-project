package com.livingworld.core.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IHeatSource {
    /** Текущая температура в Кельвинах. */
    double getTemperatureKelvin();

    /** Тепловая мощность в ваттах (W). */
    double getThermalPowerWatts();

    /** Позиция в мире. */
    BlockPos getPosition();

    /** Измерение. */
    Level getLevel();
}