package com.livingworld.geology;

import com.livingworld.geology.data.GeologyWorldData;
import com.livingworld.geology.system.ErosionSystem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeologyEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B6");
    private GeologyWorldData worldData;
    private ServerLevel overworld;
    private int tickCounter = 0;

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[LW-B6-DEBUG] GeologyEvents.onServerStarted ВЫЗВАН");
        this.overworld = event.getServer().overworld();
        this.worldData = overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(GeologyWorldData::create, GeologyWorldData::load, null),
                GeologyWorldData.DATA_NAME
        );
        LOGGER.info("[LW-B6-DEBUG] GeologyWorldData загружен: {} чанков", worldData.stability.size());
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (worldData == null) return;

        int tickRate = GeologyMod.getOptimizable().getCurrentTickRate();
        if (tickRate == Integer.MAX_VALUE) return;

        tickCounter++;
        if (tickCounter < tickRate) return;
        tickCounter = 0;

        ErosionSystem.tick(overworld, worldData);
    }
}