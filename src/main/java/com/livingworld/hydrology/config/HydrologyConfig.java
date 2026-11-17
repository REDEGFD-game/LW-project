package com.livingworld.hydrology.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Конфиг B1.
 * Сторона: common.
 */
public class HydrologyConfig {

    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.DoubleValue TEMPERATURE_BASE;
    public static final ForgeConfigSpec.DoubleValue HUMIDITY_BASE;
    public static final ForgeConfigSpec.DoubleValue RAIN_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue RIVER_FLOW_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue TICK_RATE;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.comment("B1 Hydrology — параметры симуляции").push("hydrology");

        TEMPERATURE_BASE = b
                .comment("Базовая температура (°C)")
                .defineInRange("temperatureBase", 15.0, -50.0, 60.0);

        HUMIDITY_BASE = b
                .comment("Базовая влажность (0..1)")
                .defineInRange("humidityBase", 0.5, 0.0, 1.0);

        RAIN_MULTIPLIER = b
                .comment("Множитель осадков")
                .defineInRange("rainMultiplier", 1.0, 0.0, 10.0);

        RIVER_FLOW_MULTIPLIER = b
                .comment("Множитель расхода рек")
                .defineInRange("riverFlowMultiplier", 1.0, 0.0, 10.0);

        TICK_RATE = b
                .comment("Частота тика симуляции (в тиках Minecraft)")
                .defineInRange("tickRate", 100, 20, 1200);

        b.pop();
        SPEC = b.build();
    }
}