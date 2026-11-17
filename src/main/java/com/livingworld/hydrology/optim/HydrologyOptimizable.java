package com.livingworld.hydrology.optim;

import com.livingworld.core.api.IOptimizable;
import com.livingworld.core.api.OptimizableCost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Реализация IOptimizable для B1.
 * Сторона: both.
 */
public class HydrologyOptimizable implements IOptimizable {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B1");

    private int currentLod = LEVEL_FULL;
    private int currentTickRate = 100; // 5 сек

    @Override
    public String getModuleId() {
        return "lw_hydrology";
    }

    @Override
    public int getCurrentLodLevel() {
        return currentLod;
    }

    @Override
    public void setLodLevel(int level) {
        this.currentLod = Math.min(level, getMaxDegradationLevel());
        switch (currentLod) {
            case LEVEL_FULL        -> currentTickRate = 100;
            case LEVEL_SIMPLIFIED  -> currentTickRate = 200;
            case LEVEL_STATISTICAL -> currentTickRate = 500;
            case LEVEL_AGGREGATED  -> currentTickRate = Integer.MAX_VALUE;
            default                -> currentTickRate = 100;
        }
        LOGGER.info("[LW-B1-DEBUG] setLodLevel({}) → tickRate={}", currentLod, currentTickRate);
    }

    @Override
    public int getMaxDegradationLevel() {
        return LEVEL_AGGREGATED;
    }

    @Override
    public OptimizableCost getCost() {
        return new OptimizableCost(30, 1.5);
    }

    public int getCurrentTickRate() {
        return currentTickRate;
    }
}