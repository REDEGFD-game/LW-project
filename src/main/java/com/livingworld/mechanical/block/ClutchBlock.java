package com.livingworld.mechanical.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ClutchBlock extends Block implements EntityBlock {
    public ClutchBlock(Properties properties) { super(properties); }

    @Nullable @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new ClutchBlockEntity(pos, state); }

    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof ClutchBlockEntity clutch) ClutchBlockEntity.tick(lvl, pos, st, clutch);
        };
    }

    // Вызывается, когда кликают ПУСТОЙ рукой
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        toggleClutch(level, pos, player);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // Вызывается, когда кликают С ПРЕДМЕТОМ в руке
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        toggleClutch(level, pos, player);
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private void toggleClutch(Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ClutchBlockEntity clutch) {
                clutch.toggle();
                player.displayClientMessage(Component.literal(
                        "Муфта: " + (clutch.isEngaged() ? "§aВКЛ" : "§cВЫКЛ")), true);
            }
        }
    }
}