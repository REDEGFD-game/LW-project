package com.livingworld.smartnpc.common;

import com.livingworld.settlements.data.Settlement;
import com.livingworld.settlements.data.SettlementWorldData;
import com.livingworld.smartnpc.common.entity.SmartNPCEntity;
import com.livingworld.smartnpc.common.registry.SmartNPCRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.levelgen.Heightmap;


public class NPCSpawner {
    
    public static void spawnInitialNPCs(ServerLevel level) {
        SettlementWorldData setData = level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(SettlementWorldData::create, SettlementWorldData::load, null),
                SettlementWorldData.DATA_NAME
        );

        int totalSpawned = 0;

        for (Settlement s : setData.all()) {
            if (s.population <= 0) continue;

            int toSpawn = 2 + level.random.nextInt(2);
            for (int i = 0; i < toSpawn; i++) {
                // Ищем случайную точку рядом с центром
                int x = s.center.getX() + level.random.nextInt(16) - 8;
                int z = s.center.getZ() + level.random.nextInt(16) - 8;
                
                // Ищем самый верхний блок (поверхность) в этой точке
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                BlockPos spawnPos = new BlockPos(x, y, z);

                SmartNPCEntity npc = SmartNPCRegistry.SMART_NPC.get().create(level);
                if (npc != null) {
                    npc.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0, 0);
                    level.addFreshEntity(npc);
                    totalSpawned++;
                }
            }
        }
        if (totalSpawned > 0) {
            System.out.println("[LW-SmartNPC] Заспавнено NPC: " + totalSpawned);
        }
    }
}