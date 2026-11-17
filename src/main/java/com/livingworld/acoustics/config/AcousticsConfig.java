package com.livingworld.acoustics.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AcousticsConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.DoubleValue DEFAULT_HEARING_RANGE;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.comment("B7 Acoustics").push("acoustics");
        DEFAULT_HEARING_RANGE = b.comment("Базовая дальность слуха (в блоках)").defineInRange("hearingRange", 64.0, 1.0, 512.0);
        b.pop();
        SPEC = b.build();
    }
}