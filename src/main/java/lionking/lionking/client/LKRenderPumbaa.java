package lionking.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.client.LKModelPumbaa;

@SideOnly(Side.CLIENT)
public class LKRenderPumbaa extends RenderLiving {
    private static final ResourceLocation PUMBAA_TEXTURE = new ResourceLocation("lionking", "mob/pumbaa.png");

    public LKRenderPumbaa() {
        super(new LKModelPumbaa(), 0.7F);
        this.setRenderPassModel(new LKModelPumbaa());
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return PUMBAA_TEXTURE;
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) {
        return 0.0F;
    }
}