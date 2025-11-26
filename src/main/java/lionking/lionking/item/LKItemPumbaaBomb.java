package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

import lionking.client.LKCreativeTabs;
import lionking.entity.LKEntityPumbaaBomb;

public class LKItemPumbaaBomb extends LKItem {
    public LKItemPumbaaBomb() {
        super(); 
        setMaxStackSize(16);
        setCreativeTab(LKCreativeTabs.TAB_QUEST);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!world.isRemote) {
            LKEntityPumbaaBomb bomb = new LKEntityPumbaaBomb(world, entityplayer);
            world.spawnEntityInWorld(bomb);
            world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!entityplayer.capabilities.isCreativeMode) {
                itemstack.stackSize--;
            }
        }
        return itemstack;
    }
}