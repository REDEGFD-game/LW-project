package com.livingworld.acoustics.system;

import com.livingworld.acoustics.registry.AcousticMaterialRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Распространение звука с учётом материалов.
 * Сторона: both.
 */
public class SoundPropagation {

    /**
     * Вычисляет, слышен ли звук из source в listener.
     * Учитывает расстояние и поглощение блоками.
     */
    public static boolean canHear(Level level, BlockPos source, BlockPos listener, float baseVolume, float hearingRange) {
        double distance = Math.sqrt(source.distSqr(listener));
        if (distance > hearingRange) return false;

        // Затухание по расстоянию
        float attenuation = (float) (1.0 - distance / hearingRange);

        // Проверяем блоки между источником и слушателем
        int steps = (int) distance;
        if (steps > 0) {
            for (int i = 0; i <= steps; i++) {
                double t = (double) i / steps;
                int x = (int) (source.getX() + (listener.getX() - source.getX()) * t);
                int y = (int) (source.getY() + (listener.getY() - source.getY()) * t);
                int z = (int) (source.getZ() + (listener.getZ() - source.getZ()) * t);
                BlockState state = level.getBlockState(new BlockPos(x, y, z));
                
                if (!state.isAir()) {
                    var mat = AcousticMaterialRegistry.getMaterial(state.getBlock());
                    attenuation *= (1.0f - mat.absorption);
                    if (attenuation <= 0.01f) return false;
                }
            }
        }

        return attenuation * baseVolume > 0.01f;
    }
}