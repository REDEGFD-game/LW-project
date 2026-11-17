package com.livingworld.demographics;

import com.livingworld.settlements.data.SettlementWorldData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Подключение симулятора к серверному тику.
 * Раз в 24000 тиков (один игровой день в Minecraft = 20 минут реального времени,
 * но для демографии используем "игровой год" = 24000 тиков для простоты).
 */
@Mod.EventBusSubscriber(modid = "lw_demographics", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DemographicsEvents {

    private static DemographicsSimulator simulator;
    private static int tickCounter = 0;
    private static final int YEAR_TICKS = 24000; // один "год" симуляции

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        simulator = new DemographicsSimulator(event.getServer().overworld().getSeed());
        tickCounter = 0;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (simulator == null) return;

        tickCounter++;
        if (tickCounter < YEAR_TICKS) return;
        tickCounter = 0;

        ServerLevel overworld = event.getServer().overworld();

        // Загружаем данные поселений
        SettlementWorldData settlements = overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        SettlementWorldData::create,
                        SettlementWorldData::load,
                        null
                ),
                SettlementWorldData.DATA_NAME
        );

        // Запускаем симуляцию
        simulator.tickYear(settlements.all(), overworld);

        // Помечаем данные как изменённые
        settlements.setDirty();

        System.out.println("[LW-Demographics] Год завершён. Поселений обработано: "
                + settlements.all().size());
    }
}