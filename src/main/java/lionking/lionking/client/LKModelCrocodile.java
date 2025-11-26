package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityCrocodile; 

@SideOnly(Side.CLIENT)
public class LKModelCrocodile extends ModelBase {
    private static final int TEXTURE_WIDTH = 128;
    private static final int TEXTURE_HEIGHT = 128;
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;
    private static final float PI = (float) Math.PI;

    private final ModelRenderer body;
    private final ModelRenderer tail1;
    private final ModelRenderer tail2;
    private final ModelRenderer tail3;
    private final ModelRenderer jaw;
    private final ModelRenderer head;
    private final ModelRenderer legFrontLeft;
    private final ModelRenderer legBackLeft;
    private final ModelRenderer legFrontRight;
    private final ModelRenderer legBackRight;
    private final ModelRenderer spines;

    public LKModelCrocodile() {
        textureWidth = TEXTURE_WIDTH;
        textureHeight = TEXTURE_HEIGHT;

        body = new ModelRenderer(this, 18, 83);
        body.addBox(-8.0F, -5.0F, 0.0F, 16, 9, 36);
        body.setRotationPoint(0.0F, 17.0F, -16.0F);

        tail1 = new ModelRenderer(this, 0, 28);
        tail1.addBox(-7.0F, 0.0F, 0.0F, 14, 7, 19);
        tail1.setRotationPoint(0.0F, 13.0F, 18.0F);

        tail2 = new ModelRenderer(this, 0, 55);
        tail2.addBox(-6.0F, 1.5F, 17.0F, 12, 5, 16);
        tail2.setRotationPoint(0.0F, 13.0F, 18.0F);

        tail3 = new ModelRenderer(this, 0, 77);
        tail3.addBox(-5.0F, 3.0F, 31.0F, 10, 3, 14);
        tail3.setRotationPoint(0.0F, 13.0F, 18.0F);

        jaw = new ModelRenderer(this, 58, 18);
        jaw.addBox(-6.5F, 0.3F, -19.0F, 13, 4, 19);
        jaw.setRotationPoint(0.0F, 17.0F, -16.0F);

        head = new ModelRenderer(this, 0, 0);
        head.addBox(-7.5F, -6.0F, -21.0F, 15, 6, 21);
        head.setRotationPoint(0.0F, 18.5F, -16.0F);

        legFrontLeft = new ModelRenderer(this, 2, 104);
        legFrontLeft.addBox(0.0F, 0.0F, -3.0F, 16, 3, 6);
        legFrontLeft.setRotationPoint(6.0F, 15.0F, -11.0F);
        legFrontLeft.rotateAngleZ = 25.0F * DEG_TO_RAD;

        legBackLeft = new ModelRenderer(this, 2, 104);
        legBackLeft.addBox(0.0F, 0.0F, -3.0F, 16, 3, 6);
        legBackLeft.setRotationPoint(6.0F, 15.0F, 15.0F);
        legBackLeft.rotateAngleZ = 25.0F * DEG_TO_RAD;

        legFrontRight = new ModelRenderer(this, 2, 104);
        legFrontRight.addBox(-16.0F, 0.0F, -3.0F, 16, 3, 6);
        legFrontRight.setRotationPoint(-6.0F, 15.0F, -11.0F);
        legFrontRight.mirror = true;
        legFrontRight.rotateAngleZ = -25.0F * DEG_TO_RAD;

        legBackRight = new ModelRenderer(this, 2, 104);
        legBackRight.addBox(-16.0F, 0.0F, -3.0F, 16, 3, 6);
        legBackRight.setRotationPoint(-6.0F, 15.0F, 15.0F);
        legBackRight.mirror = true;
        legBackRight.rotateAngleZ = -25.0F * DEG_TO_RAD;

        spines = new ModelRenderer(this, 46, 45);
        spines.addBox(-5.0F, 0.0F, 0.0F, 10, 4, 32);
        spines.setRotationPoint(0.0F, 9.5F, -14.0F);
        spines.rotateAngleX = -2.0F * DEG_TO_RAD;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        renderModel(scale);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entityliving, float limbSwing, float limbSwingAmount, float partialTickTime) {
        float legSwing = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
        legBackRight.rotateAngleY = legSwing;
        legBackLeft.rotateAngleY = legSwing;
        legFrontRight.rotateAngleY = legSwing;
        legFrontLeft.rotateAngleY = legSwing;

        float tailSwing = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
        tail1.rotateAngleY = tailSwing * 0.5F;
        tail2.rotateAngleY = tailSwing * 0.5625F;
        tail3.rotateAngleY = tailSwing * 0.59375F;

        if (entityliving instanceof LKEntityCrocodile) {
            head.rotateAngleX = (((LKEntityCrocodile) entityliving).getSnapTime() / 20.0F) * PI * -0.3F;
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
    }

    private void renderModel(float scale) {
        body.render(scale);
        tail1.render(scale);
        tail2.render(scale);
        tail3.render(scale);
        jaw.render(scale);
        head.render(scale);
        legFrontLeft.render(scale);
        legBackLeft.render(scale);
        legFrontRight.render(scale);
        legBackRight.render(scale);
        spines.render(scale);
    }
}