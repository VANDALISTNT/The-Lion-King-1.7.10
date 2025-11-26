package lionking.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.entity.LKEntityFlamingo;

@SideOnly(Side.CLIENT)
public class LKRenderFlamingo extends RenderLiving {
    private static final ResourceLocation FLAMINGO_TEXTURE = new ResourceLocation("lionking", "mob/flamingo.png");
    private static final ResourceLocation CHICK_TEXTURE = new ResourceLocation("lionking", "mob/flamingo_chick.png");

    public LKRenderFlamingo(LKModelFlamingo model) {
        super(model, 0.5F);
        this.setRenderPassModel(model);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        if (entity instanceof LKEntityFlamingo) {
            return ((LKEntityFlamingo) entity).isChild() ? CHICK_TEXTURE : FLAMINGO_TEXTURE;
        }
        return FLAMINGO_TEXTURE; 
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) {
        if (entity instanceof LKEntityFlamingo) {
            LKEntityFlamingo flamingo = (LKEntityFlamingo) entity;
            float wingAnglePrev = flamingo.field_756_e + (flamingo.field_752_b - flamingo.field_756_e) * partialTicks;
            float wingAngleDest = flamingo.field_757_d + (flamingo.destPos - flamingo.field_757_d) * partialTicks;
            return (MathHelper.sin(wingAnglePrev) + 1.0F) * wingAngleDest;
        }
        return 0.0F; 
    }
}