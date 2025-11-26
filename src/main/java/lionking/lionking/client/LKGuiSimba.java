package lionking.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.inventory.LKContainerSimba;
import lionking.entity.LKEntitySimba;
import lionking.client.LKSimbaSit;
import lionking.mod_LionKing;

@SideOnly(Side.CLIENT)
public class LKGuiSimba extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:gui/simba.png");
    private static final int TEXT_COLOR = 0x7A2804;

    private final LKEntitySimba simba;

    public LKGuiSimba(EntityPlayer player, LKEntitySimba simba) {
        super(new LKContainerSimba(player, simba));
        this.simba = simba;
        ySize += 24;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new LKGuiSimbaButton(0, guiLeft + 77, guiTop + 54, simba));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            mod_LionKing.networkGame.sendToServer(new LKSimbaSit(simba.getEntityId(), mc.thePlayer.dimension));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Simba", 74, 13, TEXT_COLOR);
        fontRendererObj.drawString("Inventory", 8, ySize - 94, TEXT_COLOR);
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