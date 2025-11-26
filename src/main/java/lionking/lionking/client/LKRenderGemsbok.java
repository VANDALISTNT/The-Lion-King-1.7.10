package lionking.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKRenderGemsbok extends LKRenderLiving {
    private static final ResourceLocation GEMSBOK_TEXTURE = new ResourceLocation("lionking", "mob/gemsbok.png");

    public LKRenderGemsbok() {
        super(new LKModelGemsbok(), 0.5F);
        this.setRenderPassModel(new LKModelGemsbok());
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return GEMSBOK_TEXTURE;
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) {
        return 0.0F;
    }
}