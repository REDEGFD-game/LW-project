package com.livingworld.core.api;

import net.minecraft.core.BlockPos;

/**
 * Источник света (факел, лампа, костёр).
 * Сторона: both.
 */
public interface ILightSource {
    BlockPos getLightPosition();
    int getLightLevel();       // 0..15
    int getLightColor();       // 0xRRGGBB
    double getLightRadius();   // в блоках
}