package com.livingworld.thermodynamics;

import com.livingworld.core.api.DynamicOptimizer;
import com.livingworld.thermodynamics.command.ThermodynamicsCommand;
import com.livingworld.thermodynamics.config.ThermodynamicsConfig;
import com.livingworld.thermodynamics.optim.ThermodynamicsOptimizable;
import com.livingworld.thermodynamics.registry.ThermodynamicsRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ThermodynamicsMod.MOD_ID)
public class ThermodynamicsMod {

    public static final String MOD_ID = "lw_thermodynamics";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-B2");

    private static ThermodynamicsOptimizable optimizable;

    public ThermodynamicsMod(FMLJavaModLoadingContext context) {
        LOGGER.info("[LW-B2-DEBUG] ThermodynamicsMod конструктор ВЫЗВАН");

        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::onCommonSetup);
        
        // Регистрация блоков
        ThermodynamicsRegistry.register(modBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ThermodynamicsConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(new ThermodynamicsEvents());
        MinecraftForge.EVENT_BUS.register(new ThermodynamicsCommand());

        optimizable = new ThermodynamicsOptimizable();
        DynamicOptimizer.register(optimizable);
    }

    private void onCommonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("[LW-B2-DEBUG] ThermodynamicsMod.onCommonSetup ВЫЗВАН");
    }

    public static ThermodynamicsOptimizable getOptimizable() {
        return optimizable;
    }
}