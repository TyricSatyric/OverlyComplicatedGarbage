package com.thesatyric.overly_complicated_garbage.client.entities;

import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

@Environment(EnvType.CLIENT)
public class GarbageTruckEntityModel extends EntityModel<GarbageTruckEntityModelRenderState> {
	private final ModelPart Back;
	private final ModelPart door;
	private final ModelPart Middle;
	private final ModelPart Front;
	private final ModelPart wheels;

	public GarbageTruckEntityModel(ModelPart root) {
        super(root);
        this.Back = root.getChild("Back");
		this.door = this.Back.getChild("door");
		this.Middle = root.getChild("Middle");
		this.Front = root.getChild("Front");
		this.wheels = root.getChild("wheels");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData Back = modelPartData.addChild("Back", ModelPartBuilder.create().uv(0, 69).cuboid(4.0F, -6.0F, 9.0F, 2.0F, 11.0F, 3.0F, new Dilation(0.0F))
		.uv(10, 69).cuboid(-6.0F, -6.0F, 9.0F, 2.0F, 11.0F, 3.0F, new Dilation(0.0F))
		.uv(52, 23).cuboid(-4.0F, 3.0F, 9.0F, 8.0F, 2.0F, 3.0F, new Dilation(0.0F))
		.uv(20, 69).cuboid(4.0F, -4.0F, 12.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F))
		.uv(76, 69).cuboid(-6.0F, -4.0F, 12.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F))
		.uv(68, 28).cuboid(-4.0F, 3.0F, 12.0F, 8.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(28, 77).cuboid(4.0F, -1.0F, 14.0F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F))
		.uv(36, 77).cuboid(-6.0F, -1.0F, 14.0F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F))
		.uv(68, 33).cuboid(-4.0F, 5.0F, 14.0F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 15.0F, 0.0F));

		ModelPartData door = Back.addChild("door", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 3.0F, 14.25F));

		ModelPartData cube_r1 = door.addChild("cube_r1", ModelPartBuilder.create().uv(52, 28).cuboid(-4.0F, -5.0F, 0.0F, 8.0F, 10.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.2452F, -2.7058F, 0.5236F, 0.0F, 0.0F));

		ModelPartData Middle = modelPartData.addChild("Middle", ModelPartBuilder.create().uv(0, 40).cuboid(-6.0F, -12.0F, 10.0F, 2.0F, 11.0F, 18.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-4.0F, -12.0F, 10.0F, 8.0F, 2.0F, 18.0F, new Dilation(0.0F))
		.uv(40, 40).cuboid(4.0F, -12.0F, 10.0F, 2.0F, 11.0F, 18.0F, new Dilation(0.0F))
		.uv(0, 20).cuboid(-4.0F, -3.0F, 10.0F, 8.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, -19.0F));

		ModelPartData Front = modelPartData.addChild("Front", ModelPartBuilder.create().uv(52, 0).cuboid(-6.0F, -10.0F, 6.0F, 12.0F, 9.0F, 4.0F, new Dilation(0.0F))
		.uv(52, 13).cuboid(-6.0F, -6.0F, 2.0F, 12.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, -19.0F));

		ModelPartData wheels = modelPartData.addChild("wheels", ModelPartBuilder.create().uv(28, 69).cuboid(-6.0F, -4.0F, -13.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F))
		.uv(40, 69).cuboid(4.0F, -4.0F, -13.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F))
		.uv(52, 69).cuboid(4.0F, -4.0F, 8.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F))
		.uv(64, 69).cuboid(-6.0F, -4.0F, 8.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	public void setAngles(GarbageTruckEntityModelRenderState state) {
		super.setAngles(state);
		this.animate(state.idleAnimationState, GarbageTruckEntityModelAnimation.Idle, state.age);
		this.animate(state.openDoorAnimationState, GarbageTruckEntityModelAnimation.OpenDoor, state.age);

	}


}