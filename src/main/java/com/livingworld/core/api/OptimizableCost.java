package com.livingworld.core.api;

/**
 * Заявка модуля на бюджет CPU/память.
 * Сторона: both.
 */
public class OptimizableCost {
    /** 0 = критичный, 100 = необязательный. */
    public final int priority;
    /** Ожидаемое время тика (мс) при L0. */
    public final double expectedMs;

    public OptimizableCost(int priority, double expectedMs) {
        this.priority = priority;
        this.expectedMs = expectedMs;
    }
}