package com.livingworld.core.api;

import net.minecraft.core.BlockPos;

/**
 * Объект, который реагирует на освещённость (NPC, урожай).
 * Сторона: both.
 */
public interface ILightReceiver {
    BlockPos getReceiverPosition();
    void onLightChanged(float newLightLevel);
}