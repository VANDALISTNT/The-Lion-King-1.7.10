package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelDikdik extends ModelBase {
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;
    private static final float PI = (float) Math.PI;

    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer legFrontLeft;
    private final ModelRenderer legFrontRight;
    private final ModelRenderer legBackLeft;
    private final ModelRenderer legBackRight;

    public LKModelDikdik() {
        head = new ModelRenderer(this, 42, 23);
        head.addBox(-2.0F, -9.0F, -3.0F, 4, 4, 5);
        head.setTextureOffset(18, 28).addBox(-1.0F, -7.3F, -5.0F, 2, 2, 2);
        head.setTextureOffset(0, 27).addBox(-2.8F, -11.0F, 0.5F, 1, 3, 2);
        head.setTextureOffset(8, 27).addBox(1.8F, -11.0F, 0.5F, 1, 3, 2);
        head.setTextureOffset(0, 21).addBox(-1.5F, -11.0F, 0.0F, 1, 2, 1);
        head.setTextureOffset(0, 21).addBox(0.5F, -11.0F, 0.0F, 1, 2, 1);
        head.setTextureOffset(28, 22).addBox(-1.5F, -8.0F, -2.0F, 3, 7, 3);
        head.setRotationPoint(0.0F, 11.0F, -4.5F);

        body = new ModelRenderer(this, 0, 0);
        body.addBox(-3.0F, 0.0F, 0.0F, 6, 6, 14);
        body.setRotationPoint(0.0F, 9.0F, -7.0F);

        legFrontLeft = new ModelRenderer(this, 56, 0);
        legFrontLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2);
        legFrontLeft.setRotationPoint(-1.7F, 14.0F, 5.0F);

        legFrontRight = new ModelRenderer(this, 56, 0);
        legFrontRight.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2);
        legFrontRight.setRotationPoint(1.7F, 14.0F, 5.0F);

        legBackLeft = new ModelRenderer(this, 56, 0);
        legBackLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2);
        legBackLeft.setRotationPoint(-1.7F, 14.0F, -5.0F);

        legBackRight = new ModelRenderer(this, 56, 0);
        legBackRight.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2);
        legBackRight.setRotationPoint(1.7F, 14.0F, -5.0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        renderModel(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
        head.rotateAngleX = headPitch * DEG_TO_RAD;
        head.rotateAngleY = netHeadYaw * DEG_TO_RAD;
        legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    private void renderModel(float scale) {
        head.render(scale);
        body.render(scale);
        legFrontLeft.render(scale);
        legFrontRight.render(scale);
        legBackLeft.render(scale);
        legBackRight.render(scale);
    }
}