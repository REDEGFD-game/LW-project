package com.livingworld.demographics;

/**
 * Параметры модели. Все значения можно будет вынести в конфиг позже.
 * Основано на стандартных демографических показателях (CBR/CDR) [citation:3].
 */
public final class DemographicsConfig {

    // Базовые ставки на 1000 человек в год (игровой год = 24000 тиков)
    public static final double BASE_BIRTH_RATE = 35.0;   // базовый CBR
    public static final double BASE_DEATH_RATE = 15.0;   // базовый CDR

    // Влияние факторов (множители)
    public static final double FOOD_FACTOR_WEIGHT = 0.4;   // нехватка еды повышает смертность
    public static final double HOUSING_FACTOR_WEIGHT = 0.3; // нехватка жилья снижает рождаемость
    public static final double SECURITY_FACTOR_WEIGHT = 0.2; // безопасность (рейды) влияет на смертность
    public static final double MEDICAL_FACTOR_WEIGHT = 0.1;  // медицина снижает смертность

    // Миграция
    public static final double MIGRATION_THRESHOLD = 0.7; // если жильё заполнено >70%, миграция замедляется
    public static final int MIGRATION_MIN_POP = 20;       // минимальное население для миграции

    private DemographicsConfig() {}
}