package com.livingworld.smartnpc.client;

import com.livingworld.smartnpc.client.render.SmartNPCRenderer;
import com.livingworld.smartnpc.common.registry.SmartNPCRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "lw_smartnpc", bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public class SmartNPCClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SmartNPCRegistry.SMART_NPC.get(), SmartNPCRenderer::new);
    }
}