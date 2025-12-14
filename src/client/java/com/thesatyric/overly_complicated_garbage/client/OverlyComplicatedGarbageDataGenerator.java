package com.thesatyric.overly_complicated_garbage.client;

import com.thesatyric.overly_complicated_garbage.datagen.OCGWorldGenerator;
import com.thesatyric.overly_complicated_garbage.world.gen.OCGBiomes;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class OverlyComplicatedGarbageDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(OCGWorldGenerator::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder)
    {
        registryBuilder.addRegistry(RegistryKeys.BIOME, OCGBiomes::bootstrap);
    }
}
