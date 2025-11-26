package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

import lionking.entity.LKEntityThrownTermite;
import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKItemTermite extends LKItem {
    public LKItemTermite() {
        super();
        setMaxStackSize(16);
	   setCreativeTab(LKCreativeTabs.TAB_MISC);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!world.isRemote) {
            LKEntityThrownTermite termite = new LKEntityThrownTermite(world, entityplayer);
            if (world.spawnEntityInWorld(termite)) {
                world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                for (int j = 0; j < 5; j++) {
                    world.spawnParticle("smoke", entityplayer.posX, entityplayer.posY + 0.5D, entityplayer.posZ,
                        0.0D, 0.0D, 0.0D);
                }

                if (!entityplayer.capabilities.isCreativeMode) {
                    --itemstack.stackSize;
                }
            }
        }
        return itemstack;
    }
}
