package lionking.client;

import net.minecraft.client.gui.FontRenderer; 
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityRafiki;

@SideOnly(Side.CLIENT)
public class LKRenderRafiki extends RenderLiving {
    private static final ResourceLocation RAFIKI_TEXTURE = new ResourceLocation("lionking", "mob/rafiki.png");
    private final LKModelRafiki modelRafiki;

    public LKRenderRafiki() {
        super(new LKModelRafiki(), 0.5F);
        this.modelRafiki = (LKModelRafiki) this.mainModel;
        this.setRenderPassModel(this.modelRafiki);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return RAFIKI_TEXTURE;
    }

    @Override
    public void doRender(EntityLivingBase entity, double x, double y, double z, float yaw, float partialTicks) {
        super.doRender(entity, x, y, z, yaw, partialTicks);
        if (entity instanceof LKEntityRafiki) {
            renderLivingLabel(entity, "Rafiki", x, y, z, 64);
        }
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase entity, float partialTicks) {
        super.renderEquippedItems(entity, partialTicks);
        ItemStack heldItem = entity.getHeldItem();
        
        if (heldItem != null) {
            GL11.glPushMatrix();
            modelRafiki.getRightArm().postRender(0.0625F);
            GL11.glTranslatef(0.0625F, 0.4375F, 0.2625F);

            float scale;
            if (heldItem.getItem().requiresMultipleRenderPasses()) {
                scale = 0.625F;
                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(scale, -scale, scale);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else {
                scale = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(scale, scale, scale);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            this.renderManager.itemRenderer.renderItem(entity, heldItem, 0);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) {
        return 0.0F;
    }

    protected void renderLivingLabel(EntityLivingBase entity, String label, double x, double y, double z, int maxDistance) {
        double distance = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
        if (distance <= (double)(maxDistance * maxDistance)) {
            FontRenderer fontRenderer = getFontRendererFromRenderManager();
            float f1 = 0.016666668F * 1.6F;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y + entity.height + 0.5F, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f1, -f1, f1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            fontRenderer.drawString(label, -fontRenderer.getStringWidth(label) / 2, 0, 0xFFFFFF);

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }
}