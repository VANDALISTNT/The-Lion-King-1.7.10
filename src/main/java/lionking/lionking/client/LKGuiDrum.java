package lionking.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import lionking.inventory.LKContainerDrum;

public class LKGuiDrum extends GuiContainer {
    private static final ResourceLocation texture = new ResourceLocation("lionking", "textures/gui/drum.png");
    private static final int TEXT_COLOR = 0x404040; 
    private final LKContainerDrum drumContainer;

    public LKGuiDrum(EntityPlayer player, LKContainerDrum container) {
        super(container);
        this.drumContainer = container;
        this.xSize = 176; 
        this.ySize = 166; 
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Bongo Drum", 60, 7, TEXT_COLOR); 

        for (int i = 0; i < 3; i++) {
            if (drumContainer.enchantLevels[i] > 0) {
                String levelStr = "" + drumContainer.enchantLevels[i];
                fontRendererObj.drawString(levelStr, 142 - fontRendererObj.getStringWidth(levelStr), 30 + i * 18, TEXT_COLOR); 
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}