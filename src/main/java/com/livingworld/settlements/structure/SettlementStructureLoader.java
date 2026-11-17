package com.livingworld.settlements.structure;

import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Сканирует data/lw_settlements/structures/** и регистрирует манифесты.
 * Сторона: server.
 */
public class SettlementStructureLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-Structures");

    public static void loadAll(ResourceManager rm) {
        StructureRegistry.clear();
        StructureManifestLoader.clearCache();

        // listResources ищет по всем namespace и возвращает карту path -> Resource
        Map<net.minecraft.resources.ResourceLocation, Resource> found =
                rm.listResources("structures", path -> path.getPath().endsWith(".json"));

        int count = 0;
        for (var entry : found.entrySet()) {
            var loc = entry.getKey();
            // loc = lw_settlements:structures/village/village_house.json
            if (!loc.getNamespace().equals("lw_settlements")) continue;

            String path = loc.getPath(); // structures/village/village_house.json
            String[] parts = path.split("/");
            if (parts.length < 3) continue;

            String category = parts[1];
            String name = parts[2].substring(0, parts[2].length() - 5); // убираем .json

            StructureManifest m = StructureManifestLoader.load(rm, category, name);
            if (m != null) {
                StructureRegistry.register(m);
                count++;
            }
        }

        LOGGER.info("[LW-VALIDATE] Зарегистрировано авторских структур: {}", count);
    }
}