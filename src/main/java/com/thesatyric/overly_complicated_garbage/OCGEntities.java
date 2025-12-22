package com.thesatyric.overly_complicated_garbage;

import com.thesatyric.overly_complicated_garbage.entities.GarbageTruckEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

public class OCGEntities {
    public static final EntityType<GarbageTruckEntity> TRUCK = registerEntity(OverlyComplicatedGarbage.MOD_ID,
            "garbage_truck",
            EntityType.Builder.create(GarbageTruckEntity::new, SpawnGroup.MISC).dimensions(1.5f, 1.0f));

    public static final PointOfInterestType TRASH_CANS_POI =
            PointOfInterestHelper.register(Identifier.of(OverlyComplicatedGarbage.MOD_ID, "trash_cans_poi"), 0, 1, OCGarbageBlocks.TRASH_CAN);
    public static <T extends Entity>EntityType<T> registerEntity(String namespace, String id, EntityType.Builder<T> type) {
        RegistryKey<EntityType<?>> registryKey = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(namespace, id));
        return Registry.register(Registries.ENTITY_TYPE, registryKey, type.build(registryKey));
    }
    public static void init() {

        FabricDefaultAttributeRegistry.register(TRUCK, GarbageTruckEntity.createMobAttributes());
    }

}
