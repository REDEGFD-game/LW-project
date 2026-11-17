package com.livingworld.fluiddynamics.registry;

import com.livingworld.fluiddynamics.FluidDynamicsMod;
import com.livingworld.fluiddynamics.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FluidDynamicsMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FluidDynamicsMod.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FluidDynamicsMod.MOD_ID);

    public static final RegistryObject<Block> PIPE = BLOCKS.register("fluid_pipe",
            () -> new FluidPipeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(1.0f)));
    public static final RegistryObject<Block> PUMP = BLOCKS.register("pump",
            () -> new PumpBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.0f)));

    public static final RegistryObject<Item> PIPE_ITEM = ITEMS.register("fluid_pipe", () -> new BlockItem(PIPE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PUMP_ITEM = ITEMS.register("pump", () -> new BlockItem(PUMP.get(), new Item.Properties()));

    public static final RegistryObject<BlockEntityType<FluidPipeBlockEntity>> PIPE_BE =
            BLOCK_ENTITIES.register("fluid_pipe", () -> BlockEntityType.Builder.of(FluidPipeBlockEntity::new, PIPE.get()).build(null));
    public static final RegistryObject<BlockEntityType<PumpBlockEntity>> PUMP_BE =
            BLOCK_ENTITIES.register("pump", () -> BlockEntityType.Builder.of(PumpBlockEntity::new, PUMP.get()).build(null));

    public static void register(net.minecraftforge.eventbus.api.IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
    }
}