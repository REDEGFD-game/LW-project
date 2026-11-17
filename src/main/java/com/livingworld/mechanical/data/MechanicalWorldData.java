package com.livingworld.mechanical.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Состояние механических сетей.
 * Сторона: server.
 */
public class MechanicalWorldData extends SavedData {

    public static final String DATA_NAME = "lw_mechanical";

    // Пока храним просто общий счётчик активных сетей
    public int activeNetworks = 0;

    public static MechanicalWorldData create() {
        return new MechanicalWorldData();
    }

    public static MechanicalWorldData load(CompoundTag tag, HolderLookup.Provider provider) {
        MechanicalWorldData data = new MechanicalWorldData();
        data.activeNetworks = tag.getInt("activeNetworks");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("activeNetworks", activeNetworks);
        return tag;
    }
}