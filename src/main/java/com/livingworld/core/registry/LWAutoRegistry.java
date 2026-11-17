package com.livingworld.core.registry;

import com.livingworld.core.registry.annotations.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Автоматическая регистрация блоков, предметов, BlockEntity и жидкостей
 * через аннотации @RegisterBlock, @RegisterItem, @RegisterBlockEntity, @RegisterFluid.
 * Сторона: both.
 */
public class LWAutoRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger("LW-Registry");

    private static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, "lw_core");
    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "lw_core");
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "lw_core");
    private static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, "lw_core");

    private static final Map<String, RegistryObject<Block>> REGISTERED_BLOCKS = new HashMap<>();

    public static void scan(String rootPackage) {
        LOGGER.info("[LW-Registry] Сканирование пакета {}...", rootPackage);
        Reflections reflections = new Reflections(rootPackage);

        // 1. Блоки
        Set<Class<?>> blockClasses = reflections.getTypesAnnotatedWith(RegisterBlock.class);
        for (Class<?> clazz : blockClasses) {
            try {
                RegisterBlock ann = clazz.getAnnotation(RegisterBlock.class);
                if (!Block.class.isAssignableFrom(clazz)) {
                    LOGGER.warn("[LW-Registry] {} помечен @RegisterBlock, но не наследует Block", clazz.getName());
                    continue;
                }
                Class<? extends Block> blockClass = clazz.asSubclass(Block.class);
                RegistryObject<Block> reg = BLOCKS.register(ann.value(), () -> {
                    try {
                        Constructor<? extends Block> ctor = blockClass.getConstructor(Block.Properties.class);
                        return ctor.newInstance(Block.Properties.of());
                    } catch (NoSuchMethodException e) {
                        try {
                            return blockClass.getDeclaredConstructor().newInstance();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
                REGISTERED_BLOCKS.put(ann.value(), reg);
                ITEMS.register(ann.value(), () -> new BlockItem(reg.get(), new Item.Properties()));
                LOGGER.info("[LW-Registry] Блок зарегистрирован: {}", ann.value());
            } catch (Exception e) {
                LOGGER.error("[LW-Registry] Ошибка регистрации блока {}: {}", clazz.getName(), e.getMessage());
            }
        }

        // 2. Предметы
        Set<Class<?>> itemClasses = reflections.getTypesAnnotatedWith(RegisterItem.class);
        for (Class<?> clazz : itemClasses) {
            try {
                RegisterItem ann = clazz.getAnnotation(RegisterItem.class);
                if (!Item.class.isAssignableFrom(clazz)) {
                    LOGGER.warn("[LW-Registry] {} помечен @RegisterItem, но не наследует Item", clazz.getName());
                    continue;
                }
                Class<? extends Item> itemClass = clazz.asSubclass(Item.class);
                ITEMS.register(ann.value(), () -> {
                    try {
                        return itemClass.getDeclaredConstructor(Item.Properties.class).newInstance(new Item.Properties());
                    } catch (NoSuchMethodException e) {
                        try {
                            return itemClass.getDeclaredConstructor().newInstance();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
                LOGGER.info("[LW-Registry] Предмет зарегистрирован: {}", ann.value());
            } catch (Exception e) {
                LOGGER.error("[LW-Registry] Ошибка регистрации предмета {}: {}", clazz.getName(), e.getMessage());
            }
        }

        // 3. BlockEntity (упрощённая регистрация)
        Set<Class<?>> beClasses = reflections.getTypesAnnotatedWith(RegisterBlockEntity.class);
        for (Class<?> clazz : beClasses) {
            try {
                RegisterBlockEntity ann = clazz.getAnnotation(RegisterBlockEntity.class);
                if (!BlockEntity.class.isAssignableFrom(clazz)) {
                    LOGGER.warn("[LW-Registry] {} помечен @RegisterBlockEntity, но не наследует BlockEntity", clazz.getName());
                    continue;
                }
                RegistryObject<Block> block = REGISTERED_BLOCKS.get(ann.blockClass());
                if (block == null) {
                    LOGGER.warn("[LW-Registry] BlockEntity {}: блок {} не найден", clazz.getName(), ann.blockClass());
                    continue;
                }
                @SuppressWarnings("unchecked")
                Class<? extends BlockEntity> beClass = (Class<? extends BlockEntity>) clazz;
                BLOCK_ENTITIES.register(ann.value(), () ->
                        BlockEntityType.Builder.of((pos, state) -> {
                            try {
                                return beClass.getConstructor(net.minecraft.core.BlockPos.class,
                                        net.minecraft.world.level.block.state.BlockState.class).newInstance(pos, state);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }, block.get()).build(null));
                LOGGER.info("[LW-Registry] BlockEntity зарегистрирован: {}", ann.value());
            } catch (Exception e) {
                LOGGER.error("[LW-Registry] Ошибка регистрации BlockEntity {}: {}", clazz.getName(), e.getMessage());
            }
        }

        // 4. Жидкости
        Set<Class<?>> fluidClasses = reflections.getTypesAnnotatedWith(RegisterFluid.class);
        for (Class<?> clazz : fluidClasses) {
            try {
                RegisterFluid ann = clazz.getAnnotation(RegisterFluid.class);
                if (!Fluid.class.isAssignableFrom(clazz)) {
                    LOGGER.warn("[LW-Registry] {} помечен @RegisterFluid, но не наследует Fluid", clazz.getName());
                    continue;
                }
                Class<? extends Fluid> fluidClass = clazz.asSubclass(Fluid.class);
                FLUIDS.register(ann.value(), () -> {
                    try {
                        return fluidClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                LOGGER.info("[LW-Registry] Жидкость зарегистрирована: {}", ann.value());
            } catch (Exception e) {
                LOGGER.error("[LW-Registry] Ошибка регистрации жидкости {}: {}", clazz.getName(), e.getMessage());
            }
        }

        LOGGER.info("[LW-Registry] Сканирование завершено.");
    }

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
        FLUIDS.register(modBus);
    }
}