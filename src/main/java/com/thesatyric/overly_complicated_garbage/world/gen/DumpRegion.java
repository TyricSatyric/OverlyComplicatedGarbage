package com.thesatyric.overly_complicated_garbage.world.gen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class DumpRegion extends Region {
    public DumpRegion(Identifier name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
        super.addBiomes(registry, mapper);
        this.addModifiedVanillaOverworldBiomes(mapper, builder ->{
            builder.replaceBiome(BiomeKeys.MEADOW, OCGBiomes.GARBAGE_DUMP);
        });
    }
}
