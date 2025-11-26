package lionking.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase; 
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityOutlandess;

@SideOnly(Side.CLIENT)
public class LKRenderOutlander extends RenderLiving
{
    private static final ResourceLocation OUTLANDER_LION_TEXTURE = new ResourceLocation("lionking", "mob/outlander.png");
    private static final ResourceLocation OUTLANDER_LIONESS_TEXTURE = new ResourceLocation("lionking", "mob/outlandess.png");

    public LKRenderOutlander()
    {
        super(new LKModelLion(true), 0.5F);
        this.setRenderPassModel(new LKModelLion(true));
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return (entity instanceof LKEntityOutlandess) ? OUTLANDER_LIONESS_TEXTURE : OUTLANDER_LION_TEXTURE;
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks)
    {
        return 0.0F;
    }
}