package com.livingworld.settlements.structure;

import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Валидатор NBT-структур.
 * Сторона: server.
 */
public class StructureValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-Validate");

    private static final int MAX_SIZE_XZ = 256;
    private static final int MAX_SIZE_Y = 384;

    public static boolean validate(ServerLevel level, String id, String category) {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath("lw_settlements", id);
        StructureTemplate template = level.getStructureManager().getOrCreate(loc);

        if (template == null || template.getSize().equals(Vec3i.ZERO)) {
            LOGGER.error("[LW-VALIDATE] Структура {}: FAIL — NBT не найден или пуст", id);
            return false;
        }

        Vec3i size = template.getSize();
        if (size.getX() > MAX_SIZE_XZ || size.getZ() > MAX_SIZE_XZ) {
            LOGGER.error("[LW-VALIDATE] Структура {}: FAIL — размер {}x{} больше допустимого {}x{}",
                    id, size.getX(), size.getZ(), MAX_SIZE_XZ, MAX_SIZE_XZ);
            return false;
        }
        if (size.getY() > MAX_SIZE_Y) {
            LOGGER.error("[LW-VALIDATE] Структура {}: FAIL — высота {} больше {}", id, size.getY(), MAX_SIZE_Y);
            return false;
        }

        // Проверка запрещённых блоков через сохранение в NBT
        CompoundTag tag = template.save(new CompoundTag());
        ListTag palette = tag.getList("palette", Tag.TAG_COMPOUND);
        for (int i = 0; i < palette.size(); i++) {
            String blockName = palette.getCompound(i).getString("Name");
            if (blockName.equals("minecraft:bedrock") || blockName.equals("minecraft:barrier")
                    || blockName.equals("minecraft:command_block")) {
                LOGGER.error("[LW-VALIDATE] Структура {}: FAIL — запрещённый блок {}", id, blockName);
                return false;
            }
        }

        LOGGER.info("[LW-VALIDATE] Структура {}: OK ({}x{}x{})", id, size.getX(), size.getY(), size.getZ());
        return true;
    }
}