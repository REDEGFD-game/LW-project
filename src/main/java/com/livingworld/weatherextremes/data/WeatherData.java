package com.livingworld.weatherextremes.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

public class WeatherData extends SavedData {
    public static final String DATA_NAME = "lw_weather_extremes";

    // Список центров активных штормов (X, Z) и их интенсивность
    public static class Storm {
        public int x, z;
        public float intensity; // 0..1
        public Storm(int x, int z, float intensity) {
            this.x = x; this.z = z; this.intensity = intensity;
        }
    }

    public final List<Storm> activeStorms = new ArrayList<>();

    public static WeatherData create() { return new WeatherData(); }

    public static WeatherData load(CompoundTag tag, HolderLookup.Provider provider) {
        WeatherData data = new WeatherData();
        ListTag list = tag.getList("storms", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag s = list.getCompound(i);
            data.activeStorms.add(new Storm(s.getInt("x"), s.getInt("z"), s.getFloat("i")));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Storm s : activeStorms) {
            CompoundTag c = new CompoundTag();
            c.putInt("x", s.x);
            c.putInt("z", s.z);
            c.putFloat("i", s.intensity);
            list.add(c);
        }
        tag.put("storms", list);
        return tag;
    }
}