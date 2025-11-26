package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.entity.LKEntityFlamingo; 

@SideOnly(Side.CLIENT)
public class LKModelFlamingo extends ModelBase {
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;
    private static final float PI = (float) Math.PI;

    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer tail;
    private final ModelRenderer wingLeft;
    private final ModelRenderer wingRight;
    private final ModelRenderer legLeft;
    private final ModelRenderer legRight;

    private final ModelRenderer headChild;
    private final ModelRenderer bodyChild;
    private final ModelRenderer tailChild;
    private final ModelRenderer wingLeftChild;
    private final ModelRenderer wingRightChild;
    private final ModelRenderer legLeftChild;
    private final ModelRenderer legRightChild;

    public LKModelFlamingo() {
        head = new ModelRenderer(this, 8, 24);
        head.addBox(-2.0F, -17.0F, -2.0F, 4, 4, 4);
        head.setTextureOffset(24, 27).addBox(-1.5F, -16.0F, -5.0F, 3, 2, 3);
        head.setTextureOffset(36, 30).addBox(-1.0F, -14.0F, -5.0F, 2, 1, 1);
        head.setTextureOffset(0, 16).addBox(-1.0F, -15.0F, -1.0F, 2, 14, 2);
        head.setRotationPoint(0.0F, 5.0F, -2.0F);

        body = new ModelRenderer(this, 0, 0);
        body.addBox(-3.0F, 0.0F, -4.0F, 6, 7, 8);
        body.setRotationPoint(0.0F, 3.0F, 0.0F);

        tail = new ModelRenderer(this, 42, 23);
        tail.addBox(-2.5F, 0.0F, 0.0F, 5, 3, 6);
        tail.setRotationPoint(0.0F, 4.0F, 3.0F);
        tail.rotateAngleX = -0.25F;

        wingLeft = new ModelRenderer(this, 36, 0);
        wingLeft.addBox(-1.0F, 0.0F, -3.0F, 1, 8, 6);
        wingLeft.setRotationPoint(-3.0F, 3.0F, 0.0F);

        wingRight = new ModelRenderer(this, 50, 0);
        wingRight.addBox(0.0F, 0.0F, -3.0F, 1, 8, 6);
        wingRight.setRotationPoint(3.0F, 3.0F, 0.0F);

        legLeft = new ModelRenderer(this, 30, 0);
        legLeft.addBox(-0.5F, 0.0F, -0.5F, 1, 16, 1);
        legLeft.setTextureOffset(30, 17).addBox(-1.5F, 14.9F, -3.5F, 3, 1, 3);
        legLeft.setRotationPoint(-2.0F, 8.0F, 0.0F);

        legRight = new ModelRenderer(this, 30, 0);
        legRight.addBox(-0.5F, 0.0F, -0.5F, 1, 16, 1);
        legRight.setTextureOffset(30, 17).addBox(-1.5F, 14.9F, -3.5F, 3, 1, 3);
        legRight.setRotationPoint(2.0F, 8.0F, 0.0F);

        headChild = new ModelRenderer(this, 0, 24);
        headChild.addBox(-2.0F, -4.0F, -4.0F, 4, 4, 4);
        headChild.setTextureOffset(16, 28).addBox(-1.0F, -2.0F, -6.0F, 2, 2, 2);
        headChild.setRotationPoint(0.0F, 15.0F, -3.0F);

        bodyChild = new ModelRenderer(this, 0, 0);
        bodyChild.addBox(-3.0F, 0.0F, -4.0F, 6, 5, 7);
        bodyChild.setRotationPoint(0.0F, 14.0F, 0.0F);

        tailChild = new ModelRenderer(this, 0, 14);
        tailChild.addBox(-2.0F, 0.0F, 0.0F, 4, 2, 3);
        tailChild.setRotationPoint(0.0F, 14.5F, 3.0F);
        tailChild.rotateAngleX = -0.25F;

        wingLeftChild = new ModelRenderer(this, 40, 0);
        wingLeftChild.addBox(-1.0F, 0.0F, -3.0F, 1, 4, 5);
        wingLeftChild.setRotationPoint(-3.0F, 14.0F, 0.0F);

        wingRightChild = new ModelRenderer(this, 52, 0);
        wingRightChild.addBox(0.0F, 0.0F, -3.0F, 1, 4, 5);
        wingRightChild.setRotationPoint(3.0F, 14.0F, 0.0F);

        legLeftChild = new ModelRenderer(this, 27, 0);
        legLeftChild.addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1);
        legLeftChild.setTextureOffset(27, 7).addBox(-1.5F, 3.9F, -3.5F, 3, 1, 3);
        legLeftChild.setRotationPoint(-2.0F, 19.0F, 0.0F);

        legRightChild = new ModelRenderer(this, 27, 0);
        legRightChild.addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1);
        legRightChild.setTextureOffset(27, 7).addBox(-1.5F, 3.9F, -3.5F, 3, 1, 3);
        legRightChild.setRotationPoint(2.0F, 19.0F, 0.0F);
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

        if (isChild) {
            headChild.rotateAngleX = headPitchRad;
            headChild.rotateAngleY = headYawRad;
            legLeftChild.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.9F * limbSwingAmount;
            legRightChild.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 0.9F * limbSwingAmount;
            wingLeftChild.rotateAngleZ = ageInTicks * 0.4F;
            wingRightChild.rotateAngleZ = -ageInTicks * 0.4F;
        } else {
            head.rotateAngleX = headPitchRad;
            head.rotateAngleY = headYawRad;
            legLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.9F * limbSwingAmount;
            legRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 0.9F * limbSwingAmount;
            wingLeft.rotateAngleZ = ageInTicks * 0.4F;
            wingRight.rotateAngleZ = -ageInTicks * 0.4F;

            if (entity instanceof LKEntityFlamingo) {
                int fishingTick = ((LKEntityFlamingo) entity).fishingTick;
                if (fishingTick > 100) {
                    head.rotateAngleX = PI / 20.0F * (120 - fishingTick);
                } else if (fishingTick > 20) {
                    head.rotateAngleX = PI;
                } else if (fishingTick > 0) {
                    head.rotateAngleX = PI / 20.0F * fishingTick;
                }
            }
        }
    }

    private void renderAdult(float scale) {
        head.render(scale);
        body.render(scale);
        tail.render(scale);
        wingLeft.render(scale);
        wingRight.render(scale);
        legLeft.render(scale);
        legRight.render(scale);
    }

    private void renderChild(float scale) {
        headChild.render(scale);
        bodyChild.render(scale);
        tailChild.render(scale);
        wingLeftChild.render(scale);
        wingRightChild.render(scale);
        legLeftChild.render(scale);
        legRightChild.render(scale);
    }
}