package com.livingworld.smartnpc.common.lod;

import com.livingworld.core.api.IOptimizable;

/**
 * Уровни детализации NPC.
 * L0 — полная симуляция (GOAP, память, отношения)
 * L1 — упрощённая (только базовые цели)
 * L2 — статистическая (обновление раз в N тиков)
 * L3 — агрегированная (только число, без сущности)
 */
public enum LODLevel implements IOptimizable {
    L0_FULL(0), L1_SIMPLIFIED(1), L2_STATISTICAL(2), L3_AGGREGATED(3);

    private final int level;
    LODLevel(int level) { this.level = level; }

    @Override public int getCurrentLodLevel() { return level; }
    @Override public void setLodLevel(int level) { /* enum immutable */ }
}