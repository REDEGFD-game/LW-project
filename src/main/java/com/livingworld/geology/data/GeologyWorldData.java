package com.livingworld.geology.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

/**
 * Состояние геологии: стабильность грунта, эрозия.
 * Сторона: server.
 */
public class GeologyWorldData extends SavedData {

    public static final String DATA_NAME = "lw_geology";

    // Стабильность грунта (0.0 = обвал, 1.0 = монолит)
    public final Map<Long, Float> stability = new HashMap<>();

    public static GeologyWorldData create() {
        return new GeologyWorldData();
    }

    public static GeologyWorldData load(CompoundTag tag, HolderLookup.Provider provider) {
        GeologyWorldData data = new GeologyWorldData();
        ListTag list = tag.getList("stability", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag e = list.getCompound(i);
            data.stability.put(e.getLong("k"), e.getFloat("v"));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<Long, Float> e : stability.entrySet()) {
            CompoundTag c = new CompoundTag();
            c.putLong("k", e.getKey());
            c.putFloat("v", e.getValue());
            list.add(c);
        }
        tag.put("stability", list);
        return tag;
    }

    public static long chunkKey(int chunkX, int chunkZ) {
        return ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
    }
}