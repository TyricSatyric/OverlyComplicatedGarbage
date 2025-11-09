package com.thesatyric.overly_complicated_garbage.mixin;

import com.thesatyric.overly_complicated_garbage.OCGarbageItems;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EquippableComponent.class)
public class EquippableComponentMixin{


    @Inject(method = "equip", at = @At("HEAD"), cancellable = true)
    void checkIfItemIsBag(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ActionResult> cir)
    {
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.HEAD);
        if (itemStack.getItem() == OCGarbageItems.PLASTIC_BAG)
        {
            OverlyComplicatedGarbage.LOGGER.info("Avoiding equip");
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
