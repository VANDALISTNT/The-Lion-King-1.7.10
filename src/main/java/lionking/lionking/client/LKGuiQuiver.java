package lionking.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.inventory.LKInventoryQuiver;
import lionking.inventory.LKContainerQuiver;

@SideOnly(Side.CLIENT)
public class LKGuiQuiver extends GuiContainer
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:gui/quiver.png");
    private static final ResourceLocation CHEST_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final int TEXT_COLOR_INVENTORY = 0x404040; 
    private static final int TEXT_COLOR_QUIVER = 0x3B4229;   

    public LKGuiQuiver(EntityPlayer player, LKInventoryQuiver quiver)
    {
        super(new LKContainerQuiver(player, quiver));
        xSize = 251;
        ySize = 100;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRendererObj.drawString("Inventory", 8, 6, TEXT_COLOR_INVENTORY);
        fontRendererObj.drawString("Quiver", 209, 16, TEXT_COLOR_QUIVER);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(CHEST_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, 176, 3);        
        drawTexturedModalRect(x, y + 3, 0, 125, 176, 97); 

        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(x + 201, y, 0, 0, 50, ySize); 
    }
}
