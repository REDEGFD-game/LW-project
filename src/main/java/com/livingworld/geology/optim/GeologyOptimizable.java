package com.livingworld.geology.optim;

import com.livingworld.core.api.IOptimizable;
import com.livingworld.core.api.OptimizableCost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeologyOptimizable implements IOptimizable {
    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B6");
    private int currentLod = LEVEL_FULL;
    private int currentTickRate = 200; // 10 сек

    @Override public String getModuleId() { return "lw_geology"; }
    @Override public int getCurrentLodLevel() { return currentLod; }

    @Override
    public void setLodLevel(int level) {
        this.currentLod = Math.min(level, getMaxDegradationLevel());
        switch (currentLod) {
            case LEVEL_FULL        -> currentTickRate = 200;
            case LEVEL_SIMPLIFIED  -> currentTickRate = 400;
            case LEVEL_STATISTICAL -> currentTickRate = 1000;
            case LEVEL_AGGREGATED  -> currentTickRate = Integer.MAX_VALUE;
            default                -> currentTickRate = 200;
        }
        LOGGER.info("[LW-B6-DEBUG] setLodLevel({}) → tickRate={}", currentLod, currentTickRate);
    }

    @Override public OptimizableCost getCost() { return new OptimizableCost(60, 0.5); } // Низкий приоритет
    public int getCurrentTickRate() { return currentTickRate; }
}