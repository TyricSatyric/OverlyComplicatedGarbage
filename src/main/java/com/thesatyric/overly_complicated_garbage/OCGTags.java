package com.thesatyric.overly_complicated_garbage;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class OCGTags {
    public static class Block {

    }

    public static class Item {
        public static final TagKey<net.minecraft.item.Item> BIO_ITEMS = TagKey.of(RegistryKeys.ITEM, Identifier.of(OverlyComplicatedGarbage.MOD_ID, "bio_items"));
    }
}
