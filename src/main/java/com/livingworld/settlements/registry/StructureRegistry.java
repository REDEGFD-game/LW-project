package com.livingworld.settlements.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

/**
 * Реестр структур, доступных для Settlements.
 * Загружается из JSON-манифестов в data/lw_settlements/structure_manifests/
 * или регистрируется кодом.
 */
public class StructureRegistry {

    public static class StructureEntry {
        public final ResourceLocation id;
        public final String category;       // "house", "workshop", "town_hall" и т.д.
        public final int weight;
        public final int minPopulation;     // минимальное население для появления
        public final int minTownStage;      // минимальный этап развития города (0..3)
        public final List<String> biomes;   // список биомов, где разрешено
        public final String nbtPath;        // путь к NBT внутри mod jar, например "village/house_small"

        public StructureEntry(ResourceLocation id, String category, int weight,
                              int minPopulation, int minTownStage,
                              List<String> biomes, String nbtPath) {
            this.id = id;
            this.category = category;
            this.weight = weight;
            this.minPopulation = minPopulation;
            this.minTownStage = minTownStage;
            this.biomes = biomes;
            this.nbtPath = nbtPath;
        }
    }

    private static final Map<ResourceLocation, StructureEntry> REGISTRY = new HashMap<>();

    public static void register(StructureEntry entry) {
        REGISTRY.put(entry.id, entry);
    }

    public static StructureEntry get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    /** Выбирает подходящие структуры по категории, этапу и населению. */
    public static List<StructureEntry> query(String category, int townStage, int population) {
        List<StructureEntry> result = new ArrayList<>();
        for (StructureEntry e : REGISTRY.values()) {
            if (!e.category.equals(category)) continue;
            if (e.minTownStage > townStage) continue;
            if (e.minPopulation > population) continue;
            result.add(e);
        }
        return result;
    }

    public static Collection<StructureEntry> all() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
}