package com.thesatyric.overly_complicated_garbage.items;

import com.thesatyric.overly_complicated_garbage.OCGDamageTypes;
import com.thesatyric.overly_complicated_garbage.OCGarbageBlocks;
import com.thesatyric.overly_complicated_garbage.OCGarbageItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ClickType;
import net.minecraft.world.World;

public class PlasticBagItem extends BlockItem {
    public PlasticBagItem(Settings settings) {
        super(OCGarbageBlocks.GARBAGE_BAG, settings);
    }



    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player)
        {
            if (player.getEquippedStack(EquipmentSlot.HEAD) == stack && world instanceof ServerWorld serverWorld)
            {
                player.damage(serverWorld, OCGDamageTypes.of(world, OCGDamageTypes.BAG_SUFFOCATION), 1);
            }
        }
    }


    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (otherStack.getItem() == Items.SHEARS)
        {
            if (player.getEquippedStack(EquipmentSlot.HEAD) == stack)
            {
                stack.setCount(0);
                if (player.isCreative())
                    player.dropCreativeStack(new ItemStack(OCGarbageItems.BROKEN_PLASTIC_BAG));
                else
                    player.dropItem(new ItemStack(OCGarbageItems.BROKEN_PLASTIC_BAG), true);
            }
            return true;
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }
}
