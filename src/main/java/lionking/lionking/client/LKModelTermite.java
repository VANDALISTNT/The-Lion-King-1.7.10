package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelTermite extends ModelBase
{
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer[] legs;
    private final ModelRenderer rightFeeler;
    private final ModelRenderer leftFeeler;

    private static final float DEG_TO_RAD = (float) Math.PI / 180F;

    public LKModelTermite()
    {
        textureWidth = 64;
        textureHeight = 32;

        body = new ModelRenderer(this, 10, 5);
        body.addBox(0F, 0F, 0F, 6, 6, 21);
        body.setRotationPoint(-3F, 17F, -5F);

        head = new ModelRenderer(this, 0, 0);
        head.addBox(0F, 0F, 0F, 8, 8, 7);
        head.setRotationPoint(-4F, 14F, -10F);

        legs = new ModelRenderer[6];
        float[][] legPositions = {
            {0F, -1F, 0.311111F, 0.311111F},    // leg1
            {0F, 2F, 0F, 0.311111F},            // leg2
            {0F, 4F, -0.6222222F, 0.311111F},   // leg3
            {0F, -1F, 2.85F, -0.311111F},       // leg4
            {0F, 2F, 3.11F, -0.311111F},        // leg5
            {0F, 4F, 3.75F, -0.311111F}         // leg6
        };

        for (int i = 0; i < 6; i++)
        {
            legs[i] = new ModelRenderer(this, 34, 0);
            legs[i].addBox(0F, 0F, i < 3 ? -2F : 0F, 13, 2, 2);
            legs[i].setRotationPoint(0F, 19F, legPositions[i][1]);
            setRotation(legs[i], 0F, legPositions[i][2], legPositions[i][3]);
        }

        rightFeeler = new ModelRenderer(this, 50, 18);
        rightFeeler.addBox(0F, 0F, -8F, 1, 1, 6);
        rightFeeler.setRotationPoint(-3F, 15F, -8F);
        setRotation(rightFeeler, 0F, 0F, -0.1F);

        leftFeeler = new ModelRenderer(this, 50, 18);
        leftFeeler.addBox(0F, 0F, -8F, 1, 1, 6);
        leftFeeler.setRotationPoint(2F, 15F, -8F);
        setRotation(leftFeeler, 0F, 0F, 0.1F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale, entity);
        body.render(scale);
        head.render(scale);
        for (ModelRenderer leg : legs)
            leg.render(scale);
        rightFeeler.render(scale);
        leftFeeler.render(scale);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        legs[0].rotateAngleZ = 0.311111F + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.25F; // leg1
        legs[1].rotateAngleZ = 0.311111F + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * -0.25F; // leg2
        legs[2].rotateAngleZ = 0.311111F + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.25F; // leg3

        legs[3].rotateAngleZ = -0.311111F + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.25F; // leg4
        legs[4].rotateAngleZ = -0.311111F + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * -0.25F; // leg5
        legs[5].rotateAngleZ = -0.311111F + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.25F; // leg6
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale, Entity entity)
    {
        // Head rotation could be added here if needed
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}