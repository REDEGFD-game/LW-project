package com.livingworld.hydrology;

import com.livingworld.core.api.DynamicOptimizer;
import com.livingworld.hydrology.command.HydrologyCommand;
import com.livingworld.hydrology.config.HydrologyConfig;
import com.livingworld.hydrology.optim.HydrologyOptimizable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Главный класс мода LW Hydrology (Cluster B1).
 * Сторона: server.
 * Собирается в одном jar с остальными модулями LW-Core.
 */
@Mod(HydrologyMod.MOD_ID)
public class HydrologyMod {

    public static final String MOD_ID = "lw_hydrology";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-B1");

    private static HydrologyOptimizable optimizable;

    public HydrologyMod(FMLJavaModLoadingContext context) {
        LOGGER.info("[LW-B1-DEBUG] HydrologyMod конструктор ВЫЗВАН");

        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::onCommonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HydrologyConfig.SPEC);

        // Явная регистрация событий (решение B1 из Cluster A)
        MinecraftForge.EVENT_BUS.register(new HydrologyEvents());
        MinecraftForge.EVENT_BUS.register(new HydrologyCommand());
        MinecraftForge.EVENT_BUS.register(new com.livingworld.hydrology.command.SnowCommand());

        optimizable = new HydrologyOptimizable();
        DynamicOptimizer.register(optimizable);

        LOGGER.info("[LW-B1-DEBUG] HydrologyEvents, HydrologyCommand, Optimizable зарегистрированы");
    }

    private void onCommonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("[LW-B1-DEBUG] HydrologyMod.onCommonSetup ВЫЗВАН");
    }

    public static HydrologyOptimizable getOptimizable() {
        return optimizable;
    }
}