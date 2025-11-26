package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.entity.LKEntitySimba; 
import lionking.common.LKIngame; 

@SideOnly(Side.CLIENT)
public class LKModelSimba extends ModelBase {
    private final ModelRenderer head;
    private final ModelRenderer mane;
    private final ModelRenderer body;
    private final ModelRenderer leg1;
    private final ModelRenderer leg2;
    private final ModelRenderer leg3;
    private final ModelRenderer leg4;
    private final ModelRenderer leg1Sitting;
    private final ModelRenderer leg2Sitting;
    private final ModelRenderer leg3Sitting;
    private final ModelRenderer leg4Sitting;
    private final ModelRenderer hat;
    private final ModelRenderer hatChild;

    private static final float DEG_TO_RAD = (float) Math.PI / 180F;
    private static final float CHILD_SCALE_FACTOR = 2.0F;

    public LKModelSimba() {
        textureWidth = 64;
        textureHeight = 96;

        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4F, -4F, -7F, 8, 8, 8);
        head.setRotationPoint(0F, 4F, -9F);
        head.setTextureOffset(52, 34).addBox(-2F, 0F, -9F, 4, 4, 2);

        mane = new ModelRenderer(this, 0, 36);
        mane.addBox(-7F, -7F, -5F, 14, 14, 9);
        mane.setRotationPoint(0F, 4F, -9F);

        body = new ModelRenderer(this, 0, 68);
        body.addBox(-6F, -10F, -7F, 12, 18, 10);
        body.setRotationPoint(0F, 5F, 2F);

        leg1 = new ModelRenderer(this, 0, 19);
        leg1.addBox(-2F, 0F, -2F, 4, 12, 4);
        leg1.setRotationPoint(-4F, 12F, 7F);

        leg2 = new ModelRenderer(this, 0, 19);
        leg2.addBox(-2F, 0F, -2F, 4, 12, 4);
        leg2.setRotationPoint(4F, 12F, 7F);

        leg3 = new ModelRenderer(this, 0, 19);
        leg3.addBox(-2F, 0F, -2F, 4, 12, 4);
        leg3.setRotationPoint(-4F, 12F, -5F);

        leg4 = new ModelRenderer(this, 0, 19);
        leg4.addBox(-2F, 0F, -2F, 4, 12, 4);
        leg4.setRotationPoint(4F, 12F, -5F);

        leg1Sitting = new ModelRenderer(this, 0, 19);
        leg1Sitting.addBox(-4.8F, 0F, -2F, 4, 12, 4, 0.4F);
        leg1Sitting.setRotationPoint(-4.5F, 19F, 9F);

        leg2Sitting = new ModelRenderer(this, 0, 19);
        leg2Sitting.addBox(0.8F, 0F, -2F, 4, 12, 4, 0.4F);
        leg2Sitting.setRotationPoint(4.5F, 19F, 9F);

        leg3Sitting = new ModelRenderer(this, 0, 19);
        leg3Sitting.addBox(-1F, 0F, -2F, 4, 12, 4);
        leg3Sitting.setRotationPoint(-4F, 12F, -5F);

        leg4Sitting = new ModelRenderer(this, 0, 19);
        leg4Sitting.addBox(-3F, 0F, -2F, 4, 12, 4);
        leg4Sitting.setRotationPoint(4F, 12F, -5F);

        hat = new ModelRenderer(this, 17, 25);
        hat.addBox(-4F, -16F, 0F, 8, 9, 1);
        hat.setRotationPoint(0F, 4F, -9F);

        hatChild = new ModelRenderer(this, 17, 25);
        hatChild.addBox(-4F, -13F, -2F, 8, 9, 1);
        hatChild.setRotationPoint(0F, 4F, -9F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale, entity);
        LKEntitySimba simba = (LKEntitySimba) entity;
        boolean isSitting = simba.isSitting();

        if (isChild) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0F, 8F * scale, 4F * scale);
            head.render(scale);
            if (LKIngame.isChristmas())
                hatChild.render(scale);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glScalef(1F / CHILD_SCALE_FACTOR, 1F / CHILD_SCALE_FACTOR, 1F / CHILD_SCALE_FACTOR);
            GL11.glTranslatef(0F, 24F * scale, 0F);
            body.render(scale);
            renderLegs(isSitting, scale);
            GL11.glPopMatrix();
        } else {
            head.render(scale);
            mane.render(scale);
            if (LKIngame.isChristmas())
                hat.render(scale);
            body.render(scale);
            renderLegs(isSitting, scale);
        }
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        LKEntitySimba simba = (LKEntitySimba) entity;
        if (simba.isSitting()) {
            body.rotateAngleX = 0.8F;
            body.rotationPointY = 12F;
            head.rotationPointY = 3F;
            mane.rotationPointY = 3F;
            hat.rotationPointY = 3F;
            hatChild.rotationPointY = 3F;
        } else {
            body.rotateAngleX = (float) Math.PI / 2F;
            body.rotationPointY = 5F;
            head.rotationPointY = 4F;
            mane.rotationPointY = 4F;
            hat.rotationPointY = 4F;
            hatChild.rotationPointY = 4F;
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale, Entity entity) {
        float headX = headPitch * DEG_TO_RAD;
        float headY = headYaw * DEG_TO_RAD;

        head.rotateAngleX = headX;
        head.rotateAngleY = headY;
        mane.rotateAngleX = headX;
        mane.rotateAngleY = headY;
        hat.rotateAngleX = headX;
        hat.rotateAngleY = headY;
        hatChild.rotateAngleX = headX;
        hatChild.rotateAngleY = headY;

        leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        leg1Sitting.rotateAngleX = -((float) Math.PI / 2F) + 0.3F;
        leg2Sitting.rotateAngleX = -((float) Math.PI / 2F) + 0.3F;
        leg3Sitting.rotateAngleX = -0.15F;
        leg4Sitting.rotateAngleX = -0.15F;
    }

    private void renderLegs(boolean isSitting, float scale) {
        if (isSitting) {
            leg1Sitting.render(scale);
            leg2Sitting.render(scale);
            leg3Sitting.render(scale);
            leg4Sitting.render(scale);
        } else {
            leg1.render(scale);
            leg2.render(scale);
            leg3.render(scale);
            leg4.render(scale);
        }
    }
}
