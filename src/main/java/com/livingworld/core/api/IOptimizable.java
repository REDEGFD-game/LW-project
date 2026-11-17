package com.livingworld.core.api;

/**
 * Интерфейс для LOD-оптимизации SmartNPC и других систем.
 * Уровни: L0 полный / L1 упрощённый / L2 статистический / L3 агрегированный.
 */
public interface IOptimizable {
    int LEVEL_FULL = 0;
    int LEVEL_SIMPLIFIED = 1;
    int LEVEL_STATISTICAL = 2;
    int LEVEL_AGGREGATED = 3;

    int getCurrentLodLevel();

    void setLodLevel(int level);

    default boolean isFullySimulated() {
        return getCurrentLodLevel() == LEVEL_FULL;
    }

    // --- Опциональные методы для Dynamic Optimizer (не ломают LODLevel/LODManager) ---

    /** Уникальный ID модуля. По умолчанию — имя класса. */
    default String getModuleId() {
        return getClass().getSimpleName();
    }

    /** Максимальный уровень деградации. По умолчанию L3. */
    default int getMaxDegradationLevel() {
        return LEVEL_AGGREGATED;
    }

    /** Заявка на бюджет. По умолчанию — средний приоритет, 1 мс. */
    default OptimizableCost getCost() {
        return new OptimizableCost(50, 1.0);
    }
}