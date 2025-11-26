package lionking.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import lionking.mod_LionKing;
import lionking.entity.LKEntityCoin;

@SideOnly(Side.CLIENT)
public class LKRenderThrownCoin extends Render {

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TextureMap.locationItemsTexture;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        IIcon icon = ((LKEntityCoin) entity).getCoinType() == 0 ? mod_LionKing.rafikiCoin.getIconFromDamage(0) : mod_LionKing.ziraCoin.getIconFromDamage(0);
        if (icon != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x, (float) y, (float) z);       
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);                    
            GL11.glScalef(0.5F, 0.5F, 0.5F);                         
            bindEntityTexture(entity);                                
            renderThrowable(Tessellator.instance, icon);              
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);                   
            GL11.glPopMatrix();
        }
    }

    private void renderThrowable(Tessellator tessellator, IIcon icon) {
        float minU = icon.getMinU();           
        float maxU = icon.getMaxU();           
        float minV = icon.getMinV();           
        float maxV = icon.getMaxV();           
        float width = 1.0F;                    
        float height = 0.5F;                   
        float offsetY = 0.25F;                 

        GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F); 
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);         

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);                             
        tessellator.addVertexWithUV(0.0F - height, 0.0F - offsetY, 0.0D, minU, maxV);
        tessellator.addVertexWithUV(width - height, 0.0F - offsetY, 0.0D, maxU, maxV);
        tessellator.addVertexWithUV(width - height, width - offsetY, 0.0D, maxU, minV);
        tessellator.addVertexWithUV(0.0F - height, width - offsetY, 0.0D, minU, minV);
        tessellator.draw();
    }
}