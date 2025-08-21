package com.thesatyric.overly_complicated_garbage;

import com.thesatyric.overly_complicated_garbage.items.AshDustItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class OCGarbageItems {

    public static final Item ASH_DUST = register("ash_dust", AshDustItem::new, new Item.Settings().fireproof());
//    public  static final Item ASH_BLOCK = register("ash_block", OCGarbageBlocks.ASH_BLOCK, new BlockItem.Settings().fireproof());
    public static final Item CACTUS_PRICKLES = register("cactus_prickles", Item::new, new Item.Settings().component(OCGarbageComponents.PRICKLY_COMPONENT, Boolean.TRUE));

    public static void initialize()
    {

    }


    public static Item register(String name, Block block, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(OverlyComplicatedGarbage.MOD_ID, name));
        BlockItem blockItem = new BlockItem(block, settings.registryKey(itemKey));

        return register(itemKey, blockItem);
    }

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(OverlyComplicatedGarbage.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));


        return register(itemKey, item);
    }

    public static Item register(RegistryKey<Item> itemKey, Item item)
    {
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

}
