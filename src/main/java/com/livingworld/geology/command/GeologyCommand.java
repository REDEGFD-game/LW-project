package com.livingworld.geology.command;

import com.livingworld.geology.data.GeologyWorldData;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GeologyCommand {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("lw_b6_info")
                .executes(context -> {
                    ServerLevel overworld = context.getSource().getServer().overworld();
                    GeologyWorldData data = overworld.getDataStorage().computeIfAbsent(
                            new SavedData.Factory<>(GeologyWorldData::create, GeologyWorldData::load, null),
                            GeologyWorldData.DATA_NAME
                    );
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§6[B6 Geology] §eЧанков с данными: §f" + data.stability.size()
                    ), false);
                    return 1;
                }));
    }
}