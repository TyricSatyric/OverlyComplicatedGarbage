package com.thesatyric.overly_complicated_garbage.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

// Made with Blockbench 5.0.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class BagEntityModel extends BipedEntityModel<PlayerEntityRenderState> {
	public BagEntityModel(ModelPart modelPart) {
		super(modelPart);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bb_main = modelPartData.addChild("head", ModelPartBuilder.create().uv(36, 26).cuboid(-0.5F, -1.0F, 4.75F, 1.0F, 5.0F, 0.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-6.5F, -14.5F, -6.5F, 13.0F, 13.0F, 13.0F, new Dilation(0.0F))
				.uv(0, 26).cuboid(-4.5F, -1.5F, -4.5F, 9.0F, 1.5F, 9.0F, new Dilation(0.0F))
				.uv(36, 31).cuboid(-0.5F, -1.0F, -4.75F, 1.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}


}