package com.livingworld.timeevents;

import com.livingworld.settlements.data.SettlementWorldData;
import com.livingworld.timeevents.data.TimeEventsData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Обработчики временных событий.
 * Сторона: server.
 * БЕЗ @Mod.EventBusSubscriber. Поля — экземпляра, не static.
 */
public class TimeEventsEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-TimeEvents");
    private static final int DAY_TICKS = 24000;

    private TimeEventsSimulator simulator;
    private int tickCounter = 0;

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[LW-DEBUG] TimeEventsEvents.onServerStarted ВЫЗВАН");
        simulator = new TimeEventsSimulator(event.getServer().overworld().getSeed());
        tickCounter = 0;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (simulator == null) return;

        tickCounter++;
        if (tickCounter < DAY_TICKS) return;
        tickCounter = 0;

        ServerLevel overworld = event.getServer().overworld();

        TimeEventsData teData = overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(TimeEventsData::create, TimeEventsData::load, null),
                TimeEventsData.DATA_NAME
        );

        SettlementWorldData settlements = overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(SettlementWorldData::create, SettlementWorldData::load, null),
                SettlementWorldData.DATA_NAME
        );

        simulator.tickDay(teData, settlements);
    }
}