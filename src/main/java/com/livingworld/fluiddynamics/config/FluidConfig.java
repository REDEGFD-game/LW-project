package com.livingworld.fluiddynamics.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class FluidConfig {

    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.DoubleValue BASE_PRESSURE;
    public static final ForgeConfigSpec.IntValue TICK_RATE;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.comment("B4 Fluid Dynamics").push("fluid");

        BASE_PRESSURE = b.comment("Базовое давление в трубах (кПа)").defineInRange("basePressure", 100.0, 0.0, 10000.0);
        TICK_RATE = b.comment("Частота тика (в тиках)").defineInRange("tickRate", 10, 1, 100); // 0.5 сек

        b.pop();
        SPEC = b.build();
    }
}