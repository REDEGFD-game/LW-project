package com.livingworld.timeevents.event;

import net.minecraft.nbt.CompoundTag;

/**
 * Активное событие: тип, оставшиеся дни, сила эффекта.
 */
public class ActiveEvent {

    public enum Type { FAIR, PLAGUE, DROUGHT, RAID }

    public final Type type;
    public int remainingDays;
    public final double intensity; // 0..1

    public ActiveEvent(Type type, int remainingDays, double intensity) {
        this.type = type;
        this.remainingDays = remainingDays;
        this.intensity = intensity;
    }

    public boolean tick() {
        remainingDays--;
        return remainingDays <= 0;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", type.name());
        tag.putInt("remaining", remainingDays);
        tag.putDouble("intensity", intensity);
        return tag;
    }

    public static ActiveEvent load(CompoundTag tag) {
        return new ActiveEvent(
                Type.valueOf(tag.getString("type")),
                tag.getInt("remaining"),
                tag.getDouble("intensity"));
    }
}