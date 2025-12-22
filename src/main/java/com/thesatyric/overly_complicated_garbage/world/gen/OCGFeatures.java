package com.thesatyric.overly_complicated_garbage.world.gen;

import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.world.gen.features.GarbagePileFeature;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.structure.StructureType;

public class OCGFeatures {

    public static final Feature<DefaultFeatureConfig> GARBAGE_PILES =
            Registry.register(Registries.FEATURE, Identifier.of(OverlyComplicatedGarbage.MOD_ID, "garbage_piles"), new GarbagePileFeature(DefaultFeatureConfig.CODEC));


    public static void init(){}
}
