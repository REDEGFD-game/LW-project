package com.livingworld.lightphysics.system;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Тени от источников света.
 * Сторона: client (реальный рендер в Части 2).
 */
public class ShadowSystem {

    /**
     * Заглушка для теней.
     * Реальный рендер теней требует миксинов в LightTexture.
     */
    public static float getShadowFactor(Level level, BlockPos pos) {
        // Пока просто возвращаем 1.0 (нет тени)
        return 1.0f;
    }
}