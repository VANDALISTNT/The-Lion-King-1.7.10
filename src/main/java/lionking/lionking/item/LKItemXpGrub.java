package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.client.LKCreativeTabs;

public class LKItemXpGrub extends LKItemFood {
    public LKItemXpGrub(int i) {
        super(i, 0.0F, false);
        setCreativeTab(LKCreativeTabs.TAB_QUEST);
    }

    @Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!world.isRemote) {
            world.playSoundAtEntity(entityplayer, "random.orb", 0.1F, 0.5F * ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.8F));
            entityplayer.addExperience(20 + world.rand.nextInt(15));
        }

        --itemstack.stackSize;
        return itemstack;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (entityplayer.canEat(false)) {
            entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        }
        return itemstack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 32;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemstack, int pass) {
        return true;
    }
}