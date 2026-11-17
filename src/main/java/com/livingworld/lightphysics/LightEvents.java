package com.livingworld.lightphysics;

import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B8");

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[LW-B8-DEBUG] LightEvents.onServerStarted ВЫЗВАН");
    }
}