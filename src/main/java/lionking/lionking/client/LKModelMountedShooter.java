package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelMountedShooter extends ModelBase {
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;

    private final ModelRenderer body;
    private final ModelRenderer leftLeg;
    private final ModelRenderer rightLeg;

    public LKModelMountedShooter() {
        body = new ModelRenderer(this, 0, 0);
        body.addBox(-1.0F, -2.0F, -8.0F, 4, 4, 16);
        body.setRotationPoint(-1.0F, 16.0F, 0.0F);

        leftLeg = new ModelRenderer(this, 0, 0);
        leftLeg.addBox(-0.5F, 0.0F, -0.5F, 1, 8, 1);
        leftLeg.setRotationPoint(-1.5F, 17.0F, -1.0F);
        leftLeg.rotateAngleZ = 25.0F * DEG_TO_RAD;

        rightLeg = new ModelRenderer(this, 0, 0);
        rightLeg.addBox(-0.5F, 0.0F, -0.5F, 1, 8, 1);
        rightLeg.setRotationPoint(1.5F, 17.0F, -1.0F);
        rightLeg.rotateAngleZ = -25.0F * DEG_TO_RAD;
        rightLeg.mirror = true;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        renderModel(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
        body.rotateAngleX = headPitch * DEG_TO_RAD;
    }

    private void renderModel(float scale) {
        body.render(scale);
        leftLeg.render(scale);
        rightLeg.render(scale);
    }
}