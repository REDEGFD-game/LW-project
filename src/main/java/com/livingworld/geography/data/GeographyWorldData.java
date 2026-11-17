package com.livingworld.geography.data;

import com.livingworld.geography.country.CountryBorderGenerator;
import com.livingworld.geography.road.RoadGraph;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
import com.livingworld.geography.river.RiverGenerator;

import java.util.*;

/**
 * Единое хранилище состояния LW-Geography.
 * Сохраняет: страны, узлы дорожного графа, рёбра, реки.
 */
public class GeographyWorldData extends SavedData {

    public static final String DATA_NAME = "lw_geography";

    // Страны
    public static class CountryRecord {
        public final int id, seedX, seedZ;
        public final String name;
        public CountryRecord(int id, int seedX, int seedZ, String name) {
            this.id = id; this.seedX = seedX; this.seedZ = seedZ; this.name = name;
        }
    }

    private final List<CountryRecord> countries = new ArrayList<>();
    private final List<int[]> roadNodes = new ArrayList<>();      // [x, z]
    private final List<int[]> roadEdges = new ArrayList<>();      // [fromIdx, toIdx, isTrade]
    private final List<List<int[]>> rivers = new ArrayList<>();   // список рек, каждая — список [x, z]
    private boolean generated = false;

    public static GeographyWorldData create() {
        return new GeographyWorldData();
    }

    public static GeographyWorldData load(CompoundTag tag, HolderLookup.Provider provider) {
        GeographyWorldData d = new GeographyWorldData();
        d.generated = tag.getBoolean("generated");

        ListTag cList = tag.getList("countries", Tag.TAG_COMPOUND);
        for (int i = 0; i < cList.size(); i++) {
            CompoundTag c = cList.getCompound(i);
            d.countries.add(new CountryRecord(
                    c.getInt("id"), c.getInt("x"), c.getInt("z"), c.getString("name")));
        }

        ListTag nList = tag.getList("roadNodes", Tag.TAG_INT_ARRAY);
        for (int i = 0; i < nList.size(); i++) {
            d.roadNodes.add(nList.getIntArray(i));
        }

        ListTag eList = tag.getList("roadEdges", Tag.TAG_INT_ARRAY);
        for (int i = 0; i < eList.size(); i++) {
            d.roadEdges.add(eList.getIntArray(i));
        }

        ListTag rList = tag.getList("rivers", Tag.TAG_LIST);
        for (int i = 0; i < rList.size(); i++) {
            ListTag riverPoints = (ListTag) rList.get(i);
            List<int[]> river = new ArrayList<>();
            for (int j = 0; j < riverPoints.size(); j++) {
                river.add(riverPoints.getIntArray(j));
            }
            d.rivers.add(river);
        }

        return d;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("generated", generated);

        ListTag cList = new ListTag();
        for (CountryRecord c : countries) {
            CompoundTag ct = new CompoundTag();
            ct.putInt("id", c.id);
            ct.putInt("x", c.seedX);
            ct.putInt("z", c.seedZ);
            ct.putString("name", c.name);
            cList.add(ct);
        }
        tag.put("countries", cList);

        ListTag nList = new ListTag();
        for (int[] n : roadNodes) nList.add(new net.minecraft.nbt.IntArrayTag(n));
        tag.put("roadNodes", nList);

        ListTag eList = new ListTag();
        for (int[] e : roadEdges) eList.add(new net.minecraft.nbt.IntArrayTag(e));
        tag.put("roadEdges", eList);

        ListTag rList = new ListTag();
        for (List<int[]> river : rivers) {
            ListTag rp = new ListTag();
            for (int[] p : river) rp.add(new net.minecraft.nbt.IntArrayTag(p));
            rList.add(rp);
        }
        tag.put("rivers", rList);

        return tag;
    }

    // === Геттеры/сеттеры ===
    public boolean isGenerated() { return generated; }
    public void setGenerated(boolean v) { this.generated = v; setDirty(); }

    public List<CountryRecord> getCountries() { return countries; }
    public List<int[]> getRoadNodes() { return roadNodes; }
    public List<int[]> getRoadEdges() { return roadEdges; }
    public List<List<int[]>> getRivers() { return rivers; }

    public void setFromGenerators(CountryBorderGenerator cg, RoadGraph rg, RiverGenerator rgen) {
        countries.clear();
        for (CountryBorderGenerator.Country c : cg.getCountries()) {
            countries.add(new CountryRecord(c.id, c.seedX, c.seedZ, c.name));
        }

        roadNodes.clear();
        for (RoadGraph.Node n : rg.getNodes()) {
            roadNodes.add(new int[]{(int) n.x, (int) n.z});
        }
        roadEdges.clear();
        for (RoadGraph.Edge e : rg.getEdges()) {
            roadEdges.add(new int[]{e.from, e.to, e.isTradeRoute ? 1 : 0});
        }

        rivers.clear();
        for (RiverGenerator.River r : rgen.getRivers()) {
            rivers.add(new ArrayList<>(r.points));
        }
        setDirty();
    }
}