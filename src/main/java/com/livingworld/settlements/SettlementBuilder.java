package com.livingworld.settlements;

import com.livingworld.settlements.structure.StructureManifest;
import com.livingworld.settlements.structure.StructureRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Optional;

/**
 * Размещает NBT-структуры или процедурный fallback.
 * Сторона: server.
 */
public class SettlementBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-Settlements");

    public static void placeStructure(ServerLevel level, BlockPos pos, String structurePath) {
        // 1. Пробуем найти авторский манифест
        StructureManifest manifest = StructureRegistry.get(structurePath);
        String actualId;
        if (manifest != null) {
            actualId = manifest.id;
            LOGGER.info("[LW-DEBUG] Использую структуру автора: {}", actualId);
        } else {
            actualId = structurePath;
        }

        // 2. Загружаем NBT
        StructureTemplate template = loadTemplate(level, actualId);
        if (template == null || template.getSize().equals(Vec3i.ZERO)) {
            LOGGER.warn("[LW-DEBUG] NBT {} не найден, fallback", actualId);
            placeFallback(level, pos, actualId);
            return;
        }

        // 3. Размещаем ровно в pos (без below!), без ротации и зеркала
        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setIgnoreEntities(false)
                .setFinalizeEntities(true)
                .setRotation(Rotation.NONE)
                .setMirror(Mirror.NONE);

        BlockPos placePos = pos;
        LOGGER.info("[LW-DEBUG] Попытка размещения {} в {} (размер: {})",
                actualId, placePos, template.getSize());

        boolean placed = template.placeInWorld(level, placePos, placePos, settings, level.getRandom(), 2);

        LOGGER.info("[LW-DEBUG] placeInWorld вернул: {}, блок в {} после: {}",
                placed, placePos, level.getBlockState(placePos).getBlock().getName().getString());

        if (!placed) {
            LOGGER.warn("[LW-DEBUG] Не удалось разместить {}, fallback", actualId);
            placeFallback(level, pos, actualId);
        } else {
            LOGGER.info("[LW-DEBUG] Размещена структура {} в {}", actualId, placePos);
        }
    }

    /**
     * Загрузка NBT: сначала getOrCreate, потом ручное чтение из resources/data/lw_settlements/structure/<id>.nbt
     */
    private static StructureTemplate loadTemplate(ServerLevel level, String id) {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath("lw_settlements", id);

        // Попытка 1: стандартный getOrCreate
        StructureTemplateManager manager = level.getStructureManager();
        StructureTemplate t = manager.getOrCreate(loc);
        if (t != null && !t.getSize().equals(Vec3i.ZERO)) {
            return t;
        }

        // Попытка 2: ручное чтение
        ResourceLocation resLoc = ResourceLocation.fromNamespaceAndPath(
                "lw_settlements", "structure/" + id + ".nbt"
        );
        Optional<Resource> res = level.getServer().getResourceManager().getResource(resLoc);
        if (res.isEmpty()) {
            LOGGER.warn("[LW-DEBUG] Ресурс {} не найден", resLoc);
            return null;
        }

        try (InputStream in = res.get().open()) {
            CompoundTag tag = NbtIo.readCompressed(in, NbtAccounter.unlimitedHeap());
            StructureTemplate template = new StructureTemplate();
            template.load(level.holderLookup(net.minecraft.core.registries.Registries.BLOCK), tag);
            LOGGER.info("[LW-DEBUG] Ручная загрузка {} OK, размер: {}", id, template.getSize());
            return template;
        } catch (Exception e) {
            LOGGER.error("[LW-DEBUG] Ошибка ручной загрузки {}: {}", id, e.getMessage());
            return null;
        }
    }

    private static void placeFallback(ServerLevel level, BlockPos pos, String structurePath) {
        LOGGER.info("[LW-DEBUG] NBT {} не найден, использую процедурный fallback", structurePath);

        int half = 2;

        // Пол 5x5 из камня
        for (int dx = -half; dx <= half; dx++) {
            for (int dz = -half; dz <= half; dz++) {
                level.setBlock(pos.offset(dx, -1, dz), Blocks.STONE.defaultBlockState(), 2);
            }
        }

        // Стены 4 блока (с проёмом для двери)
        for (int dy = 0; dy < 4; dy++) {
            for (int dx = -half; dx <= half; dx++) {
                for (int dz = -half; dz <= half; dz++) {
                    boolean isEdge = (dx == -half || dx == half || dz == -half || dz == half);
                    boolean isDoor = (dy == 0 && dx == 0 && dz == -half);
                    if (isEdge && !isDoor) {
                        level.setBlock(pos.offset(dx, dy, dz), Blocks.OAK_PLANKS.defaultBlockState(), 2);
                    }
                }
            }
        }

        // Крыша
        for (int dx = -half; dx <= half; dx++) {
            for (int dz = -half; dz <= half; dz++) {
                level.setBlock(pos.offset(dx, 4, dz), Blocks.OAK_PLANKS.defaultBlockState(), 2);
            }
        }

        LOGGER.info("[LW-DEBUG] Процедурный fallback размещён в {}", pos);
    }
}