package com.livingworld.settlements.structure;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Загружает манифесты структур из data/lw_settlements/structures/<category>/<name>.json.
 * Сторона: server.
 */
public class StructureManifestLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-Structures");
    private static final Gson GSON = new Gson();
    private static final Map<String, StructureManifest> CACHE = new HashMap<>();

    /**
     * Пытается загрузить манифест для структуры.
     * Возвращает null, если манифеста нет (мод использует дефолтные параметры).
     */
    public static StructureManifest load(ResourceManager rm, String category, String name) {
        String key = category + "/" + name;
        if (CACHE.containsKey(key)) return CACHE.get(key);

        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(
                "lw_settlements", "structures/" + category + "/" + name + ".json"
        );

        Optional<Resource> res = rm.getResource(loc);
        if (res.isEmpty()) {
            LOGGER.debug("[LW-VALIDATE] Манифест {} не найден — структура будет использована с дефолтными параметрами", key);
            CACHE.put(key, null);
            return null;
        }

        try (InputStreamReader reader = new InputStreamReader(res.get().open(), StandardCharsets.UTF_8)) {
            JsonElement el = JsonParser.parseReader(reader);
            StructureManifest m = GSON.fromJson(el, StructureManifest.class);
            if (m != null && m.isValid()) {
                LOGGER.info("[LW-VALIDATE] Манифест {} загружен: weight={}, category={}", key, m.weight, m.category);
                CACHE.put(key, m);
                return m;
            } else {
                LOGGER.warn("[LW-VALIDATE] Манифест {} невалиден (нет id/category/weight)", key);
            }
        } catch (Exception e) {
            LOGGER.error("[LW-VALIDATE] Ошибка чтения манифеста {}: {}", key, e.getMessage());
        }
        CACHE.put(key, null);
        return null;
    }

    public static void clearCache() {
        CACHE.clear();
    }
}