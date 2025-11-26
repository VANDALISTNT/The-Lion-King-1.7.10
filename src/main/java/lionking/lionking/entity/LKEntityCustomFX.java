package lionking.entity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LKEntityCustomFX extends EntityFX {
    private int particleTextureIndex; 
    private boolean glow; 

    public LKEntityCustomFX(World world, int textureIndex, int maxAge, boolean glow, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z);
        initializeParticle(textureIndex, maxAge, glow, motionX, motionY, motionZ); 
    }
    
    private void initializeParticle(int textureIndex, int maxAge, boolean glow, double motionX, double motionY, double motionZ) {
        setSize(0.2F, 0.2F); 
        yOffset = height / 2.0F; 
        this.motionX = 0.01D + motionX; 
        this.motionY = 0.11D + motionY; 
        this.motionZ = 0.01D + motionZ; 
        particleScale = (worldObj.rand.nextFloat() * 0.5F + 0.5F) * 2.0F; 
        particleMaxAge = maxAge; 
        particleAge = 0; 
        particleTextureIndex = textureIndex; 
        this.glow = glow; 
    }

    @Override
    protected void entityInit() {
        
    }

    @Override
    protected boolean canTriggerWalking() {
        return false; 
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (particleAge++ >= particleMaxAge) {
            setDead(); 
            return;
        }

        moveEntity(motionX, motionY, motionZ); 
        
        if (posY == prevPosY) {
            motionX *= 1.1D;
            motionZ *= 1.1D;
        }
        
        motionX *= 0.86D;
        motionY *= 0.86D;
        motionZ *= 0.86D;

        if (onGround) {
            motionX *= 0.7D;
            motionZ *= 0.7D;
        }
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        if (!glow) return super.getBrightnessForRender(partialTick);

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
        if (!glow) return super.getBrightness(partialTick);

        float ageFraction = (particleAge + partialTick) / particleMaxAge;
        ageFraction = MathHelper.clamp_float(ageFraction, 0.0F, 1.0F); 

        float baseBrightness = super.getBrightness(partialTick);
        return baseBrightness * ageFraction + (1.0F - ageFraction); 
    }
    
    public void renderParticle(Tessellator tessellator, float partialTick, float rotX, float rotY, float rotZ, float rotXZ, float rotYZ) {
        float ageFraction = (particleAge + partialTick) / particleMaxAge * 32.0F;
        ageFraction = MathHelper.clamp_float(ageFraction, 0.0F, 1.0F); 
        particleScale = this.particleScale * ageFraction; 

        float uMin = (particleTextureIndex % 16) / 16.0F; 
        float uMax = uMin + 0.0624375F; 
        float vMin = (particleTextureIndex / 16) / 16.0F; 
        float vMax = vMin + 0.0624375F; 
        float scale = 0.1F * particleScale; 
        
        float renderX = (float) (prevPosX + (posX - prevPosX) * partialTick - interpPosX);
        float renderY = (float) (prevPosY + (posY - prevPosY) * partialTick - interpPosY);
        float renderZ = (float) (prevPosZ + (posZ - prevPosZ) * partialTick - interpPosZ);

        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F); 
        tessellator.addVertexWithUV(renderX - rotX * scale - rotXZ * scale, renderY - rotY * scale, renderZ - rotZ * scale - rotYZ * scale, uMax, vMax);
        tessellator.addVertexWithUV(renderX - rotX * scale + rotXZ * scale, renderY + rotY * scale, renderZ - rotZ * scale + rotYZ * scale, uMax, vMin);
        tessellator.addVertexWithUV(renderX + rotX * scale + rotXZ * scale, renderY + rotY * scale, renderZ + rotZ * scale + rotYZ * scale, uMin, vMin);
        tessellator.addVertexWithUV(renderX + rotX * scale - rotXZ * scale, renderY - rotY * scale, renderZ + rotZ * scale - rotYZ * scale, uMin, vMax);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        
    }

    @Override
    public boolean canAttackWithItem() {
        return false; 
    }
}
