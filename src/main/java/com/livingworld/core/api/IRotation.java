package com.livingworld.core.api;

/**
 * Интерфейс вращательной энергии.
 * Сторона: both.
 */
public interface IRotation {
    /** Крутящий момент (Н·м). */
    double getTorque();

    /** Скорость вращения (рад/с). */
    double getSpeed();

    /** Мощность (Ватт) = Момент * Скорость. */
    default double getPower() {
        return getTorque() * getSpeed();
    }

    /** Направление вращения (1 = по часовой, -1 = против). */
    int getDirection();
}