package com.livingworld.weatherextremes;

import com.livingworld.weatherextremes.data.WeatherData;
import com.livingworld.weatherextremes.system.StormSystem;
import com.livingworld.weatherextremes.system.HailSystem;
import com.livingworld.weatherextremes.system.LightningSystem;
import com.livingworld.weatherextremes.system.WeatherWarningSystem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherExtremesEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B5");
    private WeatherData worldData;
    private ServerLevel overworld;
    private int tickCounter = 0;

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[LW-B5-DEBUG] WeatherExtremesEvents.onServerStarted ВЫЗВАН");
        this.overworld = event.getServer().overworld();
        this.worldData = overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(WeatherData::create, WeatherData::load, null),
                WeatherData.DATA_NAME
        );
        LOGGER.info("[LW-B5-DEBUG] WeatherData загружен: {} штормов", worldData.activeStorms.size());
    }

   @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;
    if (worldData == null) return;

    int tickRate = WeatherExtremesMod.getOptimizable().getCurrentTickRate();
    if (tickRate == Integer.MAX_VALUE) return;

    tickCounter++;
    if (tickCounter < tickRate) return;
    tickCounter = 0;

    // Вызовы систем
    StormSystem.tick(overworld, worldData);
    LightningSystem.tick(overworld, worldData); // НОВОЕ
    HailSystem.tick(overworld, worldData);      // НОВОЕ
    }
}