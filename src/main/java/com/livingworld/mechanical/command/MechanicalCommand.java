package com.livingworld.mechanical.command;

import com.livingworld.mechanical.data.MechanicalWorldData;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MechanicalCommand {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("lw_b3_info")
                .executes(context -> {
                    ServerLevel overworld = context.getSource().getServer().overworld();
                    MechanicalWorldData data = overworld.getDataStorage().computeIfAbsent(
                            new SavedData.Factory<>(MechanicalWorldData::create, MechanicalWorldData::load, null),
                            MechanicalWorldData.DATA_NAME
                    );
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§6[B3 Mechanical] §eАктивных сетей: §f" + data.activeNetworks
                    ), false);
                    return 1;
                }));
    }
}