package com.livingworld.core.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Реестр модулей для деградации под слабое железо.
 * Сторона: both.
 * <p>
 * Работает с любым {@link IOptimizable}: и с LODLevel/LODManager (SmartNPC),
 * и с HydrologyOptimizable (B1).
 */
public class DynamicOptimizer {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-Optimizer");
    private static final List<IOptimizable> MODULES = new ArrayList<>();

    public static void register(IOptimizable module) {
        MODULES.add(module);
        LOGGER.info("[LW-DEBUG] Optimizer: зарегистрирован {} (priority={})",
                module.getModuleId(), module.getCost().priority);
    }

    public static void applyGlobalQuality(int level) {
        for (IOptimizable m : MODULES) {
            int applied = Math.min(level, m.getMaxDegradationLevel());
            m.setLodLevel(applied);
            LOGGER.info("[LW-DEBUG] {} → L{}", m.getModuleId(), applied);
        }
    }

    public static List<IOptimizable> getModules() {
        return MODULES;
    }
}