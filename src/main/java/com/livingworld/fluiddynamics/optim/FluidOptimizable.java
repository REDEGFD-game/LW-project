package com.livingworld.fluiddynamics.optim;

import com.livingworld.core.api.IOptimizable;
import com.livingworld.core.api.OptimizableCost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FluidOptimizable implements IOptimizable {
    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B4");
    private int currentLod = LEVEL_FULL;
    private int currentTickRate = 10;

    @Override public String getModuleId() { return "lw_fluid_dynamics"; }
    @Override public int getCurrentLodLevel() { return currentLod; }

    @Override
    public void setLodLevel(int level) {
        this.currentLod = Math.min(level, getMaxDegradationLevel());
        switch (currentLod) {
            case LEVEL_FULL        -> currentTickRate = 10;
            case LEVEL_SIMPLIFIED  -> currentTickRate = 20;
            case LEVEL_STATISTICAL -> currentTickRate = 50;
            case LEVEL_AGGREGATED  -> currentTickRate = Integer.MAX_VALUE;
            default                -> currentTickRate = 10;
        }
        LOGGER.info("[LW-B4-DEBUG] setLodLevel({}) → tickRate={}", currentLod, currentTickRate);
    }

    @Override public OptimizableCost getCost() { return new OptimizableCost(35, 1.2); }
    public int getCurrentTickRate() { return currentTickRate; }
}