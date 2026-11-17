package com.livingworld.fluiddynamics;

import com.livingworld.fluiddynamics.data.FluidWorldData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Обработчики событий B4.
 * Сторона: server.
 */
public class FluidDynamicsEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B4");

    private FluidWorldData worldData;
    private ServerLevel overworld;
    private int tickCounter = 0;

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[LW-B4-DEBUG] FluidDynamicsEvents.onServerStarted ВЫЗВАН");

        this.overworld = event.getServer().overworld();
        this.worldData = overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(FluidWorldData::create, FluidWorldData::load, null),
                FluidWorldData.DATA_NAME
        );

        LOGGER.info("[LW-B4-DEBUG] FluidWorldData загружен: {} чанков с давлением", worldData.pressure.size());
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (worldData == null) return;

        int tickRate = FluidDynamicsMod.getOptimizable().getCurrentTickRate();
        if (tickRate == Integer.MAX_VALUE) return; // L3 — спящий

        tickCounter++;
        if (tickCounter < tickRate) return;
        tickCounter = 0;

        // В будущем здесь будет FluidGrid.tick(overworld, worldData);
        // Сейчас тик пустой, так как основная логика в BlockEntity
    }

    public FluidWorldData getWorldData() {
        return worldData;
    }
}