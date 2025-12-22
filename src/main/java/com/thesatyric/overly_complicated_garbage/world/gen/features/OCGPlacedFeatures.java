package com.thesatyric.overly_complicated_garbage.world.gen.features;

import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class OCGPlacedFeatures {
    public static final RegistryKey<PlacedFeature> GARBAGE_PILES = registerKey("garbage_piles");

    public static void bootstrap(Registerable<PlacedFeature> context){
        var configuredFeatureRegistryEntryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        register(context, GARBAGE_PILES, configuredFeatureRegistryEntryLookup.getOrThrow(OCGConfiguredFeatures.GARBAGE_PILES),
                List.of(
                        RarityFilterPlacementModifier.of(10),
                        HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
                        RandomOffsetPlacementModifier.horizontally(UniformIntProvider.create(2, 6)),
                        SquarePlacementModifier.of()
                ));
    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(OverlyComplicatedGarbage.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
