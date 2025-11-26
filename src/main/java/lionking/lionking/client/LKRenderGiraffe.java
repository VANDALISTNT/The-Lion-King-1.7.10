package lionking.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityGiraffe;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class LKRenderGiraffe extends RenderLiving
{
    private static final ResourceLocation GIRAFFE_TEXTURE = new ResourceLocation("lionking", "mob/giraffe/giraffe.png");
    private static final ResourceLocation SADDLE_TEXTURE = new ResourceLocation("lionking", "mob/giraffe/saddle.png");
    private static final String[] TIE_VARIANTS = {"tie", "tie_white", "tie_blue", "tie_yellow", "tie_red", "tie_purple", "tie_green", "tie_black"};
    private static final Map<String, ResourceLocation> TIE_TEXTURES = new HashMap<String, ResourceLocation>();

    public LKRenderGiraffe(LKModelGiraffe mainModel, LKModelGiraffe saddleModel)
    {
        super(mainModel, 0.5F);
        this.setRenderPassModel(saddleModel);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return GIRAFFE_TEXTURE;
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase entity, int pass, float partialTicks)
    {
        LKEntityGiraffe giraffe = (LKEntityGiraffe) entity;

        if (pass == 0 && giraffe.getSaddled())
        {
            bindTexture(SADDLE_TEXTURE);
            return 1;
        }
        
        if (pass == 1 && giraffe.getTie() > -1)
        {
            int tieIndex = giraffe.getTie();
            if (tieIndex >= 0 && tieIndex < TIE_VARIANTS.length)
            {
                String tieName = TIE_VARIANTS[tieIndex];
                ResourceLocation tieTexture = TIE_TEXTURES.computeIfAbsent(tieName, 
                    name -> new ResourceLocation("lionking", "mob/giraffe/" + name + ".png"));
                
                bindTexture(tieTexture);
                ((LKModelGiraffe) renderPassModel).tie.showModel = true;
                return 1;
            }
        }
        
        return -1;
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTicks)
    {
        return 0.0F;
    }
}
