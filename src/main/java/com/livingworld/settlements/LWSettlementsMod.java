package com.livingworld.settlements;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Главный класс мода LW Settlements.
 * Сторона: both.
 */
@Mod(LWSettlementsMod.MOD_ID)
public class LWSettlementsMod {

    public static final String MOD_ID = "lw_settlements";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-Settlements");

    public LWSettlementsMod() {
        LOGGER.info("[LW-DEBUG] LWSettlementsMod конструктор ВЫЗВАН");

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onCommonSetup);

        // Экземпляр спавнера — нужен для приёма TickEvent
        MinecraftForge.EVENT_BUS.register(new SettlementSpawner());
        MinecraftForge.EVENT_BUS.register(new SettlementEvents());
        MinecraftForge.EVENT_BUS.register(new com.livingworld.settlements.command.LWCommand());

        LOGGER.info("[LW-DEBUG] SettlementSpawner, SettlementEvents, LWCommand зарегистрированы");
    }

    private void onCommonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("[LW-DEBUG] LWSettlementsMod.onCommonSetup ВЫЗВАН");
    }
}