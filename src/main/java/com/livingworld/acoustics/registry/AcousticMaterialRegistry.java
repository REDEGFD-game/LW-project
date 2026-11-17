package com.livingworld.acoustics.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

/**
 * Материалы с коэффициентами поглощения звука.
 * Сторона: both.
 */
public class AcousticMaterialRegistry {

    public static class AcousticMaterial {
        public final float absorption;  // 0..1 (1 = полное поглощение)
        public final float reflection;  // 0..1 (1 = полное отражение)

        public AcousticMaterial(float absorption, float reflection) {
            this.absorption = absorption;
            this.reflection = reflection;
        }
    }

    private static final Map<Block, AcousticMaterial> MATERIALS = new HashMap<>();

    static {
        // Камень — хорошо отражает
        MATERIALS.put(Blocks.STONE, new AcousticMaterial(0.1f, 0.9f));
        MATERIALS.put(Blocks.DEEPSLATE, new AcousticMaterial(0.1f, 0.9f));

        // Дерево — поглощает
        MATERIALS.put(Blocks.OAK_PLANKS, new AcousticMaterial(0.6f, 0.4f));
        MATERIALS.put(Blocks.OAK_LOG, new AcousticMaterial(0.5f, 0.5f));

        // Стекло — отражает
        MATERIALS.put(Blocks.GLASS, new AcousticMaterial(0.2f, 0.8f));

        // Шерсть — сильное поглощение
        MATERIALS.put(Blocks.WHITE_WOOL, new AcousticMaterial(0.9f, 0.1f));

        // Земля/трава — среднее
        MATERIALS.put(Blocks.DIRT, new AcousticMaterial(0.4f, 0.6f));
        MATERIALS.put(Blocks.GRASS_BLOCK, new AcousticMaterial(0.4f, 0.6f));
    }

    public static AcousticMaterial getMaterial(Block block) {
        return MATERIALS.getOrDefault(block, new AcousticMaterial(0.3f, 0.7f));
    }

    public static void register(Block block, float absorption, float reflection) {
        MATERIALS.put(block, new AcousticMaterial(absorption, reflection));
    }
}