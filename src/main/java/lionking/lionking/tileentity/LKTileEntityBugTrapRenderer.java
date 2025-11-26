package lionking.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import lionking.mod_LionKing;
import lionking.tileentity.LKTileEntityBugTrap;

public class LKTileEntityBugTrapRenderer extends TileEntitySpecialRenderer {
    private final RenderBlocks renderBlocks = new RenderBlocks();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        LKTileEntityBugTrap bugTrap = (LKTileEntityBugTrap) tileEntity;
        renderTrapContents(bugTrap, x, y, z);
    }

    private void renderTrapContents(LKTileEntityBugTrap bugTrap, double x, double y, double z) {
        for (int slot = 0; slot < 4; slot++) {
            GL11.glPushMatrix();

            setupTransformations(x, y, z, slot);

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            renderSlotItem(bugTrap.getStackInSlot(slot));
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);

            GL11.glPopMatrix();
        }
    }

    private void setupTransformations(double x, double y, double z, int slot) {
        float offsetX = (float) x + 0.5F + (slot == 2 ? 0.035F : slot == 1 ? -0.035F : 0.0F);
        float offsetY = (float) y + 0.4F;
        float offsetZ = (float) z + 0.5F + (slot == 3 ? 0.035F : slot == 0 ? -0.035F : 0.0F);

        GL11.glTranslatef(offsetX, offsetY, offsetZ);

        float rotation;
        switch (slot) {
            case 0: rotation = 180.0F; break;
            case 1: rotation = 270.0F; break;
            case 2: rotation = 90.0F; break;
            case 3: default: rotation = 0.0F; break;
        }
        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
    }

    private void renderSlotItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItem() == null) {
            return;
        }

        Item item = itemStack.getItem();
        if (item instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType())) {
            renderBlockItem(itemStack);
        } else {
            renderFlatItem(itemStack);
        }
    }

    private void renderBlockItem(ItemStack itemStack) {
        bindTexture(TextureMap.locationBlocksTexture);
        Block block = Block.getBlockFromItem(itemStack.getItem());
        float scale = (block.getRenderType() == 1 || block.getRenderType() == 19 || block.getRenderType() == 12 || block.getRenderType() == 2) ? 0.5F : 0.25F;

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(0.0F, 0.35F, 0.0F);
        renderBlocks.renderBlockAsItem(block, itemStack.getItemDamage(), 1.0F);
        GL11.glPopMatrix();
    }

    private void renderFlatItem(ItemStack itemStack) {
        GL11.glPushMatrix();
        GL11.glScalef(0.4F, 0.4F, 0.4F);

        Item item = itemStack.getItem();
        if (item.requiresMultipleRenderPasses()) {
            bindTexture(TextureMap.locationItemsTexture);
            for (int pass = 0; pass < item.getRenderPasses(itemStack.getItemDamage()); pass++) {
                applyColorFromItem(itemStack, pass);
                drawItemSprite(item.getIconFromDamageForRenderPass(itemStack.getItemDamage(), pass));
            }
        } else {
            bindTexture(item instanceof ItemBlock ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
            applyColorFromItem(itemStack, 0);
            drawItemSprite(itemStack.getIconIndex());
        }

        GL11.glPopMatrix();
    }

    private void applyColorFromItem(ItemStack itemStack, int renderPass) {
        int color = itemStack.getItem().getColorFromItemStack(itemStack, renderPass);
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GL11.glColor4f(r, g, b, 1.0F);
    }

    private void drawItemSprite(IIcon icon) {
        Tessellator tessellator = Tessellator.instance;
        float minU = icon.getMinU();
        float maxU = icon.getMaxU();
        float minV = icon.getMinV();
        float maxV = icon.getMaxV();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV(-0.5D, -0.25D, 0.0D, minU, maxV);
        tessellator.addVertexWithUV(0.5D, -0.25D, 0.0D, maxU, maxV);
        tessellator.addVertexWithUV(0.5D, 0.75D, 0.0D, maxU, minV);
        tessellator.addVertexWithUV(-0.5D, 0.75D, 0.0D, minU, minV);
        tessellator.draw();
    }
}