package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.common.LKIngame; 

@SideOnly(Side.CLIENT)
public class LKModelLion extends ModelBase {
    private static final int TEXTURE_WIDTH = 64;
    private static final int TEXTURE_HEIGHT = 96;
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;
    private static final float PI = (float) Math.PI;

    private final ModelRenderer head;
    private final ModelRenderer headwear;
    private final ModelRenderer mane;
    private final ModelRenderer body;
    private final ModelRenderer legFrontLeft;
    private final ModelRenderer legFrontRight;
    private final ModelRenderer legBackLeft;
    private final ModelRenderer legBackRight;
    private final ModelRenderer hat;
    private final boolean isOutlander;

    public LKModelLion() {
        this(false);
    }

    public LKModelLion(boolean isOutlander) {
        this.isOutlander = isOutlander;

        textureWidth = TEXTURE_WIDTH;
        textureHeight = TEXTURE_HEIGHT;

        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4.0F, -4.0F, -7.0F, 8, 8, 8);
        head.setTextureOffset(52, 34);
        head.addBox(-2.0F, 0.0F, -9.0F, 4, 4, 2);
        head.setRotationPoint(0.0F, 4.0F, -9.0F);

        headwear = new ModelRenderer(this, 32, 0);
        headwear.addBox(-4.0F, -4.0F, -7.0F, 8, 8, 8, 0.5F);
        headwear.setRotationPoint(0.0F, 4.0F, -9.0F);

        mane = new ModelRenderer(this, 0, 36);
        mane.addBox(-7.0F, -7.0F, -5.0F, 14, 14, 9);
        mane.setRotationPoint(0.0F, 4.0F, -9.0F);

        body = new ModelRenderer(this, 0, 68);
        body.addBox(isOutlander ? -5.0F : -6.0F, -10.0F, -7.0F, isOutlander ? 10 : 12, 18, 10);
        body.setRotationPoint(0.0F, 5.0F, 2.0F);
        body.rotateAngleX = PI / 2.0F;

        legFrontLeft = new ModelRenderer(this, 0, 19);
        legFrontLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        legFrontLeft.setRotationPoint(isOutlander ? -3.0F : -4.0F, 12.0F, 7.0F);

        legFrontRight = new ModelRenderer(this, 0, 19);
        legFrontRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        legFrontRight.setRotationPoint(isOutlander ? 3.0F : 4.0F, 12.0F, 7.0F);

        legBackLeft = new ModelRenderer(this, 0, 19);
        legBackLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        legBackLeft.setRotationPoint(isOutlander ? -3.0F : -4.0F, 12.0F, -5.0F);

        legBackRight = new ModelRenderer(this, 0, 19);
        legBackRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        legBackRight.setRotationPoint(isOutlander ? 3.0F : 4.0F, 12.0F, -5.0F);

        hat = new ModelRenderer(this, 17, 25);
        hat.addBox(-4.0F, -16.0F, 0.0F, 8, 9, 1);
        hat.setRotationPoint(0.0F, 4.0F, -9.0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        if (isChild) {
            renderChild(scale);
        } else {
            renderAdult(scale);
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
        float headYawRad = netHeadYaw * DEG_TO_RAD;
        float headPitchRad = headPitch * DEG_TO_RAD;

        head.rotateAngleX = headPitchRad;
        head.rotateAngleY = headYawRad;
        headwear.rotateAngleX = headPitchRad;
        headwear.rotateAngleY = headYawRad;
        mane.rotateAngleX = headPitchRad;
        mane.rotateAngleY = headYawRad;
        hat.rotateAngleX = headPitchRad;
        hat.rotateAngleY = headYawRad;

        legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    private void renderAdult(float scale) {
        head.render(scale);
        headwear.render(scale);
        mane.render(scale);
        if (LKIngame.isChristmas()) {
            hat.render(scale);
        }
        body.render(scale);
        legFrontLeft.render(scale);
        legFrontRight.render(scale);
        legBackLeft.render(scale);
        legBackRight.render(scale);
    }

    private void renderChild(float scale) {
        float childScale = 0.5F; // 1.0F / 2.0F
        float childOffsetY = 8.0F * scale;
        float childOffsetZ = 4.0F * scale;

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, childOffsetY, childOffsetZ);
        head.render(scale);
        headwear.render(scale);
        if (LKIngame.isChristmas()) {
            GL11.glTranslatef(0.0F, 3.0F, 0.0F);
            hat.render(scale);
        }
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
