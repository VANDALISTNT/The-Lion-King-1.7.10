package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.entity.LKEntitySimba; 

@SideOnly(Side.CLIENT)
public class LKModelSimbaCharm extends ModelBase {
    private final ModelRenderer body;

    private static final float CHILD_SCALE = 0.3F;
    private static final float ADULT_SCALE = 0.4F;

    public LKModelSimbaCharm() {
        textureWidth = 64;
        textureHeight = 32;

        body = new ModelRenderer(this, 0, 0);
        body.addBox(-6F, -20F, -39F, 12, 1, 15);
        body.setRotationPoint(0F, 5F, 2F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale, entity);

        GL11.glPushMatrix();
        boolean isChild = this.isChild;
        float modelScale = isChild ? CHILD_SCALE : ADULT_SCALE;
        GL11.glScalef(modelScale, modelScale, modelScale);

        boolean isSitting = ((LKEntitySimba) entity).isSitting();
        if (isChild) {
            GL11.glTranslatef(0F, isSitting ? 1.3F : 1.6F, isSitting ? 0F : 0.2F);
        } else {
            GL11.glTranslatef(0F, isSitting ? -0.3F : -0.2F, isSitting ? -0.5F : -0.2F);
        }

        body.render(scale);
        GL11.glPopMatrix();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale, Entity entity) {
        body.rotateAngleX = (float) Math.PI / 2F;
        body.rotationPointY = 5F;
    }
}