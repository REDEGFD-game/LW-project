package com.livingworld.hydrology.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SnowCommand {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("lw_b1_snow")
                .executes(context -> {
                    ServerLevel level = context.getSource().getServer().overworld();
                    BlockPos playerPos = context.getSource().getPlayerOrException().blockPosition();

                    int count = 0;
                    for (int x = -5; x <= 5; x++) {
                        for (int z = -5; z <= 5; z++) {
                            int wx = playerPos.getX() + x;
                            int wz = playerPos.getZ() + z;
                            int wy = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, wx, wz);

                            BlockPos surface = new BlockPos(wx, wy - 1, wz);
                            BlockPos air = new BlockPos(wx, wy, wz);

                            BlockState here = level.getBlockState(surface);
                            BlockState above = level.getBlockState(air);

                            if (!here.isAir() && here.isSolid() && above.isAir()) {
                                level.setBlockAndUpdate(air, Blocks.SNOW_BLOCK.defaultBlockState());
                                count++;
                            }
                        }
                    }

                    final int finalCount = count;
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§aПоставлено снега: §f" + finalCount + " §a(в радиусе 5 блоков)"
                    ), false);
                    return 1;
                }));
    }
}