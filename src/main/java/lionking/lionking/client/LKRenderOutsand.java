package lionking.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityOutsand;
import lionking.mod_LionKing;

@SideOnly(Side.CLIENT)
public class LKRenderOutsand extends Render
{
    private final RenderBlocks renderBlocks;

    public LKRenderOutsand()
    {
        this.renderBlocks = new RenderBlocks();
        this.shadowSize = 0.5F;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TextureMap.locationBlocksTexture;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        this.bindEntityTexture(entity);
        GL11.glDisable(GL11.GL_LIGHTING);

        LKEntityOutsand outsandEntity = (LKEntityOutsand)entity;
        Block outsandBlock = mod_LionKing.outsand;
        
        renderBlocks.setRenderBoundsFromBlock(outsandBlock);
        renderBlocks.renderBlockAsItem(outsandBlock, 0, outsandEntity.getBrightness(partialTicks));
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
