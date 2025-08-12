package com.thesatyric.overly_complicated_garbage;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class OCGarbageItems {

    public static final Item ASH = register("ash", new Item.Settings().fireproof());

    public static void initialize()
    {

    }

    public static Item register(String name, Item.Settings settings) {
        Function<Item.Settings, Item> itemFactory = Item::new;
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(OverlyComplicatedGarbage.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }
}
