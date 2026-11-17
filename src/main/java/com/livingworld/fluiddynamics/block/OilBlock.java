package com.livingworld.fluiddynamics.block;

import com.livingworld.core.block.LWBlock;
import com.livingworld.core.registry.annotations.RegisterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

@RegisterBlock("oil_block")
public class OilBlock extends LWBlock {

    public OilBlock() {
        super(Block.Properties.of()
                .mapColor(MapColor.COLOR_BLACK)
                .strength(0.5f)
                .sound(SoundType.SLIME_BLOCK), // имитация «липкости» нефти
                0.9f,  // вес
                80.0f  // температурная стойкость
        );
    }
}