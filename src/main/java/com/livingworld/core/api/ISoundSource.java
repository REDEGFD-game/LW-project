package com.livingworld.core.api;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Источник звука (взрыв, машина, NPC).
 * Сторона: both.
 */
public interface ISoundSource {
    BlockPos getPosition();
    ResourceLocation getSoundId();
    float getVolume();
    float getPitch();
}