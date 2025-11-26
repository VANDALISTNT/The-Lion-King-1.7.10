package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelZebra extends ModelBase
{
    private final ModelRenderer neck;
    private final ModelRenderer body;
    private final ModelRenderer leg1;
    private final ModelRenderer leg2;
    private final ModelRenderer leg3;
    private final ModelRenderer leg4;
    private final ModelRenderer head;
    private final ModelRenderer mane;
    private final ModelRenderer tail;

    public LKModelZebra()
    {
        textureWidth = 128;
        textureHeight = 64;

        neck = new ModelRenderer(this, 98, 26);
        neck.addBox(-5F, -4F, -6F, 6, 8, 9);
        neck.setRotationPoint(2F, 3F, -6F);

        body = new ModelRenderer(this, 0, 33);
        body.addBox(-6F, -10F, -7F, 12, 10, 21);
        body.setRotationPoint(0F, 12F, -1F);

        leg1 = new ModelRenderer(this, 0, 16);
        leg1.addBox(-3F, 0F, -2F, 4, 12, 4);
        leg1.setRotationPoint(-3F, 12F, 10F);

        leg2 = new ModelRenderer(this, 0, 16);
        leg2.addBox(-1F, 0F, -2F, 4, 12, 4);
        leg2.setRotationPoint(3F, 12F, 10F);

        leg3 = new ModelRenderer(this, 0, 16);
        leg3.addBox(-3F, 0F, -3F, 4, 12, 4);
        leg3.setRotationPoint(-3F, 12F, -5F);

        leg4 = new ModelRenderer(this, 0, 16);
        leg4.addBox(-1F, 0F, -3F, 4, 12, 4);
        leg4.setRotationPoint(3F, 12F, -5F);

        head = new ModelRenderer(this, 84, 0);
        head.addBox(0F, 0F, 0F, 8, 8, 14);
        head.setRotationPoint(-4F, -3F, -19F);
        head.setTextureOffset(72, 0).addBox(1F, -3F, 10F, 1, 3, 2); // Left ear
        head.setTextureOffset(78, 0).addBox(6F, -3F, 10F, 1, 3, 2); // Right ear

        mane = new ModelRenderer(this, 92, 47);
        mane.addBox(0F, 0F, 0F, 4, 3, 14);
        mane.setRotationPoint(-2F, 5F, -1F);

        tail = new ModelRenderer(this, 0, 0);
        tail.addBox(-1F, -4F, 13.5F, 2, 12, 2);
        tail.setRotationPoint(0F, 12F, -1F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale, entity);
        
        if (isChild)
        {
            float scaleFactor = 2.0F;
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / scaleFactor, 1.0F / scaleFactor, 1.0F / scaleFactor);
            GL11.glTranslatef(0.0F, 24F * scale, 0.0F);
            renderAllParts(scale);
            GL11.glPopMatrix();
        }
        else
        {
            renderAllParts(scale);
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale, Entity entity)
    {
        neck.rotateAngleX = -1.064651F;
        head.rotateAngleX = 0.4014257F;
        mane.rotateAngleX = 2.111848F;
        
        float legSwing = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        float legSwingOpposite = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        
        leg1.rotateAngleX = legSwing;
        leg2.rotateAngleX = legSwingOpposite;
        leg3.rotateAngleX = legSwingOpposite;
        leg4.rotateAngleX = legSwing;
        
        tail.rotateAngleX = 0.296706F;
    }

    private void renderAllParts(float scale)
    {
        head.render(scale);
        neck.render(scale);
        mane.render(scale);
        body.render(scale);
        leg1.render(scale);
        leg2.render(scale);
        leg3.render(scale);
        leg4.render(scale);
        tail.render(scale);
    }
}