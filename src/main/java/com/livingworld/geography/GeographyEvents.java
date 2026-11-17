package com.livingworld.geography;

import com.livingworld.geography.data.GeographyWorldData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Обработчики событий геогенерации.
 * Сторона: server.
 * БЕЗ @Mod.EventBusSubscriber — регистрация в LWGeographyMod.
 * Метод НЕ static.
 */
public class GeographyEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-Geography");

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[LW-DEBUG] GeographyEvents.onServerStarted ВЫЗВАН");

        ServerLevel overworld = event.getServer().overworld();

        SavedData.Factory<GeographyWorldData> factory = new SavedData.Factory<>(
                GeographyWorldData::create,
                GeographyWorldData::load,
                null
        );

        GeographyWorldData data = overworld.getDataStorage().computeIfAbsent(
                factory,
                GeographyWorldData.DATA_NAME
        );

        GeographyWorldData result = GeographyGenerator.generateIfNeeded(overworld, data);
        LOGGER.info("[LW-Geography] Сгенерировано стран: {}, дорог: {}, рек: {}",
                result.getCountries().size(),
                result.getRoadEdges().size(),
                result.getRivers().size());
    }
}