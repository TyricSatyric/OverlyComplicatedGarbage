package com.thesatyric.overly_complicated_garbage.mixin;

import com.thesatyric.overly_complicated_garbage.blocks.block_entities.SuspiciousGarbageBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BrushItem;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushItem.class)
public class BrushItemMixin {

    @Inject(method = "usageTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockEntity(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/entity/BlockEntity;"))
    void brushSusGarbage(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci)
    {
        PlayerEntity playerEntity = (PlayerEntity) user;
        HitResult hitResult = this.getHitResult(playerEntity);
        BlockHitResult blockHitResult = (BlockHitResult) hitResult;
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockEntity bl2 = world.getBlockEntity(blockPos);
        if (bl2 instanceof SuspiciousGarbageBlockEntity suspiciousGarbageBlockEntity) {
            boolean bool = suspiciousGarbageBlockEntity.brush(world.getTime(), (ServerWorld)world, playerEntity, blockHitResult.getSide(), stack);
            if (bool) {
                EquipmentSlot equipmentSlot = stack.equals(playerEntity.getEquippedStack(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                stack.damage(1, playerEntity, equipmentSlot);
            }
        }

    }


    @Unique
    private HitResult getHitResult(PlayerEntity user) {
        return ProjectileUtil.getCollision(user, EntityPredicates.CAN_HIT, user.getBlockInteractionRange());
    }
}
