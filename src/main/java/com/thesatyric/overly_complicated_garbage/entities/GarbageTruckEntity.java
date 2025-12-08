package com.thesatyric.overly_complicated_garbage.entities;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.xml.crypto.Data;

public class GarbageTruckEntity extends PathAwareEntity {
    public AnimationState openDoorAnimationState = new AnimationState();
    public AnimationState idleAnimationState = new AnimationState();
    public float openDoorAnimationTimeout = 0;
    public BlockPos canPosition;
    public boolean doingAnimation;
    public boolean opening;
    public GarbageTruckEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }
    protected EntityNavigation createNavigation(World world) {
        return new TruckNavigation(this, world);
    }

    void setupAnimationStates()
    {
        if (opening && openDoorAnimationTimeout <= 0)
        {
            idleAnimationState.stop();
            if (doingAnimation)
            {
                openDoorAnimationTimeout = 50;
                openDoorAnimationState.start(this.age);
            }
            else
            {
                openDoorAnimationState.stop();
                opening = false;
            }
        }
        else
        {
            doingAnimation = false;
            --openDoorAnimationTimeout;
        }
        if(!opening)
        {
            idleAnimationState.start(this.age);
        }
    }


    public void open()
    {
        opening = true;
        doingAnimation = true;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new GoAwayGoal(this));
        this.goalSelector.add(1, new TrashCanGoal(this));

    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient())
        {
            setupAnimationStates();
        }

    }

    public static class GoAwayGoal extends Goal{
        protected final GarbageTruckEntity mob;
        public GoAwayGoal(PathAwareEntity mob)
        {
            this.mob = (GarbageTruckEntity)mob;

        }
        @Override
        public boolean canStart() {
            return this.mob.age > 20*120;
        }

        @Override
        public void start() {
            int negativeX = this.mob.random.nextBoolean() ? 1: -1;
            int negativeZ = this.mob.random.nextBoolean() ? 1: -1;
            BlockPos pos = new BlockPos(this.mob.getBlockX()+200*negativeX, 320, this.mob.getBlockZ()+200*negativeZ);
            while (this.mob.getWorld().getBlockState(pos).isAir())
            {
                pos.add(0,-1,0);
            }
            pos.add(0,1,0);
            this.mob.navigation.startMovingTo(this.mob.getBlockX()+200*negativeX, this.mob.getBlockY(), this.mob.getBlockZ()+200*negativeZ, 1f);
        }

        @Override
        public void tick() {
            if (getServerWorld(this.mob).getClosestPlayer(this.mob, 100) == null)
            {
                this.mob.discard();
            }
            super.tick();
        }
    }
}
