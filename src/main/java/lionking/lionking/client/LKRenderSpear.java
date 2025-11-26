package lionking.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import lionking.entity.LKEntitySpear;

@SideOnly(Side.CLIENT)
public class LKRenderSpear extends Render {
    private final boolean isPoisoned; 
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:item/spear.png");

    public LKRenderSpear(boolean isPoisoned) {
        this.isPoisoned = isPoisoned;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        LKEntitySpear spear = (LKEntitySpear) entity;
        bindEntityTexture(entity); 
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z); 
        GL11.glRotatef((spear.prevRotationYaw + (spear.rotationYaw - spear.prevRotationYaw) * partialTicks) - 90F, 0.0F, 1.0F, 0.0F); 
        GL11.glRotatef(spear.prevRotationPitch + (spear.rotationPitch - spear.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);     
        Tessellator tessellator = Tessellator.instance;

        int textureOffset = isPoisoned ? 10 : 0;
        float minUHandle = 0.0F;                  
        float maxUHandle = 0.5F;                  
        float minVHandle = (float) (0 + textureOffset * 10) / 32F;   
        float maxVHandle = (float) (5 + textureOffset * 10) / 32F;   
        float minUTip = 0.0F;                     
        float maxUTip = 0.15625F;                 
        float minVTip = (float) (5 + textureOffset * 10) / 32F;      
        float maxVTip = (float) (10 + textureOffset * 10) / 32F;     
        float scale = 0.05625F;                   

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);    

        float shake = (float) spear.arrowShake - partialTicks;
        if (shake > 0.0F) {
            float shakeAngle = -MathHelper.sin(shake * 3F) * shake;
            GL11.glRotatef(shakeAngle, 0.0F, 0.0F, 1.0F); 
        }

        GL11.glRotatef(45F, 1.0F, 0.0F, 0.0F);    
        GL11.glScalef(scale, scale, scale);       
        GL11.glTranslatef(-4F, 0.0F, 0.0F);       

        GL11.glNormal3f(scale, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7D, -2D, -2D, minUTip, minVTip);
        tessellator.addVertexWithUV(-7D, -2D, 2D, maxUTip, minVTip);
        tessellator.addVertexWithUV(-7D, 2D, 2D, maxUTip, maxVTip);
        tessellator.addVertexWithUV(-7D, 2D, -2D, minUTip, maxVTip);
        tessellator.draw();

        GL11.glNormal3f(-scale, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7D, 2D, -2D, minUTip, minVTip);
        tessellator.addVertexWithUV(-7D, 2D, 2D, maxUTip, minVTip);
        tessellator.addVertexWithUV(-7D, -2D, 2D, maxUTip, maxVTip);
        tessellator.addVertexWithUV(-7D, -2D, -2D, minUTip, maxVTip);
        tessellator.draw();

        for (int i = 0; i < 4; i++) {
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F); 
            GL11.glNormal3f(0.0F, 0.0F, scale);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-8D, -2D, 0.0D, minUHandle, minVHandle);
            tessellator.addVertexWithUV(8D, -2D, 0.0D, maxUHandle, minVHandle);
            tessellator.addVertexWithUV(8D, 2D, 0.0D, maxUHandle, maxVHandle);
            tessellator.addVertexWithUV(-8D, 2D, 0.0D, minUHandle, maxVHandle);
            tessellator.draw();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL); 
        GL11.glPopMatrix();
    }
}
