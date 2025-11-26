package lionking.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import lionking.tileentity.LKTileEntityMountedShooter;
import lionking.client.LKModelMountedShooter; 

public class LKTileEntityMountedShooterRenderer extends TileEntitySpecialRenderer {
    private final LKModelMountedShooter model = new LKModelMountedShooter(); 
    private static final ResourceLocation TEXTURE_WOOD = new ResourceLocation("lionking", "textures/items/shooter.png");
    private static final ResourceLocation TEXTURE_SILVER = new ResourceLocation("lionking", "textures/items/shooter_silver.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        LKTileEntityMountedShooter shooter = (LKTileEntityMountedShooter) tileEntity;
        renderShooter(shooter, (float) x, (float) y, (float) z, partialTicks);
    }

    
    private void renderShooter(LKTileEntityMountedShooter shooter, float x, float y, float z, float partialTicks) {
        
        bindTexture(shooter.getShooterType() == 0 ? TEXTURE_WOOD : TEXTURE_SILVER);

        GL11.glPushMatrix(); 
        GL11.glTranslatef(x + 0.5F, y + 1.5F, z + 0.5F); 
        GL11.glScalef(-1.0F, -1.0F, 1.0F); 
        GL11.glRotatef(shooter.getBlockMetadata() * 90F, 0.0F, 1.0F, 0.0F); 

        
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST); 

        
        model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, -shooter.fireCounter * partialTicks, 0.0625F);

        GL11.glEnable(GL11.GL_CULL_FACE); 
        GL11.glPopMatrix(); 
    }
}
