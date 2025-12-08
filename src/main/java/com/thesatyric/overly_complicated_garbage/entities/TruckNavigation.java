package com.thesatyric.overly_complicated_garbage.entities;

import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TruckNavigation extends MobNavigation {
    public TruckNavigation(MobEntity entity, World world) {
        super(entity, world);

    }
}
