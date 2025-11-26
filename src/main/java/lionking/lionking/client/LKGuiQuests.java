package lionking.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import lionking.quest.LKQuestBase;
import lionking.quest.MessageQuestCheck;
import lionking.inventory.LKContainerItemInfo;
import lionking.common.LKLevelData;
import lionking.item.LKItemInfo;
import lionking.mod_LionKing;
import net.minecraft.client.renderer.Tessellator;

@SideOnly(Side.CLIENT)
public class LKGuiQuests extends GuiScreen {
    private static final ResourceLocation TEXTURE_PAGE_LEFT = new ResourceLocation("lionking:gui/book_left.png");
    private static final ResourceLocation TEXTURE_PAGE_RIGHT = new ResourceLocation("lionking:gui/book_right.png");
    private static final ResourceLocation TEXTURE_PAGE_MENU = new ResourceLocation("lionking:gui/book_menu.png");
    private static final int TEXT_COLOR_MAIN = 0x120C01;
    private static final int TEXT_COLOR_SUB = 0x4B3A21;
    private static final int TEXT_COLOR_FLASH = 0x6A4E10;

    private final RenderItem itemRenderer = new RenderItem();
    private final int xSize = 540;
    private final int ySize = 256;
    private final Container inventorySlots;
    private final EntityPlayer player;
    private int guiLeft;
    private int guiTop;

    private List<Object> matchedRecipes;
    private boolean craftGuiOpen;
    private int recipeIndex;
    private ItemStack prevLoreStack;

    private static int page = -1;
    private static LKQuestBase selectedQuest;
    public static int flashTimer;

    public LKGuiQuests(EntityPlayer player) {
        this.inventorySlots = new LKContainerItemInfo(player);
        this.player = player;
    }

    @Override
    public void initGui() {
        super.initGui();
        mc.thePlayer.openContainer = inventorySlots;
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
        updatePage();

        buttonList.add(new LKGuiQuestsButton(-1, guiLeft - 20, guiTop + 8, null));
        for (int i = 0; i < LKQuestBase.orderedQuests.size(); i++) {
            LKQuestBase quest = (LKQuestBase) LKQuestBase.orderedQuests.get(i);
            if (quest != null) {
                buttonList.add(new LKGuiQuestsButton(i, guiLeft - 20, guiTop + 8 + ((i + 1) * 28), quest));
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (page == -1) {
            handleMainPageItemDrop();
        }
        craftGuiOpen = false;
        recipeIndex = 0;
        page = button.id;
        updatePage();
    }

    private void handleMainPageItemDrop() {
        if (inventorySlots != null && !player.worldObj.isRemote) {
            Slot slot = inventorySlots.getSlot(0);
            ItemStack stack = slot.getStack();
            if (stack != null) {
                if (!player.inventory.addItemStackToInventory(stack)) {
                    player.dropPlayerItemWithRandomChoice(stack, false);
                }
                slot.putStack(null);
            }
        }
    }

    private void updatePage() {
        if (page == -1) {
            selectedQuest = null;
        } else {
            selectedQuest = (LKQuestBase) LKQuestBase.orderedQuests.get(page);
            if (selectedQuest.canStart()) {
                mod_LionKing.networkQuests.sendToServer(new MessageQuestCheck((byte)selectedQuest.questIndex));
            }
        }
    }

    private void drawBackground(int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE_PAGE_LEFT);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2 - 15;
        drawTexturedModalRect(x + 150, y, 0, 0, 202, ySize);
        mc.getTextureManager().bindTexture(TEXTURE_PAGE_RIGHT);
        drawTexturedModalRect(x + 352, y, 0, 0, 202, ySize);
        mc.getTextureManager().bindTexture(TEXTURE_PAGE_MENU);
        drawTexturedModalRect(x + 15, y, 0, 0, 65, ySize);

        if (page == -1) {
            drawTexturedModalRect(x + 178, y + 115, 80, 92, 24, 24);
            drawTexturedModalRect(x + 174, y + 146, 80, 0, 176, 90);

            if (inventorySlots != null && matchedRecipes != null) {
                boolean mouseOver = mouseX >= x + 181 && mouseY >= y + 94 && mouseX < x + 199 && mouseY < y + 112;
                drawTexturedModalRect(x + 181, y + 94, 80, mouseOver ? 138 : 118, 18, 18);
            }
        }
    }

    private void drawForeground() {
        drawCenteredString(fontRendererObj, translate(getPageTitle()), 352, 9, TEXT_COLOR_MAIN);

        ItemStack pageIcon = getPageIcon();
        itemRenderer.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), pageIcon, 182, 5);
        itemRenderer.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), pageIcon, 182, 5);
        itemRenderer.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), pageIcon, 507, 5);
        itemRenderer.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), pageIcon, 507, 5);

        if (selectedQuest != null) {
            drawQuestDetails();
        } else {
            drawMainPageDetails();
        }
    }

    private void drawQuestDetails() {
        if (selectedQuest.canStart()) {
            int stage = selectedQuest.getQuestStage();
            if (stage == selectedQuest.getNumStages()) {
                drawCenteredString(fontRendererObj, translate("gui.lionking.quest.complete"), 352, 46, TEXT_COLOR_MAIN);
            } else {
                drawCenteredString(fontRendererObj, translate("gui.lionking.quest.objective"), 352, 37, TEXT_COLOR_SUB);
                String objective = selectedQuest.isDelayed() ? "" : translate(selectedQuest.getObjectiveByStage(stage));
                drawCenteredString(fontRendererObj, objective, 352, 51, flashTimer > 14 ? TEXT_COLOR_FLASH : TEXT_COLOR_MAIN);

                int startStage = selectedQuest.isDelayed() ? stage : stage - 1;
                for (int i = startStage; i >= 0; i--) {
                    drawCenteredString(fontRendererObj, translate(selectedQuest.getObjectiveByStage(i)) + translate("gui.lionking.quest.done"), 352, 63 + 13 * (stage - i), TEXT_COLOR_SUB);
                }
            }
        } else {
            drawCenteredString(fontRendererObj, translate("gui.lionking.quest.not_started"), 352, 51, TEXT_COLOR_MAIN);
            drawCenteredString(fontRendererObj, translate("gui.lionking.quest.requirements"), 352, 76, TEXT_COLOR_SUB);

            String[] requirements = selectedQuest.getRequirements();
            for (int i = 0; i < requirements.length; i++) {
                drawCenteredString(fontRendererObj, translate(requirements[i]), 352, 89 + 13 * i, TEXT_COLOR_SUB);
            }
        }
    }

    private void drawMainPageDetails() {
        String portalLocation = translate("gui.lionking.book.portal") + LKLevelData.homePortalX + ", " + LKLevelData.homePortalY + ", " + LKLevelData.homePortalZ;
        drawCenteredString(fontRendererObj, portalLocation, 352, 42, TEXT_COLOR_SUB);

        drawCenteredString(fontRendererObj, translate("gui.lionking.book.lost1"), 352, 63, TEXT_COLOR_SUB);
        drawCenteredString(fontRendererObj, translate("gui.lionking.book.lost2"), 352, 73, TEXT_COLOR_SUB);

        ItemStack stack = inventorySlots.getSlot(0).getStack();
        if (stack != null) {
            fontRendererObj.drawString(stack.getDisplayName(), 208, 108, TEXT_COLOR_MAIN);
            String[] info = LKItemInfo.getItemInfo(stack);
            for (int i = 0; i < info.length; i++) {
                drawCenteredString(fontRendererObj, translate(info[i]), 443, 116 + (i * 10), TEXT_COLOR_MAIN);
            }
        }
    }

    private void drawCraftGui(int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE_PAGE_MENU);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2 - 15;
        drawTexturedModalRect(x + 287, y + 103, 126, 92, 130, 90);

        if (matchedRecipes != null && matchedRecipes.size() > 1) {
            drawNavigationButtons(x, y, mouseX, mouseY);
        }

        renderRecipe(x, y);
    }

    private void drawNavigationButtons(int x, int y, int mouseX, int mouseY) {
        boolean prevHovered = mouseX >= x + 396 && mouseY >= y + 108 && mouseX < x + 402 && mouseY < y + 119;
        drawTexturedModalRect(x + 396, y + 108, 107, prevHovered ? 105 : 92, 6, 11);
        boolean nextHovered = mouseX >= x + 406 && mouseY >= y + 108 && mouseX < x + 412 && mouseY < y + 119;
        drawTexturedModalRect(x + 406, y + 108, 117, nextHovered ? 105 : 92, 6, 11);
    }

    private void renderRecipe(int x, int y) {
        ItemStack itemStack = inventorySlots.getSlot(0).getStack();
        if (matchedRecipes == null || matchedRecipes.isEmpty()) return;
        Object recipe = matchedRecipes.get(recipeIndex);
        String title = translate("gui.lionking.craft.title");

        if (recipe instanceof ShapedRecipes) {
            renderShapedRecipe((ShapedRecipes) recipe, itemStack, x, y);
        } else if (recipe instanceof ShapelessRecipes) {
            renderShapelessRecipe((ShapelessRecipes) recipe, itemStack, x, y);
        }

        drawCenteredString(fontRendererObj, title, x + 352, y + 109, TEXT_COLOR_MAIN);
    }

    private void renderShapedRecipe(ShapedRecipes recipe, ItemStack itemStack, int x, int y) {
        int width = recipe.recipeWidth;
        ItemStack[] ingredients = recipe.recipeItems;
        for (int i = 0; i < recipe.getRecipeSize(); i++) {
            ItemStack ingredient = ingredients[i];
            if (ingredient == null) continue;
            renderIngredient(ingredient, itemStack, x + 299 + ((i % width) * 18), y + 129 + (MathHelper.floor_float(i / (float)width) * 18));
        }
        renderResult(recipe.getRecipeOutput(), itemStack, x + 387, y + 147);
    }

    private void renderShapelessRecipe(ShapelessRecipes recipe, ItemStack itemStack, int x, int y) {
        List<ItemStack> ingredients = recipe.recipeItems;
        for (int i = 0; i < recipe.getRecipeSize(); i++) {
            ItemStack ingredient = ingredients.get(i);
            renderIngredient(ingredient, itemStack, x + 299 + ((i % 3) * 18), y + 129 + (MathHelper.floor_float(i / 3.0F) * 18));
        }
        renderResult(recipe.getRecipeOutput(), itemStack, x + 387, y + 147);
    }

    private void renderIngredient(ItemStack ingredient, ItemStack itemStack, int xPos, int yPos) {
        if (ingredient.getItem() == itemStack.getItem() && (ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE || ingredient.getItemDamage() == itemStack.getItemDamage())) {
            mc.getTextureManager().bindTexture(TEXTURE_PAGE_MENU);
            drawTexturedModalRect(xPos, yPos, 80, 158, 16, 16);
        }
        ItemStack renderStack = ingredient.copy();
        renderStack.stackSize = 1;
        if (ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE && ingredient.getItem() == itemStack.getItem()) {
            renderStack.setItemDamage(itemStack.getItemDamage());
        }
        renderItem(renderStack, xPos, yPos);
    }

    private void renderResult(ItemStack result, ItemStack itemStack, int xPos, int yPos) {
        if (result.getItem() == itemStack.getItem() && (result.getItem().isDamageable() || result.getItemDamage() == itemStack.getItemDamage())) {
            mc.getTextureManager().bindTexture(TEXTURE_PAGE_MENU);
            drawTexturedModalRect(xPos - 1, yPos - 1, 80, 158, 18, 18);
        }
        ItemStack renderStack = result.copy();
        if (result.getItemDamage() == OreDictionary.WILDCARD_VALUE && result.getItem() == itemStack.getItem()) {
            renderStack.setItemDamage(itemStack.getItemDamage());
        }
        renderItem(renderStack, xPos, yPos);
    }

    private void renderItem(ItemStack stack, int x, int y) {
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        itemRenderer.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), stack, x, y);
        itemRenderer.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), stack, x, y);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawBackground(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (page == -1) {
            renderSlots(mouseX, mouseY);
            renderHeldItem(mouseX, mouseY);
            renderTooltip(mouseX, mouseY);
            if (craftGuiOpen) {
                drawDefaultBackground();
                drawCraftGui(mouseX, mouseY);
            }
        }
        drawForeground();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void renderSlots(int mouseX, int mouseY) {
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        for (int i = 0; i < inventorySlots.inventorySlots.size(); i++) {
            Slot slot = (Slot) inventorySlots.inventorySlots.get(i);
            drawSlotInventory(slot);
            if (isMouseOverSlot(slot, mouseX, mouseY)) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                drawGradientRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + 16, slot.yDisplayPosition + 16, -2130706433, -2130706433);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
    }

    private void renderHeldItem(int mouseX, int mouseY) {
        InventoryPlayer inventory = mc.thePlayer.inventory;
        if (inventory.getItemStack() != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            zLevel = 200.0F;
            itemRenderer.zLevel = 200.0F;
            itemRenderer.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), inventory.getItemStack(), mouseX - guiLeft - 8, mouseY - guiTop - 8);
            itemRenderer.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), inventory.getItemStack(), mouseX - guiLeft - 8, mouseY - guiTop - 8);
            zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
            GL11.glPopMatrix();
        }
    }

    private void renderTooltip(int mouseX, int mouseY) {
        InventoryPlayer inventory = mc.thePlayer.inventory;
        if (inventory.getItemStack() == null) {
            Slot slot = getSlotAtPosition(mouseX, mouseY);
            if (slot != null && slot.getHasStack()) {
                renderItemTooltip(slot.getStack(), mouseX - guiLeft, mouseY - guiTop);
            }
        }
    }

    private void renderItemTooltip(ItemStack stack, int x, int y) {
        List<String> tooltip = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
        if (tooltip.isEmpty()) return;

        int maxWidth = 0;
        for (String line : tooltip) {
            maxWidth = Math.max(maxWidth, fontRendererObj.getStringWidth(line));
        }

        int tooltipX = x + 12;
        int tooltipY = y - 12;
        int height = 8 + (tooltip.size() > 1 ? 2 + (tooltip.size() - 1) * 10 : 0);

        zLevel = 300.0F;
        itemRenderer.zLevel = 300.0F;

        int bgColor = -267386864;
        int borderColorStart = 1347420415;
        int borderColorEnd = (borderColorStart & 16711422) >> 1 | borderColorStart & -16777216;

        drawGradientRect(tooltipX - 3, tooltipY - 4, tooltipX + maxWidth + 3, tooltipY - 3, bgColor, bgColor);
        drawGradientRect(tooltipX - 3, tooltipY + height + 3, tooltipX + maxWidth + 3, tooltipY + height + 4, bgColor, bgColor);
        drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + maxWidth + 3, tooltipY + height + 3, bgColor, bgColor);
        drawGradientRect(tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + height + 3, bgColor, bgColor);
        drawGradientRect(tooltipX + maxWidth + 3, tooltipY - 3, tooltipX + maxWidth + 4, tooltipY + height + 3, bgColor, bgColor);
        drawGradientRect(tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + height + 3 - 1, borderColorStart, borderColorEnd);
        drawGradientRect(tooltipX + maxWidth + 2, tooltipY - 3 + 1, tooltipX + maxWidth + 3, tooltipY + height + 3 - 1, borderColorStart, borderColorEnd);
        drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + maxWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
        drawGradientRect(tooltipX - 3, tooltipY + height + 2, tooltipX + maxWidth + 3, tooltipY + height + 3, borderColorEnd, borderColorEnd);

        for (int i = 0; i < tooltip.size(); i++) {
            String line = tooltip.get(i);
            if (i == 0) {
                line = "\u00a7" + Integer.toHexString(stack.getRarity().rarityColor.ordinal()) + line;
            } else {
                line = "\u00a77" + line;
            }
            fontRendererObj.drawStringWithShadow(line, tooltipX, tooltipY, -1);
            tooltipY += (i == 0 ? 12 : 10);
        }

        zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
    }

    private void drawSlotInventory(Slot slot) {
        int x = slot.xDisplayPosition;
        int y = slot.yDisplayPosition;
        ItemStack stack = slot.getStack();
        zLevel = 100.0F;
        itemRenderer.zLevel = 100.0F;

        if (stack == null) {
            IIcon icon = slot.getBackgroundIconIndex();
            if (icon != null) {
                GL11.glDisable(GL11.GL_LIGHTING);
                mc.getTextureManager().bindTexture(net.minecraft.client.renderer.texture.TextureMap.locationItemsTexture);
                drawIcon(x, y, icon, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
            }
        } else {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            itemRenderer.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), stack, x, y);
            itemRenderer.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), stack, x, y);
        }

        zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
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

    private Slot getSlotAtPosition(int x, int y) {
        for (Object slotObj : inventorySlots.inventorySlots) {
            Slot slot = (Slot) slotObj;
            if (isMouseOverSlot(slot, x, y)) {
                return slot;
            }
        }
        return null;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        boolean isPickBlock = button == mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;

        if (button == 0 || button == 1 || isPickBlock) {
            int x = (width - xSize) / 2;
            int y = (height - ySize) / 2 - 15;

            if (page == -1 && matchedRecipes != null && mouseX >= x + 181 && mouseY >= y + 94 && mouseX < x + 199 && mouseY < y + 112) {
                craftGuiOpen = !craftGuiOpen;
                recipeIndex = 0;
                mc.getSoundHandler().playSound(net.minecraft.client.audio.PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
            }

            if (craftGuiOpen && matchedRecipes != null && matchedRecipes.size() > 1) {
                handleCraftNavigation(x, y, mouseX, mouseY);
            }

            handleSlotClick(mouseX, mouseY, button, isPickBlock);
        }
    }

    private void handleCraftNavigation(int x, int y, int mouseX, int mouseY) {
        if (mouseX >= x + 396 && mouseY >= y + 108 && mouseX < x + 402 && mouseY < y + 119) {
            recipeIndex = (recipeIndex - 1 + matchedRecipes.size()) % matchedRecipes.size();
            mc.getSoundHandler().playSound(net.minecraft.client.audio.PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
        } else if (mouseX >= x + 406 && mouseY >= y + 108 && mouseX < x + 412 && mouseY < y + 119) {
            recipeIndex = (recipeIndex + 1) % matchedRecipes.size();
            mc.getSoundHandler().playSound(net.minecraft.client.audio.PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
        }
    }

    private void handleSlotClick(int mouseX, int mouseY, int button, boolean isPickBlock) {
        Slot slot = getSlotAtPosition(mouseX, mouseY);
        int slotId = (slot != null) ? slot.slotNumber : -999;
        boolean isOutsideGui = mouseX < guiLeft || mouseY < guiTop || mouseX >= guiLeft + xSize || mouseY >= guiTop + ySize;
        if (isOutsideGui) slotId = -999;

        if (slotId != -1) {
            if (isPickBlock) {
                handleMouseClick(slot, slotId, button, 3);
            } else {
                boolean isShiftClick = Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
                handleMouseClick(slot, slotId, button, isShiftClick ? 1 : 0);
            }
        }
    }

    private boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
        if (page > -1) return false;
        int slotX = guiLeft + slot.xDisplayPosition;
        int slotY = guiTop + slot.yDisplayPosition;
        return mouseX >= slotX - 1 && mouseX < slotX + 17 && mouseY >= slotY - 1 && mouseY < slotY + 17;
    }

    protected void handleMouseClick(Slot slot, int slotId, int button, int clickType) {
        if (page == -1) {
            mc.playerController.windowClick(inventorySlots.windowId, slotId, button, clickType, mc.thePlayer);
        }
    }

    @Override
    protected void keyTyped(char key, int keyCode) {
        if (keyCode == 1 || keyCode == mc.gameSettings.keyBindInventory.getKeyCode()) {
            if (craftGuiOpen) {
                craftGuiOpen = false;
                recipeIndex = 0;
            } else {
                player.closeScreen();
            }
        }
    }

    @Override
    public void onGuiClosed() {
        if (player != null) {
            inventorySlots.onContainerClosed(player);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead) {
            mc.thePlayer.closeScreen();
        }
        flashTimer = (flashTimer + 1) % 20;

        ItemStack currentStack = inventorySlots.getSlot(0).getStack();
        if (currentStack == null || (prevLoreStack != null && currentStack != prevLoreStack)) {
            craftGuiOpen = false;
            recipeIndex = 0;
            matchedRecipes = null;
        }
        if (currentStack != null) {
            prevLoreStack = currentStack;
            if (matchedRecipes == null) {
                matchedRecipes = getMatchingRecipes(currentStack);
                if (matchedRecipes.isEmpty()) matchedRecipes = null;
            }
        }
    }

    private String translate(String key) {
        String text = StatCollector.translateToLocal(key);
        if (mc.getLanguageManager().getCurrentLanguage().getLanguageCode().equals("en_US")) {
            return text.replace("centre", "center")
                       .replace("armour", "armor")
                       .replace("colour", "color")
                       .replace("honour", "honor")
                       .replace("favour", "favor")
                       .replace("neighbour", "neighbor")
                       .replace("labour", "labor")
                       .replace("customise", "customize")
                       .replace("savannah", "savanna");
        }
        return text;
    }

    private String getPageTitle() {
        return (page == -1) ? translate("gui.lionking.book.title") : selectedQuest.getName();
    }

    private ItemStack getPageIcon() {
        return (selectedQuest != null) ? selectedQuest.getIcon() : new ItemStack(mod_LionKing.questBook, 1, 1);
    }

    private List<Object> getMatchingRecipes(ItemStack itemStack) {
        List<Object> matchingRecipes = new ArrayList<>();
        List recipes = CraftingManager.getInstance().getRecipeList();

        for (Object recipe : recipes) {
            if (recipe instanceof ShapedRecipes) {
                ShapedRecipes shaped = (ShapedRecipes) recipe;
                if (isRecipeMatch(shaped.recipeItems, shaped.getRecipeOutput(), itemStack)) {
                    matchingRecipes.add(shaped);
                }
            } else if (recipe instanceof ShapelessRecipes) {
                ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
                List<ItemStack> ingredients = shapeless.recipeItems;
                ItemStack[] ingredientsArray = ingredients.toArray(new ItemStack[ingredients.size()]);
                if (isRecipeMatch(ingredientsArray, shapeless.getRecipeOutput(), itemStack)) {
                    matchingRecipes.add(shapeless);
                }
            }
        }
        return matchingRecipes;
    }

    private boolean isRecipeMatch(ItemStack[] ingredients, ItemStack result, ItemStack target) {
        for (ItemStack ingredient : ingredients) {
            if (ingredient != null && ingredient.getItem() == target.getItem() &&
                (ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE || ingredient.getItemDamage() == target.getItemDamage())) {
                return true;
            }
        }
        return result.getItem() == target.getItem() && (result.getItem().isDamageable() || result.getItemDamage() == target.getItemDamage());
    }
}