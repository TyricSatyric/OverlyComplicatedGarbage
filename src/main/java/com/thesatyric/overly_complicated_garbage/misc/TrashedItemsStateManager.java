package com.thesatyric.overly_complicated_garbage.misc;

import com.thesatyric.overly_complicated_garbage.OverlyComplicatedGarbage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import static com.ibm.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class TrashedItemsStateManager {
    public static final String ID = "trashed_state";

    public static TrashedItemsState get(ServerWorld world)
    {
        OverlyComplicatedGarbage.LOGGER.info("Get Items");
        return world.getPersistentStateManager().getOrCreate(TrashedItemsState.TYPE, ID);
    }

}
