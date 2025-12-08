package com.thesatyric.overly_complicated_garbage.misc;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.List;

public class TrashedItemsState extends PersistentState {
    public List<ItemStack> items = new ArrayList<>();
    public TrashedItemsState() {}
    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList list = new NbtList();
        for (ItemStack item : items)
        {

            NbtCompound compound = new NbtCompound();
            list.add(item.toNbt(registries));
        }
        nbt.put("items", list);
        return nbt;
    }

    public static TrashedItemsState fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries)
    {
        TrashedItemsState state = new TrashedItemsState();
        NbtList list = nbt.getList("items", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound itemNbt = list.getCompound(i);
            ItemStack stack = ItemStack.fromNbtOrEmpty(registries, itemNbt);
            state.items.add(stack);
        }
        return state;
    }

    public static final Type<TrashedItemsState> TYPE = new Type<>(
            TrashedItemsState::new,
            TrashedItemsState::fromNbt,
            null
    );
}
