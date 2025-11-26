package lionking.entity;

import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.world.World;

public class LKEntityPortalFX extends EntityPortalFX {
    public LKEntityPortalFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ, boolean isPrideLands) {
        super(world, x, y, z, motionX, motionY, motionZ);
        initializeParticleColor(isPrideLands); 
    }

    
    private void initializeParticleColor(boolean isPrideLands) {
        float brightness = worldObj.rand.nextFloat() * 0.6F + 0.4F; 
        particleRed = particleGreen = particleBlue = brightness; 

        if (isPrideLands) {
            
            particleRed *= 1.0F; 
            particleGreen *= 0.7F; 
            particleBlue *= 0.1F; 
        } else {
            
            particleRed *= 1.0F; 
            particleGreen *= 0.2F; 
            particleBlue *= 0.0F; 
        }
    }
}
