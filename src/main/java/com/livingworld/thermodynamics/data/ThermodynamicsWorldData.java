package com.livingworld.thermodynamics.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

/**
 * Состояние тепла (температура по чанкам).
 * Сторона: server.
 */
public class ThermodynamicsWorldData extends SavedData {

    public static final String DATA_NAME = "lw_thermodynamics";

    // Температура блоков/чанков (°C)
    public final Map<Long, Float> temperature = new HashMap<>();

    public static ThermodynamicsWorldData create() {
        return new ThermodynamicsWorldData();
    }

    public static ThermodynamicsWorldData load(CompoundTag tag, HolderLookup.Provider provider) {
        ThermodynamicsWorldData data = new ThermodynamicsWorldData();
        ListTag list = tag.getList("temperature", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag e = list.getCompound(i);
            data.temperature.put(e.getLong("k"), e.getFloat("v"));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<Long, Float> e : temperature.entrySet()) {
            CompoundTag c = new CompoundTag();
            c.putLong("k", e.getKey());
            c.putFloat("v", e.getValue());
            list.add(c);
        }
        tag.put("temperature", list);
        return tag;
    }

    public static long chunkKey(int chunkX, int chunkZ) {
        return ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
    }
}