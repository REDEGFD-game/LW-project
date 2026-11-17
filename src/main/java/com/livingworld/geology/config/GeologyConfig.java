package com.livingworld.geology.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GeologyConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.DoubleValue EROSION_RATE;
    public static final ForgeConfigSpec.DoubleValue COLLAPSE_THRESHOLD;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.comment("B6 Geology").push("geology");
        EROSION_RATE = b.comment("Скорость эрозии").defineInRange("erosionRate", 1.0, 0.0, 10.0);
        COLLAPSE_THRESHOLD = b.comment("Порог обвала (стабильность ниже этого значения вызывает оползень)").defineInRange("collapseThreshold", 0.2, 0.0, 1.0);
        b.pop();
        SPEC = b.build();
    }
}