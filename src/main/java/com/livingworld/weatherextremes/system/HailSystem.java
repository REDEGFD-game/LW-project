package com.livingworld.weatherextremes.system;

import com.livingworld.weatherextremes.data.WeatherData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class HailSystem {
    public static void tick(ServerLevel level, WeatherData data) {
        for (WeatherData.Storm storm : data.activeStorms) {
            if (storm.intensity < 0.7f) continue;

            BlockPos center = new BlockPos(storm.x, 64, storm.z);
            AABB area = new AABB(center).inflate(32);

            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
            for (LivingEntity entity : entities) {
                if (level.canSeeSky(entity.blockPosition())) {
                    entity.hurt(level.damageSources().generic(), 1.0f);
                }
            }

            if (level.random.nextInt(100) == 0) {
                int x = storm.x + level.random.nextInt(32) - 16;
                int z = storm.z + level.random.nextInt(32) - 16;
                int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                BlockPos pos = new BlockPos(x, y, z);
                if (level.getBlockState(pos.below()).isSolid() && level.getBlockState(pos).isAir()) {
                    level.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
                }
            }
        }
    }
}