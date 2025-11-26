package lionking.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LKRenderCrocodile extends LKRenderLiving {
    private static final ResourceLocation CROCODILE_TEXTURE = new ResourceLocation("lionking", "mob/crocodile.png");

    public LKRenderCrocodile() {
        super(new LKModelCrocodile(), 0.75F);
        this.setRenderPassModel(new LKModelCrocodile());
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return CROCODILE_TEXTURE;
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) {
        return 0.0F;
    }
}