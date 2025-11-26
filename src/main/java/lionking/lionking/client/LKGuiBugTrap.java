package lionking.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.tileentity.LKTileEntityBugTrap; 
import lionking.inventory.LKContainerBugTrap; 

@SideOnly(Side.CLIENT)
public class LKGuiBugTrap extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:gui/trap.png");
    private static final int TEXT_COLOR = 0xFFFFFF; 

    public LKGuiBugTrap(EntityPlayer player, LKTileEntityBugTrap trap) {
        super(new LKContainerBugTrap(player, trap));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Bug Trap", 49, 6, TEXT_COLOR); 
        fontRendererObj.drawString("Inventory", 8, ySize - 96, TEXT_COLOR); 
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        int guiX = (width - xSize) / 2;
        int guiY = (height - ySize) / 2;
        drawTexturedModalRect(guiX, guiY, 0, 0, xSize, ySize);
    }
}