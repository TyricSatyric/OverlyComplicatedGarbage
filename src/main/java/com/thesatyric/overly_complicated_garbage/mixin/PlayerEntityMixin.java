package com.thesatyric.overly_complicated_garbage.mixin;

import com.thesatyric.overly_complicated_garbage.OCGarbageItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "interact", at= @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;", ordinal = 0))
    void shearBag(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir)
    {
        ItemStack stack = ((PlayerEntity)(Object)this).getStackInHand(hand);
        if (entity instanceof PlayerEntity otherPlayer)
        {
            ItemStack bag = otherPlayer.getEquippedStack(EquipmentSlot.HEAD);
            if (bag.getItem() == OCGarbageItems.PLASTIC_BAG && stack.getItem() == Items.SHEARS)
            {
                stack.damage(1, ((PlayerEntity)(Object)this));
                bag.setCount(0);
                if (otherPlayer.isCreative())
                    otherPlayer.dropCreativeStack(new ItemStack(OCGarbageItems.BROKEN_PLASTIC_BAG));
                else
                    otherPlayer.dropItem(new ItemStack(OCGarbageItems.BROKEN_PLASTIC_BAG), true);
            }
        }
    }
}
