package com.thesatyric.overly_complicated_garbage.blocks;

import com.mojang.serialization.MapCodec;
import com.thesatyric.overly_complicated_garbage.OCGarbageItems;
import com.thesatyric.overly_complicated_garbage.OCProperties;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import com.thesatyric.overly_complicated_garbage.blocks.block_entities.GarbageBagBlockEntity;
import com.thesatyric.overly_complicated_garbage.blocks.block_entities.TrashCanBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.HTML;
import java.util.stream.Stream;

public class TrashCanBlock extends BlockWithEntity {
    public static final BooleanProperty HAS_BAG = OCProperties.HAS_BAG;
    public static final BooleanProperty HAS_GARBAGE = OCProperties.HAS_GARBAGE;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final BooleanProperty STICKY = OCProperties.STICKY;
    static final VoxelShape EMPTY_SHAPE;
    static final VoxelShape OPEN_BAG_SHAPE;
    static final VoxelShape CLOSED_BAG_SHAPE;
    static final VoxelShape FILLER_SHAPE;
    public TrashCanBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(HAS_BAG, false)
                .with(HAS_GARBAGE, false)
                .with(OPEN, true)
                .with(STICKY, false));
    }
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(TrashCanBlock::new);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrashCanBlockEntity(pos, state);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{HAS_BAG, OPEN, HAS_GARBAGE, STICKY});
    }


    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof TrashCanBlockEntity trashCanBlockEntity)) {
            return super.onUse(state, world, pos, player, hit);
        }
        Item held_item = player.getStackInHand(player.getActiveHand()).getItem();
        OverlyComplicatedGarbage.LOGGER.info(held_item.toString());
        if (held_item != Items.AIR) {
            if(!state.get(HAS_BAG))
            {
                if(!state.get(STICKY) && held_item == Items.SLIME_BALL)
                {
                    world.setBlockState(pos, state.with(STICKY, true));
                    player.getStackInHand(player.getActiveHand()).decrement(1);
                    world.playSound(player, pos, SoundEvents.ITEM_HONEYCOMB_WAX_ON, SoundCategory.BLOCKS);
                    for (int i = 0; i < world.random.nextInt(10)+30; i++) {
                        world.addParticle(ParticleTypes.WAX_ON,
                                pos.getX() + world.random.nextDouble(),
                                pos.getY() + world.random.nextDouble(),
                                pos.getZ() + world.random.nextDouble(),
                                world.random.nextDouble() * 2 - 1,
                                world.random.nextDouble() * 2 - 1,
                                world.random.nextDouble() * 2 - 1);
                    }
                    return ActionResult.SUCCESS;
                }
                else if(state.get(STICKY) && player.getStackInHand(player.getActiveHand()).isIn(ItemTags.AXES))
                {
                    world.setBlockState(pos, state.with(STICKY, false));
                    player.getStackInHand(player.getActiveHand()).damage(1, player);

                    world.playSound(player, pos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS);
                    for (int i = 0; i < world.random.nextInt(10)+30; i++) {
                        world.addParticle(ParticleTypes.WAX_OFF,
                                pos.getX()+world.random.nextDouble(),
                                pos.getY()+world.random.nextDouble(),
                                pos.getZ()+world.random.nextDouble(),
                                world.random.nextDouble()*2-1,
                                world.random.nextDouble()*2-1,
                                world.random.nextDouble()*2-1);
                    }

                    return ActionResult.SUCCESS;
                }
            }
            if (held_item == OCGarbageItems.PLASTIC_BAG || held_item == OCGarbageItems.HELD_PLASTIC_BAG)
            {
                if (state.get(HAS_BAG))
                {
                    if(held_item == OCGarbageItems.PLASTIC_BAG)
                    {
                        trashCanBlockEntity.addItem(world, pos, player.getStackInHand(player.getActiveHand()), state);
                        world.playSound(player, pos, SoundEvents.ITEM_BUNDLE_INSERT, SoundCategory.BLOCKS);
                        return ActionResult.SUCCESS;
                    }
                    return ActionResult.PASS;
                }
                if (held_item == OCGarbageItems.HELD_PLASTIC_BAG)
                {
                    Iterable<ItemStack> items = player.getStackInHand(player.getActiveHand()).getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT).iterateNonEmpty();
                    for (ItemStack stack : items)
                    {
                        trashCanBlockEntity.addItem(world, pos, stack, state);
                    }
                    world.setBlockState(pos, state.with(HAS_BAG, true));
                    player.getStackInHand(player.getActiveHand()).setCount(0);
                }
                else
                {
                    world.setBlockState(pos, state.with(HAS_BAG, true).with(OPEN, true));
                    player.getStackInHand(player.getActiveHand()).decrementUnlessCreative(1, player);
                }
                return ActionResult.SUCCESS;
            }

            if (!state.get(OPEN) || trashCanBlockEntity.itemCount == trashCanBlockEntity.MAX_ITEMS || !state.get(HAS_BAG))
                return ActionResult.PASS;
            trashCanBlockEntity.addItem(world, pos, player.getStackInHand(player.getActiveHand()), state);
            world.playSound(player, pos, SoundEvents.ITEM_BUNDLE_INSERT, SoundCategory.BLOCKS);
        }
        else {

            OverlyComplicatedGarbage.LOGGER.info("Using without item");
            if (!state.get(HAS_BAG))
                return ActionResult.PASS;
            world.setBlockState(pos, state.with(OPEN, !state.get(OPEN)));
            world.playSound(player, pos, SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.BLOCKS);
            if (player.isSneaking() && state.get(HAS_BAG))
            {
                ItemStack stack = new ItemStack(OCGarbageItems.HELD_PLASTIC_BAG);
                stack.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(trashCanBlockEntity.getItems()));
                player.setStackInHand(player.getActiveHand(), stack);
                world.setBlockState(pos, state.with(HAS_BAG, false).with(HAS_GARBAGE, false).with(OPEN, false));
                trashCanBlockEntity.clear();
            }
        }
        return ActionResult.SUCCESS;

    }
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        TrashCanBlockEntity entity = (TrashCanBlockEntity) world.getBlockEntity(pos);
        if (entity == null)
            return;
        if (entity.itemCount > 0) {
            world.setBlockState(pos, state.with(HAS_GARBAGE, true).with(HAS_BAG, true).with(OPEN, false));
        }
        else {
            world.setBlockState(pos, state.with(HAS_GARBAGE, false));
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!(world.getBlockEntity(pos) instanceof TrashCanBlockEntity trashCanBlockEntity)) {
            return super.onBreak(world, pos, state, player);
        }
        trashCanBlockEntity.dropEverything(world, pos);
        if (state.get(HAS_BAG)) {
            ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(OCGarbageItems.PLASTIC_BAG));
            world.spawnEntity(entity);
        }
        return super.onBreak(world, pos, state, player);
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
