package com.livingworld.core.event;

import com.livingworld.core.LWCore;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Тонкая обёртка Forge EventBus.
 * НЕ заменяет Forge EventBus, а только предоставляет единую точку входа
 * для модов Cluster A, чтобы избежать прямых вызовов MinecraftForge повсюду.
 */
public final class LWEventBus {

    /**
     * Шина событий мода (MOD bus).
     * Ссылка на шину событий, сохранённая из конструктора LWCore.
     */
    public static IEventBus modBus() {
        return LWCore.getModEventBus();
    }

    /**
     * Шина событий Forge (FORGE bus), для игровых событий.
     */
    public static IEventBus forgeBus() {
        return MinecraftForge.EVENT_BUS;
    }

    /**
     * Отправка события в шину событий Forge.
     */
    public static void post(Event event) {
        MinecraftForge.EVENT_BUS.post(event);
    }

    private LWEventBus() {}
}