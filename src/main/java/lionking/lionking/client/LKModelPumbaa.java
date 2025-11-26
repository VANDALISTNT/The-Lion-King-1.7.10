package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.common.LKIngame; 

@SideOnly(Side.CLIENT)
public class LKModelPumbaa extends ModelBase {
    private static final int TEXTURE_WIDTH = 64;
    private static final int TEXTURE_HEIGHT = 64;
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;
    private static final float PI = (float) Math.PI;

    private final ModelRenderer snout;
    private final ModelRenderer leftEar;
    private final ModelRenderer rightEar;
    private final ModelRenderer tail;
    private final ModelRenderer mane;
    private final ModelRenderer hair;
    private final ModelRenderer leftHorn;
    private final ModelRenderer rightHorn;
    private final ModelRenderer head;
    private final ModelRenderer hat;
    private final ModelRenderer body;
    private final ModelRenderer legFrontLeft;
    private final ModelRenderer legFrontRight;
    private final ModelRenderer legBackLeft;
    private final ModelRenderer legBackRight;

    public LKModelPumbaa() {
        textureWidth = TEXTURE_WIDTH;
        textureHeight = TEXTURE_HEIGHT;

        snout = new ModelRenderer(this, 0, 0);
        snout.addBox(-3.0F, -2.6F, -11.0F, 6, 4, 3);
        snout.setRotationPoint(0.0F, 9.0F, -5.0F);

        leftEar = new ModelRenderer(this, 0, 31);
        leftEar.addBox(-4.5F, -9.0F, -3.0F, 1, 4, 2);
        leftEar.setRotationPoint(0.0F, 9.0F, -5.0F);

        rightEar = new ModelRenderer(this, 6, 31);
        rightEar.addBox(3.5F, -9.0F, -3.0F, 1, 4, 2);
        rightEar.setRotationPoint(0.0F, 9.0F, -5.0F);

        tail = new ModelRenderer(this, 60, 55);
        tail.addBox(-0.5F, -1.5F, 1.0F, 1, 8, 1);
        tail.setRotationPoint(0.0F, 11.0F, 8.0F);

        mane = new ModelRenderer(this, 36, 18);
        mane.addBox(-3.5F, -4.0F, -2.0F, 5, 4, 9);
        mane.setRotationPoint(1.0F, 9.0F, -7.0F);
        mane.rotateAngleX = -0.417716F;

        hair = new ModelRenderer(this, 0, 10);
        hair.addBox(-2.0F, -7.3F, -4.7F, 4, 5, 5);
        hair.setRotationPoint(0.0F, 9.0F, -5.0F);

        leftHorn = new ModelRenderer(this, 54, 31);
        leftHorn.addBox(-9.0F, -2.0F, -7.0F, 4, 1, 1);
        leftHorn.setRotationPoint(0.0F, 9.0F, -5.0F);

        rightHorn = new ModelRenderer(this, 54, 31);
        rightHorn.addBox(5.0F, -2.0F, -7.0F, 4, 1, 1);
        rightHorn.setRotationPoint(0.0F, 9.0F, -5.0F);

        head = new ModelRenderer(this, 28, 0);
        head.addBox(-5.0F, -6.0F, -8.0F, 10, 10, 8);
        head.setRotationPoint(0.0F, 9.0F, -5.0F);

        hat = new ModelRenderer(this, 34, 36);
        hat.addBox(-4.0F, -15.0F, -4.5F, 8, 9, 1);
        hat.setRotationPoint(0.0F, 9.0F, -5.0F);

        body = new ModelRenderer(this, 0, 37);
        body.addBox(-5.0F, -10.0F, -7.0F, 12, 17, 10);
        body.setRotationPoint(-1.0F, 11.0F, 2.0F);
        body.rotateAngleX = PI / 2.0F;

        legFrontLeft = new ModelRenderer(this, 0, 20);
        legFrontLeft.addBox(-2.0F, 0.0F, -2.0F, 3, 8, 3);
        legFrontLeft.setRotationPoint(-3.0F, 16.0F, 6.0F);

        legFrontRight = new ModelRenderer(this, 0, 20);
        legFrontRight.addBox(-1.5F, 0.0F, -2.0F, 3, 8, 3);
        legFrontRight.setRotationPoint(3.0F, 16.0F, 6.0F);

        legBackLeft = new ModelRenderer(this, 0, 20);
        legBackLeft.addBox(-2.0F, 0.0F, -2.0F, 3, 8, 3);
        legBackLeft.setRotationPoint(-3.0F, 16.0F, -4.0F);

        legBackRight = new ModelRenderer(this, 0, 20);
        legBackRight.addBox(-1.0F, 0.0F, -2.0F, 3, 8, 3);
        legBackRight.setRotationPoint(3.0F, 16.0F, -4.0F);

        tail.rotateAngleX = 0.698132F; 
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        renderModel(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
        float headYawRad = netHeadYaw * DEG_TO_RAD;
        float headPitchRad = headPitch * DEG_TO_RAD;

        head.rotateAngleX = headPitchRad;
        head.rotateAngleY = headYawRad;
        hat.rotateAngleX = headPitchRad;
        hat.rotateAngleY = headYawRad;
        snout.rotateAngleX = headPitchRad;
        snout.rotateAngleY = headYawRad;
        hair.rotateAngleX = -0.104719F + headPitchRad;
        hair.rotateAngleY = headYawRad;
        rightEar.rotateAngleX = headPitchRad;
        rightEar.rotateAngleY = headYawRad;
        leftEar.rotateAngleX = headPitchRad;
        leftEar.rotateAngleY = headYawRad;
        rightHorn.rotateAngleX = headPitchRad;
        rightHorn.rotateAngleY = 0.436332F + headYawRad;
        leftHorn.rotateAngleX = headPitchRad;
        leftHorn.rotateAngleY = -0.436332F + headYawRad;

        legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    private void renderModel(float scale) {
        snout.render(scale);
        leftEar.render(scale);
        rightEar.render(scale);
        tail.render(scale);
        mane.render(scale);
        hair.render(scale);
        leftHorn.render(scale);
        rightHorn.render(scale);
        head.render(scale);
        if (LKIngame.isChristmas()) {
            hat.render(scale);
        }
        body.render(scale);
        legFrontLeft.render(scale);
        legFrontRight.render(scale);
        legBackLeft.render(scale);
        legBackRight.render(scale);
    }
}
