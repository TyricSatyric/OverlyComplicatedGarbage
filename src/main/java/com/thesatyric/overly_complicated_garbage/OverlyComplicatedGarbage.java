package com.thesatyric.overly_complicated_garbage;

import com.thesatyric.overly_complicated_garbage.world.gen.DumpRegion;
import com.thesatyric.overly_complicated_garbage.world.gen.OCGMaterialRules;
import com.thesatyric.overly_complicated_garbage.world.gen.OCGFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.SharedConstants;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class OverlyComplicatedGarbage implements ModInitializer, TerraBlenderApi {
    public static final String MOD_ID = "overly_complicated_garbage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final TagKey<Block> REPLACEABLE_BLOCK = TagKey.of(RegistryKeys.BLOCK, Identifier.of(OverlyComplicatedGarbage.MOD_ID, "garbage_replaceable_blocks"));

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
        OCGBlockEntities.initialize();
        OCGarbageBlocks.initialize();
        OCGarbageItems.initialize();
        OCGEntities.init();
        OCGFeatures.init();
        Registry.register(Registries.ITEM_GROUP, GARBAGE_ITEM_GROUP_KEY, GARBAGE_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(GARBAGE_ITEM_GROUP_KEY).register(itemGroup ->{
            itemGroup.add(OCGarbageItems.ASH_DUST);
            itemGroup.add(OCGarbageItems.CACTUS_PRICKLES);
            itemGroup.add(OCGarbageItems.BIOMASS_PROCESSOR);
            itemGroup.add(OCGarbageItems.ECO_FRIENDLY_PLASTIC);
            itemGroup.add(OCGarbageItems.ECO_FRIENDLY_PLASTIC_BLOCK);
            itemGroup.add(OCGarbageItems.PLASTIC_BAG);
            itemGroup.add(OCGarbageItems.BROKEN_PLASTIC_BAG);
            itemGroup.add(OCGarbageItems.TRASH_CAN);
            itemGroup.add(OCGarbageItems.GARBAGE_BLOCK);
            itemGroup.add(OCGarbageItems.SUSPICIOUS_GARBAGE_BLOCK);
        });
        LOGGER.info("Get ready to overcomplicate your garbage!");
    }

    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new DumpRegion(Identifier.of(MOD_ID, "overworld"), 2));
        TerraBlenderApi.super.onTerraBlenderInitialized();
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, OCGMaterialRules.makeRules());
    }
}
