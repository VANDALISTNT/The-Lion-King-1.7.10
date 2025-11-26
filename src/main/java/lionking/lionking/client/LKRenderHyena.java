package lionking.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityHyena;
import lionking.entity.LKEntitySkeletalHyena;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class LKRenderHyena extends LKRenderLiving {
    private static final Map<Integer, ResourceLocation> HYENA_TEXTURES = new HashMap<Integer, ResourceLocation>();
    public static final ResourceLocation SKELETON_TEXTURE = new ResourceLocation("lionking", "textures/mob/hyena_skeleton.png");

    public LKRenderHyena() {
        super(new LKModelHyena(), 0.5F);
        this.setRenderPassModel(new LKModelHyena());
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        if (entity instanceof LKEntitySkeletalHyena) {
            return SKELETON_TEXTURE;
        }

        LKEntityHyena hyena = (LKEntityHyena) entity;
        int hyenaType = hyena.getHyenaType();
        return HYENA_TEXTURES.computeIfAbsent(hyenaType, type -> 
            new ResourceLocation("lionking", "textures/mob/hyena_" + type + ".png"));
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) {
        return 0.0F;
    }
}