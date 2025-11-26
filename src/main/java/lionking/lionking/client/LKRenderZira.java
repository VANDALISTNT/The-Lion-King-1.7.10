package lionking.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import lionking.common.LKLevelData; 
import lionking.quest.LKQuestBase; 
import lionking.entity.LKEntityZira; 
import lionking.client.LKTickHandlerClient; 

@SideOnly(Side.CLIENT)
public class LKRenderZira extends RenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:mob/zira.png");

    public LKRenderZira() {
        super(new LKModelLion(true), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        super.doRender(entity, x, y, z, yaw, partialTicks);
        if (renderName()) {
            func_147906_a(entity, StatCollector.translateToLocal("entity.lionking.zira.name"), x, y, z, 64);
        }

        if (LKLevelData.ziraStage > 25) {
            LKTickHandlerClient.ziraBoss = (LKEntityZira) entity;
        }
    }

    private boolean renderName() {
        EntityLivingBase player = renderManager.livingPlayer;
        if (player != null && player instanceof EntityPlayer && ((EntityPlayer) player).capabilities.isCreativeMode) {
            return true;
        } else if (LKQuestBase.outlandsQuest.getQuestStage() > 1) {
            return true;
        } else if (LKQuestBase.outlandsQuest.getQuestStage() == 1 && !LKQuestBase.outlandsQuest.isDelayed()) {
            return true;
        }
        return false;
    }
}