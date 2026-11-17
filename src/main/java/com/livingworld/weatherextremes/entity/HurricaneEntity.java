package com.livingworld.weatherextremes.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class HurricaneEntity extends Mob {
    public HurricaneEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        this.setDeltaMovement(0.1, 0, 0);
        this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());

        if (this.tickCount % 20 == 0) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            BlockPos center = this.blockPosition();
            AABB area = new AABB(center).inflate(10);

            for (BlockPos pos : BlockPos.betweenClosed(
                    center.offset(-10, -10, -10), center.offset(10, 10, 10))) {
                var state = serverLevel.getBlockState(pos);
                if (state.is(Blocks.OAK_LOG) || state.is(Blocks.OAK_LEAVES)) {
                    serverLevel.destroyBlock(pos, true, this);
                }
            }

            List<LivingEntity> entities = serverLevel.getEntitiesOfClass(LivingEntity.class, area);
            for (LivingEntity entity : entities) {
                if (entity != this) {
                    entity.hurt(serverLevel.damageSources().flyIntoWall(), 5.0f);
                }
            }
        }
    }

    @Override
    public boolean isPushable() { return false; }
}