package com.livingworld.smartnpc.common.data;

import net.minecraft.util.RandomSource;

/**
 * Личность NPC: 5 черт (Big Five) + 1 слой отношений.
 * Использует RandomSource (Minecraft 1.21.x) вместо java.util.Random.
 */
public class NPCPersonality {
    public final double openness;        // 0..1
    public final double conscientiousness;
    public final double extraversion;
    public final double agreeableness;
    public final double neuroticism;

    public NPCPersonality(double o, double c, double e, double a, double n) {
        this.openness = o;
        this.conscientiousness = c;
        this.extraversion = e;
        this.agreeableness = a;
        this.neuroticism = n;
    }

    public static NPCPersonality random(RandomSource r) {
        return new NPCPersonality(
                r.nextDouble(), r.nextDouble(), r.nextDouble(),
                r.nextDouble(), r.nextDouble());
    }

    /** Склонность к риску (влияет на выбор действий). */
    public double riskTolerance() {
        return (openness * 0.5 + (1 - neuroticism) * 0.5);
    }
}