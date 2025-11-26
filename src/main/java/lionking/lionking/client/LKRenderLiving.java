package lionking.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityQuestAnimal;
import lionking.entity.LKEntityLionBase;
import lionking.entity.LKEntityScar;
import lionking.common.LKIngame;
import lionking.client.LKTickHandlerClient;

@SideOnly(Side.CLIENT)
public abstract class LKRenderLiving extends RenderLiving {
    private static final RenderItem ITEM_RENDERER = new RenderItem();
    private final float scale;
    private final String name;

    public LKRenderLiving(ModelBase model) {
        this(model, 1.0F, null);
    }

    public LKRenderLiving(ModelBase model, float scale) {
        this(model, scale, null);
    }

    public LKRenderLiving(ModelBase model, String name) {
        this(model, 1.0F, name);
    }

    public LKRenderLiving(ModelBase model, float scale, String name) {
        super(model, 0.5F * scale);
        this.scale = scale;
        this.name = name;
    }

    @Override
    public void doRender(EntityLivingBase entity, double x, double y, double z, float yaw, float partialTicks) {
        super.doRender(entity, x, y, z, yaw, partialTicks);

        if (name != null) {
            renderCustomLivingLabel(entity, name, x, y, z, 64);
        }

        if (entity instanceof LKEntityQuestAnimal) {
            LKEntityQuestAnimal animal = (LKEntityQuestAnimal) entity;
            EntityLivingBase player = renderManager.livingPlayer;

            if (animal instanceof LKEntityLionBase && ((LKEntityLionBase) animal).isHostile()) {
                return;
            }

            if (animal.quest.hasQuest() && animal.getHealth() > 0 && player instanceof EntityPlayer && 
                LKIngame.hasAmulet((EntityPlayer) player)) {
                ItemStack requiredItem = animal.quest.getRequiredItem();
                if (requiredItem != null && requiredItem.getItem() != null) {
                    renderQuestItem(animal, x, y + animal.getQuestItemRenderOffset(), z, 64, requiredItem);
                }
            }
        }

        if (entity instanceof LKEntityScar && ((LKEntityScar) entity).isScarHostile()) {
            LKTickHandlerClient.scarBoss = (LKEntityScar) entity;
        }
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entity, float partialTicks) {
        GL11.glScalef(scale, scale, scale);
    }

    protected void renderCustomLivingLabel(EntityLivingBase entity, String name, double x, double y, double z, int maxDistance) {
        double distanceSq = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
        if (distanceSq <= (double)(maxDistance * maxDistance)) {
            FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
            float scaleFactor = 1.6F;
            float scaleMultiplier = 0.016666668F * scaleFactor;
            
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y + entity.height + 0.5F, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-scaleMultiplier, -scaleMultiplier, scaleMultiplier);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            int textWidth = fontRenderer.getStringWidth(name) / 2;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            tessellator.addVertex((double)(-textWidth - 1), -1.0D, 0.0D);
            tessellator.addVertex((double)(-textWidth - 1), 8.0D, 0.0D);
            tessellator.addVertex((double)(textWidth + 1), 8.0D, 0.0D);
            tessellator.addVertex((double)(textWidth + 1), -1.0D, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);

            fontRenderer.drawString(name, -textWidth, 0, 553648127);
            GL11.glDepthMask(true);
            fontRenderer.drawString(name, -textWidth, 0, -1);

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    private void renderQuestItem(EntityLivingBase entity, double x, double y, double z, int maxDistance, ItemStack itemStack) {
        if (entity.getDistanceSqToEntity(renderManager.livingPlayer) > (maxDistance * maxDistance)) {
            return;
        }

        String countText = "x" + itemStack.stackSize;
        FontRenderer fontRenderer = getFontRendererFromRenderManager();
        float scaleFactor = 1.6F;
        float scaleMultiplier = 0.016666668F * scaleFactor;

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + 2.3F, (float)z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scaleMultiplier, -scaleMultiplier, scaleMultiplier);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        renderTextWithOutline(fontRenderer, countText);

        GL11.glPushMatrix();
        ITEM_RENDERER.renderItemAndEffectIntoGUI(fontRenderer, renderManager.renderEngine, 
            new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage()), -21, -4);
        GL11.glPopMatrix();

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    private void renderTextWithOutline(FontRenderer fontRenderer, String text) {
        fontRenderer.drawString(text, -1, 1, 0);
        fontRenderer.drawString(text, -1, 0, 0);
        fontRenderer.drawString(text, -1, -1, 0);
        fontRenderer.drawString(text, 0, 1, 0);
        fontRenderer.drawString(text, 0, -1, 0);
        fontRenderer.drawString(text, 1, 1, 0);
        fontRenderer.drawString(text, 1, 0, 0);
        fontRenderer.drawString(text, 1, -1, 0);
        fontRenderer.drawString(text, 0, 0, -1);
    }
}