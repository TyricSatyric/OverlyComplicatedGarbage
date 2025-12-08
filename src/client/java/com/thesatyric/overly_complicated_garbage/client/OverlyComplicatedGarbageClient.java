package com.thesatyric.overly_complicated_garbage.client;

import com.google.common.collect.Sets;
import com.thesatyric.overly_complicated_garbage.OCGEntities;
import com.thesatyric.overly_complicated_garbage.OCGarbageParticles;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.client.entities.GarbageTruckEntityModel;
import com.thesatyric.overly_complicated_garbage.client.entities.GarbageTruckEntityRenderer;
import com.thesatyric.overly_complicated_garbage.client.particles.AshDustParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

import java.util.Set;

public class OverlyComplicatedGarbageClient implements ClientModInitializer {
    public static final EntityModelLayer GARBAGE_TRUCK = new EntityModelLayer(Identifier.of(OverlyComplicatedGarbage.MOD_ID, "garbage_truck"), "main");

    @Override
    public void onInitializeClient() {
        OverlyComplicatedGarbage.LOGGER.info("Initializing Client");
        EntityRendererRegistry.register(OCGEntities.TRUCK, GarbageTruckEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(GARBAGE_TRUCK, GarbageTruckEntityModel::getTexturedModelData);
        ParticleFactoryRegistry.getInstance().register(OCGarbageParticles.ASH_DUST_PARTICLE, AshDustParticle.Factory::new);
    }
}
