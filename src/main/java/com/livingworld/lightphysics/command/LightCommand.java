package com.livingworld.lightphysics.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LightCommand {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("lw_b8_info")
                .executes(context -> {
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§6[B8 Light] §eАктивных источников света: §f" 
                            + com.livingworld.lightphysics.registry.LightSourceRegistry.getSources().size()
                    ), false);
                    return 1;
                }));
    }
}