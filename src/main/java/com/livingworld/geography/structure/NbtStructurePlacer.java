package com.livingworld.geography.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class NbtStructurePlacer implements IStructurePlacer {

    @Override
    public boolean place(ServerLevel level, BlockPos pos, String structurePath) {
        ResourceLocation id = ResourceLocation.tryParse(structurePath);
        if (id == null) {
            System.err.println("[LW-Geography] Неверный ResourceLocation: " + structurePath);
            return false;
        }

        StructureTemplateManager manager = level.getStructureManager();
        StructureTemplate template = manager.getOrCreate(id);

        if (template == null || template.getSize().equals(BlockPos.ZERO)) {
            System.err.println("[LW-Geography] Структура не найдена или пуста: " + structurePath);
            return false;
        }

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setIgnoreEntities(false)
                .setFinalizeEntities(true);

        boolean placed = template.placeInWorld(
                level,
                pos,
                pos,
                settings,
                level.getRandom(),
                2
        );

        if (!placed) {
            System.err.println("[LW-Geography] Не удалось разместить: " + structurePath);
        }
        return placed;
    }
}