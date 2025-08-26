package com.thesatyric.overly_complicated_garbage;

import com.thesatyric.overly_complicated_garbage.blocks.AshBlock;
import com.thesatyric.overly_complicated_garbage.blocks.BiomassProcessor;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class OCGarbageBlocks {
    public static final AshBlock ASH_LAYER = (AshBlock) register("ash_layer",
                AshBlock::new,
                AbstractBlock.Settings.create()
                        .breakInstantly()
                        .sounds(BlockSoundGroup.SAND)
                        .nonOpaque()
                        .blockVision((state, world, pos) -> (Integer)state.get(AshBlock.LAYERS) >= 16));

//    public static final ColoredFallingBlock ASH_BLOCK = (ColoredFallingBlock) register("ash_block",
//                (settings) -> new ColoredFallingBlock(new ColorCode(ColorHelper.getArgb(10, 10, 10)), settings),
//            ColoredFallingBlock.Settings.create()
//                        .breakInstantly()
//                        .sounds(BlockSoundGroup.SAND));
    public static final BiomassProcessor BIOMASS_PROCESSOR = (BiomassProcessor) register("biomass_processor",
                BiomassProcessor::new,
                AbstractBlock.Settings.create()
                        .sounds(BlockSoundGroup.ANVIL));


    public static void initialize() {}


    private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
        RegistryKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.registryKey(blockKey));


        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(OverlyComplicatedGarbage.MOD_ID, name));
    }

    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(OverlyComplicatedGarbage.MOD_ID, name));
    }


}
