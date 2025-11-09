package com.thesatyric.overly_complicated_garbage.blocks;

import com.mojang.serialization.MapCodec;
import com.thesatyric.overly_complicated_garbage.OCProperties;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.blocks.block_entities.GarbageBagBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GarbageBag extends BlockWithEntity {
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final IntProperty FILL = OCProperties.GARBAGE_FILL;
    
    public GarbageBag(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(OPEN, true)));
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FILL, 0)));
    }
    public BlockState withFill(int amount) {
        return (BlockState)this.getDefaultState().with(this.getFill(), amount);
    }
    public BlockState withOpen(boolean open) {
        return (BlockState)this.getDefaultState().with(this.getOpen(), open);
    }

    private Property<Integer> getFill() {
        return FILL;
    }
    private Property<Boolean> getOpen() {
        return OPEN;
    }


    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(GarbageBag::new);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GarbageBagBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof GarbageBagBlockEntity garbageBlockEntity)) {
            return super.onUse(state, world, pos, player, hit);
        }
        OverlyComplicatedGarbage.LOGGER.info(player.getStackInHand(player.getActiveHand()).toString());
        if (player.getStackInHand(player.getActiveHand()).getItem() != Items.AIR) {
            OverlyComplicatedGarbage.LOGGER.info("Adding Stack!");
            if (!state.get(OPEN) || garbageBlockEntity.itemCount == garbageBlockEntity.MAX_ITEMS)
                return ActionResult.PASS;
            garbageBlockEntity.addItem(world, pos, player.getStackInHand(player.getActiveHand()), state);
            world.playSound(player, pos, SoundEvents.ITEM_BUNDLE_INSERT, SoundCategory.BLOCKS);
        } else {
            OverlyComplicatedGarbage.LOGGER.info("Using without item");
            world.setBlockState(pos, state.with(OPEN, !state.get(OPEN)));
            world.playSound(player, pos, SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.BLOCKS);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        GarbageBagBlockEntity entity = (GarbageBagBlockEntity) world.getBlockEntity(pos);
        if (entity == null)
            return;
        if (entity.itemCount == 128) {
            world.setBlockState(pos, state.with(GarbageBag.FILL, 2));
        }
        else if (entity.itemCount > 0) {
            world.setBlockState(pos, state.with(GarbageBag.FILL, 1));
        }
        else {
            world.setBlockState(pos, state.with(GarbageBag.FILL, 0));
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!(world.getBlockEntity(pos) instanceof GarbageBagBlockEntity garbageBlockEntity)) {
            return super.onBreak(world, pos, state, player);
        }
        garbageBlockEntity.dropEverything(world, pos);
        return super.onBreak(world, pos, state, player);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, OPEN, FILL});
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(1, 0, 1,15, 9, 15);
    }

    @Override
    protected VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(1, 0, 1,15, 9, 15);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(1, 0, 1,15, 9, 15);
    }

    @Override
    protected VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return Block.createCuboidShape(1, 0, 1,15, 9, 15);
    }
}
