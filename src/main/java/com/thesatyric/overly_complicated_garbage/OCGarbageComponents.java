package com.thesatyric.overly_complicated_garbage;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class OCGarbageComponents {

    public static final ComponentType<Boolean> PRICKLY_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(OverlyComplicatedGarbage.MOD_ID, "prickly"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );
    public static void initialize()
    {

    }
}
