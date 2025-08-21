package com.thesatyric.overly_complicated_garbage.client;

import com.thesatyric.overly_complicated_garbage.OCGarbageParticles;
import com.thesatyric.overly_complicated_garbage.OCProperties;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.client.particles.AshDustParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.particle.AbstractDustParticle;
import net.minecraft.client.particle.DustPlumeParticle;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.particle.DustParticleEffect;

public class OverlyComplicatedGarbageClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        OverlyComplicatedGarbage.LOGGER.info("Initializing Client");
        ParticleFactoryRegistry.getInstance().register(OCGarbageParticles.ASH_DUST_PARTICLE, AshDustParticle.Factory::new);
    }
}
