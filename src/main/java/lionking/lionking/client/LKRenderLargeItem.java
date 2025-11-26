package lionking.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class LKRenderLargeItem implements IItemRenderer {
    private static final Map<String, ResourceLocation> LARGE_ITEM_TEXTURES = new HashMap<String, ResourceLocation>();
    private static final float SCALE_FACTOR = 2.0F;
    private static final ResourceLocation GLINT_TEXTURE = LKClientProxy.ITEM_GLINT_TEXTURE; // Исправлено: itemGlintTexture ? ITEM_GLINT_TEXTURE

    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemStack, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
        GL11.glScalef(SCALE_FACTOR, SCALE_FACTOR, 1.0F);

        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        ResourceLocation texture = getItemTexture(itemStack);
        renderBaseItem(textureManager, texture);

        if (itemStack != null && itemStack.hasEffect(0)) {
            renderGlintEffect(textureManager);
        }

        GL11.glPopMatrix();
    }

    private ResourceLocation getItemTexture(ItemStack itemStack) {
        String itemName = Item.itemRegistry.getNameForObject(itemStack.getItem());
        ResourceLocation texture = LARGE_ITEM_TEXTURES.get(itemName);

        if (texture == null) {
            String texturePath = "lionking:item/large/" + itemName.substring(itemName.indexOf("lionking:") + 9) + ".png";
            texture = new ResourceLocation(texturePath);
            LARGE_ITEM_TEXTURES.put(itemName, texture);
        }

        return texture;
    }

    private void renderBaseItem(TextureManager textureManager, ResourceLocation texture) {
        textureManager.bindTexture(texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ItemRenderer.renderItemIn2D(Tessellator.instance, 1.0F, 0.0F, 0.0F, 1.0F, 32, 32, 0.0625F);
    }

    private void renderGlintEffect(TextureManager textureManager) {
        GL11.glDepthFunc(GL11.GL_EQUAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        textureManager.bindTexture(GLINT_TEXTURE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);

        float brightness = 0.76F;
        GL11.glColor4f(0.5F * brightness, 0.25F * brightness, 0.8F * brightness, 1.0F);

        GL11.glMatrixMode(GL11.GL_TEXTURE);
        renderGlintPass(3000L, -50.0F);
        renderGlintPass(4873L, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
    }

    private void renderGlintPass(long timeModulus, float rotation) {
        GL11.glPushMatrix();
        float scale = 0.125F;
        GL11.glScalef(scale, scale, scale);
        float offset = (float)(System.currentTimeMillis() % timeModulus) / timeModulus * 8.0F;
        GL11.glTranslatef(rotation > 0 ? -offset : offset, 0.0F, 0.0F);
        GL11.glRotatef(rotation, 0.0F, 0.0F, 1.0F);
        ItemRenderer.renderItemIn2D(Tessellator.instance, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
        GL11.glPopMatrix();
    }
}