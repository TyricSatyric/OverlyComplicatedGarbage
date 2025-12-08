package com.thesatyric.overly_complicated_garbage.client.entities;

import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.client.OverlyComplicatedGarbageClient;
import com.thesatyric.overly_complicated_garbage.entities.GarbageTruckEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GarbageTruckEntityRenderer extends MobEntityRenderer<GarbageTruckEntity, GarbageTruckEntityModelRenderState, GarbageTruckEntityModel> {

    private static final Identifier TEXTURE = Identifier.of(OverlyComplicatedGarbage.MOD_ID, "textures/entity/garbage_truck/garbage_truck.png");

    public GarbageTruckEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new GarbageTruckEntityModel(context.getPart(OverlyComplicatedGarbageClient.GARBAGE_TRUCK)), 0.9f);
    }

    @Override
    public Identifier getTexture(GarbageTruckEntityModelRenderState state) {
        return TEXTURE;
    }

    @Override
    public GarbageTruckEntityModelRenderState createRenderState() {
        return new GarbageTruckEntityModelRenderState();
    }

    @Override
    public void updateRenderState(GarbageTruckEntity livingEntity, GarbageTruckEntityModelRenderState livingEntityRenderState, float f) {
        super.updateRenderState(livingEntity, livingEntityRenderState, f);
        livingEntityRenderState.idleAnimationState.copyFrom(livingEntity.idleAnimationState);
        livingEntityRenderState.openDoorAnimationState.copyFrom(livingEntity.openDoorAnimationState);
    }

}
