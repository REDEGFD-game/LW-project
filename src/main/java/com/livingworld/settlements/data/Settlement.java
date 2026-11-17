package com.livingworld.settlements.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Состояние одного поселения. Сохраняется внутри SettlementWorldData.
 */
public class Settlement {

    public enum Stage {
        HAMLET(0), VILLAGE(1), TOWN(2), CITY(3);
        public final int level;
        Stage(int level) { this.level = level; }
    }

    public BlockPos center;
    public String name;
    public Stage stage = Stage.HAMLET;
    public int population = 0;
    public int housingCapacity = 0;
    public int foodReserve = 0;
    public int budget = 0;

    // Построенные структуры: id + позиция
    public static class PlacedStructure {
        public final String structureId;
        public final BlockPos pos;
        public PlacedStructure(String structureId, BlockPos pos) {
            this.structureId = structureId;
            this.pos = pos;
        }
    }
    public final List<PlacedStructure> placedStructures = new ArrayList<>();

    // Точки подключения для новых структур (например, дороги, входы)
    public final List<BlockPos> connectionPoints = new ArrayList<>();

    public Settlement(BlockPos center, String name) {
        this.center = center;
        this.name = name;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("center", center.asLong());
        tag.putString("name", name);
        tag.putInt("stage", stage.level);
        tag.putInt("population", population);
        tag.putInt("housing", housingCapacity);
        tag.putInt("food", foodReserve);
        tag.putInt("budget", budget);

        ListTag placed = new ListTag();
        for (PlacedStructure ps : placedStructures) {
            CompoundTag pt = new CompoundTag();
            pt.putString("id", ps.structureId);
            pt.putLong("pos", ps.pos.asLong());
            placed.add(pt);
        }
        tag.put("placed", placed);

        ListTag conns = new ListTag();
        for (BlockPos p : connectionPoints) {
            CompoundTag ct = new CompoundTag();
            ct.putLong("pos", p.asLong());
            conns.add(ct);
        }
        tag.put("connections", conns);

        return tag;
    }

    public static Settlement load(CompoundTag tag) {
        BlockPos center = BlockPos.of(tag.getLong("center"));
        Settlement s = new Settlement(center, tag.getString("name"));
        s.stage = Stage.values()[tag.getInt("stage")];
        s.population = tag.getInt("population");
        s.housingCapacity = tag.getInt("housing");
        s.foodReserve = tag.getInt("food");
        s.budget = tag.getInt("budget");

        ListTag placed = tag.getList("placed", Tag.TAG_COMPOUND);
        for (int i = 0; i < placed.size(); i++) {
            CompoundTag pt = placed.getCompound(i);
            s.placedStructures.add(new PlacedStructure(
                    pt.getString("id"), BlockPos.of(pt.getLong("pos"))));
        }

        ListTag conns = tag.getList("connections", Tag.TAG_COMPOUND);
        for (int i = 0; i < conns.size(); i++) {
            s.connectionPoints.add(BlockPos.of(conns.getCompound(i).getLong("pos")));
        }

        return s;
    }
}