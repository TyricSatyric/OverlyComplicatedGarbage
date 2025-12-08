package com.thesatyric.overly_complicated_garbage.entities;

import com.thesatyric.overly_complicated_garbage.OCGEntities;
import com.thesatyric.overly_complicated_garbage.OCGarbageBlocks;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.blocks.TrashCanBlock;
import com.thesatyric.overly_complicated_garbage.blocks.block_entities.TrashCanBlockEntity;
import com.thesatyric.overly_complicated_garbage.misc.TrashedItemsStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TrashCanGoal extends Goal {
    protected final GarbageTruckEntity mob;
    public TrashCanGoal(PathAwareEntity mob)
    {
        this.mob = (GarbageTruckEntity)mob;

    }
    @Override
    public boolean canStart() {
        return !getNearbyTrashCans().isEmpty();
    }

    @Override
    public void start() {
        List<BlockPos> nearbyCans = getNearbyTrashCans();
        if (!nearbyCans.isEmpty())
        {
            int index = 0;
            BlockPos pos = nearbyCans.get(index);
            while(!this.mob.getWorld().getBlockState(pos).get(TrashCanBlock.HAS_GARBAGE))
            {
                index++;
                if (index >= nearbyCans.size())
                    return;
                pos = nearbyCans.get(index);
            }
            this.mob.canPosition = pos;
            this.mob.getNavigation().startMovingTo(nearbyCans.getFirst().getX(), nearbyCans.getFirst().getY(), nearbyCans.getFirst().getZ(), 0.5f);
            OverlyComplicatedGarbage.LOGGER.info("moving!");
            OverlyComplicatedGarbage.LOGGER.info(this.mob.getWorld().getBlockState(this.mob.canPosition).toString());
        }
    }


    @Override
    public void tick() {
        super.tick();
        if (this.mob.canPosition == null) return;
        BlockState state = this.mob.getWorld().getBlockState(this.mob.canPosition);
        if (state.getBlock() != OCGarbageBlocks.TRASH_CAN)
            return;
        if (this.mob.getNavigation().isIdle())
        {
            this.mob.getNavigation().startMovingTo(this.mob.canPosition.getX(),this.mob.canPosition.getY(),this.mob.canPosition.getZ(), 0.5f);
        }
        if (!state.get(TrashCanBlock.HAS_GARBAGE))
        {
            this.mob.canPosition = null;
            return;
        }
        if (this.mob.canPosition.isWithinDistance(
                new Vec3i(this.mob.getBlockPos().getX(),this.mob.getBlockPos().getY(),this.mob.getBlockPos().getZ()),
                2) && state.get(TrashCanBlock.HAS_GARBAGE))
        {
            TrashCanBlockEntity be = (TrashCanBlockEntity)this.mob.getWorld().getBlockEntity(this.mob.canPosition);
            if(!state.get(TrashCanBlock.STICKY))
                this.mob.getWorld().setBlockState(this.mob.canPosition, state.with(TrashCanBlock.HAS_BAG, false).with(TrashCanBlock.HAS_GARBAGE, false));
            else
                this.mob.getWorld().setBlockState(this.mob.canPosition, state.with(TrashCanBlock.HAS_GARBAGE, false));

            if (be != null)
            {
                OverlyComplicatedGarbage.LOGGER.info("clearing");
                DefaultedList<ItemStack> items = be.getItems();
                if(this.mob.getWorld() instanceof ServerWorld serverWorld)
                {
                    for (ItemStack stack : items)
                    {
                        TrashedItemsStateManager.get(serverWorld).items.add(stack.copy());
                    }
                    TrashedItemsStateManager.get(serverWorld).markDirty();
                }
                be.clear();
            }
            this.mob.open();
            this.mob.canPosition = null;
        }

    }

    @Override
    public boolean shouldContinue() {
        return this.mob.canPosition != null;
    }

    private List<BlockPos> getNearbyTrashCans()
    {
        BlockPos pos = this.mob.getBlockPos();
        PointOfInterestStorage pointOfInterestStorage = ((ServerWorld) this.mob.getWorld()).getPointOfInterestStorage();
        Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle(
                (poiType) -> poiType.value() == (OCGEntities.TRASH_CANS_POI),
                pos,
                20,
                PointOfInterestStorage.OccupationStatus.ANY);
        ArrayList<BlockPos> finalList = new ArrayList<BlockPos>();
        for (PointOfInterest poi : stream.toList())
        {
            BlockPos bPos = poi.getPos();
            BlockState state = this.mob.getWorld().getBlockState(bPos);
            if (state.getBlock() == OCGarbageBlocks.TRASH_CAN)
            {
                if (state.get(TrashCanBlock.HAS_BAG))
                {
                    finalList.add(bPos);
                }
            }
        }
        return finalList.stream().toList();
    }
}
