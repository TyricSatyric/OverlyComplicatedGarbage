package com.thesatyric.overly_complicated_garbage;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;

public class OCProperties {
    public static final IntProperty ASH_LAYERS = IntProperty.of("layers", 1, 16);
    public static final IntProperty GARBAGE_FILL = IntProperty.of("fill", 0, 2);
    public static final BooleanProperty HAS_GARBAGE = BooleanProperty.of("has_items");
    public static final BooleanProperty HAS_BAG = BooleanProperty.of("has_bag");
    public static final BooleanProperty STICKY = BooleanProperty.of("sticky");
    public static final Identifier INTERACTED_WITH_BIOMASS_PROCESSOR = register("interact_with_biomass_processor", StatFormatter.DEFAULT);
    public static void initialize()
    {

    }


    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = Identifier.of(OverlyComplicatedGarbage.MOD_ID, id);
        Registry.register(Registries.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }
}
