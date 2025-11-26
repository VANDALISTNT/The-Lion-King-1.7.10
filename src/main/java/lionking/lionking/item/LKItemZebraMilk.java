package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.player.EntityPlayer;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

public class LKItemZebraMilk extends LKItem {
    public LKItemZebraMilk() {
        super();
        setMaxStackSize(1);
        setCreativeTab(LKCreativeTabs.TAB_FOOD);
        setUnlocalizedName("zebraMilk");
        setTextureName("lionking:zebraMilk");
    }

    @Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!world.isRemote) {
            entityplayer.clearActivePotions();
        }

        if (--itemstack.stackSize <= 0) {
            return new ItemStack(mod_LionKing.jar);
        }
        return itemstack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (entityplayer.canEat(false)) {
            entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        }
        return itemstack;
    }
}