package com.livingworld.weatherextremes.command;

import com.livingworld.weatherextremes.data.WeatherData;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WeatherCommand {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("lw_b5_info")
                .executes(context -> {
                    ServerLevel overworld = context.getSource().getServer().overworld();
                    WeatherData data = overworld.getDataStorage().computeIfAbsent(
                            new SavedData.Factory<>(WeatherData::create, WeatherData::load, null),
                            WeatherData.DATA_NAME
                    );
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§6[B5 Weather] §eАктивных штормов: §f" + data.activeStorms.size()
                    ), false);
                    return 1;
                }));
    }
}