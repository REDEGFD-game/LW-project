package com.livingworld.mechanical.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Конфиг B3.
 * Сторона: common.
 */
public class MechanicalConfig {

    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.DoubleValue FRICTION;
    public static final ForgeConfigSpec.IntValue TICK_RATE;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.comment("B3 Mechanical Power").push("mechanical");

        FRICTION = b
                .comment("Потери энергии на трение (0..1)")
                .defineInRange("friction", 0.05, 0.0, 1.0);

        TICK_RATE = b
                .comment("Частота тика симуляции (в тиках)")
                .defineInRange("tickRate", 1, 1, 20); // каждый тик

        b.pop();
        SPEC = b.build();
    }
}