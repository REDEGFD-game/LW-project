package com.livingworld.geography;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Главный класс мода LW Geography.
 * Сторона: both.
 * Регистрация событий — явная, через экземпляр (B1).
 */
@Mod(LWGeographyMod.MOD_ID)
public class LWGeographyMod {

    public static final String MOD_ID = "lw_geography";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-Geography");

    public LWGeographyMod() {
        LOGGER.info("[LW-DEBUG] LWGeographyMod конструктор ВЫЗВАН");

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(new GeographyEvents());
        LOGGER.info("[LW-DEBUG] GeographyEvents зарегистрирован в MinecraftForge.EVENT_BUS");
    }

    private void onCommonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("[LW-DEBUG] LWGeographyMod.onCommonSetup ВЫЗВАН");
    }
}