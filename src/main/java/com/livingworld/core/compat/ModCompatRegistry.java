package com.livingworld.core.compat;

import com.livingworld.core.LWCore;
import net.minecraftforge.fml.ModList;
import java.util.HashMap;
import java.util.Map;

public final class ModCompatRegistry {
    // Сохранение минимальной версии (строки), а не VersionRange
    private static final Map<String, String> MIN_VERSIONS = new HashMap<>();

    public static void init() {
        MIN_VERSIONS.put(LWCore.MOD_ID, "1.0.0");
    }

    public static void register(String modId, String minVersion) {
        MIN_VERSIONS.put(modId, minVersion);
    }

    public static boolean validateAll() {
        for (Map.Entry<String, String> entry : MIN_VERSIONS.entrySet()) {
            String modId = entry.getKey();
            String minVer = entry.getValue();

            var container = ModList.get().getModContainerById(modId).orElse(null);
            if (container == null) {
                LWCore.LOGGER.error("Отсутствует обязательный мод: {}", modId);
                return false;
            }

            String actualVer = container.getModInfo().getVersion().toString();
            if (compareVersions(actualVer, minVer) < 0) {
                LWCore.LOGGER.error("Версия {} слишком низкая: требуется >= {}, найдена {}",
                        modId, minVer, actualVer);
                return false;
            }
        }
        return true;
    }

    /** Сравнение версий в формате "x.y.z"; возвращает отрицательное, 0 или положительное число. */
    private static int compareVersions(String a, String b) {
        String[] pa = a.split("\\.");
        String[] pb = b.split("\\.");
        int len = Math.max(pa.length, pb.length);
        for (int i = 0; i < len; i++) {
            int va = i < pa.length ? parseIntSafe(pa[i]) : 0;
            int vb = i < pb.length ? parseIntSafe(pb[i]) : 0;
            if (va != vb) return Integer.compare(va, vb);
        }
        return 0;
    }

    private static int parseIntSafe(String s) {
        try { return Integer.parseInt(s.replaceAll("[^0-9].*", "")); }
        catch (NumberFormatException e) { return 0; }
    }

    private ModCompatRegistry() {}
}