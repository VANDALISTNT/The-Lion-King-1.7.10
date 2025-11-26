package lionking.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntitySimba;

@SideOnly(Side.CLIENT)
public class LKGuiSimbaButton extends GuiButton
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:gui/simba.png");
    private static final int BUTTON_WIDTH = 22;
    private static final int BUTTON_HEIGHT = 22;

    private final LKEntitySimba simba;

    public LKGuiSimbaButton(int id, int x, int y, LKEntitySimba simba)
    {
        super(id, x, y, BUTTON_WIDTH, BUTTON_HEIGHT, "Simba");
        this.simba = simba;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (!visible)
        {
            return;
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        mc.getTextureManager().bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        boolean isHovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
        int u = getHoverState(isHovered) == 2 ? 202 : 178;
        int v = simba.isSitting() ? 24 : 0;

        drawTexturedModalRect(xPosition, yPosition, u, v, width, height);
        mouseDragged(mc, mouseX, mouseY);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}
