package com.livingworld.hydrology.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

/**
 * Состояние климата: температура, влажность, облака, реки.
 * Сторона: server.
 * <p>
 * Ключ сетки — упакованный (chunkX, chunkZ), чтобы хранить по чанку.
 */
public class HydrologyWorldData extends SavedData {

    public static final String DATA_NAME = "lw_hydrology";

    public final Map<Long, Float> temperature = new HashMap<>();
    public final Map<Long, Float> humidity = new HashMap<>();
    public final Map<Long, Float> rainfall = new HashMap<>();
    public final Map<Long, Float> riverFlow = new HashMap<>();
    public final Map<Long, Float> groundwater = new HashMap<>();

    public static HydrologyWorldData create() {
        return new HydrologyWorldData();
    }

    public static HydrologyWorldData load(CompoundTag tag, HolderLookup.Provider provider) {
        HydrologyWorldData data = new HydrologyWorldData();
        readMap(tag, "temperature", data.temperature);
        readMap(tag, "humidity", data.humidity);
        readMap(tag, "rainfall", data.rainfall);
        readMap(tag, "riverFlow", data.riverFlow);
        readMap(tag, "groundwater", data.groundwater);
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        writeMap(tag, "temperature", temperature);
        writeMap(tag, "humidity", humidity);
        writeMap(tag, "rainfall", rainfall);
        writeMap(tag, "riverFlow", riverFlow);
        writeMap(tag, "groundwater", groundwater);
        return tag;
    }

    private static void readMap(CompoundTag tag, String key, Map<Long, Float> map) {
        ListTag list = tag.getList(key, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag e = list.getCompound(i);
            map.put(e.getLong("k"), e.getFloat("v"));
        }
    }

    private static void writeMap(CompoundTag tag, String key, Map<Long, Float> map) {
        ListTag list = new ListTag();
        for (Map.Entry<Long, Float> e : map.entrySet()) {
            CompoundTag c = new CompoundTag();
            c.putLong("k", e.getKey());
            c.putFloat("v", e.getValue());
            list.add(c);
        }
        tag.put(key, list);
    }

    public static long chunkKey(int chunkX, int chunkZ) {
        return ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
    }

    public static int keyX(long key) {
        return (int) (key >> 32);
    }

    public static int keyZ(long key) {
        return (int) key;
    }

    public static long blockKey(BlockPos pos) {
        return chunkKey(pos.getX() >> 4, pos.getZ() >> 4);
    }
}