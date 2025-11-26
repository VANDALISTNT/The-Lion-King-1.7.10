package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.common.LKIngame; 

@SideOnly(Side.CLIENT)
public class LKModelRafiki extends ModelBase {
    private static final int TEXTURE_WIDTH = 64;
    private static final int TEXTURE_HEIGHT = 64;
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;
    private static final float PI = (float) Math.PI;

    private final ModelRenderer body;
    private final ModelRenderer rightArm;
    private final ModelRenderer leftArm;
    private final ModelRenderer rightLeg;
    private final ModelRenderer leftLeg;
    private final ModelRenderer tail1;
    private final ModelRenderer tail2;
    private final ModelRenderer tail3;
    private final ModelRenderer tail4;
    private final ModelRenderer head;
    private final ModelRenderer hair;
    private final ModelRenderer hat;

    public LKModelRafiki() {
        textureWidth = TEXTURE_WIDTH;
        textureHeight = TEXTURE_HEIGHT;

        body = new ModelRenderer(this, 16, 16);
        body.addBox(0.0F, 0.0F, 0.0F, 8, 12, 4);
        body.setRotationPoint(-4.0F, 1.0F, -2.825F);
        body.rotateAngleX = 0.3F;

        rightArm = new ModelRenderer(this, 40, 17);
        rightArm.addBox(0.0F, 0.0F, 0.0F, 3, 11, 3);
        rightArm.setRotationPoint(-7.0F, 2.0F, -1.5F);

        leftArm = new ModelRenderer(this, 40, 17);
        leftArm.addBox(0.0F, 0.0F, 0.0F, 3, 11, 3);
        leftArm.setRotationPoint(4.0F, 2.0F, -1.5F);

        rightLeg = new ModelRenderer(this, 0, 16);
        rightLeg.addBox(0.0F, 0.0F, 0.0F, 4, 13, 4);
        rightLeg.setRotationPoint(-4.1F, 11.0F, 0.0F);

        leftLeg = new ModelRenderer(this, 0, 16);
        leftLeg.addBox(0.0F, 0.0F, 0F, 4, 13, 4);
        leftLeg.setRotationPoint(0.1F, 11.0F, 0.0F);

        tail1 = new ModelRenderer(this, 0, 58);
        tail1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 5);
        tail1.setRotationPoint(-0.5F, 11.0F, 3.0F);
        tail1.rotateAngleX = 1.0F;

        tail2 = new ModelRenderer(this, 12, 59);
        tail2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 4);
        tail2.setRotationPoint(-0.5F, 7.0F, 6.0F);

        tail3 = new ModelRenderer(this, 22, 60);
        tail3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3);
        tail3.setRotationPoint(-0.5F, 7.0F, 10.2F);
        tail3.rotateAngleX = -1.0F;

        tail4 = new ModelRenderer(this, 30, 56);
        tail4.addBox(0.0F, 0.0F, 0.0F, 1, 1, 7);
        tail4.setRotationPoint(-0.5F, 9.5F, 11.7F);
        tail4.rotateAngleX = -2.0F;

        head = new ModelRenderer(this, 0, 0);
        head.addBox(-3.5F, -1.0F, -5.0F, 7, 7, 6);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);

        hair = new ModelRenderer(this, 28, 0);
        hair.addBox(-5.0F, -2.0F, -3.0F, 10, 10, 5);
        hair.setRotationPoint(0.0F, 0.0F, 0.0F);

        hat = new ModelRenderer(this, 0, 33);
        hat.addBox(-4.0F, -10.0F, -3.1F, 8, 9, 1);
        hat.setRotationPoint(0.0F, 0.0F, 0.0F);
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

        head.rotateAngleY = headYawRad;
        head.rotateAngleX = headPitchRad;
        hair.rotateAngleY = headYawRad;
        hair.rotateAngleX = headPitchRad - 11.0F * DEG_TO_RAD;
        hat.rotateAngleY = headYawRad;
        hat.rotateAngleX = headPitchRad;

        rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 2.0F * limbSwingAmount * 0.5F;
        leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        rightArm.rotateAngleZ = MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        leftArm.rotateAngleZ = -MathHelper.cos(ageInTicks * 0.09F) * 0.05F - 0.05F;
        rightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        leftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

        rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
    }

    private void renderModel(float scale) {
        body.render(scale);
        rightArm.render(scale);
        leftArm.render(scale);
        rightLeg.render(scale);
        leftLeg.render(scale);
        tail1.render(scale);
        tail2.render(scale);
        tail3.render(scale);
        tail4.render(scale);
        head.render(scale);
        hair.render(scale);
        if (LKIngame.isChristmas()) {
            hat.render(scale);
        }
    }

    public ModelRenderer getRightArm() {
        return rightArm;
    }
}
