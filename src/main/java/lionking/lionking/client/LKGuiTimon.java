package lionking.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.entity.LKEntityTimon;
import lionking.inventory.LKContainerTimon;
import lionking.inventory.LKSlotTimon;
import lionking.mod_LionKing;

@SideOnly(Side.CLIENT)
public class LKGuiTimon extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:gui/timon.png");
    private static final int TEXT_COLOR = 0x7F472F;

    private RenderItem itemRenderer = new RenderItem();

    public LKGuiTimon(EntityPlayer player, LKEntityTimon timon) {
        super(new LKContainerTimon(player, timon));
        ySize += 24;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Timon & Pumbaa", 51, 13, TEXT_COLOR);
        fontRendererObj.drawString("Inventory", 8, ySize - 94, TEXT_COLOR);

        for (int i = 0; i < 5; i++) {
            LKSlotTimon slot = (LKSlotTimon) inventorySlots.inventorySlots.get(i);
            String costStr = String.valueOf(slot.getCost());
            int xOffset = costStr.length() == 1 ? 18 + 33 * i : 23 + 33 * i;
            itemRenderer.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), new ItemStack(mod_LionKing.bug), xOffset, 51);
            fontRendererObj.drawString(costStr, costStr.length() == 1 ? 11 + 33 * i : 10 + 33 * i, 55, TEXT_COLOR);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}