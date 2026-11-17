package com.livingworld.smartnpc.common.data;

import java.util.*;

/**
 * Память NPC: список недавних событий с весами.
 * Используется GOAP/Utility для принятия решений.
 */
public class NPCMemory {

    public static class MemoryEntry {
        public final String type;
        public final String target;
        public final double weight;
        public final long tick;
        public MemoryEntry(String type, String target, double weight, long tick) {
            this.type = type; this.target = target; this.weight = weight; this.tick = tick;
        }
    }

    private final Deque<MemoryEntry> entries = new ArrayDeque<>();
    private static final int MAX_ENTRIES = 64;

    public void remember(String type, String target, double weight, long tick) {
        entries.addFirst(new MemoryEntry(type, target, weight, tick));
        if (entries.size() > MAX_ENTRIES) entries.removeLast();
    }

    public List<MemoryEntry> recent(int count) {
        return entries.stream().limit(count).toList();
    }

    /** Суммарный вес событий заданного типа. */
    public double totalWeight(String type) {
        return entries.stream()
                .filter(e -> e.type.equals(type))
                .mapToDouble(e -> e.weight)
                .sum();
    }
}