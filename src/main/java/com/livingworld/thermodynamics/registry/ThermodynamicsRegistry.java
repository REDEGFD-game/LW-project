package com.livingworld.thermodynamics.registry;

import com.livingworld.thermodynamics.ThermodynamicsMod;
import com.livingworld.thermodynamics.block.SteamMachineBlock;
import com.livingworld.thermodynamics.block.SteamMachineBlockEntity;
import com.livingworld.thermodynamics.block.ThermalPipeBlock;
import com.livingworld.thermodynamics.block.ThermalPipeBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Реестр блоков, предметов и BlockEntity для B2.
 * Сторона: both.
 */
public class ThermodynamicsRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ThermodynamicsMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ThermodynamicsMod.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ThermodynamicsMod.MOD_ID);

    // --- Блоки ---
    public static final RegistryObject<Block> THERMAL_PIPE = BLOCKS.register("thermal_pipe",
            () -> new ThermalPipeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(1.0f)));

    public static final RegistryObject<Block> STEAM_MACHINE = BLOCKS.register("steam_machine",
            () -> new SteamMachineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.0f)));

    // --- Предметы-блоки (BlockItem) ---
    public static final RegistryObject<Item> THERMAL_PIPE_ITEM = ITEMS.register("thermal_pipe",
            () -> new BlockItem(THERMAL_PIPE.get(), new Item.Properties()));

    public static final RegistryObject<Item> STEAM_MACHINE_ITEM = ITEMS.register("steam_machine",
            () -> new BlockItem(STEAM_MACHINE.get(), new Item.Properties()));

    // --- BlockEntity ---
    public static final RegistryObject<BlockEntityType<ThermalPipeBlockEntity>> THERMAL_PIPE_BE =
            BLOCK_ENTITIES.register("thermal_pipe", () -> BlockEntityType.Builder.of(ThermalPipeBlockEntity::new, THERMAL_PIPE.get()).build(null));

    public static final RegistryObject<BlockEntityType<SteamMachineBlockEntity>> STEAM_MACHINE_BE =
            BLOCK_ENTITIES.register("steam_machine", () -> BlockEntityType.Builder.of(SteamMachineBlockEntity::new, STEAM_MACHINE.get()).build(null));

    public static void register(net.minecraftforge.eventbus.api.IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
    }
}