package lionking.client;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import lionking.entity.LKEntityScarRug;

public class LKRenderScarRug extends Render {
    private static final ResourceLocation TEXTURE_SCAR = new ResourceLocation("lionking:item/rug_scar.png");
    private static final ResourceLocation TEXTURE_ZIRA = new ResourceLocation("lionking:item/rug_zira.png");
    private static final float SCALE_FACTOR = -1.0F;
    private static final float Y_OFFSET = 1.5F;
    private static final float MODEL_SCALE = 0.0625F;
    
    private final LKModelScarRug model = new LKModelScarRug();

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        
        GL11.glTranslatef((float)x, (float)y + Y_OFFSET, (float)z);
        
        bindEntityTexture(entity);
        
        GL11.glScalef(SCALE_FACTOR, SCALE_FACTOR, 1.0F);
        GL11.glRotatef(yaw % 360.0F, 0.0F, 1.0F, 0.0F);
        
        model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, MODEL_SCALE);
        
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return ((LKEntityScarRug)entity).getType() == 1 ? TEXTURE_ZIRA : TEXTURE_SCAR;
    }
}
