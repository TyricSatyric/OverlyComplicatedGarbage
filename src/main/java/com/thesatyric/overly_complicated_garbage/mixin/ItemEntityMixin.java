package com.thesatyric.overly_complicated_garbage.mixin;

import com.thesatyric.overly_complicated_garbage.OCGarbageComponents;
import com.thesatyric.overly_complicated_garbage.OCGarbageItems;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow private int itemAge;
    @Shadow private int health;


    @Shadow public abstract boolean isFireImmune();

    @Shadow public abstract ItemStack getStack();

    @Shadow public abstract void setStack(ItemStack stack);

    @Unique public boolean canDespawn = false;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;discard()V", ordinal = 1))
    public void tick(ItemEntity instance)
    {
        if (canDespawn)
        {
            this.remove(Entity.RemovalReason.DISCARDED);
        }
        else
        {
//            OverlyComplicatedGarbage.LOGGER.info("AHAHAHHA Item didn't despawn!");
            itemAge = 0;
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void separateWhenWater(CallbackInfo ci) {
        if (getStack().getItem() == OCGarbageItems.CACTUS_PRICKLES)
        {
            getStack().set(OCGarbageComponents.PRICKLY_COMPONENT, Boolean.TRUE);
        }
        if (isTouchingWater() && Boolean.TRUE.equals(getStack().get(OCGarbageComponents.PRICKLY_COMPONENT)) && getStack().getItem() != OCGarbageItems.CACTUS_PRICKLES)
        {
            getStack().remove(OCGarbageComponents.PRICKLY_COMPONENT);
            if (getWorld() instanceof ServerWorld)
            {
                getWorld().spawnEntity(new ItemEntity(getWorld(), getX(), getY(), getZ(), new ItemStack(OCGarbageItems.CACTUS_PRICKLES, getStack().getCount())));
            }
        }
    }

    /**
     * @author TyricSatyric
     * @reason Make item invulnerable to cacti
     */
    @Overwrite
    public final boolean damage(ServerWorld world, DamageSource source, float amount) {

        if (this.isAlwaysInvulnerableTo(source)) {
            return false;
        } else if (!world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && source.getAttacker() instanceof MobEntity) {
            return false;
        } else if (!this.getStack().takesDamageFrom(source)) {
            return false;
        } else if (source.isOf(DamageTypes.CACTUS))
        {
            if (!Boolean.TRUE.equals(getStack().get(OCGarbageComponents.PRICKLY_COMPONENT)))
            {
                ItemStack newStack = getStack().copy();
                newStack.set(OCGarbageComponents.PRICKLY_COMPONENT, true);
                setStack(newStack);
            }
            return false;
        }
        else {
            this.scheduleVelocityUpdate();
            this.health = (int)((float)this.health - amount);
            this.emitGameEvent(GameEvent.ENTITY_DAMAGE, source.getAttacker());
            if (this.health <= 0) {
                OverlyComplicatedGarbage.LOGGER.info("Dead");
                OverlyComplicatedGarbage.LOGGER.info(source.toString());
                this.getStack().onItemEntityDestroyed((ItemEntity)(Object)this);
                this.discard();
            }

            return true;
        }
    }

    @Inject(method = "setDespawnImmediately", at = @At(value = "HEAD"))
    public void makeDespawnable(CallbackInfo ci)
    {
        canDespawn = true;
    }


    public void setOnFireFromLava()
    {

        if (!isFireImmune())
        {
            ItemStack new_stack = getStack().withItem(OCGarbageItems.ASH_DUST);
            ItemEntity new_entity = new ItemEntity(getWorld(), getX(), getY(), getZ(), new_stack);
            if (getWorld() instanceof ServerWorld)
            {
                getWorld().spawnEntity(new_entity);
                getWorld().playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_BURN, this.getSoundCategory(), 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
            }
            this.discard();
        }
    }

    
    public void attemptTickInVoid() {
        if (Objects.equals(this.getWorld().getDimensionEntry().getIdAsString(), "minecraft:the_end"))
        {
            if (this.getY() < (double)(this.getWorld().getBottomY())) {
                this.setVelocity(0, 5, 0);
            }
        }
        else
        {
            if (this.getY() < (double)(this.getWorld().getBottomY())) {
                this.setVelocity(0, 1, 0);
            }

        }
    }

}
