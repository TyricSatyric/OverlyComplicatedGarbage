package com.thesatyric.overly_complicated_garbage.blocks.block_entities;

import com.thesatyric.overly_complicated_garbage.OCGBlockEntities;
import com.thesatyric.overly_complicated_garbage.OCGEntities;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.blocks.GarbageBag;
import com.thesatyric.overly_complicated_garbage.blocks.TrashCanBlock;
import com.thesatyric.overly_complicated_garbage.entities.GarbageTruckEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.Vibrations;

import java.util.Objects;
import java.util.Random;

public class TrashCanBlockEntity extends BlockEntity implements Inventory{
    private DefaultedList<ItemStack> items;
    public final int MAX_ITEMS = 128;
    public int itemCount = 0;
    private int latestSlot = 0;

    public TrashCanBlockEntity(BlockPos pos, BlockState state) {
        super(OCGBlockEntities.TRASH_CAN_BLOCK_ENTITY, pos, state);
        this.items = DefaultedList.ofSize(128, ItemStack.EMPTY);
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

        ItemStack stackToAdd = stack.copy();
        stackToAdd.setCount(itemsToAdd);
        items.set(latestSlot, stackToAdd);
        stack.decrement(itemsToAdd);
        latestSlot += 1;
        itemCount += itemsToAdd;
        if (itemCount > 0) {
            world.setBlockState(pos, state.with(TrashCanBlock.HAS_BAG, true).with(TrashCanBlock.HAS_GARBAGE, true));
        }
        else {
            world.setBlockState(pos, state.with(TrashCanBlock.HAS_GARBAGE, false));
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
        itemCount = 0;
        latestSlot = 0;
        this.items.clear();
    }

}
