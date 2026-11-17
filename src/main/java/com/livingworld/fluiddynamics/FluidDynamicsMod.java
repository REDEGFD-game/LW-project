package com.livingworld.fluiddynamics;

import com.livingworld.core.api.DynamicOptimizer;
import com.livingworld.fluiddynamics.command.FluidCommand;
import com.livingworld.fluiddynamics.config.FluidConfig;
import com.livingworld.fluiddynamics.optim.FluidOptimizable;
import com.livingworld.fluiddynamics.registry.FluidRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Главный класс мода LW Fluid Dynamics (Cluster B4).
 * Сторона: server.
 */
@Mod(FluidDynamicsMod.MOD_ID)
public class FluidDynamicsMod {

    public static final String MOD_ID = "lw_fluid_dynamics";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-B4");

    private static FluidOptimizable optimizable;

    public FluidDynamicsMod(FMLJavaModLoadingContext context) {
        LOGGER.info("[LW-B4-DEBUG] FluidDynamicsMod конструктор ВЫЗВАН");

        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::onCommonSetup);

        // Регистрация блоков и BlockEntity
        FluidRegistry.register(modBus);

        // Регистрация конфига
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FluidConfig.SPEC);

        // Регистрация событий (явная, решение B1)
        MinecraftForge.EVENT_BUS.register(new FluidDynamicsEvents());
        MinecraftForge.EVENT_BUS.register(new FluidCommand());

        // Регистрация в Dynamic Optimizer
        optimizable = new FluidOptimizable();
        DynamicOptimizer.register(optimizable);

        LOGGER.info("[LW-B4-DEBUG] FluidDynamicsEvents, FluidCommand, Optimizable зарегистрированы");
    }

    private void onCommonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("[LW-B4-DEBUG] FluidDynamicsMod.onCommonSetup ВЫЗВАН");
    }

    public static FluidOptimizable getOptimizable() {
        return optimizable;
    }
}