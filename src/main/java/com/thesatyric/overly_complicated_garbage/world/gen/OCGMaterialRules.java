package com.thesatyric.overly_complicated_garbage.world.gen;

import com.thesatyric.overly_complicated_garbage.OCGarbageBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class OCGMaterialRules {
    public static final MaterialRules.MaterialRule GARBAGE = makeStateRule(OCGarbageBlocks.GARBAGE_BLOCK);
    public static final MaterialRules.MaterialRule DIRT = makeStateRule(Blocks.DIRT);

    public static MaterialRules.MaterialRule makeRules()
    {
        MaterialRules.MaterialCondition isAtOrAboveWater = MaterialRules.water(-2, 0);
        MaterialRules.MaterialRule grassSurface = MaterialRules.sequence(MaterialRules.condition(isAtOrAboveWater, GARBAGE), DIRT);
        return MaterialRules.sequence(
                MaterialRules.condition(MaterialRules.biome(OCGBiomes.GARBAGE_DUMP), GARBAGE),
                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, grassSurface)
        );
    }


    static MaterialRules.MaterialRule makeStateRule(Block block){
        return MaterialRules.block(block.getDefaultState());
    }
}
