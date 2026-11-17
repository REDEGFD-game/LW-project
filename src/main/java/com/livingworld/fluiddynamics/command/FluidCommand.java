package com.livingworld.fluiddynamics.command;

import com.livingworld.fluiddynamics.data.FluidWorldData;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Команда /lw_b4_info.
 * Сторона: server.
 */
public class FluidCommand {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("lw_b4_info")
                .executes(context -> {
                    ServerLevel overworld = context.getSource().getServer().overworld();
                    FluidWorldData data = overworld.getDataStorage().computeIfAbsent(
                            new SavedData.Factory<>(FluidWorldData::create, FluidWorldData::load, null),
                            FluidWorldData.DATA_NAME
                    );

                    context.getSource().sendSuccess(() -> Component.literal(
                            "§6=== B4 Fluid Dynamics ==="), false);
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§eЧанков с давлением: §f" + data.pressure.size()), false);

                    return 1;
                }));
    }
}