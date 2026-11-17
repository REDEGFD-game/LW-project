package com.livingworld.core.api;

import net.minecraft.core.BlockPos;
import java.util.List;

public interface IStormTracker {
    /** Список активных штормов (центры). */
    List<BlockPos> getActiveStorms();
}