package com.livingworld.weatherextremes;

import com.livingworld.core.api.DynamicOptimizer;
import com.livingworld.weatherextremes.command.WeatherCommand;
import com.livingworld.weatherextremes.config.WeatherConfig;
import com.livingworld.weatherextremes.optim.WeatherOptimizable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(WeatherExtremesMod.MOD_ID)
public class WeatherExtremesMod {
    public static final String MOD_ID = "lw_weather_extremes";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-B5");
    private static WeatherOptimizable optimizable;

    public WeatherExtremesMod(FMLJavaModLoadingContext context) {
        LOGGER.info("[LW-B5-DEBUG] WeatherExtremesMod конструктор ВЫЗВАН");
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::onCommonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WeatherConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(new WeatherExtremesEvents());
        MinecraftForge.EVENT_BUS.register(new WeatherCommand());

        optimizable = new WeatherOptimizable();
        DynamicOptimizer.register(optimizable);
    }

    private void onCommonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("[LW-B5-DEBUG] WeatherExtremesMod.onCommonSetup ВЫЗВАН");
    }

    public static WeatherOptimizable getOptimizable() { return optimizable; }
}