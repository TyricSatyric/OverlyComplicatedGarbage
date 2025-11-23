package com.thesatyric.overly_complicated_garbage;

import com.thesatyric.overly_complicated_garbage.blocks.block_entities.GarbageBagBlockEntity;
import com.thesatyric.overly_complicated_garbage.blocks.block_entities.TrashCanBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class OCGBlockEntities {
    public static final BlockEntityType<GarbageBagBlockEntity> GARBAGE_BAG_BLOCK_ENTITY = register("bag", GarbageBagBlockEntity::new, OCGarbageBlocks.GARBAGE_BAG);
    public static final BlockEntityType<TrashCanBlockEntity> TRASH_CAN_BLOCK_ENTITY = register("trash_can", TrashCanBlockEntity::new, OCGarbageBlocks.TRASH_CAN);
    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.of(OverlyComplicatedGarbage.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void initialize(){}
}
