package com.livingworld.geography.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface IStructurePlacer {
    boolean place(ServerLevel level, BlockPos pos, String structurePath);
}