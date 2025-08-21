package com.thesatyric.overly_complicated_garbage;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class OCGarbageParticles {
    public static final SimpleParticleType ASH_DUST_PARTICLE = FabricParticleTypes.simple();
    public static void initialize()
    {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(OverlyComplicatedGarbage.MOD_ID, "ash_dust_particle"), ASH_DUST_PARTICLE);
    }
}
