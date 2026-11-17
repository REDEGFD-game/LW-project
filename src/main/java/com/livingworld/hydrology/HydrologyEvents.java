package com.livingworld.hydrology;

import com.livingworld.hydrology.data.HydrologyWorldData;
import com.livingworld.hydrology.system.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HydrologyEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B1");

    private HydrologyWorldData worldData;
    private ServerLevel overworld;
    private int tickCounter = 0;

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[LW-B1-DEBUG] HydrologyEvents.onServerStarted ВЫЗВАН");
        this.overworld = event.getServer().overworld();
        this.worldData = overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(HydrologyWorldData::create, HydrologyWorldData::load, null),
                HydrologyWorldData.DATA_NAME
        );
        LOGGER.info("[LW-B1-DEBUG] HydrologyWorldData загружен: {} чанков с климатом", worldData.temperature.size());
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (worldData == null) return;

        int tickRate = HydrologyMod.getOptimizable().getCurrentTickRate();
        if (tickRate == Integer.MAX_VALUE) return;

        tickCounter++;
        if (tickCounter < tickRate) return;
        tickCounter = 0;

        ClimateGrid.tick(overworld, worldData);
        CloudSystem.tick(overworld, worldData);
        PrecipitationSystem.tick(overworld, worldData);
        RiverNetwork.tick(overworld, worldData);
        GroundwaterSystem.tick(overworld, worldData);
        DroughtFloodEvents.tick(overworld, worldData);
        GlacierSystem.tick(overworld, worldData);   // <-- ГЛАВНОЕ: вызов должен быть здесь
    }

    public HydrologyWorldData getWorldData() {
        return worldData;
    }
}