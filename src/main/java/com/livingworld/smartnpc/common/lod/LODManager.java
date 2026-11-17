package com.livingworld.smartnpc.common.lod;

import com.livingworld.core.api.IOptimizable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Управление LOD для NPC.
 * Определяет уровень детализации на основе расстояния до игрока
 * и количества активных сущностей.
 */
public class LODManager {

    /** Обновить LOD для всех NPC в мире на основе позиции игрока. */
    public void updateLOD(List<Entity> npcs, Vec3 playerPos) {
        for (Entity npc : npcs) {
            if (!(npc instanceof IOptimizable opt)) continue;
            double dist = npc.position().distanceTo(playerPos);
            int level;
            if (dist < 32) level = IOptimizable.LEVEL_FULL;
            else if (dist < 128) level = IOptimizable.LEVEL_SIMPLIFIED;
            else if (dist < 512) level = IOptimizable.LEVEL_STATISTICAL;
            else level = IOptimizable.LEVEL_AGGREGATED;
            opt.setLodLevel(level);
        }
    }
}