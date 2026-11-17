package com.livingworld.mechanical;

import com.livingworld.mechanical.data.MechanicalWorldData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MechanicalEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B3");
    private MechanicalWorldData worldData;
    private int tickCounter = 0;

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[LW-B3-DEBUG] MechanicalEvents.onServerStarted ВЫЗВАН");
        ServerLevel overworld = event.getServer().overworld();
        this.worldData = overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(MechanicalWorldData::create, MechanicalWorldData::load, null),
                MechanicalWorldData.DATA_NAME
        );
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (worldData == null) return;

        int tickRate = MechanicalMod.getOptimizable().getCurrentTickRate();
        if (tickRate == Integer.MAX_VALUE) return;

        tickCounter++;
        if (tickCounter < tickRate) return;
        tickCounter = 0;
        
        // Тик вращения обрабатывается внутри BlockEntity (ShaftBlockEntity.tick)
    }
}