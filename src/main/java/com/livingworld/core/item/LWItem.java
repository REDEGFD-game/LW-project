package com.livingworld.core.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Базовый предмет LW с расширенными параметрами.
 * Сторона: both.
 */
public class LWItem extends Item {

    protected final LWItemProperties lwProps;

    public LWItem(LWItemProperties props) {
        super(new Item.Properties());
        this.lwProps = props;
    }

    public LWItemProperties getLWProperties() { return lwProps; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Категория: §f" + lwProps.category.name()));
        tooltip.add(Component.literal("§7Редкость: §f" + lwProps.rarity.name()));
        tooltip.add(Component.literal("§7Вес: §f" + lwProps.weight + " кг"));
        if (lwProps.price > 0) {
            tooltip.add(Component.literal("§7Цена: §f" + lwProps.price));
        }
        if (lwProps.durability > 0) {
            tooltip.add(Component.literal("§7Прочность: §f" + lwProps.durability));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}