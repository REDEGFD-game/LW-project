package com.livingworld.weatherextremes.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Конфиг B5 (Экстремальная погода).
 * Сторона: common.
 */
public class WeatherConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.DoubleValue STORM_CHANCE;
    public static final ForgeConfigSpec.DoubleValue LIGHTNING_CHANCE;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.comment("B5 Weather Extremes").push("weather");

        STORM_CHANCE = b
                .comment("Шанс шторма (0..1)")
                .defineInRange("stormChance", 0.1, 0.0, 1.0);

        LIGHTNING_CHANCE = b
                .comment("Шанс молнии во время шторма")
                .defineInRange("lightningChance", 0.05, 0.0, 1.0);

        b.pop();
        SPEC = b.build();
    }
}