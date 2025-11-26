package lionking.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.quest.LKQuestBase;

@SideOnly(Side.CLIENT)
public class LKGuiQuestsButton extends GuiButton
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:gui/book_menu.png");
    private static final int BUTTON_WIDTH = 135;
    private static final int BUTTON_HEIGHT = 20;
    private static final int TEXT_COLOR = 0x120C01; 

    private final LKQuestBase quest;

    public LKGuiQuestsButton(int id, int x, int y, LKQuestBase quest)
    {
        super(id, x, y, BUTTON_WIDTH, BUTTON_HEIGHT, quest == null ? "Main Page" : quest.getName());
        this.quest = quest;
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

        int v = determineTextureV();
        drawTexturedModalRect(xPosition, yPosition, 121, v, width / 2, height);
        drawTexturedModalRect(xPosition + width / 2, yPosition, 256 - width / 2, v, width / 2, height);

        mouseDragged(mc, mouseX, mouseY);
        drawCenteredString(mc.fontRenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, TEXT_COLOR);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private int determineTextureV()
    {
        if (quest == null)
        {
            return 216; 
        }
        if (!quest.canStart())
        {
            return 196; 
        }
        if (LKGuiQuests.flashTimer > 14 && quest.canStart() && quest.checked == 0)
        {
            return 236; 
        }
        return 216; 
    }

    @Override
    public void drawCenteredString(FontRenderer fontRenderer, String text, int x, int y, int color)
    {
        fontRenderer.drawString(text, x - fontRenderer.getStringWidth(text) / 2, y, color);
    }
}
