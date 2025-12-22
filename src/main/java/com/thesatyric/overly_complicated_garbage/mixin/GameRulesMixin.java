package com.thesatyric.overly_complicated_garbage.mixin;

import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(GameRules.class)
public class GameRulesMixin {
    @Shadow
    @Final
    private Map<GameRules.Key<?>, GameRules.Rule<?>> rules;

    @Inject(method = "<init>(Ljava/util/Map;Lnet/minecraft/resource/featuretoggle/FeatureSet;)V", at = @At("TAIL"))
    private <E extends Enum<E>> void overrideDefaults(CallbackInfo info) {


        this.rules.forEach((key, rule) -> {
            if(rule instanceof  GameRules.BooleanRule booleanRule){
                if (key == GameRules.TNT_EXPLOSION_DROP_DECAY ||key == GameRules.BLOCK_EXPLOSION_DROP_DECAY || key == GameRules.MOB_EXPLOSION_DROP_DECAY){
                    booleanRule.set(false, null);
                }
            }
        });
    }
}
