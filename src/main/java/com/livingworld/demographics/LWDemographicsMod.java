package com.livingworld.demographics;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(LWDemographicsMod.MOD_ID)
public class LWDemographicsMod {
    public static final String MOD_ID = "lw_demographics";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-Demographics");

    public LWDemographicsMod(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        
        LOGGER.info("LW-Demographics инициализирован.");
    }
}