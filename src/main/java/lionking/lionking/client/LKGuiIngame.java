package lionking.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap; 
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.client.LKClientProxy;
import lionking.entity.LKEntityZira; 
import lionking.mod_LionKing; 

@SideOnly(Side.CLIENT)
public class LKGuiIngame {
    private static final ResourceLocation FLATULENCE_TEXTURE = new ResourceLocation("lionking:gui/flatulence.png");
    private static final float TEXTURE_SCALE = 0.00390625F;

    public static void drawBossHealth(Minecraft mc, EntityLivingBase boss) {
        if (boss == null || !boss.isEntityAlive()) {
            return;
        }

        mc.getTextureManager().bindTexture(LKClientProxy.ICONS_TEXTURE);
        boolean isZira = boss instanceof LKEntityZira;

        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight); 
        int screenWidth = resolution.getScaledWidth();
        int barX = screenWidth / 2 - 91;
        int barWidth = (int) (boss.getHealth() / boss.getMaxHealth() * 183);

        drawTexturedModalRect(barX, 12, 0, isZira ? 16 : 0, 182, 5);
        if (barWidth > 0) {
            drawTexturedModalRect(barX, 12, 0, isZira ? 21 : 5, barWidth, 5);
        }

        String bossName = isZira ? "Zira" : "Scar";
        int nameColor = isZira ? 0xF23C00 : 0xAED110;
        mc.fontRenderer.drawStringWithShadow(bossName, screenWidth / 2 - mc.fontRenderer.getStringWidth(bossName) / 2, 2, nameColor);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderPortalOverlay(float alpha, Minecraft mc, boolean isPrideLands) {
        if (mc.currentScreen != null || mc.gameSettings.keyBindPlayerList.getIsKeyPressed()) { 
            return;
        }

        alpha = Math.min(1.0F, alpha);
        alpha = alpha * alpha * alpha * 0.8F + 0.2F;

        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight); 
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();

        setupOverlayRendering(alpha);
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        IIcon texture = isPrideLands ? mod_LionKing.lionPortal.getBlockTextureFromSide(0) : mod_LionKing.outlandsPortal.getBlockTextureFromSide(0);
        drawFullScreenQuad(width, height, texture.getMinU(), texture.getMinV(), texture.getMaxU(), texture.getMaxV());
        finishOverlayRendering();
    }

    public static void renderFlatulenceOverlay(float alpha, Minecraft mc) {
        if (mc.currentScreen != null || mc.gameSettings.keyBindPlayerList.getIsKeyPressed()) { 
            return;
        }

        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight); 
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();

        setupOverlayRendering(alpha);
        mc.getTextureManager().bindTexture(FLATULENCE_TEXTURE);
        drawFullScreenQuad(width, height, 0.0F, 0.0F, 1.0F, 1.0F);
        finishOverlayRendering();
    }

    private static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0.0, u * TEXTURE_SCALE, (v + height) * TEXTURE_SCALE);
        tessellator.addVertexWithUV(x + width, y + height, 0.0, (u + width) * TEXTURE_SCALE, (v + height) * TEXTURE_SCALE);
        tessellator.addVertexWithUV(x + width, y, 0.0, (u + width) * TEXTURE_SCALE, v * TEXTURE_SCALE);
        tessellator.addVertexWithUV(x, y, 0.0, u * TEXTURE_SCALE, v * TEXTURE_SCALE);
        tessellator.draw();
    }

    private static void drawFullScreenQuad(int width, int height, float uMin, float vMin, float uMax, float vMax) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0, height, -90.0, uMin, vMax);
        tessellator.addVertexWithUV(width, height, -90.0, uMax, vMax);
        tessellator.addVertexWithUV(width, 0, -90.0, uMax, vMin);
        tessellator.addVertexWithUV(0, 0, -90.0, uMin, vMin);
        tessellator.draw();
    }

    private static void setupOverlayRendering(float alpha) {
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
    }

    private static void finishOverlayRendering() {
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
