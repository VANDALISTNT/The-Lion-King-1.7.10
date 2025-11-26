package lionking.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import lionking.entity.LKEntityTermiteQueen;

@SideOnly(Side.CLIENT)
public class LKRenderTermiteQueen extends RenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:mob/termite_queen.png");

    public LKRenderTermiteQueen() {
        super(new LKModelTermiteQueen(), 0.5F); 
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entity, float partialTicks) {
        float scale = ((LKEntityTermiteQueen) entity).getScale(); 
        GL11.glScalef(scale, scale, scale);                       
    }
}
