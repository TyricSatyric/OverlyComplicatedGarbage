package com.thesatyric.overly_complicated_garbage;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverlyComplicatedGarbage implements ModInitializer {
    public static final String MOD_ID = "overly_complicated_garbage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        OCGarbageBlocks.initialize();
        OCGarbageItems.initialize();
        LOGGER.info("Get ready to overcomplicate your garbage!");
    }
}
