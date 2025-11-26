package lionking.entity;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
import net.minecraft.util.MathHelper;

import java.awt.Color;

public class LKEntityPassionFX extends EntityFX {
    public LKEntityPassionFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        initializeParticle(world, motionX, motionY, motionZ);
    }

    private void initializeParticle(World world, double motionX, double motionY, double motionZ) {
        int rgb = Color.HSBtoRGB(world.rand.nextFloat(), 1.0F, 1.0F);
        Color color = new Color(rgb);
        particleRed = color.getRed() / 255.0F;
        particleGreen = color.getGreen() / 255.0F;
        particleBlue = color.getBlue() / 255.0F;
        particleMaxAge = 32;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        float ageFraction = (particleAge + partialTick) / particleMaxAge;
        ageFraction = MathHelper.clamp_float(ageFraction, 0.0F, 1.0F);

        int baseBrightness = super.getBrightnessForRender(partialTick);
        int lowBits = baseBrightness & 255;
        int highBits = baseBrightness >> 16 & 255;
        lowBits += (int) (ageFraction * 15.0F * 16.0F);
        lowBits = Math.min(lowBits, 240);

        return lowBits | (highBits << 16);
    }

    @Override
    public float getBrightness(float partialTick) {
        float ageFraction = (particleAge + partialTick) / particleMaxAge;
        ageFraction = MathHelper.clamp_float(ageFraction, 0.0F, 1.0F);

        float baseBrightness = super.getBrightness(partialTick);
        return baseBrightness * ageFraction + (1.0F - ageFraction);
    }
}