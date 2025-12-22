package com.thesatyric.overly_complicated_garbage.world.gen;

import com.thesatyric.overly_complicated_garbage.OCGEntities;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.world.gen.features.GarbagePileFeature;
import com.thesatyric.overly_complicated_garbage.world.gen.features.OCGPlacedFeatures;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class OCGBiomes {
    public static final RegistryKey<Biome> GARBAGE_DUMP = register("garbage_dump");

    private static RegistryKey<Biome> register(String name) {
        return RegistryKey.of(RegistryKeys.BIOME, Identifier.of(OverlyComplicatedGarbage.MOD_ID, name));
    }

    public static void bootstrap(Registerable<Biome> context)
    {
        context.register(GARBAGE_DUMP, garbageDumpBiome(context));
    }

    public static Biome garbageDumpBiome(Registerable<Biome> context)
    {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        spawnBuilder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.STRAY, 2, 1, 3));
        spawnBuilder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.HUSK, 2, 1, 3));
        spawnBuilder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.BOGGED, 2, 1, 3));
        spawnBuilder.spawn(SpawnGroup.MISC, new SpawnSettings.SpawnEntry(OCGEntities.TRUCK, 1, 1, 3));
        DefaultBiomeFeatures.addBatsAndMonsters(spawnBuilder);
        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        biomeBuilder.feature(GenerationStep.Feature.LOCAL_MODIFICATIONS, OCGPlacedFeatures.GARBAGE_PILES);
        DefaultBiomeFeatures.addMineables(biomeBuilder);
        DefaultBiomeFeatures.addDungeons(biomeBuilder);
        DefaultBiomeFeatures.addDefaultOres(biomeBuilder);


        BiomeEffects.Builder effects = new BiomeEffects.Builder();
        effects.skyColor(9086068);
        effects.fogColor(406272);
        effects.waterColor(1916448);
        effects.waterFogColor(406272);
        effects.grassColor(1977856);
        effects.foliageColor(1977856);

        return new Biome.Builder()
                .precipitation(true)
                .downfall(0f)
                .temperature(1f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects(effects.build())
                .build();

    }

}
