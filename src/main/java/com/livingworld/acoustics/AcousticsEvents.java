package com.livingworld.acoustics;

import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcousticsEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B7");

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[LW-B7-DEBUG] AcousticsEvents.onServerStarted ВЫЗВАН");
    }
}