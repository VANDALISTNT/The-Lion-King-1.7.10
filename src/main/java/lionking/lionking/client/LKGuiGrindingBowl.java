package lionking.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.tileentity.LKTileEntityGrindingBowl;
import lionking.inventory.LKContainerGrindingBowl;

@SideOnly(Side.CLIENT)
public class LKGuiGrindingBowl extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:gui/grind.png");
    private static final int TEXT_COLOR = 0x140C02; 

    private final LKContainerGrindingBowl container;

    public LKGuiGrindingBowl(EntityPlayer player, LKTileEntityGrindingBowl bowl) {
        super(new LKContainerGrindingBowl(player, bowl));
        this.container = (LKContainerGrindingBowl) inventorySlots;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Grinding Bowl", 49, 6, TEXT_COLOR);
        fontRendererObj.drawString("Inventory", 8, ySize - 94, TEXT_COLOR);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        int grindProgress = container.getGrindProgressScaled(45);
        drawTexturedModalRect(x + 62, y + 35, 176, 0, grindProgress, 15); 
    }
}