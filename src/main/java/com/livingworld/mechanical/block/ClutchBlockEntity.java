package com.livingworld.mechanical.block;

import com.livingworld.core.api.IRotation;
import com.livingworld.core.api.IRotationConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ClutchBlockEntity extends BlockEntity implements IRotationConsumer {

    private double torque = 0;
    private double speed = 0;
    private boolean engaged = true;

    public ClutchBlockEntity(BlockPos pos, BlockState state) {
        super(com.livingworld.mechanical.registry.MechanicalRegistry.CLUTCH_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ClutchBlockEntity clutch) {
        if (level.isClientSide) return;

        if (clutch.engaged && clutch.speed > 0) {
            for (Direction dir : Direction.values()) {
                BlockEntity be = level.getBlockEntity(pos.relative(dir));
                if (be instanceof IRotationConsumer consumer && be != clutch) {
                    // ИСПРАВЛЕНО: передаём getRotation(), а не сам BlockEntity
                    consumer.receiveRotation(clutch.getRotation());
                }
            }
        }
    }

    public void toggle() {
        this.engaged = !this.engaged;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean isEngaged() { return engaged; }

    @Override
    public void receiveRotation(IRotation rotation) {
        if (engaged) {
            this.speed = rotation.getSpeed();
            this.torque = rotation.getTorque();
            setChanged();
        }
    }

    @Override
    public IRotation getRotation() {
        return new IRotation() {
            @Override public double getTorque() { return torque; }
            @Override public double getSpeed() { return speed; }
            @Override public int getDirection() { return 1; }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putDouble("torque", torque);
        tag.putDouble("speed", speed);
        tag.putBoolean("engaged", engaged);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.torque = tag.getDouble("torque");
        this.speed = tag.getDouble("speed");
        this.engaged = tag.getBoolean("engaged");
    }
}