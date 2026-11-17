package com.livingworld.timeevents.command;

import com.livingworld.timeevents.calendar.Calendar;
import com.livingworld.timeevents.data.TimeEventsData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CalendarCommand {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("lw_season")
                .executes(ctx -> {
                    ServerLevel level = ctx.getSource().getServer().overworld();
                    TimeEventsData data = level.getDataStorage().computeIfAbsent(
                            new SavedData.Factory<>(TimeEventsData::create, TimeEventsData::load, null),
                            TimeEventsData.DATA_NAME
                    );
                    Calendar cal = data.getCalendar();
                    ctx.getSource().sendSuccess(() -> Component.literal(
                            "§6Сезон: §e" + cal.getSeason() + " §7| Год: §f" + cal.getYear()
                                    + " §7| День: §f" + (cal.getDayOfSeason() + 1) + "/30"
                    ), false);
                    return 1;
                })
                .then(Commands.literal("set")
                        .then(Commands.argument("season", StringArgumentType.word())
                                .executes(ctx -> {
                                    String input = StringArgumentType.getString(ctx, "season").toUpperCase();
                                    Calendar.Season target;
                                    try {
                                        target = Calendar.Season.valueOf(input);
                                    } catch (IllegalArgumentException e) {
                                        ctx.getSource().sendFailure(Component.literal(
                                                "§cНеверный сезон. Доступно: SPRING, SUMMER, AUTUMN, WINTER"
                                        ));
                                        return 0;
                                    }
                                    ServerLevel level = ctx.getSource().getServer().overworld();
                                    TimeEventsData data = level.getDataStorage().computeIfAbsent(
                                            new SavedData.Factory<>(TimeEventsData::create, TimeEventsData::load, null),
                                            TimeEventsData.DATA_NAME
                                    );
                                    Calendar cal = data.getCalendar();
                                    int targetDay = target.ordinal() * Calendar.DAYS_PER_SEASON;
                                    int diff = (targetDay - cal.getDay() + Calendar.DAYS_PER_YEAR) % Calendar.DAYS_PER_YEAR;
                                    for (int i = 0; i < diff; i++) cal.advanceDay();
                                    data.setDirty();

                                    ctx.getSource().sendSuccess(() -> Component.literal(
                                            "§aСезон изменён на §e" + target + "§a. Календарь: " + cal
                                    ), true);
                                    return 1;
                                })))
                .then(Commands.literal("next")
                        .executes(ctx -> {
                            ServerLevel level = ctx.getSource().getServer().overworld();
                            TimeEventsData data = level.getDataStorage().computeIfAbsent(
                                    new SavedData.Factory<>(TimeEventsData::create, TimeEventsData::load, null),
                                    TimeEventsData.DATA_NAME
                            );
                            Calendar cal = data.getCalendar();
                            for (int i = 0; i < Calendar.DAYS_PER_SEASON; i++) cal.advanceDay();
                            data.setDirty();
                            ctx.getSource().sendSuccess(() -> Component.literal(
                                    "§aПромотан один сезон. Сейчас: §e" + cal
                            ), true);
                            return 1;
                        })));
    }
}