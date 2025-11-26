package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKModelHyenaHead extends ModelBase
{
    private final ModelRenderer head;

    public LKModelHyenaHead(boolean isEntity)
    {
        head = new ModelRenderer(this, 0, 0)
            .addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6)
            .setTextureOffset(0, 15).addBox(-3.0F, -8.0F, 0.0F, 1, 2, 2)
            .setTextureOffset(6, 15).addBox(2.0F, -8.0F, 0.0F, 1, 2, 2);

        if (isEntity)
        {
            head.setRotationPoint(0.0F, 24.0F, 0.0F);
        }
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        head.render(scale);
    }
}