package com.livingworld.thermodynamics.optim;

import com.livingworld.core.api.IOptimizable;
import com.livingworld.core.api.OptimizableCost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Реализация IOptimizable для B2.
 * Сторона: both.
 */
public class ThermodynamicsOptimizable implements IOptimizable {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B2");

    private int currentLod = LEVEL_FULL;
    private int currentTickRate = 20; // 1 сек

    @Override
    public String getModuleId() {
        return "lw_thermodynamics";
    }

    @Override
    public int getCurrentLodLevel() {
        return currentLod;
    }

    @Override
    public void setLodLevel(int level) {
        this.currentLod = Math.min(level, getMaxDegradationLevel());
        switch (currentLod) {
            case LEVEL_FULL        -> currentTickRate = 20;
            case LEVEL_SIMPLIFIED  -> currentTickRate = 40;
            case LEVEL_STATISTICAL -> currentTickRate = 100;
            case LEVEL_AGGREGATED  -> currentTickRate = Integer.MAX_VALUE;
            default                -> currentTickRate = 20;
        }
        LOGGER.info("[LW-B2-DEBUG] setLodLevel({}) → tickRate={}", currentLod, currentTickRate);
    }

    @Override
    public OptimizableCost getCost() {
        return new OptimizableCost(40, 0.8);
    }

    public int getCurrentTickRate() {
        return currentTickRate;
    }
}