package com.livingworld.mechanical.registry;

import com.livingworld.mechanical.MechanicalMod;
import com.livingworld.mechanical.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MechanicalRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MechanicalMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MechanicalMod.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MechanicalMod.MOD_ID);

    public static final RegistryObject<Block> SHAFT = BLOCKS.register("shaft",
            () -> new ShaftBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(1.5f)));
    public static final RegistryObject<Block> GEAR = BLOCKS.register("gear",
            () -> new GearBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(1.5f)));
    public static final RegistryObject<Block> CLUTCH = BLOCKS.register("clutch",
            () -> new ClutchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(1.5f)));

    public static final RegistryObject<Item> SHAFT_ITEM = ITEMS.register("shaft", () -> new BlockItem(SHAFT.get(), new Item.Properties()));
    public static final RegistryObject<Item> GEAR_ITEM = ITEMS.register("gear", () -> new BlockItem(GEAR.get(), new Item.Properties()));
    public static final RegistryObject<Item> CLUTCH_ITEM = ITEMS.register("clutch", () -> new BlockItem(CLUTCH.get(), new Item.Properties()));

    public static final RegistryObject<BlockEntityType<ShaftBlockEntity>> SHAFT_BE =
            BLOCK_ENTITIES.register("shaft", () -> BlockEntityType.Builder.of(ShaftBlockEntity::new, SHAFT.get()).build(null));
    public static final RegistryObject<BlockEntityType<GearBlockEntity>> GEAR_BE =
            BLOCK_ENTITIES.register("gear", () -> BlockEntityType.Builder.of(GearBlockEntity::new, GEAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<ClutchBlockEntity>> CLUTCH_BE =
            BLOCK_ENTITIES.register("clutch", () -> BlockEntityType.Builder.of(ClutchBlockEntity::new, CLUTCH.get()).build(null));

    public static void register(net.minecraftforge.eventbus.api.IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
    }
}