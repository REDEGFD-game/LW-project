package com.livingworld.core.unit;

/**
 * Единая система единиц.
 * Энергия: J (джоуль)
 * Мощность: W (ватт) = J/s
 * Момент силы: N·m
 * Расход жидкости: L/s (литров в секунду)
 * Время: тик (tick), 1 секунда = 20 тиков
 */
public final class Units {
    public static final double TICKS_PER_SECOND = 20.0;

    public static double ticksToSeconds(long ticks) {
        return ticks / TICKS_PER_SECOND;
    }

    public static long secondsToTicks(double seconds) {
        return Math.round(seconds * TICKS_PER_SECOND);
    }

    public static double joulesToWatts(double joules, double seconds) {
        return joules / seconds;
    }

    public static double wattsToJoules(double watts, double seconds) {
        return watts * seconds;
    }

    public static double litersPerTickToLitersPerSecond(double lpt) {
        return lpt * TICKS_PER_SECOND;
    }

    private Units() {}
}