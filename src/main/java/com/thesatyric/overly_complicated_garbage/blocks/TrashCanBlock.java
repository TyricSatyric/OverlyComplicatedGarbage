package com.thesatyric.overly_complicated_garbage.blocks;

import com.mojang.serialization.MapCodec;
import com.thesatyric.overly_complicated_garbage.OCProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.EmptyStackException;
import java.util.stream.Stream;

public class TrashCanBlock extends BlockWithEntity {
    public static final BooleanProperty HAS_BAG = OCProperties.HAS_BAG;
    public static final BooleanProperty HAS_GARBAGE = OCProperties.HAS_GARBAGE;
    public static final BooleanProperty OPEN = Properties.OPEN;
    static final VoxelShape EMPTY_SHAPE;
    static final VoxelShape OPEN_BAG_SHAPE;
    static final VoxelShape CLOSED_BAG_SHAPE;
    static final VoxelShape FILLER_SHAPE;
    public TrashCanBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(HAS_BAG, false).with(HAS_GARBAGE, false).with(OPEN, false)));
    }
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{HAS_BAG, OPEN, HAS_GARBAGE});
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HAS_BAG))
        {
            if(state.get(OPEN))
            {
                if(state.get(HAS_GARBAGE))
                {
                    return FILLER_SHAPE;
                }
                return OPEN_BAG_SHAPE;
            }
            return CLOSED_BAG_SHAPE;
        }
        return EMPTY_SHAPE;
    }

    @Override
    protected VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HAS_BAG))
        {
            if(state.get(OPEN))
            {
                if(state.get(HAS_GARBAGE))
                {
                    return FILLER_SHAPE;
                }
                return OPEN_BAG_SHAPE;
            }
            return CLOSED_BAG_SHAPE;
        }
        return EMPTY_SHAPE;
    }

    @Override
    protected VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        if (state.get(HAS_BAG))
        {
            if(state.get(OPEN))
            {
                if(state.get(HAS_GARBAGE))
                {
                    return FILLER_SHAPE;
                }
                return OPEN_BAG_SHAPE;
            }
            return CLOSED_BAG_SHAPE;
        }
        return EMPTY_SHAPE;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HAS_BAG))
        {
            if(state.get(OPEN))
            {
                if(state.get(HAS_GARBAGE))
                {
                    return FILLER_SHAPE;
                }
                return OPEN_BAG_SHAPE;
            }
            return CLOSED_BAG_SHAPE;
        }
        return EMPTY_SHAPE;
    }

    static {
        EMPTY_SHAPE = Stream.of(
                Block.createCuboidShape(4, 0, 4, 12, 1, 12),
                Block.createCuboidShape(4, 0, 12, 12, 10, 13),
                Block.createCuboidShape(4, 0, 3, 12, 10, 4),
                Block.createCuboidShape(12, 0, 3, 13, 10, 13),
                Block.createCuboidShape(3, 0, 3, 4, 10, 13),
                Block.createCuboidShape(3, 10, 13, 13, 12, 14),
                Block.createCuboidShape(3, 10, 2, 13, 12, 3),
                Block.createCuboidShape(13, 10, 2, 14, 12, 14),
                Block.createCuboidShape(2, 10, 2, 3, 12, 14),
                Block.createCuboidShape(14, 12, 1.25, 14.75, 13, 14.75),
                Block.createCuboidShape(1.25, 12, 1.25, 2, 13, 14.75),
                Block.createCuboidShape(2, 12, 1.25, 14, 13, 2),
                Block.createCuboidShape(2, 12, 14, 14, 13, 14.75)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        OPEN_BAG_SHAPE = Stream.of(
                EMPTY_SHAPE,
                Block.createCuboidShape(1, 11, 15, 15, 13, 16),
                Block.createCuboidShape(1, 13, 14, 15, 13.5, 15),
                Block.createCuboidShape(1, 11, 0, 15, 13, 1),
                Block.createCuboidShape(1, 13, 1, 15, 13.5, 2),
                Block.createCuboidShape(0, 11, 1, 1, 13, 15),
                Block.createCuboidShape(1, 13, 2, 2, 13.5, 14),
                Block.createCuboidShape(3, 12, 13, 13, 13, 14),
                Block.createCuboidShape(5, 6, 11, 11, 10, 12),
                Block.createCuboidShape(5, 6, 4, 11, 10, 5),
                Block.createCuboidShape(4, 6, 4, 5, 10, 12),
                Block.createCuboidShape(11, 6, 4, 12, 10, 12),
                Block.createCuboidShape(5, 5, 5, 11, 6, 11),
                Block.createCuboidShape(12, 10, 4, 13, 12, 12),
                Block.createCuboidShape(3, 10, 4, 4, 12, 12),
                Block.createCuboidShape(3, 10, 12, 13, 12, 13),
                Block.createCuboidShape(3, 10, 3, 13, 12, 4),
                Block.createCuboidShape(3, 12, 2, 13, 13, 3),
                Block.createCuboidShape(2, 12, 2, 3, 13, 14),
                Block.createCuboidShape(13, 12, 2, 14, 13, 14),
                Block.createCuboidShape(15, 11, 1, 16, 13, 15),
                Block.createCuboidShape(14, 13, 2, 15, 13.5, 14)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        CLOSED_BAG_SHAPE = Stream.of(
                EMPTY_SHAPE,
                Block.createCuboidShape(3, 12, 13, 13, 15, 14),
                Block.createCuboidShape(12, 15, 4, 13, 16, 12),
                Block.createCuboidShape(10, 16, 4, 12, 17, 12),
                Block.createCuboidShape(4, 16, 4, 6, 17, 12),
                Block.createCuboidShape(6, 16, 10, 10, 17, 12),
                Block.createCuboidShape(6, 16, 4, 10, 17, 6),
                Block.createCuboidShape(6, 17, 6, 10, 18, 10),
                Block.createCuboidShape(3, 15, 12, 13, 16, 13),
                Block.createCuboidShape(3, 15, 4, 4, 16, 12),
                Block.createCuboidShape(3, 15, 3, 13, 16, 4),
                Block.createCuboidShape(3, 12, 2, 13, 15, 3),
                Block.createCuboidShape(2, 12, 2, 3, 15, 14),
                Block.createCuboidShape(13, 12, 2, 14, 15, 14)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        FILLER_SHAPE = Stream.of(
                OPEN_BAG_SHAPE,
                Block.createCuboidShape(5, 6, 5, 11, 10, 11),
                Block.createCuboidShape(4, 10, 4, 12, 12, 12)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    }
}
