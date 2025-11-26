package lionking.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

import lionking.tileentity.LKTileEntityMobSpawner;

public class LKTileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        renderMobSpawner((LKTileEntityMobSpawner) tileEntity, x, y, z, partialTicks);
    }

    private void renderMobSpawner(LKTileEntityMobSpawner spawner, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);

        Entity entity = spawner.getMobEntity();
        if (entity != null) {
            renderMobEntity(spawner, entity, x, y, z, partialTicks);
        }

        GL11.glPopMatrix();
    }

    private void renderMobEntity(LKTileEntityMobSpawner spawner, Entity entity, double x, double y, double z, float partialTicks) {
        entity.setWorld(spawner.getWorldObj());
        float scale = 0.4375F;

        GL11.glTranslatef(0.0F, 0.4F, 0.0F);

        float rotationYaw = (float) (spawner.yaw2 + (spawner.yaw - spawner.yaw2) * partialTicks);
        GL11.glRotatef(rotationYaw * 10.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);

        GL11.glTranslatef(0.0F, -0.4F, 0.0F);
        GL11.glScalef(scale, scale, scale);

        entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
    }
}