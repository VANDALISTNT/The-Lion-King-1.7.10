package lionking.client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityDart;

@SideOnly(Side.CLIENT)
public class LKRenderDart extends Render
{
    private static final float SCALE = 0.05625F;
    private final ResourceLocation texture;

    public LKRenderDart(String type)
    {
        this.texture = new ResourceLocation("lionking", "item/dart_" + type + ".png");
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks)
    {
        LKEntityDart dart = (LKEntityDart) entity;
        
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        
        float interpolatedYaw = dart.prevRotationYaw + (dart.rotationYaw - dart.prevRotationYaw) * partialTicks - 90.0F;
        float interpolatedPitch = dart.prevRotationPitch + (dart.rotationPitch - dart.prevRotationPitch) * partialTicks;
        
        GL11.glRotatef(interpolatedYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(interpolatedPitch, 0.0F, 0.0F, 1.0F);
        
        applyShake(dart, partialTicks);
        
        renderDartModel();
        
        GL11.glPopMatrix();
    }

    private void applyShake(LKEntityDart dart, float partialTicks)
    {
        float shake = (float)dart.arrowShake - partialTicks;
        if (shake > 0.0F)
        {
            float shakeAngle = -MathHelper.sin(shake * 3.0F) * shake;
            GL11.glRotatef(shakeAngle, 0.0F, 0.0F, 1.0F);
        }
    }

    private void renderDartModel()
    {
        bindEntityTexture(null); 
        Tessellator tessellator = Tessellator.instance;
        
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(SCALE, SCALE, SCALE);
        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);

        renderDartSides(tessellator);
        renderDartBody(tessellator);
        
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    }

    private void renderDartSides(Tessellator tessellator)
    {
        float uMin = 0.0F;
        float uMax = 0.15625F;
        float vMinSide = 5.0F / 32.0F;
        float vMaxSide = 10.0F / 32.0F;

        GL11.glNormal3f(SCALE, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7.0, -2.0, -2.0, uMin, vMinSide);
        tessellator.addVertexWithUV(-7.0, -2.0, 2.0, uMax, vMinSide);
        tessellator.addVertexWithUV(-7.0, 2.0, 2.0, uMax, vMaxSide);
        tessellator.addVertexWithUV(-7.0, 2.0, -2.0, uMin, vMaxSide);
        tessellator.draw();

        GL11.glNormal3f(-SCALE, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7.0, 2.0, -2.0, uMin, vMinSide);
        tessellator.addVertexWithUV(-7.0, 2.0, 2.0, uMax, vMinSide);
        tessellator.addVertexWithUV(-7.0, -2.0, 2.0, uMax, vMaxSide);
        tessellator.addVertexWithUV(-7.0, -2.0, -2.0, uMin, vMaxSide);
        tessellator.draw();
    }

    private void renderDartBody(Tessellator tessellator)
    {
        float uMinBody = 0.0F;
        float uMaxBody = 0.5F;
        float vMinBody = 0.0F / 32.0F;
        float vMaxBody = 5.0F / 32.0F;

        for (int i = 0; i < 4; i++)
        {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, SCALE);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-8.0, -2.0, 0.0, uMinBody, vMinBody);
            tessellator.addVertexWithUV(8.0, -2.0, 0.0, uMaxBody, vMinBody);
            tessellator.addVertexWithUV(8.0, 2.0, 0.0, uMaxBody, vMaxBody);
            tessellator.addVertexWithUV(-8.0, 2.0, 0.0, uMinBody, vMaxBody);
            tessellator.draw();
        }
    }
}
