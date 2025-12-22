package com.thesatyric.overly_complicated_garbage.world.gen.features;

import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.world.gen.OCGBiomes;
import com.thesatyric.overly_complicated_garbage.world.gen.OCGFeatures;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class OCGConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> GARBAGE_PILES = registerKey("garbage_piles");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context){
        register(context, GARBAGE_PILES, OCGFeatures.GARBAGE_PILES, DefaultFeatureConfig.INSTANCE);
    }


    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(OverlyComplicatedGarbage.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
