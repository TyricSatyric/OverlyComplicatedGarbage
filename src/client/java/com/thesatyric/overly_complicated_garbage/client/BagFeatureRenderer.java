package com.thesatyric.overly_complicated_garbage.client;

import com.mojang.serialization.Codec;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

import java.util.Map;

public class BagFeatureRenderer <S extends BipedEntityRenderState, M extends EntityModel<S>> extends FeatureRenderer<S, M> {
    private final BagEntityModel model;
    private final EquipmentRenderer equipmentRenderer;
    public BagFeatureRenderer(FeatureRendererContext<S, M> context, LoadedEntityModels entityModels, EquipmentRenderer equipmentRenderer) {
        super(context);
        this.model = new BagEntityModel(entityModels.getModelPart(OCGEntityModelLayers.BAG));
        this.equipmentRenderer = equipmentRenderer;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, S state, float limbAngle, float limbDistance) {
        ItemStack itemStack = state.equippedHeadStack;
        matrices.push();
        EquippableComponent equippableComponent = (EquippableComponent)itemStack.get(DataComponentTypes.EQUIPPABLE);
        if (equippableComponent != null && !equippableComponent.assetId().isEmpty())
        {
            //VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(Identifier.of(OverlyComplicatedGarbage.MOD_ID, "textures/entity/plastic_bag/plastic_bag.png")));
            Identifier identifier = Identifier.of(OverlyComplicatedGarbage.MOD_ID, "textures/entity/bag/bag.png");
            this.model.setAngles((PlayerEntityRenderState) state);
            matrices.translate(0.0F, 0.0F, 0.0F);
            //this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

            this.equipmentRenderer.render(EquipmentModel.LayerType.HUMANOID, equippableComponent.assetId().get(), this.model, itemStack, matrices, vertexConsumers, light, identifier);
            matrices.pop();
        }

    }

    @Environment(EnvType.CLIENT)
    public static enum CustomLayerType implements StringIdentifiable {
        BAG("bag");

        public static final Codec<EquipmentModel.LayerType> CODEC = StringIdentifiable.createCodec(EquipmentModel.LayerType::values);
        private final String name;

        private CustomLayerType(final String name) {
            this.name = name;
        }

        public String asString() {
            return this.name;
        }
    }
}
