package com.livingworld.smartnpc.common.entity;

import com.livingworld.smartnpc.common.data.NPCMemory;
import com.livingworld.smartnpc.common.data.NPCPersonality;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;

/**
 * Базовый NPC. Наследуется от PathfinderMob (серверная логика).
 * ВАЖНО: здесь НЕЛЬЗЯ импортировать net.minecraft.client.*
 */
public class SmartNPCEntity extends PathfinderMob {

    private NPCPersonality personality;
    private final NPCMemory memory = new NPCMemory();
    private int lodLevel = 0;

    public SmartNPCEntity(EntityType<? extends PathfinderMob> type, Level level) {
    super(type, level);
    // level.random имеет тип RandomSource — теперь типы совпадают
    this.personality = NPCPersonality.random(level.random);
    }

    public NPCPersonality getPersonality() { return personality; }
    public NPCMemory getMemory() { return memory; }

    public int getLodLevel() { return lodLevel; }
    public void setLodLevel(int level) { this.lodLevel = level; }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        CompoundTag p = new CompoundTag();
        p.putDouble("o", personality.openness);
        p.putDouble("c", personality.conscientiousness);
        p.putDouble("e", personality.extraversion);
        p.putDouble("a", personality.agreeableness);
        p.putDouble("n", personality.neuroticism);
        tag.put("personality", p);
        tag.putInt("lod", lodLevel);
    }
    public static AttributeSupplier.Builder createAttributes() {
    return PathfinderMob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.25D)
            .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        CompoundTag p = tag.getCompound("personality");
        this.personality = new NPCPersonality(
                p.getDouble("o"), p.getDouble("c"), p.getDouble("e"),
                p.getDouble("a"), p.getDouble("n"));
        this.lodLevel = tag.getInt("lod");
    }

    @Override
    protected void registerGoals() {
    // 0 = наивысший приоритет
    this.goalSelector.addGoal(0, new FloatGoal(this)); // Плавать в воде, чтобы не утонуть
    this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0D)); // Гулять по округе
    this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F)); // Смотреть на игрока
    this.goalSelector.addGoal(3, new RandomLookAroundGoal(this)); // Смотреть по сторонам
    }
}