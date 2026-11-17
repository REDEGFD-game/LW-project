package com.livingworld.mechanical;

import com.livingworld.core.api.DynamicOptimizer;
import com.livingworld.mechanical.command.MechanicalCommand;
import com.livingworld.mechanical.config.MechanicalConfig;
import com.livingworld.mechanical.optim.MechanicalOptimizable;
import com.livingworld.mechanical.registry.MechanicalRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MechanicalMod.MOD_ID)
public class MechanicalMod {
    public static final String MOD_ID = "lw_mechanical_power";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-B3");

    private static MechanicalOptimizable optimizable;

    public MechanicalMod(FMLJavaModLoadingContext context) {
        LOGGER.info("[LW-B3-DEBUG] MechanicalMod конструктор ВЫЗВАН");
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::onCommonSetup);

        MechanicalRegistry.register(modBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MechanicalConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(new MechanicalEvents());
        MinecraftForge.EVENT_BUS.register(new MechanicalCommand());

        optimizable = new MechanicalOptimizable();
        DynamicOptimizer.register(optimizable);
    }

    private void onCommonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("[LW-B3-DEBUG] MechanicalMod.onCommonSetup ВЫЗВАН");
    }

    public static MechanicalOptimizable getOptimizable() { return optimizable; }
}