package com.livingworld.thermodynamics.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Конфиг B2.
 * Сторона: common.
 */
public class ThermodynamicsConfig {

    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.DoubleValue AMBIENT_TEMP;
    public static final ForgeConfigSpec.DoubleValue HEAT_TRANSFER_RATE;
    public static final ForgeConfigSpec.IntValue TICK_RATE;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.comment("B2 Thermodynamics — параметры симуляции тепла").push("thermodynamics");

        AMBIENT_TEMP = b
                .comment("Базовая температура воздуха (°C)")
                .defineInRange("ambientTemp", 15.0, -50.0, 60.0);

        HEAT_TRANSFER_RATE = b
                .comment("Скорость передачи тепла между блоками (0..1)")
                .defineInRange("heatTransferRate", 0.1, 0.0, 1.0);

        TICK_RATE = b
                .comment("Частота тика симуляции (в тиках Minecraft)")
                .defineInRange("tickRate", 20, 1, 200); // 1 секунда

        b.pop();
        SPEC = b.build();
    }
}