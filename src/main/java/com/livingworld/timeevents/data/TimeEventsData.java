package com.livingworld.timeevents.data;

import com.livingworld.timeevents.calendar.Calendar;
import com.livingworld.timeevents.event.ActiveEvent;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

/**
 * Глобальное состояние TimeEvents: календарь + активные события по поселениям.
 * Привязано к Overworld (официальная рекомендация Forge для 1.21.x).
 */
public class TimeEventsData extends SavedData {

    public static final String DATA_NAME = "lw_timeevents";

    private Calendar calendar = new Calendar();
    // ключ поселения -> список активных событий
    private final Map<String, List<ActiveEvent>> activeEvents = new HashMap<>();

    public static TimeEventsData create() { return new TimeEventsData(); }

    public static TimeEventsData load(CompoundTag tag, HolderLookup.Provider provider) {
        TimeEventsData d = new TimeEventsData();
        d.calendar = Calendar.load(tag.getCompound("calendar"));

        CompoundTag ev = tag.getCompound("events");
        for (String key : ev.getAllKeys()) {
            ListTag list = ev.getList(key, Tag.TAG_COMPOUND);
            List<ActiveEvent> events = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                events.add(ActiveEvent.load(list.getCompound(i)));
            }
            d.activeEvents.put(key, events);
        }
        return d;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("calendar", calendar.save());

        CompoundTag ev = new CompoundTag();
        for (Map.Entry<String, List<ActiveEvent>> e : activeEvents.entrySet()) {
            ListTag list = new ListTag();
            for (ActiveEvent a : e.getValue()) list.add(a.save());
            ev.put(e.getKey(), list);
        }
        tag.put("events", ev);
        return tag;
    }

    public Calendar getCalendar() { return calendar; }

    public List<ActiveEvent> getEvents(String settlementKey) {
        return activeEvents.computeIfAbsent(settlementKey, k -> new ArrayList<>());
    }

    public void addEvent(String settlementKey, ActiveEvent event) {
        getEvents(settlementKey).add(event);
        setDirty();
    }

    /** Тик всех активных событий; удаляет истёкшие. */
    public void tickAllEvents() {
        for (List<ActiveEvent> events : activeEvents.values()) {
            events.removeIf(ActiveEvent::tick);
        }
        setDirty();
    }

    /** Проверка: активно ли событие заданного типа в поселении. */
    public boolean hasActive(String settlementKey, ActiveEvent.Type type) {
        return getEvents(settlementKey).stream().anyMatch(e -> e.type == type);
    }
}