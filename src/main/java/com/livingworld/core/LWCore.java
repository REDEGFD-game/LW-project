package com.livingworld.core;

import com.livingworld.core.compat.ModCompatRegistry;
import com.livingworld.fluiddynamics.block.OilBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(LWCore.MOD_ID)
public class LWCore {
    public static final String MOD_ID = "lw_core";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-Core");

    private static IEventBus MOD_BUS;

    // --- Регистрация (вручную) ---
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);

    public static final RegistryObject<Block> OIL_BLOCK = BLOCKS.register("oil_block", OilBlock::new);
    public static final RegistryObject<Item> OIL_BLOCK_ITEM = ITEMS.register("oil_block",
            () -> new BlockItem(OIL_BLOCK.get(), new Item.Properties()));

    public LWCore(FMLJavaModLoadingContext context) {
        MOD_BUS = context.getModEventBus();

        BLOCKS.register(MOD_BUS);
        ITEMS.register(MOD_BUS);

        ModCompatRegistry.init();
        LOGGER.info("LW-Core инициализирован. Единицы: J, W, N·m, L/s.");
    }

    public static IEventBus getModEventBus() {
        if (MOD_BUS == null) throw new IllegalStateException("LWCore ещё не инициализирован!");
        return MOD_BUS;
    }
}