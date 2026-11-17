package com.livingworld.fluiddynamics.block;

import com.livingworld.core.api.IFluidNetwork;
import com.livingworld.core.api.IRotation;
import com.livingworld.core.api.IRotationConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PumpBlockEntity extends BlockEntity implements IRotationConsumer {

    private double torque = 0;
    private double speed = 0;

    public PumpBlockEntity(BlockPos pos, BlockState state) {
        super(com.livingworld.fluiddynamics.registry.FluidRegistry.PUMP_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PumpBlockEntity pump) {
        if (level.isClientSide) return;

        // Если насос получает вращение (speed > 0), он качает жидкость
        if (pump.speed > 0) {
            for (Direction dir : Direction.values()) {
                BlockEntity be = level.getBlockEntity(pos.relative(dir));
                if (be instanceof IFluidNetwork network) {
                    // Перекачиваем 10 мБ за тик
                    network.addFluid((int) (pump.speed / 2));
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