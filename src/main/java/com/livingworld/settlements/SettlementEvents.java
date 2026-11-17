package com.livingworld.settlements;

import com.livingworld.settlements.data.SettlementWorldData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.livingworld.settlements.structure.SettlementStructureLoader;
import com.livingworld.settlements.structure.StructureManifest;
import com.livingworld.settlements.structure.StructureRegistry;
import com.livingworld.settlements.structure.StructureValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Обработчики событий поселений.
 * Сторона: server.
 */
public class SettlementEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-Settlements");

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[LW-DEBUG] SettlementEvents.onServerStarted ВЫЗВАН");
        ServerLevel overworld = event.getServer().overworld();
        
        SettlementStructureLoader.loadAll(event.getServer().getResourceManager());

        SettlementWorldData data = overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(SettlementWorldData::create, SettlementWorldData::load, null),
                SettlementWorldData.DATA_NAME
        );
        for (StructureManifest m : StructureRegistry.all()) {
        StructureValidator.validate(overworld, m.id, m.category);
        }


        LOGGER.info("[LW-DEBUG] SettlementWorldData загружен, поселений: {}", data.all().size());
        
        if (data.all().isEmpty()) {
            LOGGER.info("[LW-DEBUG] Поселений нет. Прогрейте мир через Chunky и введите /lw_force_gen");
        }
    }
}