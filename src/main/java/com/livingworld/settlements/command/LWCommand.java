package com.livingworld.settlements.command;

import com.livingworld.geography.GeographyGenerator;
import com.livingworld.geography.data.GeographyWorldData;
import com.livingworld.settlements.SettlementSpawner;
import com.livingworld.settlements.data.Settlement;
import com.livingworld.settlements.data.SettlementWorldData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Команды мода LW Settlements.
 * Сторона: server.
 */
public class LWCommand {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        // /lw_info
        dispatcher.register(Commands.literal("lw_info")
                .executes(context -> {
                    ServerLevel overworld = context.getSource().getServer().overworld();
                    SettlementWorldData data = getData(overworld);

                    context.getSource().sendSuccess(() -> Component.literal("§6--- LW Info ---"), false);

                    int totalPop = 0;
                    for (Settlement s : data.all()) totalPop += s.population;
                    final int finalTotalPop = totalPop;

                    context.getSource().sendSuccess(() -> Component.literal(
                            "§eПоселений: §f" + data.all().size() + " §e| Общее население: §f" + finalTotalPop
                    ), false);

                    for (Settlement s : data.all()) {
                        context.getSource().sendSuccess(() -> Component.literal(
                                "§a" + s.name + " §7| §fX: " + s.center.getX()
                                + " Y: " + s.center.getY()
                                + " Z: " + s.center.getZ()
                                + " §7| §eНаселение: §f" + s.population
                        ), false);
                    }
                    return 1;
                }));

        // /lw_force_gen — оставляем для совместимости (но теперь он не сканирует чанки)
        dispatcher.register(Commands.literal("lw_force_gen")
                .executes(context -> {
                    ServerLevel overworld = context.getSource().getServer().overworld();

                    SavedData.Factory<GeographyWorldData> geoFactory = new SavedData.Factory<>(
                            GeographyWorldData::create,
                            GeographyWorldData::load,
                            null
                    );
                    GeographyWorldData geoData = overworld.getDataStorage().computeIfAbsent(
                            geoFactory, GeographyWorldData.DATA_NAME);

                    GeographyWorldData result = GeographyGenerator.generateIfNeeded(overworld, geoData);
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§aГеография: стран " + result.getCountries().size()
                            + ", дорог " + result.getRoadEdges().size()
                            + ", рек " + result.getRivers().size()), false);

                    context.getSource().sendSuccess(() -> Component.literal(
                            "§eИспользуйте /lw_add_city <x> <z> и /lw_add_village <x> <z> для добавления точек," +
                            " затем /lw_place_manual."), false);
                    return 1;
                }));

        // /lw_add_city <x> <z>
        dispatcher.register(Commands.literal("lw_add_city")
                .then(Commands.argument("x", IntegerArgumentType.integer())
                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                .executes(context -> {
                                    int x = IntegerArgumentType.getInteger(context, "x");
                                    int z = IntegerArgumentType.getInteger(context, "z");
                                    SettlementSpawner.addCity(x, z);
                                    context.getSource().sendSuccess(() -> Component.literal(
                                            "§aГород добавлен: X=" + x + ", Z=" + z), false);
                                    return 1;
                                }))));

        // /lw_add_village <x> <z>
        dispatcher.register(Commands.literal("lw_add_village")
                .then(Commands.argument("x", IntegerArgumentType.integer())
                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                .executes(context -> {
                                    int x = IntegerArgumentType.getInteger(context, "x");
                                    int z = IntegerArgumentType.getInteger(context, "z");
                                    SettlementSpawner.addVillage(x, z);
                                    context.getSource().sendSuccess(() -> Component.literal(
                                            "§aДеревня добавлена: X=" + x + ", Z=" + z), false);
                                    return 1;
                                }))));

        // /lw_place_manual
        dispatcher.register(Commands.literal("lw_place_manual")
                .executes(context -> {
                    ServerLevel overworld = context.getSource().getServer().overworld();
                    SettlementSpawner.placeAllManual(overworld);
                    context.getSource().sendSuccess(() -> Component.literal(
                            "§aРучные поселения размещены"), false);
                    return 1;
                }));

        // /lw_spawn_npc
        dispatcher.register(Commands.literal("lw_spawn_npc")
                .executes(context -> {
                    ServerLevel overworld = context.getSource().getServer().overworld();
                    com.livingworld.smartnpc.common.NPCSpawner.spawnInitialNPCs(overworld);
                    context.getSource().sendSuccess(() -> Component.literal("§aNPC заспавнены!"), false);
                    return 1;
                }));
    }

    // Вспомогательный метод — на уровне класса, НЕ внутри registerCommands
    private static SettlementWorldData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(SettlementWorldData::create, SettlementWorldData::load, null),
                SettlementWorldData.DATA_NAME
        );
    }
}