package com.livingworld.weatherextremes.system;

import com.livingworld.weatherextremes.config.WeatherConfig;
import com.livingworld.weatherextremes.data.WeatherData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Random;

public class LightningSystem {
    private static final Random RANDOM = new Random();

    public static void tick(ServerLevel level, WeatherData data) {
        double chance = WeatherConfig.LIGHTNING_CHANCE.get();
        for (WeatherData.Storm storm : data.activeStorms) {
            if (RANDOM.nextDouble() < chance) {
                int x = storm.x + RANDOM.nextInt(64) - 32;
                int z = storm.z + RANDOM.nextInt(64) - 32;
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
                if (bolt != null) {
                    bolt.moveTo(x, y, z);
                    level.addFreshEntity(bolt);
                }
            }
        }
    }
}