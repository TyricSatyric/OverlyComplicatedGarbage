package com.thesatyric.overly_complicated_garbage.blocks.block_entities;

import com.mojang.logging.LogUtils;
import com.thesatyric.overly_complicated_garbage.OCGBlockEntities;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.blocks.SuspiciousGarbageBlock;
import com.thesatyric.overly_complicated_garbage.misc.TrashedItemsState;
import com.thesatyric.overly_complicated_garbage.misc.TrashedItemsStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;

public class SuspiciousGarbageBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private int brushesCount;
    private long nextDustTime;
    private long nextBrushTime;
    private ItemStack item;
    @Nullable
    private Direction hitDirection;

    public SuspiciousGarbageBlockEntity(BlockPos pos, BlockState state) {
        super(OCGBlockEntities.SUSPICIOUS_GARBAGE_BLOCK_ENTITY, pos, state);
    }


    public boolean brush(long worldTime, ServerWorld world, PlayerEntity player, Direction hitDirection, ItemStack brush) {
        if (this.hitDirection == null) {
            this.hitDirection = hitDirection;
        }
        this.nextDustTime = worldTime + 40L;
        if (worldTime < this.nextBrushTime) {
            return false;
        } else {
            this.nextBrushTime = worldTime + 10L;
            int i = this.getDustedLevel();
            if (++this.brushesCount >= 10) {
                this.finishBrushing(world, player, brush);
                return true;
            } else {
                world.scheduleBlockTick(this.getPos(), this.getCachedState().getBlock(), 2);
                int j = this.getDustedLevel();
                if (i != j) {
                    BlockState blockState = this.getCachedState();
                    BlockState blockState2 = (BlockState)blockState.with(Properties.DUSTED, j);
                    world.setBlockState(this.getPos(), blockState2, 3);
                }

                return false;
            }
        }
    }


    private void finishBrushing(ServerWorld world, PlayerEntity player, ItemStack brush) {
        if (getWorld() instanceof ServerWorld serverWorld)
        {
            List<ItemStack> items = TrashedItemsStateManager.get(serverWorld).items;
            OverlyComplicatedGarbage.LOGGER.info(items.toString());
            if (!items.isEmpty()){
                ItemStack itemStack = items.remove(serverWorld.random.nextBetweenExclusive(0, items.size()));
                world.spawnEntity(new ItemEntity(world, this.pos.getX()-0.5, this.pos.getY()-0.5, this.pos.getZ()-0.5, itemStack));
                TrashedItemsStateManager.get(serverWorld).writeNbt(new NbtCompound(), serverWorld.getRegistryManager());
            }
        }
        world.breakBlock(this.pos, false);
    }


    public void scheduledTick(ServerWorld world) {
        if (this.brushesCount != 0 && world.getTime() >= this.nextDustTime) {
            int i = this.getDustedLevel();
            this.brushesCount = Math.max(0, this.brushesCount - 2);
            int j = this.getDustedLevel();
            if (i != j) {
                world.setBlockState(this.getPos(), (BlockState)this.getCachedState().with(Properties.DUSTED, j), 3);
            }

            int k = 4;
            this.nextDustTime = world.getTime() + 4L;
        }

        if (this.brushesCount == 0) {
            this.hitDirection = null;
            this.nextDustTime = 0L;
            this.nextBrushTime = 0L;
        } else {
            world.scheduleBlockTick(this.getPos(), this.getCachedState().getBlock(), 2);
        }

    }




    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbtCompound = super.toInitialChunkDataNbt(registries);
        if (this.hitDirection != null) {
            nbtCompound.putInt("hit_direction", this.hitDirection.ordinal());
        }

        if (!(this.item == null)) {
            if (!this.item.isEmpty())
            {
                nbtCompound.put("item", this.item.toNbt(registries));
            }
        }

        return nbtCompound;
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        if (getWorld() instanceof ServerWorld serverWorld)
        {
            List<ItemStack> items = TrashedItemsStateManager.get(serverWorld).items;
            if (items.isEmpty())
                this.item = ItemStack.EMPTY;
            else
                this.item = items.get(serverWorld.random.nextBetweenExclusive(0, items.size()));
        }

        if (nbt.contains("hit_direction")) {
            this.hitDirection = Direction.values()[nbt.getInt("hit_direction")];
        }

    }


    private int getDustedLevel() {
        if (this.brushesCount == 0) {
            return 0;
        } else if (this.brushesCount < 3) {
            return 1;
        } else {
            return this.brushesCount < 6 ? 2 : 3;
        }
    }

    @Nullable
    public Direction getHitDirection() {
        return this.hitDirection;
    }

    public ItemStack getItem() {
        return this.item;
    }
}
