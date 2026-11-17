package com.livingworld.settlements.structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Реестр авторских структур с приоритетом над процедурными шаблонами.
 * Сторона: both.
 */
public class StructureRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-Structures");
    private static final Map<String, StructureManifest> BY_ID = new HashMap<>();
    private static final Map<String, List<StructureManifest>> BY_CATEGORY = new HashMap<>();

    public static void register(StructureManifest m) {
        if (BY_ID.containsKey(m.id)) {
            LOGGER.warn("[LW-VALIDATE] Структура {} уже зарегистрирована, авторский вариант перезаписан", m.id);
        }
        BY_ID.put(m.id, m);
        BY_CATEGORY.computeIfAbsent(m.category, k -> new ArrayList<>()).add(m);
        LOGGER.info("[LW-VALIDATE] Структура {}: OK (category={}, weight={})", m.id, m.category, m.weight);
    }

    public static StructureManifest get(String id) {
        return BY_ID.get(id);
    }

    public static List<StructureManifest> getByCategory(String category) {
        return BY_CATEGORY.getOrDefault(category, Collections.emptyList());
    }

    public static Collection<StructureManifest> all() {
        return BY_ID.values();
    }

    public static void clear() {
        BY_ID.clear();
        BY_CATEGORY.clear();
    }
}