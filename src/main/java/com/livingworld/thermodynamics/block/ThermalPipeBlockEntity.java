package com.livingworld.thermodynamics.block;

import com.livingworld.thermodynamics.block.ThermalPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * BlockEntity тепловой трубы.
 * Сторона: both.
 */
public class ThermalPipeBlockEntity extends BlockEntity {

    private float temperature = 15.0f;

    public ThermalPipeBlockEntity(BlockPos pos, BlockState state) {
        super(com.livingworld.thermodynamics.registry.ThermodynamicsRegistry.THERMAL_PIPE_BE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("temperature", temperature);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.temperature = tag.getFloat("temperature");
    }

    public float getTemperature() { return temperature; }
    public void setTemperature(float temp) { this.temperature = temp; setChanged(); }
}