package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.common.LKIngame; 

@SideOnly(Side.CLIENT)
public class LKModelTimon extends ModelBase {
    private final ModelRenderer head;
    private final ModelRenderer hat;
    private final ModelRenderer body;
    private final ModelRenderer rightArm;
    private final ModelRenderer leftArm;
    private final ModelRenderer rightLeg;
    private final ModelRenderer leftLeg;
    private final ModelRenderer tail;
    private final ModelRenderer leftEar;
    private final ModelRenderer rightEar;

    private static final float DEG_TO_RAD = (float) Math.PI / 180F;

    public LKModelTimon() {
        textureWidth = 64;
        textureHeight = 64;

        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4F, -8F, -4F, 8, 7, 6);
        head.setRotationPoint(0F, 1F, 1F);
        head.setTextureOffset(46, 7).addBox(-3.5F, -9F, -2F, 7, 1, 2);
        head.setTextureOffset(0, 0).addBox(-1F, -5F, -5F, 2, 2, 1);

        hat = new ModelRenderer(this, 0, 31);
        hat.addBox(-4F, -17F, -1F, 8, 9, 1);
        hat.setRotationPoint(0F, 1F, 1F);

        body = new ModelRenderer(this, 40, 12);
        body.addBox(-4F, 0F, -2F, 8, 15, 4);
        body.setRotationPoint(0F, 0F, 0F);

        rightArm = new ModelRenderer(this, 20, 13);
        rightArm.addBox(-3F, -2F, -2F, 3, 12, 3);
        rightArm.setRotationPoint(-4F, 3F, 0.5F);

        leftArm = new ModelRenderer(this, 20, 13);
        leftArm.addBox(-1F, -2F, -2F, 3, 12, 3);
        leftArm.setRotationPoint(5F, 3F, 0.5F);

        rightLeg = new ModelRenderer(this, 0, 13);
        rightLeg.addBox(-2F, 0F, -2F, 3, 9, 3);
        rightLeg.setRotationPoint(-1.7F, 15F, 0.5F);
        rightLeg.setTextureOffset(44, 0).addBox(-2.7F, 8F, -3.9F, 4, 1, 6);

        leftLeg = new ModelRenderer(this, 0, 13);
        leftLeg.addBox(-2F, 0F, -2F, 3, 9, 3);
        leftLeg.setRotationPoint(2.7F, 15F, 0.5F);
        leftLeg.setTextureOffset(44, 0).addBox(-2.3F, 8F, -3.9F, 4, 1, 6);

        tail = new ModelRenderer(this, 0, 13);
        tail.addBox(-1F, 0F, 0F, 2, 2, 16);
        tail.setRotationPoint(0F, 9F, 1F);

        leftEar = new ModelRenderer(this, 36, 0);
        leftEar.addBox(-7F, -5F, -1.5F, 3, 5, 1);
        leftEar.setRotationPoint(0F, 1F, 1F);

        rightEar = new ModelRenderer(this, 36, 0);
        rightEar.addBox(4F, -5F, -1.5F, 3, 5, 1);
        rightEar.setRotationPoint(0F, 1F, 1F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale, entity);
        head.render(scale);
        if (LKIngame.isChristmas())
            hat.render(scale);
        body.render(scale);
        rightArm.render(scale);
        leftArm.render(scale);
        rightLeg.render(scale);
        leftLeg.render(scale);
        tail.render(scale);
        leftEar.render(scale);
        rightEar.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale, Entity entity) {
        float headY = headYaw * DEG_TO_RAD;
        float headX = headPitch * DEG_TO_RAD;

        head.rotateAngleY = headY;
        head.rotateAngleX = headX;
        hat.rotateAngleY = headY;
        hat.rotateAngleX = headX;

        rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        tail.rotateAngleX = -0.908F + headX;

        leftEar.rotateAngleZ = 0.38397F;
        leftEar.rotateAngleY = headY;
        leftEar.rotateAngleX = headX;

        rightEar.rotateAngleZ = -0.38397F;
        rightEar.rotateAngleY = headY;
        rightEar.rotateAngleX = headX;
    }
}
