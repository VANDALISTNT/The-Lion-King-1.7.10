package lionking.item;

import net.minecraftforge.common.DimensionManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumRarity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

import lionking.mod_LionKing; 
import lionking.client.LKCreativeTabs; 
import lionking.common.LKIngame; 
import lionking.common.LKLevelData;
import lionking.common.LKTeleporterFeather; 

public class LKItemOutlandsFeather extends LKItem {
    public LKItemOutlandsFeather() {
        super();
        setMaxStackSize(16);
        setCreativeTab(LKCreativeTabs.TAB_QUEST);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (LKLevelData.ziraStage > 9 && LKLevelData.ziraStage < 14) {
            return itemstack;
        }

        if (entityplayer.dimension != mod_LionKing.idPrideLands && entityplayer.dimension != mod_LionKing.idOutlands) {
            return itemstack;
        }

        world.playSoundAtEntity(entityplayer, "portal.portal", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
        int targetDimension = (entityplayer.dimension == mod_LionKing.idOutlands)
                ? mod_LionKing.idPrideLands
                : mod_LionKing.idOutlands;

        if (entityplayer instanceof EntityPlayerMP && world instanceof WorldServer) {
            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(
                    (EntityPlayerMP) entityplayer,
                    targetDimension,
                    new LKTeleporterFeather(DimensionManager.getWorld(targetDimension), LKIngame.getSimbas(entityplayer))
            );
            if (!entityplayer.capabilities.isCreativeMode) {
                itemstack.stackSize--;
            }
        }

        return itemstack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }
}