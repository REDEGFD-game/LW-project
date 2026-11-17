package com.livingworld.smartnpc;

import com.livingworld.smartnpc.common.entity.SmartNPCEntity;
import com.livingworld.smartnpc.common.registry.SmartNPCRegistry;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(SmartNPCMod.MOD_ID)
public class SmartNPCMod {
    public static final String MOD_ID = "lw_smartnpc";
    public static final Logger LOGGER = LoggerFactory.getLogger("LW-SmartNPC");

    public SmartNPCMod(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        
        SmartNPCRegistry.ENTITIES.register(modBus);
        
        // Регистрация атрибутов для NPC
        modBus.addListener(this::registerAttributes);
        
        LOGGER.info("LW-SmartNPC инициализирован.");
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SmartNPCRegistry.SMART_NPC.get(), SmartNPCEntity.createAttributes().build());
    }
}