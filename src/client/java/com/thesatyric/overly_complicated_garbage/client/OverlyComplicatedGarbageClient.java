package com.thesatyric.overly_complicated_garbage.client;

import com.thesatyric.overly_complicated_garbage.OCGarbageParticles;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.client.particles.AshDustParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class OverlyComplicatedGarbageClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        OverlyComplicatedGarbage.LOGGER.info("Initializing Client");
        //OCGEntityModelLayers.initialize();
        ParticleFactoryRegistry.getInstance().register(OCGarbageParticles.ASH_DUST_PARTICLE, AshDustParticle.Factory::new);
    }
}
