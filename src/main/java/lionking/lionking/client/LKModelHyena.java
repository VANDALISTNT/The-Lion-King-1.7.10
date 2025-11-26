package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.entity.LKEntitySkeletalHyena; 

@SideOnly(Side.CLIENT)
public class LKModelHyena extends ModelBase {
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;
    private static final float PI = (float) Math.PI;

    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer legFrontLeft;
    private final ModelRenderer legFrontRight;
    private final ModelRenderer legBackLeft;
    private final ModelRenderer legBackRight;
    private final ModelRenderer tail;

    public LKModelHyena() {
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 6);
        head.setTextureOffset(0, 15);
        head.addBox(-3.0F, -5.0F, 1.0F, 1, 2, 2);
        head.setTextureOffset(6, 15);
        head.addBox(2.0F, -5.0F, 1.0F, 1, 2, 2);
        head.setRotationPoint(-1.0F, 13.5F, -9.0F);

        body = new ModelRenderer(this, 28, 11);
        body.addBox(-4.0F, -8.0F, -3.0F, 6, 15, 6);
        body.setTextureOffset(16, 20);
        body.addBox(-2.0F, -8.0F, 3.0F, 2, 11, 1);
        body.setRotationPoint(0.0F, 14.0F, 2.0F);
        body.rotateAngleX = PI / 2.0F;

        legFrontLeft = new ModelRenderer(this, 0, 22);
        legFrontLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2);
        legFrontLeft.setRotationPoint(-2.5F, 16.0F, 7.0F);

        legFrontRight = new ModelRenderer(this, 0, 22);
        legFrontRight.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2);
        legFrontRight.setRotationPoint(0.5F, 16.0F, 7.0F);

        legBackLeft = new ModelRenderer(this, 0, 22);
        legBackLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2);
        legBackLeft.setRotationPoint(-2.5F, 16.0F, -4.0F);

        legBackRight = new ModelRenderer(this, 0, 22);
        legBackRight.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2);
        legBackRight.setRotationPoint(0.5F, 16.0F, -4.0F);

        tail = new ModelRenderer(this, 16, 20);
        tail.addBox(-1.0F, 1.5F, -1.0F, 2, 9, 1);
        tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        tail.rotateAngleX = 1.2F;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        renderModel(entity, scale);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        tail.rotateAngleY = 0.0F;
        legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
        head.rotateAngleX = headPitch * DEG_TO_RAD;
        head.rotateAngleY = netHeadYaw * DEG_TO_RAD;
    }

    private void renderModel(Entity entity, float scale) {
        if (entity instanceof LKEntitySkeletalHyena) {
            if (!((LKEntitySkeletalHyena) entity).isHeadless()) {
                head.render(scale);
            }
        } else {
            head.render(scale);
        }
        body.render(scale);
        legFrontLeft.render(scale);
        legFrontRight.render(scale);
        legBackLeft.render(scale);
        legBackRight.render(scale);
        tail.render(scale);
    }
}