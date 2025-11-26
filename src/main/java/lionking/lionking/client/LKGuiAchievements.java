package lionking.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.MathHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.AchievementPage;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import lionking.mod_LionKing;
import lionking.common.LKAchievementList;
import net.minecraft.client.renderer.Tessellator;

@SideOnly(Side.CLIENT)
public class LKGuiAchievements extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:gui/achievements.png");
    private static final int MAP_TOP = LKAchievementList.minDisplayColumn * 24 - 112;
    private static final int MAP_LEFT = LKAchievementList.minDisplayRow * 24 - 112;
    private static final int MAP_BOTTOM = LKAchievementList.maxDisplayColumn * 24 - 77;
    private static final int MAP_RIGHT = LKAchievementList.maxDisplayRow * 24 - 77;

    protected final int paneWidth = 256;
    protected final int paneHeight = 202;
    private final StatFileWriter statFileWriter;
    private final GuiScreen parentScreen; 
    private final LinkedList<Achievement> vanillaAchievements = new LinkedList<>();
    private int currentPage;
    private GuiButton pageButton;

    private double mapX;
    private double mapY;
    private double targetMapX;
    private double targetMapY;
    private double prevMapX;
    private double prevMapY;
    private int mouseX;
    private int mouseY;
    private int mouseButtonState;

    public LKGuiAchievements(StatFileWriter statFileWriter, GuiScreen parentScreen) {
        this.statFileWriter = statFileWriter;
        this.parentScreen = parentScreen;
        this.currentPage = -1; 
        this.mapX = this.targetMapX = this.prevMapX = LKAchievementList.enterPrideLands.displayColumn * 24 - 141 / 2 - 12;
        this.mapY = this.targetMapY = this.prevMapY = LKAchievementList.enterPrideLands.displayRow * 24 - 141 / 2;

        for (Object achievement : AchievementList.achievementList) {
            Achievement ach = (Achievement) achievement;
            if (!AchievementPage.isAchievementInPages(ach)) {
                vanillaAchievements.add(ach);
            }
        }
    }

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(new GuiButton(1, width / 2 + 24, height / 2 + 74, 80, 20, StatCollector.translateToLocal("gui.done")));
        pageButton = new GuiButton(2, (width - paneWidth) / 2 + 24, height / 2 + 74, 125, 20, AchievementPage.getTitle(currentPage));
        buttonList.add(pageButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            mc.displayGuiScreen(parentScreen); 
        } else if (button.id == 2) {
            currentPage = (currentPage + 1) % (AchievementPage.getAchievementPages().size() + 1);
            pageButton.displayString = AchievementPage.getTitle(currentPage);
        }
    }

    @Override
    protected void keyTyped(char key, int keyCode) {
        if (keyCode == mc.gameSettings.keyBindInventory.getKeyCode()) {
            mc.displayGuiScreen(parentScreen); 
        } else {
            super.keyTyped(key, keyCode);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        handleMouseInput(mouseX, mouseY);
        drawDefaultBackground();
        drawAchievementBackground(mouseX, mouseY, partialTicks);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        drawTitle();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void handleMouseInput(int mouseX, int mouseY) {
        if (Mouse.isButtonDown(0)) {
            int paneX = (width - paneWidth) / 2 + 8;
            int paneY = (height - paneHeight) / 2 + 17;

            if ((mouseButtonState == 0 || mouseButtonState == 1) && mouseX >= paneX && mouseX < paneX + 224 && mouseY >= paneY && mouseY < paneY + 155) {
                if (mouseButtonState == 0) {
                    mouseButtonState = 1;
                } else {
                    mapX -= (mouseX - this.mouseX);
                    mapY -= (mouseY - this.mouseY);
                    targetMapX = prevMapX = mapX;
                    targetMapY = prevMapY = mapY;
                }
                this.mouseX = mouseX;
                this.mouseY = mouseY;
            }
            clampMapPosition();
        } else {
            mouseButtonState = 0;
        }
    }

    private void clampMapPosition() {
        targetMapX = MathHelper.clamp_double(targetMapX, MAP_TOP, MAP_BOTTOM - 1);
        targetMapY = MathHelper.clamp_double(targetMapY, MAP_LEFT, MAP_RIGHT - 1);
    }

    @Override
    public void updateScreen() {
        prevMapX = mapX;
        prevMapY = mapY;
        double deltaX = targetMapX - mapX;
        double deltaY = targetMapY - mapY;
        double distanceSquared = deltaX * deltaX + deltaY * deltaY;

        if (distanceSquared < 4.0) {
            mapX += deltaX;
            mapY += deltaY;
        } else {
            mapX += deltaX * 0.85;
            mapY += deltaY * 0.85;
        }
    }

    private void drawTitle() {
        int x = (width - paneWidth) / 2 + 15;
        int y = (height - paneHeight) / 2 + 5;
        fontRendererObj.drawString("Achievements", x, y, 0);
    }

    private void drawAchievementBackground(int mouseX, int mouseY, float partialTicks) {
        int mapXInterpolated = MathHelper.floor_double(prevMapX + (mapX - prevMapX) * partialTicks);
        int mapYInterpolated = MathHelper.floor_double(prevMapY + (mapY - prevMapY) * partialTicks);
        mapXInterpolated = MathHelper.clamp_int(mapXInterpolated, MAP_TOP, MAP_BOTTOM - 1);
        mapYInterpolated = MathHelper.clamp_int(mapYInterpolated, MAP_LEFT, MAP_RIGHT - 1);

        int paneX = (width - paneWidth) / 2;
        int paneY = (height - paneHeight) / 2;
        int contentX = paneX + 16;
        int contentY = paneY + 17;

        zLevel = 0.0F;
        GL11.glDepthFunc(GL11.GL_GEQUAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        drawBackgroundPattern(mapXInterpolated, mapYInterpolated, contentX, contentY);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        List<Achievement> achievements = (currentPage == -1) ? vanillaAchievements : AchievementPage.getAchievementPage(currentPage).getAchievements();
        drawAchievementConnections(achievements, mapXInterpolated, mapYInterpolated, contentX, contentY);
        Achievement hoveredAchievement = drawAchievements(achievements, mapXInterpolated, mapYInterpolated, contentX, contentY, mouseX, mouseY);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(paneX, paneY, 0, 0, paneWidth, paneHeight);
        GL11.glPopMatrix();

        zLevel = 0.0F;
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (hoveredAchievement != null) {
            drawAchievementTooltip(hoveredAchievement, mouseX, mouseY);
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        RenderHelper.disableStandardItemLighting();
    }

    private void drawBackgroundPattern(int mapX, int mapY, int contentX, int contentY) {
        int tileX = (mapX + 288) >> 4;
        int tileY = (mapY + 288) >> 4;
        int offsetX = (mapX + 288) % 16;
        int offsetY = (mapY + 288) % 16;
        Random rand = new Random();

        for (int y = 0; y * 16 - offsetY < 155; y++) {
            float brightness = 0.6F - (tileY + y) / 26.0F * 0.3F;
            GL11.glColor4f(brightness, brightness, brightness, 1.0F);

            for (int x = 0; x * 16 - offsetX < 224; x++) {
                rand.setSeed(1234 + tileX + x);
                rand.nextInt();
                int depth = rand.nextInt(1 + tileY + y) + (tileY + y) / 2;
                IIcon icon = getBackgroundIcon(depth, tileX + x, tileY + y);

                mc.getTextureManager().bindTexture(net.minecraft.client.renderer.texture.TextureMap.locationBlocksTexture);
                drawIcon(contentX + x * 16 - offsetX, contentY + y * 16 - offsetY, icon, 16, 16);
            }
        }
    }

    private void drawIcon(int x, int y, IIcon icon, int width, int height) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, zLevel, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + width, y + height, zLevel, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + width, y, zLevel, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(x, y, zLevel, icon.getMinU(), icon.getMinV());
        tessellator.draw();
    }

    private IIcon getBackgroundIcon(int depth, int x, int y) {
        if ((y == 31 && (x < 10 || x > 27)) || (y == 32 && (x < 15 || x > 22)) || y > 32) {
            if (depth == 30) return mod_LionKing.prideCoal.getIcon(2, 1);
            if (depth == 34) return mod_LionKing.oreSilver.getIcon(2, 1);
            return mod_LionKing.pridestone.getIcon(2, 1);
        }
        if (depth == 34) return mod_LionKing.orePeacock.getIcon(2, 0);
        if (depth == 22) return mod_LionKing.oreSilver.getIcon(2, 0);
        if (depth == 10) return mod_LionKing.prideCoal.getIcon(2, 0);
        return mod_LionKing.pridestone.getIcon(2, 0);
    }

    private void drawAchievementConnections(List<Achievement> achievements, int mapX, int mapY, int contentX, int contentY) {
        for (Achievement achievement : achievements) {
            if (achievement.parentAchievement != null && achievements.contains(achievement.parentAchievement)) {
                int x1 = achievement.displayColumn * 24 - mapX + 11 + contentX;
                int y1 = achievement.displayRow * 24 - mapY + 11 + contentY;
                int x2 = achievement.parentAchievement.displayColumn * 24 - mapX + 11 + contentX;
                int y2 = achievement.parentAchievement.displayRow * 24 - mapY + 11 + contentY;

                boolean isUnlocked = statFileWriter.hasAchievementUnlocked(achievement);
                boolean canUnlock = statFileWriter.canUnlockAchievement(achievement);
                int alpha = Math.sin((System.currentTimeMillis() % 600) / 600.0 * Math.PI * 2.0) > 0.6 ? 255 : 130;
                int color = isUnlocked ? -9408400 : (canUnlock ? (65280 + (alpha << 24)) : -16777216);

                drawHorizontalLine(x1, x2, y1, color);
                drawVerticalLine(x2, y1, y2, color);
            }
        }
    }

    private Achievement drawAchievements(List<Achievement> achievements, int mapX, int mapY, int contentX, int contentY, int mouseX, int mouseY) {
        Achievement hoveredAchievement = null;
        RenderItem renderItem = new RenderItem();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        for (Achievement achievement : achievements) {
            int x = achievement.displayColumn * 24 - mapX;
            int y = achievement.displayRow * 24 - mapY;
            if (x < -24 || y < -24 || x > 224 || y > 155) continue;

            float brightness = statFileWriter.hasAchievementUnlocked(achievement) ? 1.0F :
                              statFileWriter.canUnlockAchievement(achievement) ?
                              (Math.sin((System.currentTimeMillis() % 600) / 600.0 * Math.PI * 2.0) < 0.6 ? 0.6F : 0.8F) : 0.3F;
            GL11.glColor4f(brightness, brightness, brightness, 1.0F);

            mc.getTextureManager().bindTexture(TEXTURE);
            int renderX = contentX + x;
            int renderY = contentY + y;
            drawTexturedModalRect(renderX - 2, renderY - 2, achievement.getSpecial() ? 26 : 0, 202, 26, 26);

            if (!statFileWriter.canUnlockAchievement(achievement)) {
                GL11.glColor4f(0.1F, 0.1F, 0.1F, 1.0F);
                renderItem.renderWithColor = false;
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
            renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), achievement.theItemStack, renderX + 3, renderY + 3);
            GL11.glDisable(GL11.GL_LIGHTING);
            renderItem.renderWithColor = true;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (mouseX >= renderX && mouseX <= renderX + 22 && mouseY >= renderY && mouseY <= renderY + 22 &&
                mouseX >= contentX && mouseX < contentX + 224 && mouseY >= contentY && mouseY < contentY + 155) {
                hoveredAchievement = achievement;
            }
        }
        return hoveredAchievement;
    }

    private void drawAchievementTooltip(Achievement achievement, int mouseX, int mouseY) {
        String name = StatCollector.translateToLocal(achievement.statId);
        String desc = StatCollector.translateToLocal(achievement.statId + ".desc");
        int x = mouseX + 12;
        int y = mouseY - 4;
        int width = Math.max(fontRendererObj.getStringWidth(name), 123);
        int height;

        if (statFileWriter.canUnlockAchievement(achievement)) {
            height = fontRendererObj.splitStringWidth(desc, width) + (statFileWriter.hasAchievementUnlocked(achievement) ? 12 : 0);
            drawGradientRect(x - 3, y - 3, x + width + 3, y + height + 15, -1073741824, -1073741824);
            fontRendererObj.drawSplitString(desc, x, y + 12, width, -6250336);
            if (statFileWriter.hasAchievementUnlocked(achievement)) {
                fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal("achievement.taken"), x, y + height + 4, -7302913);
            }
        } else {
            String parentStatId = achievement.parentAchievement.statId;
            String requires = StatCollector.translateToLocalFormatted("achievement.requires",
                StatCollector.translateToLocal(parentStatId));
            height = fontRendererObj.splitStringWidth(requires, width);
            drawGradientRect(x - 3, y - 3, x + width + 3, y + height + 15, -1073741824, -1073741824);
            fontRendererObj.drawSplitString(requires, x, y + 12, width, -9416624);
        }

        int nameColor = statFileWriter.canUnlockAchievement(achievement) ? (achievement.getSpecial() ? -128 : -1) : (achievement.getSpecial() ? -8355776 : -8355712);
        fontRendererObj.drawStringWithShadow(name, x, y, nameColor);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}