package com.livingworld.fluiddynamics.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

/**
 * Состояние жидкостей.
 * Сторона: server.
 */
public class FluidWorldData extends SavedData {

    public static final String DATA_NAME = "lw_fluid_dynamics";

    // Храним давление по чанкам (ключ — chunkKey, значение — давление)
    public final Map<Long, Float> pressure = new HashMap<>();

    public static FluidWorldData create() {
        return new FluidWorldData();
    }

    public static FluidWorldData load(CompoundTag tag, HolderLookup.Provider provider) {
        FluidWorldData data = new FluidWorldData();
        CompoundTag pressureTag = tag.getCompound("pressure");
        for (String key : pressureTag.getAllKeys()) {
            data.pressure.put(Long.parseLong(key), pressureTag.getFloat(key));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag pressureTag = new CompoundTag();
        for (Map.Entry<Long, Float> entry : pressure.entrySet()) {
            pressureTag.putFloat(String.valueOf(entry.getKey()), entry.getValue());
        }
        tag.put("pressure", pressureTag);
        return tag;
    }

    public static long chunkKey(int chunkX, int chunkZ) {
        return ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
    }
}