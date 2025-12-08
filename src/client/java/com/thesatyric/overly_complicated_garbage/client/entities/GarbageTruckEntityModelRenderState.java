package com.thesatyric.overly_complicated_garbage.client.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.AnimationState;

@Environment(EnvType.CLIENT)
public class GarbageTruckEntityModelRenderState extends LivingEntityRenderState {
    public final AnimationState openDoorAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public GarbageTruckEntityModelRenderState(){}
}
