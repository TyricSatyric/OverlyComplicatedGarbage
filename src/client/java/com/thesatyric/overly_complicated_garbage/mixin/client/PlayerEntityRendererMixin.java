package com.thesatyric.overly_complicated_garbage.mixin.client;

import com.thesatyric.overly_complicated_garbage.client.BagFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.struct.Constructor;

import javax.management.ConstructorParameters;
import java.util.List;

@Mixin(PlayerEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class PlayerEntityRendererMixin {
    @Inject(at = @At("RETURN"), method = "<init>")
    void addFeatures(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci)
    {
        ((LivingEntityRendererAccessor)(Object)this).invokeAddFeature(new BagFeatureRenderer((PlayerEntityRenderer) (Object) this, ctx.getEntityModels(), ctx.getEquipmentRenderer()));
    }

}


