package com.livingworld.timeevents.calendar;

import net.minecraft.nbt.CompoundTag;

/**
 * Игровой календарь: день (0..29), сезон (0..3), год.
 * Один сезон = 30 игровых дней, один год = 4 сезона = 120 дней.
 * Один игровой день = 24000 тиков (стандарт Minecraft).
 * Чистая логика, не зависит от API Minecraft (кроме CompoundTag для сохранения).
 */
public class Calendar {

    public static final int DAYS_PER_SEASON = 30;
    public static final int SEASONS_PER_YEAR = 4;
    public static final int DAYS_PER_YEAR = DAYS_PER_SEASON * SEASONS_PER_YEAR;

    public enum Season {
        SPRING, SUMMER, AUTUMN, WINTER;
        public static Season fromIndex(int i) { return values()[i % 4]; }
    }

    private int day;     // 0..DAYS_PER_YEAR-1
    private int year;    // 0..

    public Calendar() { this.day = 0; this.year = 0; }

    /** Продвинуть календарь на один день. */
    public void advanceDay() {
        day++;
        if (day >= DAYS_PER_YEAR) {
            day = 0;
            year++;
        }
    }

    public int getDay() { return day; }
    public int getYear() { return year; }

    /** День внутри сезона (0..29). */
    public int getDayOfSeason() { return day % DAYS_PER_SEASON; }

    /** Индекс сезона (0..3). */
    public int getSeasonIndex() { return day / DAYS_PER_SEASON; }

    public Season getSeason() { return Season.fromIndex(getSeasonIndex()); }

    /** Абсолютный номер дня с начала мира. */
    public int getAbsoluteDay() { return year * DAYS_PER_YEAR + day; }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("day", day);
        tag.putInt("year", year);
        return tag;
    }

    public static Calendar load(CompoundTag tag) {
        Calendar c = new Calendar();
        c.day = tag.getInt("day");
        c.year = tag.getInt("year");
        return c;
    }

    @Override
    public String toString() {
        return String.format("Год %d, %s, день %d", year, getSeason(), getDayOfSeason() + 1);
    }
}