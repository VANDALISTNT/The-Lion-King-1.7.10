package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelVulture extends ModelBase
{
    private final ModelRenderer body;
    private final ModelRenderer tail;
    private final ModelRenderer legLeft;
    private final ModelRenderer legRight;
    private final ModelRenderer neck;
    private final ModelRenderer head;
    private final ModelRenderer wingLeft;
    private final ModelRenderer wingRight;

    private static final float DEG_TO_RAD = (float) Math.PI / 180F;

    public LKModelVulture()
    {
        textureWidth = 64;
        textureHeight = 64;

        body = new ModelRenderer(this, 0, 46);
        body.addBox(0F, 0F, 0F, 8, 10, 8);
        body.setRotationPoint(-4F, 8F, -4F);

        tail = new ModelRenderer(this, 44, 50);
        tail.addBox(0F, 0F, 0F, 6, 10, 4);
        tail.setRotationPoint(-3F, 13F, 4F);

        legLeft = new ModelRenderer(this, 40, 0);
        legLeft.addBox(0F, 0F, 0F, 2, 12, 2);
        legLeft.setRotationPoint(1F, 12F, 2F);

        legRight = new ModelRenderer(this, 40, 0);
        legRight.addBox(0F, 0F, 0F, 2, 12, 2);
        legRight.setRotationPoint(-3F, 12F, 2F);
        legRight.mirror = true;

        neck = new ModelRenderer(this, 0, 14);
        neck.addBox(0F, 0F, 0F, 4, 5, 4);
        neck.setRotationPoint(-2F, 3F, -4F);

        head = new ModelRenderer(this, 0, 0);
        head.addBox(-3F, -2F, -3F, 6, 6, 6);
        head.setRotationPoint(0.5F, 0F, -2.5F);
        head.setTextureOffset(28, 0).addBox(-1.5F, 1.5F, -6.9F, 3, 2, 2); // Eyes
        head.setTextureOffset(46, 29).addBox(-2F, 0.5F, -7F, 4, 2, 5);    // Beak

        wingLeft = new ModelRenderer(this, 0, 26);
        wingLeft.addBox(0F, 0F, 0F, 2, 10, 7);
        wingLeft.setRotationPoint(4F, 5F, -2F);

        wingRight = new ModelRenderer(this, 0, 26);
        wingRight.addBox(-2F, 0F, 0F, 2, 10, 7);
        wingRight.setRotationPoint(-4F, 5F, -2F);
        wingRight.mirror = true;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale, entity);
        body.render(scale);
        tail.render(scale);
        legLeft.render(scale);
        legRight.render(scale);
        neck.render(scale);
        head.render(scale);
        wingLeft.render(scale);
        wingRight.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale, Entity entity)
    {
        head.rotateAngleX = -headPitch * DEG_TO_RAD;
        head.rotateAngleY = headYaw * DEG_TO_RAD;

        body.rotateAngleX = 0.698131F; // ~40 degrees
        tail.rotateAngleX = body.rotateAngleX;
        neck.rotateAngleX = 0.4364323F; // ~25 degrees

        legRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        legLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        wingRight.rotateAngleZ = ageInTicks;
        wingLeft.rotateAngleZ = -ageInTicks;
    }
}