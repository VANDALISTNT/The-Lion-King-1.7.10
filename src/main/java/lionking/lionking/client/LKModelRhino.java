package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelRhino extends ModelBase {
    private static final int TEXTURE_WIDTH = 64;
    private static final int TEXTURE_HEIGHT = 64;
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;
    private static final float PI = (float) Math.PI;

    private final ModelRenderer horn;
    private final ModelRenderer backHorn;
    private final ModelRenderer leftEar;
    private final ModelRenderer rightEar;
    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer legFrontLeft;
    private final ModelRenderer legFrontRight;
    private final ModelRenderer legBackLeft;
    private final ModelRenderer legBackRight;

    private final float childOffsetY = 8.0F;
    private final float childOffsetZ = 4.0F;

    public LKModelRhino() {
        textureWidth = TEXTURE_WIDTH;
        textureHeight = TEXTURE_HEIGHT;

        horn = new ModelRenderer(this, 56, 0);
        horn.addBox(-1.0F, -9.0F, -4.5F, 2, 6, 2);
        horn.setRotationPoint(0.0F, 9.0F, -11.0F);

        backHorn = new ModelRenderer(this, 60, 8);
        backHorn.addBox(-0.5F, -6.5F, 0.0F, 1, 3, 1);
        backHorn.setRotationPoint(0.0F, 9.0F, -11.0F);

        leftEar = new ModelRenderer(this, 44, 0);
        leftEar.addBox(-4.6F, -6.0F, 3.0F, 1, 3, 2);
        leftEar.setRotationPoint(0.0F, 9.0F, -11.0F);

        rightEar = new ModelRenderer(this, 50, 0);
        rightEar.addBox(3.6F, -6.0F, 3.0F, 1, 3, 2);
        rightEar.setRotationPoint(0.0F, 9.0F, -11.0F);

        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 12);
        head.setRotationPoint(0.0F, 9.0F, -11.0F);

        body = new ModelRenderer(this, 0, 32);
        body.addBox(-5.466667F, -10.0F, -8.0F, 13, 22, 10);
        body.setRotationPoint(-1.0F, 9.0F, 1.0F);

        legFrontLeft = new ModelRenderer(this, 0, 20);
        legFrontLeft.addBox(-3.0F, 0.0F, -2.0F, 4, 8, 4);
        legFrontLeft.setRotationPoint(-3.0F, 16.0F, 10.0F);

        legFrontRight = new ModelRenderer(this, 0, 20);
        legFrontRight.addBox(-1.0F, 0.0F, -2.0F, 4, 8, 4);
        legFrontRight.setRotationPoint(3.0F, 16.0F, 10.0F);

        legBackLeft = new ModelRenderer(this, 0, 20);
        legBackLeft.addBox(-3.0F, 0.0F, -3.0F, 4, 8, 4);
        legBackLeft.setRotationPoint(-3.0F, 16.0F, -5.0F);

        legBackRight = new ModelRenderer(this, 0, 20);
        legBackRight.addBox(-1.0F, 0.0F, -3.0F, 4, 8, 4);
        legBackRight.setRotationPoint(3.0F, 16.0F, -5.0F);

        setInitialRotationAngles();
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

        if (isChild) {
            renderChild(entity, scale);
        } else {
            renderAdult(scale);
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
        float headX = headPitch * DEG_TO_RAD + 0.279256F;
        float headY = netHeadYaw * DEG_TO_RAD;

        head.rotateAngleX = headX;
        head.rotateAngleY = headY;
        horn.rotateAngleX = headX;
        horn.rotateAngleY = headY;
        backHorn.rotateAngleX = headX;
        backHorn.rotateAngleY = headY;
        leftEar.rotateAngleX = headX;
        leftEar.rotateAngleY = headY;
        rightEar.rotateAngleX = headX;
        rightEar.rotateAngleY = headY;

        legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    private void setInitialRotationAngles() {
        body.rotateAngleX = PI / 2.0F; 
    }

    private void renderAdult(float scale) {
        head.render(scale);
        horn.render(scale);
        backHorn.render(scale);
        leftEar.render(scale);
        rightEar.render(scale);
        body.render(scale);
        legFrontLeft.render(scale);
        legFrontRight.render(scale);
        legBackLeft.render(scale);
        legBackRight.render(scale);
    }

    private void renderChild(Entity entity, float scale) {
        float childScale = 0.5F; 

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, childOffsetY * scale, childOffsetZ * scale);
        head.render(scale);
        leftEar.render(scale);
        rightEar.render(scale);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glScalef(childScale, childScale, childScale);
        GL11.glTranslatef(0.0F, 24.0F * scale, 0.0F);
        body.render(scale);
        legFrontLeft.render(scale);
        legFrontRight.render(scale);
        legBackLeft.render(scale);
        legBackRight.render(scale);
        GL11.glPopMatrix();
    }
}