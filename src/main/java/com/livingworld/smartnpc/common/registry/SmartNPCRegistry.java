package com.livingworld.smartnpc.common.registry;

import com.livingworld.smartnpc.common.entity.SmartNPCEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SmartNPCRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "lw_smartnpc");

    public static final RegistryObject<EntityType<SmartNPCEntity>> SMART_NPC =
            ENTITIES.register("smart_npc", () ->
                    EntityType.Builder.of(SmartNPCEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.8f)
                            .build("smart_npc"));
}