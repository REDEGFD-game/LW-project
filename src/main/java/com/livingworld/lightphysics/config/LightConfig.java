package com.livingworld.lightphysics.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LightConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SHADOWS;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.comment("B8 Light Physics").push("light");
        ENABLE_SHADOWS = b.comment("Включить динамические тени (требует ресурсов)").define("enableShadows", false);
        b.pop();
        SPEC = b.build();
    }
}