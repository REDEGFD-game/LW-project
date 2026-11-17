package com.livingworld.smartnpc.server;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod.EventBusSubscriber(modid = "lw_smartnpc", bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.DEDICATED_SERVER)
public class ServerSetup {
    @SubscribeEvent
    public static void onServerSetup(FMLDedicatedServerSetupEvent event) {
        System.out.println("[LW-SmartNPC] Инициализация серверной стороны");
    }
}