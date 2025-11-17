package com.thesatyric.overly_complicated_garbage.blocks.block_entities;

import com.thesatyric.overly_complicated_garbage.OCGBlockEntities;
import com.thesatyric.overly_complicated_garbage.OCProperties;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.blocks.GarbageBag;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;

public class GarbageBagBlockEntity extends BlockEntity implements Inventory {
    private DefaultedList<ItemStack> items;
    public final int MAX_ITEMS = 128;
    public int itemCount = 0;
    private int latestSlot = 0;
    public GarbageBagBlockEntity(BlockPos pos, BlockState state) {
        super(OCGBlockEntities.GARBAGE_BAG_BLOCK_ENTITY, pos, state);
        this.items = DefaultedList.ofSize(128, ItemStack.EMPTY);
    }
    void print(String msg)
    {
        OverlyComplicatedGarbage.LOGGER.info(msg);
    }

    public DefaultedList<ItemStack> getItems()
    {
        return items;
    }
    public void addItem(World world, BlockPos pos, ItemStack stack, BlockState state)
    {
        var itemsToAdd = 0;
        OverlyComplicatedGarbage.LOGGER.info(String.valueOf(stack.getCount()));
        if (itemCount + stack.getCount() > MAX_ITEMS)
        {
            itemsToAdd = MAX_ITEMS-itemCount;
        }
        else {
            itemsToAdd = stack.getCount();
        }
        for (ItemStack item : items) {
            if (item.getItem() == stack.getItem())
            {
                if (item.getCount() < item.getMaxCount() - itemsToAdd)
                {
                    item.increment(itemsToAdd);
                    stack.decrement(itemsToAdd);
                    itemCount += itemsToAdd;
                }
                else
                {
                    item.increment(item.getMaxCount() - item.getCount());
                    stack.decrement(item.getMaxCount() - item.getCount());
                    itemCount += item.getMaxCount() - item.getCount();
                    itemsToAdd -= item.getMaxCount() - item.getCount();
                }
            }
        }
        if (itemsToAdd == 0) {
            if (itemCount == 128) {
                world.setBlockState(pos, state.with(GarbageBag.FILL, 2));
            }
            else if (itemCount > 0) {
                world.setBlockState(pos, state.with(GarbageBag.FILL, 1));
            }
            else {
                world.setBlockState(pos, state.with(GarbageBag.FILL, 0));
            }
            markDirty();
            return;
        }
        ItemStack stackToAdd = new ItemStack(stack.getItem(), itemsToAdd);
        items.set(latestSlot, stackToAdd);
        stack.decrement(itemsToAdd);
        latestSlot += 1;
        itemCount += itemsToAdd;
        if (itemCount == 128) {
            world.setBlockState(pos, state.with(GarbageBag.FILL, 2));
        }
        else if (itemCount > 0) {
            world.setBlockState(pos, state.with(GarbageBag.FILL, 1));
        }
        else {
            world.setBlockState(pos, state.with(GarbageBag.FILL, 0));
        }
        markDirty();
        OverlyComplicatedGarbage.LOGGER.info(String.valueOf(itemCount));
    }


    public void dropEverything(World world, BlockPos pos)
    {
        if (world.isClient())
            return;
        for(ItemStack stack : items)
        {
            if (stack == ItemStack.EMPTY)
                return;
            ItemEntity entity = new ItemEntity(world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, stack);
            Random random = new Random();
            world.spawnEntity(entity);
            entity.addVelocity(random.nextDouble(-0.1, 0.1), random.nextDouble(0, 0.2), random.nextDouble(-0.1, 0.1));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        Inventories.writeNbt(nbt, this.items, registries);
        nbt.putInt("latest_slot", latestSlot);
        nbt.putInt("total_items", itemCount);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.items = DefaultedList.ofSize(128, ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.items, registries);
        itemCount = nbt.getInt("total_items");
        latestSlot = nbt.getInt("latest_slot");
    }


    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.items;
    }

    protected void setHeldStacks(DefaultedList<ItemStack> items) {
        this.items = items;
    }


    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }

    @Override
    public int size() {
        return 128;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = (ItemStack) Objects.requireNonNullElse((ItemStack)this.items.get(slot), ItemStack.EMPTY);
        this.items.set(slot, ItemStack.EMPTY);

        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return this.removeStack(slot, 1);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            this.removeStack(slot, 1);
        }
        else {
            this.items.set(slot, stack);
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    @Override
    public void clear() {
        this.items.clear();
    }
}
