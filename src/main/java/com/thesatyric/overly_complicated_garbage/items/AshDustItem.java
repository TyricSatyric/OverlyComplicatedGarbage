package com.thesatyric.overly_complicated_garbage.items;

import com.thesatyric.overly_complicated_garbage.OCGarbageBlocks;
import com.thesatyric.overly_complicated_garbage.OCGarbageParticles;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class AshDustItem extends Item {
    float FORCE = 0.5f;
    public AshDustItem(Settings settings) {
        super(settings);
    }

    public ActionResult use(World world, PlayerEntity user, Hand hand)
    {
        ItemStack itemStack = user.getStackInHand(hand);
        Vec3d pos = user.getPos();
        Vec3d forward = user.getRotationVector().normalize();
        Vec3d up = new Vec3d(0, 1, 0);
        Vec3d right = forward.crossProduct(up).normalize();
        for (int i = -1; i < 2  ; i++)
        {
            float offset = i * 0.5f;
            Vec3d particlePos = pos
                            .add(forward.multiply(1.2))
                            .add(right.multiply(offset))
                            .add(0, 1.5, 0);
            world.addImportantParticle(
                    OCGarbageParticles.ASH_DUST_PARTICLE,
                    particlePos.x,
                    particlePos.y,
                    particlePos.z,
                    forward.x*0.2f,
                    forward.y*0.2f,
                    forward.z*0.2f);
        }
        Vec3d blockPos = pos.add(forward.multiply(3));
        Vec3d lookingPos = pos.add(0, 1, 0);
        BlockPos lookingBlock;
        for (int i = 0; i < user.getBlockInteractionRange(); i++) {
            lookingPos = lookingPos.add(forward);
            lookingBlock = new BlockPos(new Vec3i((int)Math.floor(lookingPos.x), (int)Math.floor(lookingPos.y), (int)Math.floor(lookingPos.z)));
            if (!world.getBlockState(lookingBlock).isAir())
            {
                blockPos = lookingPos;
                break;

            }
        }
        BlockPos currentPos = new BlockPos(new Vec3i((int)Math.floor(blockPos.x), (int)Math.floor(blockPos.y), (int)Math.floor(blockPos.z)));
        while (!world.getBlockState(currentPos).isAir())
        {
            currentPos = currentPos.up();
        }
        while (world.getBlockState(currentPos).isAir())
        {
            currentPos = currentPos.down();
        }
        world.setBlockState(currentPos.up(), OCGarbageBlocks.ASH_LAYER.withLayers(1));
        OverlyComplicatedGarbage.LOGGER.info(currentPos.toString());
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrementUnlessCreative(1, user);
        return ActionResult.SUCCESS;
    }
}
