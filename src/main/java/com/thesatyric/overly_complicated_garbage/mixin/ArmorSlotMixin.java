package com.thesatyric.overly_complicated_garbage.mixin;

import com.thesatyric.overly_complicated_garbage.OCGarbageItems;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.screen.slot.ArmorSlot")
public class ArmorSlotMixin extends Slot{

    public ArmorSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }


    @Inject(
            method = "canTakeItems",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventPlasticBagRemove(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = this.getStack();
        if (itemStack.getItem() == OCGarbageItems.PLASTIC_BAG)
        {
            cir.setReturnValue(playerEntity.isCreative());
        }
    }

}
