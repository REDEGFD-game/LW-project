package com.livingworld.smartnpc.common.ai;

import java.util.*;

/**
 * Utility AI: выбор цели по максимальной полезности.
 * Полезность = вес цели * значение потребности (0..1).
 */
public class UtilityAI {

    public static class Consideration {
        public final String goalName;
        public final double weight;
        public final double score; // 0..1
        public Consideration(String goalName, double weight, double score) {
            this.goalName = goalName;
            this.weight = weight;
            this.score = score;
        }
        public double utility() { return weight * score; }
    }

    /**
     * Выбирает цель с максимальной полезностью.
     */
    public String selectGoal(List<Consideration> considerations) {
        return considerations.stream()
                .max(Comparator.comparingDouble(Consideration::utility))
                .map(c -> c.goalName)
                .orElse("idle");
    }
}