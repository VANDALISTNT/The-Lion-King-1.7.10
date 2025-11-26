package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelGiraffe extends ModelBase {
    private static final int TEXTURE_WIDTH = 128;
    private static final int TEXTURE_HEIGHT = 64;
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;
    private static final float PI = (float) Math.PI;

    private final ModelRenderer body;
    private final ModelRenderer neck;
    private final ModelRenderer head;
    private final ModelRenderer tail;
    private final ModelRenderer legFrontLeft;
    private final ModelRenderer legFrontRight;
    private final ModelRenderer legBackLeft;
    private final ModelRenderer legBackRight;
    public final ModelRenderer tie;

    public LKModelGiraffe(float scaleFactor) {
        textureWidth = TEXTURE_WIDTH;
        textureHeight = TEXTURE_HEIGHT;

        body = new ModelRenderer(this, 0, 0);
        body.addBox(-6.0F, -8.0F, -13.0F, 12, 16, 26, scaleFactor);
        body.setRotationPoint(0.0F, -11.0F, 0.0F);

        neck = new ModelRenderer(this, 0, 44);
        neck.addBox(-4.5F, -13.0F, -4.5F, 9, 11, 9, scaleFactor);
        neck.setTextureOffset(78, 0);
        neck.addBox(-3.0F, -37.0F, -3.0F, 6, 40, 6, scaleFactor);
        neck.setRotationPoint(0.0F, -14.0F, -7.0F);

        tie = new ModelRenderer(this, 78, 0);
        tie.addBox(-3.0F, -37.0F, -3.0F, 6, 40, 6, 0.5F);
        tie.setRotationPoint(0.0F, -14.0F, -7.0F);
        tie.showModel = false;

        head = new ModelRenderer(this, 96, 48);
        head.addBox(-3.0F, -43.0F, -6.0F, 6, 6, 10, scaleFactor);
        head.setTextureOffset(10, 0);
        head.addBox(-4.0F, -45.0F, 1.5F, 1, 3, 2, scaleFactor);
        head.setTextureOffset(17, 0);
        head.addBox(3.0F, -45.0F, 1.5F, 1, 3, 2, scaleFactor);
        head.setTextureOffset(0, 0);
        head.addBox(-2.5F, -47.0F, 0.0F, 1, 4, 1, scaleFactor);
        head.setTextureOffset(5, 0);
        head.addBox(1.5F, -47.0F, 0.0F, 1, 4, 1, scaleFactor);
        head.setTextureOffset(76, 56);
        head.addBox(-2.0F, -41.0F, -11.0F, 4, 3, 5, scaleFactor);
        head.setRotationPoint(0.0F, -14.0F, -7.0F);

        tail = new ModelRenderer(this, 104, 0);
        tail.addBox(-0.5F, 0.0F, 0.0F, 1, 24, 1, scaleFactor);
        tail.setRotationPoint(0.0F, -12.0F, 13.0F);

        legFrontLeft = new ModelRenderer(this, 112, 0);
        legFrontLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 27, 4, scaleFactor);
        legFrontLeft.setRotationPoint(-3.9F, -3.0F, 8.0F);

        legFrontRight = new ModelRenderer(this, 112, 0);
        legFrontRight.addBox(-2.0F, 0.0F, -2.0F, 4, 27, 4, scaleFactor);
        legFrontRight.setRotationPoint(3.9F, -3.0F, 8.0F);
        legFrontRight.mirror = true;

        legBackLeft = new ModelRenderer(this, 112, 0);
        legBackLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 27, 4, scaleFactor);
        legBackLeft.setRotationPoint(-3.9F, -3.0F, -7.0F);

        legBackRight = new ModelRenderer(this, 112, 0);
        legBackRight.addBox(-2.0F, 0.0F, -2.0F, 4, 27, 4, scaleFactor);
        legBackRight.setRotationPoint(3.9F, -3.0F, -7.0F);
        legBackRight.mirror = true;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        if (isChild) {
            renderChild(scale);
        } else {
            renderAdult(entity, scale);
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
        if (entity != null && entity.riddenByEntity instanceof EntityPlayer) {
            neck.rotateAngleX = PI / 2.0F;
            neck.rotateAngleY = 0.0F;
            tie.rotateAngleX = PI / 2.0F;
            tie.rotateAngleY = 0.0F;
            head.rotateAngleX = 0.0F;
            head.rotateAngleY = 0.0F;
            head.setRotationPoint(0.0F, 25.0F, -48.0F);
        } else {
            setHeadAndNeckRotationAngles(netHeadYaw, headPitch);
            head.setRotationPoint(0.0F, -14.0F, -7.0F);
        }

        legFrontLeft.rotateAngleX = 0.5F * MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        legFrontRight.rotateAngleX = 0.5F * MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackLeft.rotateAngleX = 0.5F * MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
        legBackRight.rotateAngleX = 0.5F * MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        tail.rotateAngleZ = 0.2F * MathHelper.cos(limbSwing * 0.6662F + PI) * 1.4F * limbSwingAmount;
    }

    private void setHeadAndNeckRotationAngles(float netHeadYaw, float headPitch) {
        float baseAngleX = 10.0F * DEG_TO_RAD;
        float headYawRad = netHeadYaw * DEG_TO_RAD;
        float headPitchRad = headPitch * DEG_TO_RAD;

        neck.rotateAngleX = baseAngleX + headPitchRad;
        neck.rotateAngleY = headYawRad;
        head.rotateAngleX = baseAngleX + headPitchRad;
        head.rotateAngleY = headYawRad;
        tie.rotateAngleX = baseAngleX + headPitchRad;
        tie.rotateAngleY = headYawRad;
    }

    private void renderAdult(Entity entity, float scale) {
        head.render(scale);
        body.render(scale);
        neck.render(scale);
        tie.render(scale);
        legFrontLeft.render(scale);
        legFrontRight.render(scale);
        legBackLeft.render(scale);
        legBackRight.render(scale);
        tail.render(scale);
    }

    private void renderChild(float scale) {
        float childScale = 0.5F;
        float childOffsetY = 8.0F * scale;
        float childOffsetZ = 4.0F * scale;

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, childOffsetY, childOffsetZ);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glScalef(childScale, childScale, childScale);
        GL11.glTranslatef(0.0F, 24.0F * scale, 0.0F);
        head.render(scale);
        body.render(scale);
        neck.render(scale);
        tie.render(scale);
        legFrontLeft.render(scale);
        legFrontRight.render(scale);
        legBackLeft.render(scale);
        legBackRight.render(scale);
        tail.render(scale);
        GL11.glPopMatrix();
    }
}