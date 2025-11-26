package lionking.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.entity.LKEntityDikdik;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class LKRenderDikdik extends LKRenderLiving {
    private static final Map<Integer, ResourceLocation> DIKDIK_TEXTURES = new HashMap<Integer, ResourceLocation>();

    public LKRenderDikdik() {
        super(new LKModelDikdik(), 0.8F);
        this.setRenderPassModel(new LKModelDikdik());
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        if (entity instanceof LKEntityDikdik) {
            int dikdikType = ((LKEntityDikdik) entity).getDikdikType();
            return DIKDIK_TEXTURES.computeIfAbsent(dikdikType, 
                type -> new ResourceLocation("lionking", "mob/dikdik_" + type + ".png"));
        }
        return DIKDIK_TEXTURES.computeIfAbsent(0, 
            type -> new ResourceLocation("lionking", "mob/dikdik_0.png")); 
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) {
        return 0.0F;
    }
}