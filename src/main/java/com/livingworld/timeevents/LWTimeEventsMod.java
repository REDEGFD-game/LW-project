package com.livingworld.timeevents;

import com.livingworld.timeevents.command.CalendarCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LWTimeEventsMod.MOD_ID)
public class LWTimeEventsMod {
    public static final String MOD_ID = "lw_timeevents";

    public LWTimeEventsMod(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();

        // Регистрация событий времени
        MinecraftForge.EVENT_BUS.register(new TimeEventsEvents());
        
        // Регистрация команды /lw_season
        MinecraftForge.EVENT_BUS.register(new CalendarCommand());
    }
}