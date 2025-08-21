package com.thesatyric.overly_complicated_garbage.client.particles;

import com.thesatyric.overly_complicated_garbage.OCGarbageBlocks;
import com.thesatyric.overly_complicated_garbage.blocks.AshBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.BlockStateRaycastContext;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class AshDustParticle extends AnimatedParticle {

    AshDustParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int color, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 0.0f);
        this.velocityMultiplier = 0.92F;
        this.scale = 0.5F;
        this.setAlpha(1.0F);
        this.setColor((float) 0.1f, (float) 0.1f, (float) 0.1f);
        this.maxAge = 60;
        this.setSpriteForAge(spriteProvider);
        this.collidesWithWorld = true;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }

    public void tick() {
        super.tick();
        if (!this.dead) {

            this.setSpriteForAge(this.spriteProvider);
            if (this.age > this.maxAge / 2) {
                this.setAlpha(1.0F - ((float) this.age - (float) (this.maxAge / 2)) / (float) this.maxAge);
            }

            if (this.world.getBlockState(BlockPos.ofFloored(this.x, this.y, this.z)).isAir()) {
                this.velocityY -= (double) 0.005F + random.nextTriangular((double) 0.002F, (double)0.01F);
            }
        }

    }



    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new AshDustParticle(clientWorld, d, e, f, g, h, i, ColorHelper.getArgb(255, 255, 255, 255), this.spriteProvider);
        }
    }
}