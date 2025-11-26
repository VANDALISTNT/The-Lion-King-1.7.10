package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelGemsbok extends ModelBase {
    private static final int TEXTURE_WIDTH = 128;
    private static final int TEXTURE_HEIGHT = 64;
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;
    private static final float PI = (float) Math.PI;

    private final ModelRenderer head;
    private final ModelRenderer tail;
    private final ModelRenderer earLeft;
    private final ModelRenderer earRight;
    private final ModelRenderer neck;
    private final ModelRenderer body;
    private final ModelRenderer legFrontLeft;
    private final ModelRenderer legFrontRight;
    private final ModelRenderer legBackLeft;
    private final ModelRenderer legBackRight;
    private final ModelRenderer leftHorn;
    private final ModelRenderer rightHorn;

    private final float childOffsetY = 8.0F;
    private final float childOffsetZ = 4.0F;

    public LKModelGemsbok() {
        textureWidth = TEXTURE_WIDTH;
        textureHeight = TEXTURE_HEIGHT;

        head = new ModelRenderer(this, 28, 0);
        head.addBox(-3.0F, -10.0F, -6.0F, 6, 7, 12);
        head.setRotationPoint(0.0F, 4.0F, -9.0F);

        tail = new ModelRenderer(this, 0, 0);
        tail.addBox(0.0F, 0.0F, 0.0F, 2, 12, 2);
        tail.setRotationPoint(-1.0F, 3.0F, 11.0F);
        tail.rotateAngleX = 0.2967059F;

        earLeft = new ModelRenderer(this, 28, 19);
        earLeft.addBox(-3.8F, -12.0F, 3.0F, 1, 3, 2);
        earLeft.setRotationPoint(0.0F, 4.0F, -9.0F);

        earRight = new ModelRenderer(this, 34, 19);
        earRight.addBox(2.8F, -12.0F, 3.0F, 1, 3, 2);
        earRight.setRotationPoint(0.0F, 4.0F, -9.0F);

        neck = new ModelRenderer(this, 0, 14);
        neck.addBox(-2.5F, -6.0F, -5.0F, 5, 8, 9);
        neck.setRotationPoint(0.0F, 4.0F, -9.0F);
        neck.rotateAngleX = -1.06465F;

        body = new ModelRenderer(this, 0, 31);
        body.addBox(-7.0F, -10.0F, -7.0F, 13, 10, 23);
        body.setRotationPoint(0.5F, 12.0F, -3.0F);

        legFrontLeft = new ModelRenderer(this, 0, 38);
        legFrontLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        legFrontLeft.setRotationPoint(-4.0F, 12.0F, 10.0F);

        legFrontRight = new ModelRenderer(this, 0, 38);
        legFrontRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        legFrontRight.setRotationPoint(4.0F, 12.0F, 10.0F);

        legBackLeft = new ModelRenderer(this, 0, 38);
        legBackLeft.addBox(-2.0F, 0.0F, -3.0F, 4, 12, 4);
        legBackLeft.setRotationPoint(-4.0F, 12.0F, -7.0F);

        legBackRight = new ModelRenderer(this, 0, 38);
        legBackRight.addBox(-2.0F, 0.0F, -3.0F, 4, 12, 4);
        legBackRight.setRotationPoint(4.0F, 12.0F, -7.0F);

        leftHorn = new ModelRenderer(this, 0, 0);
        leftHorn.addBox(-2.8F, -9.5F, 5.8F, 1, 1, 13);
        leftHorn.setRotationPoint(0.0F, 4.0F, -9.0F);

        rightHorn = new ModelRenderer(this, 0, 0);
        rightHorn.addBox(1.8F, -9.5F, 5.8F, 1, 1, 13);
        rightHorn.setRotationPoint(0.0F, 4.0F, -9.0F);
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
        float headPitchRad = headPitch * DEG_TO_RAD + 0.4014257F; 
        float headYawRad = netHeadYaw * DEG_TO_RAD;

        head.rotateAngleX = headPitchRad;
        head.rotateAngleY = headYawRad;
        neck.rotateAngleY = headYawRad * 0.7F;
        rightHorn.rotateAngleX = headPitchRad;
        rightHorn.rotateAngleY = headYawRad;
        leftHorn.rotateAngleX = headPitchRad;
        leftHorn.rotateAngleY = headYawRad;
        earLeft.rotateAngleX = headPitchRad;
        earLeft.rotateAngleY = headYawRad;
        earRight.rotateAngleX = headPitchRad;
        earRight.rotateAngleY = headYawRad;

        legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    private void renderAdult(float scale) {
        head.render(scale);
        neck.render(scale);
        leftHorn.render(scale);
        rightHorn.render(scale);
        earLeft.render(scale);
        earRight.render(scale);
        body.render(scale);
        legFrontLeft.render(scale);
        legFrontRight.render(scale);
        legBackLeft.render(scale);
        legBackRight.render(scale);
        tail.render(scale);
    }

    private void renderChild(float scale) {
        float childScale = 0.5F; 

        GL11.glPushMatrix();
        GL11.glScalef(childScale, childScale, childScale);
        GL11.glTranslatef(0.0F, 24.0F * scale, 0.0F); 
        head.render(scale);
        neck.render(scale);
        leftHorn.render(scale);  
        rightHorn.render(scale); 
        earLeft.render(scale);
        earRight.render(scale);
        body.render(scale);
        legFrontLeft.render(scale);
        legFrontRight.render(scale);
        legBackLeft.render(scale);
        legBackRight.render(scale);
        tail.render(scale);
        GL11.glPopMatrix();
    }
}