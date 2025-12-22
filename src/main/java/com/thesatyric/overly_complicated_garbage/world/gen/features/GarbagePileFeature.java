package com.thesatyric.overly_complicated_garbage.world.gen.features;

import com.mojang.serialization.Codec;
import com.thesatyric.overly_complicated_garbage.OCGarbageBlocks;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.List;

public class GarbagePileFeature extends Feature<DefaultFeatureConfig> {
    public GarbagePileFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        Random random = context.getRandom();
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        int radius = 3 + random.nextInt(5);
        int maxHeight = Math.round(radius * 1.5f) + random.nextInt(3);

        OctavePerlinNoiseSampler noise =
                OctavePerlinNoiseSampler.create(random, List.of(2, 3, 4));
        boolean blocksPlaced = false;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {

                int worldX = origin.getX() + x;
                int worldZ = origin.getZ() + z;

                double dist = Math.sqrt(x * x + z * z);
                if (dist > radius) continue;

                double d = dist / radius;
                double dome = 1.0 - Math.pow(d, 1.6);
                double cliff = 1.0 - Math.pow(d, 4) * 0.9;

                double noiseValue = noise.sample(worldX * 0.08, 0, worldZ * 0.08) * 0.25;
                double heightFactor = Math.max(dome, cliff) + noiseValue;

                if (heightFactor <= 0) continue;

                int columnHeight = (int) (heightFactor * maxHeight);
                if (columnHeight <= 0) continue;

                int surfaceY = world.getTopY(
                        Heightmap.Type.WORLD_SURFACE_WG,
                        worldX,
                        worldZ
                );

                BlockPos.Mutable pos = new BlockPos.Mutable(worldX, surfaceY, worldZ);

                while (pos.getY() > 30
                        && world.getBlockState(pos.down()).isIn(OverlyComplicatedGarbage.REPLACEABLE_BLOCK)) {
                    pos.move(0, -1, 0);
                }

                for (int y = 0; y < columnHeight; y++) {
                    if (!world.getBlockState(pos).isIn(OverlyComplicatedGarbage.REPLACEABLE_BLOCK)) {
                        pos.move(0, 1, 0);
                        continue;
                    }
                    blocksPlaced = true;
                    world.setBlockState(
                            pos,
                            random.nextFloat() < 0.7f
                                    ? OCGarbageBlocks.GARBAGE_BLOCK.getDefaultState()
                                    : OCGarbageBlocks.SUSPICIOUS_GARBAGE_BLOCK.getDefaultState(),
                            3
                    );
                    pos.move(0, 1, 0);
                }
            }
        }

        return blocksPlaced;
    }
}
