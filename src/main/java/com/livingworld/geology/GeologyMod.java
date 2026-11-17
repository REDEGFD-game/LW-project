package com.livingworld.geology;

import com.livingworld.core.api.DynamicOptimizer;
import com.livingworld.geology.command.GeologyCommand;
import com.livingworld.geology.config.GeologyConfig;
import com.livingworld.geology.optim.GeologyOptimizable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(GeologyMod.MOD_ID)
public class GeologyMod {
    public static final String MOD_ID = "lw_geology";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-B6");
    private static GeologyOptimizable optimizable;

    public GeologyMod(FMLJavaModLoadingContext context) {
        LOGGER.info("[LW-B6-DEBUG] GeologyMod конструктор ВЫЗВАН");
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::onCommonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GeologyConfig.SPEC);
        MinecraftForge.EVENT_BUS.register(new GeologyEvents());
        MinecraftForge.EVENT_BUS.register(new GeologyCommand());

        optimizable = new GeologyOptimizable();
        DynamicOptimizer.register(optimizable);
    }

    private void onCommonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("[LW-B6-DEBUG] GeologyMod.onCommonSetup ВЫЗВАН");
    }

    public static GeologyOptimizable getOptimizable() { return optimizable; }
}