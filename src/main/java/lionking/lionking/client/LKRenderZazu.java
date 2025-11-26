package lionking.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import lionking.entity.LKEntityZazu;

@SideOnly(Side.CLIENT)
public class LKRenderZazu extends RenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:mob/zazu.png");

    public LKRenderZazu(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) {
        LKEntityZazu zazu = (LKEntityZazu) entity;
        float tickTime = zazu.ticksExisted + partialTicks;
        float wingFlap = MathHelper.sin(tickTime * 0.2F) * 0.5F; 
        float wingHeight = (float) (zazu.motionY + 0.5F); 
        return (wingFlap + 1.0F) * MathHelper.clamp_float(wingHeight, 0.0F, 1.0F);
    }
}