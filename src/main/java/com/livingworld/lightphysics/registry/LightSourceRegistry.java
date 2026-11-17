package com.livingworld.lightphysics.registry;

import com.livingworld.core.api.ILightSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Реестр активных источников света.
 * Сторона: both.
 */
public class LightSourceRegistry {

    private static final List<ILightSource> SOURCES = new ArrayList<>();

    public static void addSource(ILightSource source) {
        SOURCES.add(source);
    }

    public static void removeSource(ILightSource source) {
        SOURCES.remove(source);
    }

    public static List<ILightSource> getSources() {
        return SOURCES;
    }

    public static void clear() {
        SOURCES.clear();
    }
}