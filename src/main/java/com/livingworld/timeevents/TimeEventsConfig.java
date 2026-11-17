package com.livingworld.timeevents;

/**
 * Параметры событий. Пороги и вероятности можно вынести в конфиг позже.
 */
public final class TimeEventsConfig {

    // Ярмарка: раз в сезон (осень) при достаточном населении
    public static final int FAIR_MIN_POPULATION = 80;
    public static final int FAIR_SEASON = 2; // осень

    // Эпидемия: вероятность на сезон при перенаселённости
    public static final double PLAGUE_BASE_CHANCE = 0.05;
    public static final double PLAGUE_OVERCROWDING_FACTOR = 0.15;

    // Засуха: вероятность летом, влияет на еду
    public static final double DROUGHT_BASE_CHANCE = 0.10;
    public static final int DROUGHT_FOOD_LOSS_PERCENT = 30;

    // Набег: вероятность при низкой безопасности
    public static final double RAID_BASE_CHANCE = 0.08;
    public static final double RAID_LOW_SECURITY_FACTOR = 0.20;

    // Длительность эффектов (в днях)
    public static final int PLAGUE_DURATION_DAYS = 10;
    public static final int DROUGHT_DURATION_DAYS = 15;
    public static final int RAID_DURATION_DAYS = 3;

    private TimeEventsConfig() {}
}