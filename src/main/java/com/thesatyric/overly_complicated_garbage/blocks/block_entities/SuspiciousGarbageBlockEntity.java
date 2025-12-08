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
        if (getWorld() instanceof ServerWorld serverWorld)
        {
            List<ItemStack> items = TrashedItemsStateManager.get(serverWorld).items;
            if (items.isEmpty())
                this.item = ItemStack.EMPTY;
            else
                this.item = items.get(serverWorld.random.nextBetweenExclusive(0, items.size()));
        }
    }

    public boolean brush(long worldTime, ServerWorld world, PlayerEntity player, Direction hitDirection, ItemStack brush) {
        if (this.hitDirection == null) {
            this.hitDirection = hitDirection;
        }
        OverlyComplicatedGarbage.LOGGER.info("BRUSHING!");
        this.nextDustTime = worldTime + 40L;
        if (worldTime < this.nextBrushTime) {
            return false;
        } else {
            this.nextBrushTime = worldTime + 10L;
            this.generateItem(world, player, brush);
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

    private void generateItem(ServerWorld world, PlayerEntity player, ItemStack brush) {
        if (this.item == null)
        {
            if (getWorld() instanceof ServerWorld serverWorld)
            {
                List<ItemStack> items = TrashedItemsStateManager.get(serverWorld).items;
                if (items.isEmpty())
                    this.item = ItemStack.EMPTY;
                else
                    this.item = items.get(serverWorld.random.nextBetweenExclusive(0, items.size()));
            }
            this.markDirty();
        }
    }

    private void finishBrushing(ServerWorld world, PlayerEntity player, ItemStack brush) {
        this.spawnItem(world, player, brush);
        BlockState blockState = this.getCachedState();
        world.syncWorldEvent(3008, this.getPos(), Block.getRawIdFromState(blockState));
        Block block = this.getCachedState().getBlock();
        Block block2;
        if (block instanceof SuspiciousGarbageBlock suspiciousGarbageBlock) {
            block2 = suspiciousGarbageBlock.getBaseBlock();
        } else {
            block2 = Blocks.AIR;
        }

        world.setBlockState(this.pos, block2.getDefaultState(), 3);
    }

    private void spawnItem(ServerWorld world, PlayerEntity player, ItemStack brush) {
        this.generateItem(world, player, brush);
        if (this.item != null) {
            if(item.isEmpty())
                return;
            double d = (double) EntityType.ITEM.getWidth();
            double e = (double)1.0F - d;
            double f = d / (double)2.0F;
            Direction direction = (Direction) Objects.requireNonNullElse(this.hitDirection, Direction.UP);
            BlockPos blockPos = this.pos.offset(direction, 1);
            double g = (double)blockPos.getX() + (double)0.5F * e + f;
            double h = (double)blockPos.getY() + (double)0.5F + (double)(EntityType.ITEM.getHeight() / 2.0F);
            double i = (double)blockPos.getZ() + (double)0.5F * e + f;
            ItemEntity itemEntity = new ItemEntity(world, g, h, i, this.item.split(world.random.nextInt(21) + 10));
            itemEntity.setVelocity(Vec3d.ZERO);
            world.spawnEntity(itemEntity);
            this.item = ItemStack.EMPTY;
        }

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
