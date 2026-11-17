package com.livingworld.core.api;

/**
 * Интерфейс для блоков, которые могут принимать вращение (шестерни, валы).
 * Сторона: both.
 */
public interface IRotationConsumer {
    /** Получить вращение от соседнего блока. */
    void receiveRotation(IRotation rotation);

    /** Получить текущее вращение. */
    IRotation getRotation();
}