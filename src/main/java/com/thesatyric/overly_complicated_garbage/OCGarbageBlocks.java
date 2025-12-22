package com.thesatyric.overly_complicated_garbage;

import com.thesatyric.overly_complicated_garbage.blocks.*;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
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
                        .sounds(BlockSoundGroup.ANVIL)
                        .resistance(100f)
                        .hardness(3f)
                        .requiresTool());
    public static final Block ECO_FRIENDLY_PLASTIC_BLOCK = register("eco_friendly_plastic_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.COBWEB)
                    .hardness(0.7f)
                    .resistance(0.5f)
                    .solid());
    public static final CactusPricklesBlock CACTUS_PLACED_PRICKLES = (CactusPricklesBlock) register("cactus_prickles_placed",
            CactusPricklesBlock::new,
            AbstractBlock.Settings.create()
                    .breakInstantly()
                    .nonOpaque()
                    .noCollision()
                    .strength(0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .sounds(BlockSoundGroup.MOSS_BLOCK));
    public static final Block GARBAGE_BAG = register("garbage_bag", GarbageBag::new, AbstractBlock.Settings.create()
            .nonOpaque()
            .pistonBehavior(PistonBehavior.BLOCK)
            .sounds(BlockSoundGroup.AZALEA_LEAVES)
            .hardness(0.5f));
    public static final Block TRASH_CAN = register("trash_can", TrashCanBlock::new, AbstractBlock.Settings.create()
            .nonOpaque()
            .pistonBehavior(PistonBehavior.BLOCK)
            .sounds(BlockSoundGroup.STONE)
            .ticksRandomly()
            .hardness(0.8f));
    public static final Block GARBAGE_BLOCK = register("garbage_block", Block::new, AbstractBlock.Settings.create()
            .sounds(BlockSoundGroup.SLIME)
            .hardness(0.4f));
    public static final Block SUSPICIOUS_GARBAGE_BLOCK = register("suspicious_garbage_block", SuspiciousGarbageBlock::new, AbstractBlock.Settings.create()
            .sounds(BlockSoundGroup.SLIME)
            .hardness(0.6f));



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
