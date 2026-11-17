package com.livingworld.thermodynamics.system;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

/**
 * Реестр теплоёмкости блоков.
 * Чем выше значение, тем медленнее блок нагревается.
 * Сторона: both.
 */
public class HeatCapacityRegistry {

    private static final Map<Block, Float> CAPACITIES = new HashMap<>();

    static {
        // Металлы (быстро нагреваются и остывают)
        CAPACITIES.put(Blocks.IRON_BLOCK, 0.5f);
        CAPACITIES.put(Blocks.COPPER_BLOCK, 0.4f);
        CAPACITIES.put(Blocks.GOLD_BLOCK, 0.3f);

        // Камень (средне)
        CAPACITIES.put(Blocks.STONE, 1.0f);
        CAPACITIES.put(Blocks.DEEPSLATE, 1.2f);
        CAPACITIES.put(Blocks.COBBLESTONE, 1.1f);

        // Дерево (хороший изолятор, но легко воспламеняется)
        CAPACITIES.put(Blocks.OAK_PLANKS, 2.5f);
        CAPACITIES.put(Blocks.OAK_LOG, 3.0f);

        // Стекло (изолятор)
        CAPACITIES.put(Blocks.GLASS, 2.0f);

        // Земля/трава
        CAPACITIES.put(Blocks.DIRT, 1.5f);
        CAPACITIES.put(Blocks.GRASS_BLOCK, 1.5f);

        // Вода и лёд
        CAPACITIES.put(Blocks.WATER, 4.2f);
        CAPACITIES.put(Blocks.ICE, 2.1f);
        CAPACITIES.put(Blocks.SNOW_BLOCK, 2.0f);
    }

    public static float getCapacity(Block block) {
        return CAPACITIES.getOrDefault(block, 1.0f);
    }

    public static void register(Block block, float capacity) {
        CAPACITIES.put(block, capacity);
    }
}