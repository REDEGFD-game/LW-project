package com.livingworld.core.api;

import net.minecraft.core.BlockPos;

/**
 * Слушатель звука (игрок, NPC).
 * Сторона: both.
 */
public interface ISoundListener {
    BlockPos getListenerPosition();
    float getHearingRange();
}