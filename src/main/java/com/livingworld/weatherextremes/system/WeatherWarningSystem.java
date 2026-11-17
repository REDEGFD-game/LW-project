package com.livingworld.weatherextremes.system;

import com.livingworld.weatherextremes.data.WeatherData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class WeatherWarningSystem {
    public static void tick(ServerLevel level, WeatherData data) {
        for (ServerPlayer player : level.players()) {
            for (WeatherData.Storm storm : data.activeStorms) {
                Vec3 stormPos = new Vec3(storm.x, 64, storm.z);
                double dist = player.position().distanceTo(stormPos);
                
                // Если игрок в радиусе 64 блоков от шторма
                if (dist < 64) {
                    // Отправляем сообщение, если у игрока его еще нет (проверка через кулдаун или NBT)
                    // Для простоты шлем каждый раз, но с задержкой (в тиках)
                    if (level.getGameTime() % 600 == 0) { // раз в 30 секунд
                        player.sendSystemMessage(Component.literal(
                                "§c⚠ ВНИМАНИЕ: Вы в зоне шторма! Ищите укрытие! §c⚠"
                        ));
                    }
                }
            }
        }
    }
}