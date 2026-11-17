package com.livingworld.lightphysics.system;

import com.livingworld.core.api.ILightSource;
import com.livingworld.lightphysics.registry.LightSourceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Распространение света с учётом препятствий.
 * Сторона: both.
 */
public class LightPropagation {

    /**
     * Рассчитать суммарную освещённость в точке от всех источников света.
     */
    public static float getLightAt(Level level, BlockPos pos) {
        float totalLight = 0;

        for (ILightSource source : LightSourceRegistry.getSources()) {
            double distance = Math.sqrt(source.getLightPosition().distSqr(pos));
            if (distance > source.getLightRadius()) continue;

            // Затухание по расстоянию
            float attenuation = (float) (1.0 - distance / source.getLightRadius());

            // Проверяем блоки между источником и точкой
            int steps = (int) distance;
            if (steps > 0) {
                for (int i = 0; i <= steps; i++) {
                    double t = (double) i / steps;
                    int x = (int) (source.getLightPosition().getX() + (pos.getX() - source.getLightPosition().getX()) * t);
                    int y = (int) (source.getLightPosition().getY() + (pos.getY() - source.getLightPosition().getY()) * t);
                    int z = (int) (source.getLightPosition().getZ() + (pos.getZ() - source.getLightPosition().getZ()) * t);
                    BlockState state = level.getBlockState(new BlockPos(x, y, z));

                    if (!state.isAir() && state.isSolid()) {
                        attenuation *= 0.5f; // Непрозрачный блок режет свет вдвое
                        if (attenuation < 0.01f) break;
                    }
                }
            }

            totalLight += attenuation * source.getLightLevel();
        }

        return Math.min(totalLight, 15.0f);
    }
}