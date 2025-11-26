package lionking.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.entity.LKEntityCustomFX;
import lionking.client.LKClientProxy;

@SideOnly(Side.CLIENT)
public class LKRenderCustomFX extends Render {
    public static float ticks;

    public LKRenderCustomFX() {
        this.shadowSize = 0.0F;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return LKClientProxy.ICONS_TEXTURE; 
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        LKEntityCustomFX particle = (LKEntityCustomFX) entity;
        setupRenderEnvironment(particle, partialTicks);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(particle.getBrightnessForRender(partialTicks));
        
        renderParticle(particle, tessellator, partialTicks);
        
        tessellator.draw();
    }

    private void setupRenderEnvironment(LKEntityCustomFX particle, float partialTicks) {
        bindEntityTexture(particle);
        
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        EntityFX.interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        EntityFX.interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        EntityFX.interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
    }

    private void renderParticle(LKEntityCustomFX particle, Tessellator tessellator, float partialTicks) {
        float rotationX = ActiveRenderInfo.rotationX;
        float rotationZ = ActiveRenderInfo.rotationZ;
        float rotationYZ = ActiveRenderInfo.rotationYZ;
        float rotationXY = ActiveRenderInfo.rotationXY;
        float rotationXZ = ActiveRenderInfo.rotationXZ;
        
        particle.renderParticle(tessellator, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
    }
}