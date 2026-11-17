package com.livingworld.lightphysics;

import com.livingworld.core.api.DynamicOptimizer;
import com.livingworld.lightphysics.command.LightCommand;
import com.livingworld.lightphysics.config.LightConfig;
import com.livingworld.lightphysics.optim.LightOptimizable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(LightPhysicsMod.MOD_ID)
public class LightPhysicsMod {
    public static final String MOD_ID = "lw_light_physics";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-B8");

    public LightPhysicsMod(FMLJavaModLoadingContext context) {
        LOGGER.info("[LW-B8-DEBUG] LightPhysicsMod конструктор ВЫЗВАН");
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::onCommonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LightConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(new LightEvents());
        MinecraftForge.EVENT_BUS.register(new LightCommand());

        DynamicOptimizer.register(new LightOptimizable());
    }

    private void onCommonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("[LW-B8-DEBUG] LightPhysicsMod.onCommonSetup ВЫЗВАН");
    }
}