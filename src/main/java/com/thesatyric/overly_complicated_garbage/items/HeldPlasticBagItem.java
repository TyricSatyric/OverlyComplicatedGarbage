package com.thesatyric.overly_complicated_garbage.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Objects;

public class HeldPlasticBagItem extends Item {
    public HeldPlasticBagItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player)
        {
            if (player.getOffHandStack() != stack && player.getMainHandStack() != stack)
            {
                player.dropItem(stack.copy(), false);
                stack.decrement(1);
            }

        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        ItemUsage.spawnItemContents(entity, Objects.requireNonNull(entity.getStack().set(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT)).iterateNonEmptyCopy());
        super.onItemEntityDestroyed(entity);
    }
}
