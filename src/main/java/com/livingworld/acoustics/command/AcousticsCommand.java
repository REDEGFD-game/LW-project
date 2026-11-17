package com.livingworld.acoustics.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AcousticsCommand {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("lw_b7_info")
                .executes(context -> {
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§6[B7 Acoustics] §eАктивных источников звука: §f" 
                            + com.livingworld.acoustics.registry.SoundSourceRegistry.getSources().size()
                    ), false);
                    return 1;
                }));
    }
}