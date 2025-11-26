package lionking.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import lionking.client.LKRenderHyena;
import lionking.client.LKModelHyenaHead;

import java.util.HashMap;
import java.util.Map;

public class LKTileEntityHyenaHeadRenderer extends TileEntitySpecialRenderer {
    private final LKModelHyenaHead model = new LKModelHyenaHead(false);
    private static final Map<Integer, ResourceLocation> TEXTURE_CACHE = new HashMap<Integer, ResourceLocation>();
    private static final ResourceLocation TEXTURE_SKELETON = LKRenderHyena.SKELETON_TEXTURE;

    static {
        TEXTURE_CACHE.put(0, new ResourceLocation("lionking", "textures/mobs/hyena_0.png"));
        TEXTURE_CACHE.put(1, new ResourceLocation("lionking", "textures/mobs/hyena_1.png"));
        TEXTURE_CACHE.put(2, new ResourceLocation("lionking", "textures/mobs/hyena_2.png"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        LKTileEntityHyenaHead head = (LKTileEntityHyenaHead) tileEntity;
        renderHead(head, (float) x, (float) y, (float) z);
    }

    private void renderHead(LKTileEntityHyenaHead head, float x, float y, float z) {
        int hyenaType = head.getHyenaType();
        bindTexture(hyenaType == 3 ? TEXTURE_SKELETON : TEXTURE_CACHE.getOrDefault(hyenaType, TEXTURE_CACHE.get(0)));

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);

        setupTransformations(x, y, z, head.getBlockMetadata(), head.getRotation() * 360.0F / 16.0F);

        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    private void setupTransformations(float x, float y, float z, int metadata, float rotation) {
        float offsetX = x + 0.5F;
        float offsetY = y;
        float offsetZ = z + 0.5F;

        if (metadata != 1) {
            switch (metadata) {
                case 2:
                    offsetZ += 0.31F;
                    break;
                case 3:
                    offsetZ -= 0.31F;
                    rotation = 180.0F;
                    break;
                case 4:
                    offsetX += 0.31F;
                    rotation = 270.0F;
                    break;
                case 5:
                default:
                    offsetX -= 0.31F;
                    rotation = 90.0F;
                    break;
            }
            offsetY += 0.25F;
        }

        GL11.glTranslatef(offsetX, offsetY, offsetZ);
        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
    }
}