package com.livingworld.hydrology.system;

import com.livingworld.hydrology.data.HydrologyWorldData;
import com.livingworld.timeevents.calendar.Calendar;
import com.livingworld.timeevents.data.TimeEventsData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class GlacierSystem {
    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B1");
    private static final Random RANDOM = new Random();

    private static final int GLACIER_MIN_Y = 150;

    public static void tick(ServerLevel level, HydrologyWorldData hydro) {
        TimeEventsData timeData = level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(TimeEventsData::create, TimeEventsData::load, null),
                TimeEventsData.DATA_NAME
        );
        Calendar.Season season = timeData.getCalendar().getSeason();
        boolean isWinter = (season == Calendar.Season.WINTER);
        boolean isSummer = (season == Calendar.Season.SUMMER);

        level.players().forEach(player -> {
            int pcx = player.blockPosition().getX() >> 4;
            int pcz = player.blockPosition().getZ() >> 4;

            for (int dx = -3; dx <= 3; dx++) {
                for (int dz = -3; dz <= 3; dz++) {
                    int cx = pcx + dx;
                    int cz = pcz + dz;

                    int x = (cx << 4) + 8;
                    int z = (cz << 4) + 8;

                    int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                    if (y <= GLACIER_MIN_Y) continue;

                    long key = HydrologyWorldData.chunkKey(cx, cz);
                    float airTemp = hydro.temperature.getOrDefault(key, 15f);
                    if (isWinter) airTemp -= 10f;
                    if (isSummer) airTemp += 15f;

                    BlockPos surface = new BlockPos(x, y - 1, z);
                    BlockPos air = new BlockPos(x, y, z);

                    BlockState here = level.getBlockState(surface);
                    BlockState above = level.getBlockState(air);

                    // ===== ЗИМА: СТАВИМ СНЕГ =====
                    if (airTemp < 0f) {
                        if (!here.isAir() && here.isSolid() && above.isAir()) {
                            level.setBlockAndUpdate(air, Blocks.SNOW_BLOCK.defaultBlockState());
                            float flow = hydro.riverFlow.getOrDefault(key, 0f);
                            hydro.riverFlow.put(key, flow + 0.01f);
                        }
                    }

                    // ===== ЛЕТО: ТАЕМ 100% =====
                    else if (isSummer) {
                        // Ищем снег в 5 блоках вверх
                        for (int dy = 0; dy <= 5; dy++) {
                            BlockPos snowPos = air.above(dy);
                            if (level.getBlockState(snowPos).is(Blocks.SNOW_BLOCK)) {
                                // 1. Снег тает — удаляем
                                level.removeBlock(snowPos, false);
                                // 2. Ставим ВОДУ на его место
                                level.setBlockAndUpdate(snowPos, Blocks.WATER.defaultBlockState());
                                // 3. Питаем реки
                                float flow = hydro.riverFlow.getOrDefault(key, 0f);
                                hydro.riverFlow.put(key, flow + 0.1f);
                            }
                        }
                    }
                }
            }
        });

        hydro.setDirty();
    }
}