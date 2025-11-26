package lionking.item;

import net.minecraftforge.common.DimensionManager;
import net.minecraft.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;

import lionking.mod_LionKing; 
import lionking.common.LKIngame;
import lionking.client.LKCreativeTabs;
import lionking.common.LKTeleporterUpendi;

public class LKItemPassionFruit extends LKItemFood {
    public LKItemPassionFruit() {
        super(0, 0.0F, false); 
        setMaxStackSize(1); 
		setCreativeTab(LKCreativeTabs.TAB_FOOD);
    }

    @Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!entityplayer.capabilities.isCreativeMode) {
            itemstack.stackSize--;
        }
        mod_LionKing.proxy.playPortalFXForUpendi(world);

        if (!world.isRemote && entityplayer instanceof EntityPlayerMP) {
            int targetDimension = (entityplayer.dimension == mod_LionKing.idUpendi)
                    ? mod_LionKing.idPrideLands
                    : mod_LionKing.idUpendi;

            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(
                    (EntityPlayerMP) entityplayer,
                    targetDimension,
                    new LKTeleporterUpendi(DimensionManager.getWorld(targetDimension), LKIngame.getSimbas(entityplayer))
            );
        }
        return itemstack;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if ((entityplayer.dimension == mod_LionKing.idUpendi || entityplayer.dimension == mod_LionKing.idPrideLands)
                && (entityplayer.capabilities.isCreativeMode || entityplayer.getHealth() == entityplayer.getMaxHealth())) {
            entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        }
        return itemstack;
    }
}
