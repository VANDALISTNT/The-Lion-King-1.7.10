package lionking.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityLioness;

@SideOnly(Side.CLIENT)
public class LKRenderLion extends LKRenderLiving
{
    private static final ResourceLocation LION_TEXTURE = new ResourceLocation("lionking", "mob/lion.png");
    private static final ResourceLocation LIONESS_TEXTURE = new ResourceLocation("lionking", "mob/lioness.png");

    public LKRenderLion()
    {
        super(new LKModelLion(), 0.5F);
        this.setRenderPassModel(new LKModelLion());
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return (entity instanceof LKEntityLioness) ? LIONESS_TEXTURE : LION_TEXTURE;
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) {
        return 0.0F;
    }
}
