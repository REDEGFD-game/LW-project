package com.livingworld.settlements.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

/**
 * Глобальное состояние всех поселений. Привязано к Overworld,
 * как рекомендует официальная документация Forge для 1.21.x [citation:4].
 */
public class SettlementWorldData extends SavedData {

    public static final String DATA_NAME = "lw_settlements";

    private final Map<String, Settlement> settlements = new HashMap<>();

    public static SettlementWorldData create() {
        return new SettlementWorldData();
    }

    public static SettlementWorldData load(CompoundTag tag, HolderLookup.Provider provider) {
        SettlementWorldData data = new SettlementWorldData();
        ListTag list = tag.getList("settlements", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag st = list.getCompound(i);
            String key = st.getString("key");
            data.settlements.put(key, Settlement.load(st.getCompound("data")));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<String, Settlement> e : settlements.entrySet()) {
            CompoundTag st = new CompoundTag();
            st.putString("key", e.getKey());
            st.put("data", e.getValue().save());
            list.add(st);
        }
        tag.put("settlements", list);
        return tag;
    }

    public Settlement getOrCreate(String key, net.minecraft.core.BlockPos center, String name) {
        Settlement s = settlements.get(key);
        if (s == null) {
            s = new Settlement(center, name);
            settlements.put(key, s);
            setDirty();
        }
        return s;
    }

    public Settlement get(String key) {
        return settlements.get(key);
    }

    public Collection<Settlement> all() {
        return Collections.unmodifiableCollection(settlements.values());
    }

    public void markDirty() {
        setDirty();
    }
}