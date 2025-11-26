package lionking.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityRhino;

@SideOnly(Side.CLIENT)
public class LKRenderRhino extends RenderLiving {
    private static final ResourceLocation RHINO_TEXTURE = new ResourceLocation("lionking", "mob/rhino.png");

    public LKRenderRhino() {
        super(new LKModelRhino(), 1.2F);
        this.setRenderPassModel(new LKModelRhino());
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return RHINO_TEXTURE;
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) {
        return 0.0F;
    }
}