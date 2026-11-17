package com.livingworld.acoustics.optim;

import com.livingworld.core.api.IOptimizable;
import com.livingworld.core.api.OptimizableCost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcousticsOptimizable implements IOptimizable {
    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B7");
    private int currentLod = LEVEL_FULL;

    @Override public String getModuleId() { return "lw_acoustics"; }
    @Override public int getCurrentLodLevel() { return currentLod; }

    @Override
    public void setLodLevel(int level) {
        this.currentLod = Math.min(level, getMaxDegradationLevel());
        LOGGER.info("[LW-B7-DEBUG] setLodLevel({})", currentLod);
    }

    @Override public OptimizableCost getCost() { return new OptimizableCost(70, 0.2); } // Низкий приоритет
}