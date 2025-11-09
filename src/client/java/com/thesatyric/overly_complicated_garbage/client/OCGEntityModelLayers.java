package com.thesatyric.overly_complicated_garbage.client;

import com.google.common.collect.Sets;
import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.util.Set;

public class OCGEntityModelLayers {
    private static final Set<EntityModelLayer> LAYERS = Sets.newHashSet();
    public static final EntityModelLayer BAG = registerMain("bag");

    private static EntityModelLayer registerMain(String id) {
        return register(id, "main");
    }
    public static void initialize(){}
    private static EntityModelLayer register(String id, String layer) {
        EntityModelLayer entityModelLayer = create(id, layer);
        if (!LAYERS.add(entityModelLayer)) {
            throw new IllegalStateException("Duplicate registration for " + String.valueOf(entityModelLayer));
        } else {
            return entityModelLayer;
        }
    }


    private static EntityModelLayer create(String id, String layer) {
        return new EntityModelLayer(Identifier.of(OverlyComplicatedGarbage.MOD_ID, id), layer);
    }
}
