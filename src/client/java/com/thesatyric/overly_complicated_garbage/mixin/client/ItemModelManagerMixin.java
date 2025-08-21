package com.thesatyric.overly_complicated_garbage.mixin.client;

import com.thesatyric.overly_complicated_garbage.OCGarbageComponents;
import com.thesatyric.overly_complicated_garbage.OCGarbageItems;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(ItemModelManager.class)
public class ItemModelManagerMixin {
    @Shadow @Final private Function<Identifier, ItemModel> modelGetter;

    @Inject(method = "update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
    at=@At("TAIL"))
    public void showPrickles(ItemRenderState renderState, ItemStack stack, ModelTransformationMode transformationMode, World world, LivingEntity entity, int seed, CallbackInfo ci)
    {
        if (Boolean.TRUE.equals(stack.get(OCGarbageComponents.PRICKLY_COMPONENT)))
        {
            ItemStack prickles = new ItemStack(OCGarbageItems.CACTUS_PRICKLES);
            Identifier identifier = prickles.get(DataComponentTypes.ITEM_MODEL);
            ItemModel itemModel = this.modelGetter.apply(identifier);
            ClientWorld clientWorld;
            if (world instanceof ClientWorld) {
                clientWorld = (ClientWorld)world;
            } else {
                clientWorld = null;
            }

            itemModel.update(renderState, stack, (ItemModelManager)(Object)this, transformationMode, clientWorld, entity, seed);
        }

    }
}
