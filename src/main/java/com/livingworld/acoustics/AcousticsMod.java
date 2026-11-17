package com.livingworld.acoustics;

import com.livingworld.acoustics.command.AcousticsCommand;
import com.livingworld.acoustics.config.AcousticsConfig;
import com.livingworld.acoustics.optim.AcousticsOptimizable;
import com.livingworld.core.api.DynamicOptimizer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(AcousticsMod.MOD_ID)
public class AcousticsMod {
    public static final String MOD_ID = "lw_acoustics";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-B7");

    public AcousticsMod(FMLJavaModLoadingContext context) {
        LOGGER.info("[LW-B7-DEBUG] AcousticsMod конструктор ВЫЗВАН");
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::onCommonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AcousticsConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(new AcousticsEvents());
        MinecraftForge.EVENT_BUS.register(new AcousticsCommand());

        DynamicOptimizer.register(new AcousticsOptimizable());
    }

    private void onCommonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("[LW-B7-DEBUG] AcousticsMod.onCommonSetup ВЫЗВАН");
    }
}