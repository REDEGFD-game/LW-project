package com.livingworld.timeevents;

import com.livingworld.settlements.data.Settlement;
import com.livingworld.settlements.data.SettlementWorldData;
import com.livingworld.timeevents.calendar.Calendar;
import com.livingworld.timeevents.data.TimeEventsData;
import com.livingworld.timeevents.event.ActiveEvent;

import java.util.Random;

/**
 * Симулятор событий. Вызывается раз в игровой день.
 * Читает состояние поселений из SettlementWorldData,
 * создаёт события с вероятностями, зависящими от сезона и состояния.
 */
public class TimeEventsSimulator {

    private final Random random;

    public TimeEventsSimulator(long seed) {
        this.random = new Random(seed);
    }

    /** Один шаг: продвинуть календарь, тикнуть события, проверить новые триггеры. */
    public void tickDay(TimeEventsData data, SettlementWorldData settlements) {
        Calendar cal = data.getCalendar();
        cal.advanceDay();
        data.tickAllEvents();

        for (Settlement s : settlements.all()) {
            String key = s.name + "@" + s.center.asLong();
            checkFair(data, s, key, cal);
            checkPlague(data, s, key, cal);
            checkDrought(data, s, key, cal);
            checkRaid(data, s, key, cal);
        }
        data.setDirty();
    }

    private void checkFair(TimeEventsData data, Settlement s, String key, Calendar cal) {
        if (cal.getSeason() != Calendar.Season.AUTUMN) return;
        if (cal.getDayOfSeason() != 0) return; // только первый день осени
        if (s.population < TimeEventsConfig.FAIR_MIN_POPULATION) return;
        if (data.hasActive(key, ActiveEvent.Type.FAIR)) return;

        data.addEvent(key, new ActiveEvent(ActiveEvent.Type.FAIR, 3, 1.0));
        System.out.println("[LW-TimeEvents] Ярмарка в " + s.name);
    }

    private void checkPlague(TimeEventsData data, Settlement s, String key, Calendar cal) {
        if (data.hasActive(key, ActiveEvent.Type.PLAGUE)) return;
        double overcrowding = s.housingCapacity > 0
                ? Math.max(0, (double) s.population / s.housingCapacity - 1.0)
                : 0;
        double chance = TimeEventsConfig.PLAGUE_BASE_CHANCE
                + overcrowding * TimeEventsConfig.PLAGUE_OVERCROWDING_FACTOR;
        if (random.nextDouble() < chance) {
            data.addEvent(key, new ActiveEvent(ActiveEvent.Type.PLAGUE,
                    TimeEventsConfig.PLAGUE_DURATION_DAYS, 0.5 + overcrowding));
            System.out.println("[LW-TimeEvents] Эпидемия в " + s.name);
        }
    }

    private void checkDrought(TimeEventsData data, Settlement s, String key, Calendar cal) {
        if (cal.getSeason() != Calendar.Season.SUMMER) return;
        if (data.hasActive(key, ActiveEvent.Type.DROUGHT)) return;
        if (random.nextDouble() < TimeEventsConfig.DROUGHT_BASE_CHANCE) {
            int loss = s.foodReserve * TimeEventsConfig.DROUGHT_FOOD_LOSS_PERCENT / 100;
            s.foodReserve = Math.max(0, s.foodReserve - loss);
            data.addEvent(key, new ActiveEvent(ActiveEvent.Type.DROUGHT,
                    TimeEventsConfig.DROUGHT_DURATION_DAYS, 0.6));
            System.out.println("[LW-TimeEvents] Засуха в " + s.name + " (потеря еды: " + loss + ")");
        }
    }

    private void checkRaid(TimeEventsData data, Settlement s, String key, Calendar cal) {
        if (data.hasActive(key, ActiveEvent.Type.RAID)) return;
        // Низкая безопасность: нет стен/стражи (заглушка — бюджет < 100)
        double lowSecurity = s.budget < 100 ? 1.0 : 0.0;
        double chance = TimeEventsConfig.RAID_BASE_CHANCE
                + lowSecurity * TimeEventsConfig.RAID_LOW_SECURITY_FACTOR;
        if (random.nextDouble() < chance) {
            data.addEvent(key, new ActiveEvent(ActiveEvent.Type.RAID,
                    TimeEventsConfig.RAID_DURATION_DAYS, 0.7));
            System.out.println("[LW-TimeEvents] Набег на " + s.name);
        }
    }
}