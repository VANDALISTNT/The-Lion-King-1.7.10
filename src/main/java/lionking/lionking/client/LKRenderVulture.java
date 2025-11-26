package lionking.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import lionking.entity.LKEntityVulture;

@SideOnly(Side.CLIENT)
public class LKRenderVulture extends RenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:mob/vulture.png");

    public LKRenderVulture(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) {
        LKEntityVulture vulture = (LKEntityVulture) entity;
        float tickTime = vulture.ticksExisted + partialTicks;
        float wingFlap = MathHelper.sin(tickTime * 0.15F) * 0.4F; 
        float wingHeight = (float) (vulture.motionY + 0.5F); 
        return (wingFlap + 1.0F) * MathHelper.clamp_float(wingHeight, 0.0F, 1.0F);
    }
}