package com.livingworld.core.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

/**
 * Поставщик осадков.
 * Сторона: server.
 */
public interface IRainfallProvider {
    /** Интенсивность осадков 0..1 в точке. */
    double getRainfall(ServerLevel level, BlockPos pos);
}