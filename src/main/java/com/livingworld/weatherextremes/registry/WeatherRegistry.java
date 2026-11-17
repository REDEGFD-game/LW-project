package com.livingworld.weatherextremes.registry;

import com.livingworld.weatherextremes.WeatherExtremesMod;
import com.livingworld.weatherextremes.entity.HurricaneEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WeatherRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WeatherExtremesMod.MOD_ID);

    public static final RegistryObject<EntityType<HurricaneEntity>> HURRICANE = ENTITIES.register("hurricane",
            () -> EntityType.Builder.of(HurricaneEntity::new, MobCategory.MISC)
                    .sized(4.0F, 4.0F) // Размер хитбокса
                    .build("hurricane"));

    public static void register(net.minecraftforge.eventbus.api.IEventBus modBus) {
        ENTITIES.register(modBus);
    }
}