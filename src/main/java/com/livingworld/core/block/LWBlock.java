package com.livingworld.core.block;

import net.minecraft.world.level.block.Block;

/**
 * Базовый блок LW с параметрами.
 * Сторона: both.
 */
public class LWBlock extends Block {

    protected final float weight;
    protected final float tempResistance;

    public LWBlock(Properties properties, float weight, float tempResistance) {
        super(properties);
        this.weight = weight;
        this.tempResistance = tempResistance;
    }

    public float getWeight() { return weight; }
    public float getTempResistance() { return tempResistance; }
}