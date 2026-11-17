package com.livingworld.weatherextremes.system;

import com.livingworld.hydrology.data.HydrologyWorldData;
import com.livingworld.weatherextremes.data.WeatherData;
import com.livingworld.weatherextremes.entity.HurricaneEntity;
import com.livingworld.weatherextremes.registry.WeatherRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class StormSystem {
    private static final Logger LOGGER = LoggerFactory.getLogger("LW-B5");
    private static final Random RANDOM = new Random();

    public static void tick(ServerLevel level, WeatherData data) {
        // Раз в 600 тиков (30 сек) пытаемся создать новый шторм
        if (RANDOM.nextInt(600) != 0) return;

        HydrologyWorldData hydro = level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(HydrologyWorldData::create, HydrologyWorldData::load, null),
                HydrologyWorldData.DATA_NAME
        );

        for (var entry : hydro.humidity.entrySet()) {
            if (entry.getValue() > 0.8f) {
                long key = entry.getKey();
                int cx = HydrologyWorldData.keyX(key);
                int cz = HydrologyWorldData.keyZ(key);

                boolean exists = data.activeStorms.stream().anyMatch(s ->
                        Math.abs(s.x - (cx << 4)) < 64 && Math.abs(s.z - (cz << 4)) < 64);

                if (!exists) {
                    WeatherData.Storm storm = new WeatherData.Storm(cx << 4, cz << 4, 0.5f + RANDOM.nextFloat() * 0.5f);
                    data.activeStorms.add(storm);
                    data.setDirty();
                    LOGGER.info("[LW-B5-DEBUG] Создан новый шторм в чанке {},{}", cx, cz);

                    // Если шторм сильный — спавним ураган
                    if (storm.intensity > 0.9f) {
                        if (level.getEntitiesOfClass(HurricaneEntity.class,
                                new AABB(new BlockPos(storm.x, 64, storm.z)).inflate(64)).isEmpty()) {
                            HurricaneEntity hurricane = WeatherRegistry.HURRICANE.get().create(level);
                            if (hurricane != null) {
                                hurricane.moveTo(storm.x, 80, storm.z, 0, 0);
                                level.addFreshEntity(hurricane);
                                LOGGER.info("[LW-B5-DEBUG] Заспавнен ураган в {},{}", storm.x, storm.z);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}