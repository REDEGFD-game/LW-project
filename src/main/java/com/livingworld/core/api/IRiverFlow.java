package com.livingworld.core.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

/**
 * Расход воды в реке (м³/с).
 * Сторона: server.
 */
public interface IRiverFlow {
    double getFlow(ServerLevel level, BlockPos pos);
}