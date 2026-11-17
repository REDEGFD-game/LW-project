package com.livingworld.thermodynamics.command;
import com.livingworld.thermodynamics.data.ThermodynamicsWorldData;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
public class ThermodynamicsCommand {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("lw_b2_info")
                .executes(context -> {
                    ServerLevel overworld = context.getSource().getServer().overworld();
                    ThermodynamicsWorldData data = overworld.getDataStorage().computeIfAbsent(
                            new SavedData.Factory<>(ThermodynamicsWorldData::create, ThermodynamicsWorldData::load, null),
                            ThermodynamicsWorldData.DATA_NAME
                    );
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§6[B2 Thermodynamics] §eЧанков с теплом: §f" + data.temperature.size()
                    ), false);
                    return 1;
                }));
    }
}