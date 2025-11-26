package lionking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;

import lionking.entity.LKEntityGiraffe;
import lionking.client.LKCreativeTabs;
import lionking.common.LKAngerable;

public class LKItemGiraffeSaddle extends LKItem {
    public LKItemGiraffeSaddle() {
        super(); 
        setMaxStackSize(1);
        setCreativeTab(LKCreativeTabs.TAB_MISC);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity) {
        if (!(entity instanceof LKEntityGiraffe)) {
            return false;
        }

        LKEntityGiraffe giraffe = (LKEntityGiraffe) entity;
        if (canSaddleGiraffe(giraffe)) {
            giraffe.setSaddled(true);
            itemStack.stackSize--;
        }
        return true;
    }

    private boolean canSaddleGiraffe(LKEntityGiraffe giraffe) {
        return !giraffe.getSaddled() && !giraffe.isChild();
    }
}