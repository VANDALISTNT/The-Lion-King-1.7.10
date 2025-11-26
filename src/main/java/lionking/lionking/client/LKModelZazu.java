package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelZazu extends ModelBase
{
    private final ModelRenderer head;
    private final ModelRenderer headwear;
    private final ModelRenderer body;
    private final ModelRenderer rightLeg;
    private final ModelRenderer leftLeg;
    private final ModelRenderer rightWing;
    private final ModelRenderer leftWing;
    private final ModelRenderer bill;
    private final ModelRenderer tail;

    private static final float DEG_TO_RAD = (float) Math.PI / 180F;

    public LKModelZazu()
    {
        textureWidth = 64;
        textureHeight = 32;

        head = new ModelRenderer(this, 0, 0);
        head.addBox(-2F, -6F, -2F, 4, 5, 3);
        head.setRotationPoint(0F, 16F, -4F);

        headwear = new ModelRenderer(this, 14, 0);
        headwear.addBox(-2F, -6.25F, -0.75F, 4, 5, 3, 0.25F);
        headwear.setRotationPoint(0F, 16F, -4F);

        bill = new ModelRenderer(this, 46, 25);
        bill.addBox(-2F, -4F, -7F, 4, 2, 5);
        bill.setRotationPoint(0F, 15F, -4F);

        body = new ModelRenderer(this, 0, 10);
        body.addBox(-3F, -4F, -3F, 5, 7, 5);
        body.setRotationPoint(0.5F, 16F, 0.5F);

        rightLeg = new ModelRenderer(this, 26, 0);
        rightLeg.addBox(-1F, 0F, -3F, 3, 5, 3);
        rightLeg.setRotationPoint(-2F, 19F, 1F);

        leftLeg = new ModelRenderer(this, 26, 0);
        leftLeg.addBox(-1F, 0F, -3F, 3, 5, 3);
        leftLeg.setRotationPoint(1F, 19F, 1F);

        rightWing = new ModelRenderer(this, 24, 13);
        rightWing.addBox(-0.5F, 0F, -3F, 1, 4, 6);
        rightWing.setRotationPoint(-3F, 15F, 0F);

        leftWing = new ModelRenderer(this, 24, 13);
        leftWing.addBox(-0.5F, 0F, -3F, 1, 4, 6);
        leftWing.setRotationPoint(3F, 15F, 0F);

        tail = new ModelRenderer(this, 44, 5);
        tail.addBox(-2F, 3F, 0F, 4, 9, 1);
        tail.setRotationPoint(0F, 16F, 0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale, entity);

        if (isChild)
        {
            float scaleFactor = 2.0F;
            GL11.glPushMatrix();
            GL11.glTranslatef(0F, 5F * scale, 2F * scale);
            head.render(scale);
            bill.render(scale);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glScalef(1F / scaleFactor, 1F / scaleFactor, 1F / scaleFactor);
            GL11.glTranslatef(0F, 24F * scale, 0F);
            body.render(scale);
            rightLeg.render(scale);
            leftLeg.render(scale);
            rightWing.render(scale);
            leftWing.render(scale);
            tail.render(scale);
            GL11.glPopMatrix();
        }
        else
        {
            headwear.render(scale);
            head.render(scale);
            bill.render(scale);
            body.render(scale);
            rightLeg.render(scale);
            leftLeg.render(scale);
            rightWing.render(scale);
            leftWing.render(scale);
            tail.render(scale);
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale, Entity entity)
    {
        float headX = -headPitch * DEG_TO_RAD;
        float headY = headYaw * DEG_TO_RAD;

        head.rotateAngleX = headX;
        head.rotateAngleY = headY;
        headwear.rotateAngleX = headX + 0.2F;
        headwear.rotateAngleY = headY;
        bill.rotateAngleX = headX;
        bill.rotateAngleY = headY;

        body.rotateAngleX = (float) Math.PI / 2F;

        rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        rightWing.rotateAngleZ = ageInTicks * 0.8F;
        leftWing.rotateAngleZ = -ageInTicks * 0.8F;

        tail.rotateAngleX = 2.0F;
    }
}