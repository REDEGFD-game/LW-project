package com.livingworld.fluiddynamics.block;

import com.livingworld.core.api.IFluidNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FluidPipeBlockEntity extends BlockEntity implements IFluidNetwork {

    private int fluidAmount = 0;
    private double pressure = 0;

    public FluidPipeBlockEntity(BlockPos pos, BlockState state) {
        super(com.livingworld.fluiddynamics.registry.FluidRegistry.PIPE_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FluidPipeBlockEntity pipe) {
        if (level.isClientSide) return;
        
        // Простая логика: трубы выравнивают давление с соседями (через FluidGrid в будущем)
        // Пока что просто храним данные.
    }

    @Override public int getFluidAmount() { return fluidAmount; }
    @Override public double getPressure() { return pressure; }

    @Override
    public void addFluid(int amount) {
        this.fluidAmount += amount;
        setChanged();
    }

    @Override
    public int drainFluid(int amount) {
        int drained = Math.min(this.fluidAmount, amount);
        this.fluidAmount -= drained;
        setChanged();
        return drained;
    }

    @Override
    public boolean isConnected(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof IFluidNetwork;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("fluid", fluidAmount);
        tag.putDouble("pressure", pressure);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.fluidAmount = tag.getInt("fluid");
        this.pressure = tag.getDouble("pressure");
    }
}