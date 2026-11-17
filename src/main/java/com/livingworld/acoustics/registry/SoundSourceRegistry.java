package com.livingworld.acoustics.registry;

import com.livingworld.core.api.ISoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Реестр активных источников звука.
 * Сторона: both.
 */
public class SoundSourceRegistry {

    private static final List<ISoundSource> SOURCES = new ArrayList<>();

    public static void addSource(ISoundSource source) {
        SOURCES.add(source);
    }

    public static List<ISoundSource> getSources() {
        return SOURCES;
    }

    public static void clear() {
        SOURCES.clear();
    }
}