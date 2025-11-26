package lionking.client;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.tileentity.LKTileEntityGrindingBowl;

@SideOnly(Side.CLIENT)
public class LKRenderGrindingStick extends TileEntitySpecialRenderer
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking", "item/stick.png");
    private final LKModelGrindingStick model;

    public LKRenderGrindingStick()
    {
        this.model = new LKModelGrindingStick();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks)
    {
        LKTileEntityGrindingBowl bowl = (LKTileEntityGrindingBowl) tileEntity;
        float rotation = bowl.stickRotation + (partialTicks * 8.0F);
        renderStick((float)x, (float)y, (float)z, rotation);
    }

    private void renderStick(float x, float y, float z, float rotation)
    {
        bindTexture(TEXTURE);
        
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef(x + 0.5F, y + 1.4F, z + 0.5F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
        
        model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        
        GL11.glPopMatrix();
    }
}