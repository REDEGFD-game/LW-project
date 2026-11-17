package com.livingworld.lightphysics.optim;

import com.livingworld.core.api.IOptimizable;
import com.livingworld.core.api.OptimizableCost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightOptimizable implements IOptimizable {
    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B8");
    private int currentLod = LEVEL_FULL;

    @Override public String getModuleId() { return "lw_light_physics"; }
    @Override public int getCurrentLodLevel() { return currentLod; }

    @Override
    public void setLodLevel(int level) {
        this.currentLod = Math.min(level, getMaxDegradationLevel());
        LOGGER.info("[LW-B8-DEBUG] setLodLevel({})", currentLod);
    }

    @Override public OptimizableCost getCost() { return new OptimizableCost(80, 0.1); } // Самый низкий приоритет
}