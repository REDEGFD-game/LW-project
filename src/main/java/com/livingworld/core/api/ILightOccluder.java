package com.livingworld.core.api;

import net.minecraft.core.BlockPos;

/**
 * Блок, который блокирует свет (непрозрачный).
 * Сторона: both.
 */
public interface ILightOccluder {
    float getOpacity();  // 0 = прозрачный, 1 = полностью блокирует
}