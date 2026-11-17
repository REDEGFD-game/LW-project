package com.livingworld.hydrology.command;

import com.livingworld.hydrology.data.HydrologyWorldData;
import com.livingworld.timeevents.calendar.Calendar;
import com.livingworld.timeevents.data.TimeEventsData;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HydrologyCommand {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("lw_b1_info")
                .executes(context -> {
                    ServerLevel overworld = context.getSource().getServer().overworld();
                    HydrologyWorldData data = overworld.getDataStorage().computeIfAbsent(
                            new SavedData.Factory<>(HydrologyWorldData::create, HydrologyWorldData::load, null),
                            HydrologyWorldData.DATA_NAME
                    );

                    TimeEventsData timeData = overworld.getDataStorage().computeIfAbsent(
                            new SavedData.Factory<>(TimeEventsData::create, TimeEventsData::load, null),
                            TimeEventsData.DATA_NAME
                    );

                    context.getSource().sendSuccess(() -> Component.literal(
                            "§6=== B1 Hydrology ==="), false);
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§eСезон: §f" + timeData.getCalendar().getSeason()
                                    + " §7| Год: §f" + timeData.getCalendar().getYear()
                                    + " §7| День: §f" + (timeData.getCalendar().getDayOfSeason() + 1) + "/30"
                    ), false);
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§eЧанков с климатом: §f" + data.temperature.size()), false);
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§eЧанков с осадками: §f" + data.rainfall.size()), false);
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§eЧанков с реками:   §f" + data.riverFlow.size()), false);
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§eГрунтовые воды:    §f" + data.groundwater.size()), false);

                    if (!data.temperature.isEmpty()) {
                        float sum = 0;
                        float min = Float.MAX_VALUE;
                        float max = -Float.MAX_VALUE;
                        for (float v : data.temperature.values()) {
                            sum += v;
                            if (v < min) min = v;
                            if (v > max) max = v;
                        }
                        float avg = sum / data.temperature.size();
                        final float finalMin = min;
                        final float finalMax = max;
                        final float finalAvg = avg;
                        context.getSource().sendSuccess(() -> Component.literal(
                                String.format("§eТемпература: §fср. %.1f°C §7| §fmin %.1f°C §7| §fmax %.1f°C",
                                        finalAvg, finalMin, finalMax)), false);
                    }

                    return 1;
                }));
    }
}