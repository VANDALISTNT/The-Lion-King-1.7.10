package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelTermiteQueen extends ModelBase
{
    private final ModelRenderer body;
    private final ModelRenderer tail1;
    private final ModelRenderer tail2;
    private final ModelRenderer head;
    private final ModelRenderer[] legs;

    private static final float DEG_TO_RAD = (float) Math.PI / 180F;

    public LKModelTermiteQueen()
    {
        textureWidth = 128;
        textureHeight = 64;

        body = new ModelRenderer(this, 0, 0);
        body.addBox(-5F, -4F, 0F, 10, 8, 26);
        body.setRotationPoint(0F, 13F, -11F);

        tail1 = new ModelRenderer(this, 0, 35);
        tail1.addBox(-4F, -3F, 0F, 8, 6, 12);
        tail1.setRotationPoint(0F, 13F, 14F);

        tail2 = new ModelRenderer(this, 42, 37);
        tail2.addBox(-3F, -2F, 0F, 6, 4, 12);
        tail2.setRotationPoint(0F, 13F, 25F);

        head = new ModelRenderer(this, 47, 11);
        head.addBox(-3F, -3F, -8F, 6, 6, 8);
        head.setRotationPoint(0F, 13F, -11F);
        head.setTextureOffset(47, 4).addBox(-2.5F, -2.5F, -13F, 1, 1, 5); // Left antenna
        head.setTextureOffset(47, 4).addBox(1.5F, -2.5F, -13F, 1, 1, 5);  // Right antenna

        legs = new ModelRenderer[6];
        float[][] legPositions = {
            {-5F, -8F}, {5F, -8F}, // leg1, leg2
            {-5F, -1F}, {5F, -1F}, // leg3, leg4
            {-5F, 10F}, {5F, 10F}  // leg5, leg6
        };

        for (int i = 0; i < 6; i++)
        {
            legs[i] = new ModelRenderer(this, 96, 6);
            legs[i].addBox(-1F, 0F, -1F, 2, 9, 2);
            legs[i].setRotationPoint(legPositions[i][0], 13F, legPositions[i][1]);
            legs[i].setTextureOffset(85, 18).addBox(-13.5F, 7.5F, -0.5F, 14, 1, 1); // Foot
        }
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale, entity);
        body.render(scale);
        tail1.render(scale);
        tail2.render(scale);
        head.render(scale);
        for (ModelRenderer leg : legs)
            leg.render(scale);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        legs[1].rotateAngleZ = -100F * DEG_TO_RAD + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.25F; // leg2
        legs[3].rotateAngleZ = -100F * DEG_TO_RAD + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * -0.25F; // leg4
        legs[5].rotateAngleZ = -100F * DEG_TO_RAD + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.25F; // leg6

        legs[0].rotateAngleZ = -80F * DEG_TO_RAD + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.25F; // leg1
        legs[2].rotateAngleZ = -80F * DEG_TO_RAD + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * -0.25F; // leg3
        legs[4].rotateAngleZ = -80F * DEG_TO_RAD + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.25F; // leg5

        tail1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.15F;
        tail2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.2F;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale, Entity entity)
    {
        head.rotateAngleX = headPitch * DEG_TO_RAD;
        head.rotateAngleY = headYaw * DEG_TO_RAD;

        legs[0].rotateAngleX = (float) Math.PI; // leg1
        legs[2].rotateAngleX = (float) Math.PI; // leg3
        legs[4].rotateAngleX = (float) Math.PI; // leg5
    }
}