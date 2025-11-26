package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelGrindingStick extends ModelBase {
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;

    private final ModelRenderer stick;

    public LKModelGrindingStick() {
        stick = new ModelRenderer(this, 0, 0);
        stick.addBox(-0.5F, -11.0F, -0.5F, 1, 12, 1);
        stick.setRotationPoint(0.0F, 18.0F, 0.0F);
        stick.rotateAngleX = -35.0F * DEG_TO_RAD;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        stick.render(scale);
    }
}