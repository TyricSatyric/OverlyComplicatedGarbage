package com.thesatyric.overly_complicated_garbage.blocks;

import com.mojang.serialization.MapCodec;
import com.thesatyric.overly_complicated_garbage.OCGarbageItems;
import com.thesatyric.overly_complicated_garbage.OCProperties;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AshBlock extends FallingBlock {
    public AshBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(LAYERS, 1));

    }


    @Override
    protected MapCodec<? extends AshBlock> getCodec() {
        return null;
    }

    @Override
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return 0;
    }

    public static final MapCodec<AshBlock> CODEC = createCodec(AshBlock::new);
    public static final int MAX_LAYERS = 16;
    public static final IntProperty LAYERS;
    private static final VoxelShape[] SHAPES_BY_LAYERS;

    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        if (type == NavigationType.LAND) {
            return (Integer)state.get(LAYERS) < 10;
        } else {
            return false;
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        if (world.getBlockState(pos.down()).getBlock() instanceof AshBlock)
        {
            if (world.getBlockState(pos.down()).get(LAYERS) < 16)
            {
                FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(world, pos, state);
                this.configureFallingBlockEntity(fallingBlockEntity);
            }
        }
    }

    public BlockState withLayers(int layers) {
        return (BlockState)this.getDefaultState().with(this.getLayers(), layers);
    }

    private Property<Integer> getLayers() {
        return LAYERS;
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && !player.isCreative())
        {
            ItemStack drop = new ItemStack(OCGarbageItems.ASH_DUST, state.get(LAYERS));
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop));
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onDestroyedOnLanding(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
        super.onDestroyedOnLanding(world, pos, fallingBlockEntity);
        BlockState blockBelow = world.getBlockState(pos);
        BlockState blockBelowUp = world.getBlockState(pos.up());
        if (blockBelow.getBlock() instanceof AshBlock)
        {
            int totalLayers;
            if (blockBelowUp.getBlock() instanceof AshBlock)
            {
                OverlyComplicatedGarbage.LOGGER.info(blockBelowUp.get(LAYERS).toString());
                OverlyComplicatedGarbage.LOGGER.info("Up");
                totalLayers = blockBelowUp.get(LAYERS) + fallingBlockEntity.getBlockState().get(LAYERS);
                if (totalLayers > 16)
                {
                    world.setBlockState(pos.up(), this.withLayers(16));
                    world.setBlockState(pos.up().up(), this.withLayers(totalLayers-16));
                }
                else
                {
                    world.setBlockState(pos.up(), this.withLayers(totalLayers));
                }
            }
            else
            {
                OverlyComplicatedGarbage.LOGGER.info(blockBelow.get(LAYERS).toString());
                OverlyComplicatedGarbage.LOGGER.info("Normal");
                totalLayers = blockBelow.get(LAYERS) + fallingBlockEntity.getBlockState().get(LAYERS);
                if (totalLayers > 16)
                {
                    world.setBlockState(pos, this.withLayers(16));
                    world.setBlockState(pos.up(), this.withLayers(totalLayers-16));
                }
                else
                {
                    world.setBlockState(pos, this.withLayers(totalLayers));

                }
            }

        }
        else
        {
            if (!world.isClient)
            {
                ItemStack drop = new ItemStack(OCGarbageItems.ASH_DUST, fallingBlockEntity.getBlockState().get(LAYERS));
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop));
            }
        }

    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES_BY_LAYERS[(Integer)state.get(LAYERS)];
    }

    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES_BY_LAYERS[(Integer)state.get(LAYERS) - 1];
    }

    protected VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return SHAPES_BY_LAYERS[(Integer)state.get(LAYERS)];
    }

    protected VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES_BY_LAYERS[(Integer)state.get(LAYERS)];
    }

    protected boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return (Integer)state.get(LAYERS) == 16 ? 0.2F : 1.0F;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{LAYERS});
    }

    static {
        LAYERS = OCProperties.ASH_LAYERS;
        SHAPES_BY_LAYERS = createLayers();
    }

    private static VoxelShape[] createLayers()
    {
        return new VoxelShape[]{
                VoxelShapes.empty(),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)1.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)2.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)3.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)4.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)5.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)6.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)7.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)8.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)9.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)10.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)11.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)12.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)13.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)14.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)15.0F, (double)16.0F),
                Block.createCuboidShape((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)16.0F, (double)16.0F),
        };
    }
}
