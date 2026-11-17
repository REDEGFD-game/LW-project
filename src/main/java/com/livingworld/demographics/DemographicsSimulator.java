package com.livingworld.demographics;

import com.livingworld.settlements.data.Settlement;
import net.minecraft.server.level.ServerLevel;

import java.util.Collection;
import java.util.Random;

/**
 * Симулятор демографии. Вызывается раз в игровой год (или раз в сезон).
 * Обновляет население каждого поселения на основе:
 * - еды (foodReserve)
 * - жилья (housingCapacity)
 * - безопасности (заглушка, позже из TimeEvents)
 * - медицины (заглушка, позже из технологий)
 */
public class DemographicsSimulator {

    private final Random random;

    public DemographicsSimulator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Один шаг симуляции (один игровой год).
     * @param settlements все поселения
     * @param level серверный мир (для проверки рейдов и т.п.)
     */
    public void tickYear(Collection<Settlement> settlements, ServerLevel level) {
        for (Settlement s : settlements) {
            if (s.population <= 0) continue;

            double birthRate = computeBirthRate(s);
            double deathRate = computeDeathRate(s);

            int births = (int) Math.round(s.population * birthRate / 1000.0);
            int deaths = (int) Math.round(s.population * deathRate / 1000.0);

            // Миграция: если есть избыток жилья и еды, приток; если нехватка — отток
            int migration = computeMigration(s);

            int delta = births - deaths + migration;
            s.population = Math.max(0, s.population + delta);

            // Расход еды: 1 единица на человека в год (упрощённо)
            int foodConsumed = s.population;
            s.foodReserve = Math.max(0, s.foodReserve - foodConsumed);

            // Бюджет: налоги с населения
            s.budget += s.population / 2;

            // Обновление этапа города по населению
            updateStage(s);

            // Отметить данные как изменённые (SettlementWorldData.setDirty вызовется отдельно)
        }
    }

    /** Рождаемость: базовый CBR, снижается при нехватке жилья. */
    private double computeBirthRate(Settlement s) {
        double housingRatio = s.housingCapacity > 0
                ? (double) s.population / s.housingCapacity
                : 1.0;
        double housingFactor = Math.max(0.2, 1.0 - housingRatio * DemographicsConfig.HOUSING_FACTOR_WEIGHT);
        return DemographicsConfig.BASE_BIRTH_RATE * housingFactor;
    }

    /** Смертность: базовый CDR, повышается при нехватке еды и низкой безопасности. */
    private double computeDeathRate(Settlement s) {
        double foodFactor = s.foodReserve <= 0 ? 2.0 : 1.0; // голод удваивает смертность
        // TODO: безопасность из TimeEvents (рейды, болезни)
        double securityFactor = 1.0;
        return DemographicsConfig.BASE_DEATH_RATE * foodFactor * securityFactor;
    }

    /** Миграция: положительная при избытке жилья и еды, отрицательная при нехватке. */
    private int computeMigration(Settlement s) {
        if (s.population < DemographicsConfig.MIGRATION_MIN_POP) return 0;

        double housingRatio = s.housingCapacity > 0
                ? (double) s.population / s.housingCapacity
                : 1.0;

        if (housingRatio > DemographicsConfig.MIGRATION_THRESHOLD && s.foodReserve <= 0) {
            // Нехватка жилья и еды → отток
            return -(int) Math.round(s.population * 0.02);
        } else if (housingRatio < 0.5 && s.foodReserve > s.population) {
            // Избыток жилья и еды → приток (из других поселений)
            return (int) Math.round(s.population * 0.01);
        }
        return 0;
    }

    /** Обновление этапа города по населению. */
    private void updateStage(Settlement s) {
        if (s.population >= 500) s.stage = Settlement.Stage.CITY;
        else if (s.population >= 150) s.stage = Settlement.Stage.TOWN;
        else if (s.population >= 50) s.stage = Settlement.Stage.VILLAGE;
        else s.stage = Settlement.Stage.HAMLET;
    }
}