package com.livingworld.geography.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class GeographyData extends SavedData {
    private static final String DATA_NAME = "lw_geography";

    private CompoundTag rawData = new CompoundTag();

    public static GeographyData create() {
        return new GeographyData();
    }

    public static GeographyData load(CompoundTag tag, HolderLookup.Provider provider) {
        GeographyData data = new GeographyData();
        data.rawData = tag.getCompound("data");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("data", rawData);
        return tag;
    }

    public CompoundTag getRawData() {
        return rawData;
    }

    public void setRawData(CompoundTag data) {
        this.rawData = data;
        setDirty();
    }
}