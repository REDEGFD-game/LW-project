package com.livingworld.mechanical.block;

import com.livingworld.core.api.IRotation;
import com.livingworld.core.api.IRotationConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShaftBlockEntity extends BlockEntity implements IRotationConsumer {

    private double torque = 0;
    private double speed = 0;

    public ShaftBlockEntity(BlockPos pos, BlockState state) {
        super(com.livingworld.mechanical.registry.MechanicalRegistry.SHAFT_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ShaftBlockEntity shaft) {
        if (level.isClientSide) return;

        // Если на валу есть вращение, передаём его соседним валам
        if (shaft.speed > 0) {
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = pos.relative(dir);
                BlockEntity be = level.getBlockEntity(neighbor);
                if (be instanceof IRotationConsumer consumer && be != shaft) {
                    consumer.receiveRotation(new IRotation() {
                        @Override public double getTorque() { return shaft.torque; }
                        @Override public double getSpeed() { return shaft.speed; }
                        @Override public int getDirection() { return 1; }
                    });
                }
            }
        }
    }

    @Override
    public void receiveRotation(IRotation rotation) {
        this.torque = rotation.getTorque();
        this.speed = rotation.getSpeed();
        setChanged();
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
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.torque = tag.getDouble("torque");
        this.speed = tag.getDouble("speed");
    }
}