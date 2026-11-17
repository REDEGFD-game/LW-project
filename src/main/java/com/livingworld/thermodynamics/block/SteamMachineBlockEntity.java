package com.livingworld.thermodynamics.block;

import com.livingworld.core.api.IRotation;
import com.livingworld.core.api.IRotationConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * BlockEntity паровой машины.
 * Сторона: both.
 * Потребляет тепло, выдаёт вращение.
 */
public class SteamMachineBlockEntity extends BlockEntity implements IRotation {

    private float temperature = 15.0f;
    private double torque = 0;
    private double speed = 0;

    public SteamMachineBlockEntity(BlockPos pos, BlockState state) {
        super(com.livingworld.thermodynamics.registry.ThermodynamicsRegistry.STEAM_MACHINE_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SteamMachineBlockEntity machine) {
        if (level.isClientSide) return;

        // Генерация вращения от температуры
        if (machine.temperature > 100f) {
            machine.torque = (machine.temperature - 100f) * 0.1;
            machine.speed = 10.0 + (machine.temperature - 100f) * 0.05;
        } else {
            machine.torque = 0;
            machine.speed = 0;
        }

        // Если машина работает, передаём вращение соседним блокам (валам)
        if (machine.speed > 0) {
            for (Direction dir : Direction.values()) {
                BlockEntity be = level.getBlockEntity(pos.relative(dir));
                if (be instanceof IRotationConsumer consumer) {
                    consumer.receiveRotation(machine);
                }
            }
        }
    }

    @Override public double getTorque() { return torque; }
    @Override public double getSpeed() { return speed; }
    @Override public int getDirection() { return 1; }

    public float getTemperature() { return temperature; }
    public void setTemperature(float temp) { this.temperature = temp; setChanged(); }

    @Override
    protected void saveAdditional(net.minecraft.nbt.CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("temperature", temperature);
    }

    @Override
    protected void loadAdditional(net.minecraft.nbt.CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.temperature = tag.getFloat("temperature");
    }
}