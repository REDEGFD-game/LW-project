package com.livingworld.core.item;

/**
 * Параметры предмета LW.
 * Сторона: both.
 */
public class LWItemProperties {

    public enum Rarity { COMMON, UNCOMMON, RARE, EPIC, LEGENDARY }
    public enum Category { TOOL, WEAPON, FOOD, MATERIAL, COMPONENT, DOCUMENT, MONEY, DECORATION }

    public float weight = 1.0f;             // кг
    public int durability = 0;              // 0 = неразрушимый
    public float price = 0.0f;              // в валюте государства
    public Rarity rarity = Rarity.COMMON;
    public Category category = Category.MATERIAL;
    public float tempResistance = 100.0f;   // °C
    public String soundTag = "item.generic";
    public java.util.List<String> tags = new java.util.ArrayList<>();
}