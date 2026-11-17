package com.livingworld.core.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Интерфейс жидкостной сети.
 * Сторона: both.
 */
public interface IFluidNetwork {
    /** Получить объём жидкости в сети (в мБ). */
    int getFluidAmount();

    /** Получить давление в сети (в кПа). */
    double getPressure();

    /** Добавить жидкость в сеть. */
    void addFluid(int amount);

    /** Забрать жидкость из сети. */
    int drainFluid(int amount);

    /** Проверить, есть ли связь с другой сетью. */
    boolean isConnected(Level level, BlockPos pos);
}