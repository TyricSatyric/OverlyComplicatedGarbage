package com.thesatyric.overly_complicated_garbage;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class OCGDamageTypes {
    public static final RegistryKey<DamageType> BAG_SUFFOCATION = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(OverlyComplicatedGarbage.MOD_ID, "bag_suffocation"));

    public static DamageSource of(World world, RegistryKey<DamageType> key)
    {
        return new DamageSource(world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(key));
    }
}
