package com.livingworld.smartnpc.client.render;

import com.livingworld.smartnpc.common.entity.SmartNPCEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Рендерер NPC. Подключается ТОЛЬКО из client-пакета.
 * Если этот класс импортируется из common — сервер упадёт.
 */
public class SmartNPCRenderer extends MobRenderer<SmartNPCEntity, HumanoidModel<SmartNPCEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.parse("lw_smartnpc:textures/entity/npc.png");

    public SmartNPCRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(SmartNPCEntity entity) {
        return TEXTURE;
    }
}