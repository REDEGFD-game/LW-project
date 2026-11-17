package com.livingworld.thermodynamics;

import com.livingworld.thermodynamics.data.ThermodynamicsWorldData;
import com.livingworld.thermodynamics.system.HeatGrid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Обработчики событий B2.
 * Сторона: server.
 */
public class ThermodynamicsEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B2");

    private ThermodynamicsWorldData worldData;
    private ServerLevel overworld;
    private int tickCounter = 0;

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[LW-B2-DEBUG] ThermodynamicsEvents.onServerStarted ВЫЗВАН");

        this.overworld = event.getServer().overworld();
        this.worldData = overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(ThermodynamicsWorldData::create, ThermodynamicsWorldData::load, null),
                ThermodynamicsWorldData.DATA_NAME
        );

        LOGGER.info("[LW-B2-DEBUG] ThermodynamicsWorldData загружен: {} чанков", worldData.temperature.size());
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (worldData == null) return;

        int tickRate = ThermodynamicsMod.getOptimizable().getCurrentTickRate();
        if (tickRate == Integer.MAX_VALUE) return; // L3 — спящий

        tickCounter++;
        if (tickCounter < tickRate) return;
        tickCounter = 0;

        // Главный тик тепловой симуляции
        HeatGrid.tick(overworld, worldData);
    }

    public ThermodynamicsWorldData getWorldData() {
        return worldData;
    }
}