package com.thesatyric.overly_complicated_garbage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverlyComplicatedGarbage implements ModInitializer {
    public static final String MOD_ID = "overly_complicated_garbage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final RegistryKey<ItemGroup> GARBAGE_ITEM_GROUP_KEY =
            RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MOD_ID, "garbage_group"));
    public static final ItemGroup GARBAGE_ITEM_GROUP = FabricItemGroup.builder()
            .icon(()-> new ItemStack(OCGarbageItems.ASH_DUST))
            .displayName(Text.translatable("itemGroup.overly_complicated_garbage"))
            .build();

    @Override
    public void onInitialize() {
        OCGarbageComponents.initialize();
        OCGarbageParticles.initialize();
        OCProperties.initialize();
        OCGarbageBlocks.initialize();
        OCGarbageItems.initialize();
        Registry.register(Registries.ITEM_GROUP, GARBAGE_ITEM_GROUP_KEY, GARBAGE_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(GARBAGE_ITEM_GROUP_KEY).register(itemGroup ->{
            itemGroup.add(OCGarbageItems.ASH_DUST);
            itemGroup.add(OCGarbageItems.CACTUS_PRICKLES);
            itemGroup.add(OCGarbageItems.BIOMASS_PROCESSOR);
        });
        LOGGER.info("Get ready to overcomplicate your garbage!");
    }
}
