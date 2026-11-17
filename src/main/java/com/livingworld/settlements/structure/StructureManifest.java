package com.livingworld.settlements.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Манифест NBT-структуры автора.
 * Сторона: both (используется на сервере, но может читаться и на клиенте).
 * <p>
 * Читается из JSON рядом с .nbt:
 * data/lw_settlements/structures/<category>/<name>.json
 */
public class StructureManifest {

    public String id;
    public String category;
    public int weight = 1;
    public List<String> biomes = new ArrayList<>();
    public int minStage = 0;
    public int minPopulation = 0;
    public int[] anchor = {0, 0, 0};
    public List<int[]> connectionPoints = new ArrayList<>();
    public List<String> tags = new ArrayList<>();

    public StructureManifest() {}

    public StructureManifest(String id, String category) {
        this.id = id;
        this.category = category;
    }

    public boolean isValid() {
        return id != null && !id.isEmpty() && category != null && !category.isEmpty() && weight > 0;
    }
}