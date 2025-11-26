package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.player.EntityPlayer;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKItemMangoJuice extends LKItemFood {
    private final int healAmount = 7;
    private final float saturationModifier = 0.8F;

    public LKItemMangoJuice() {
        super(7, 0.8F, false);
        setMaxStackSize(1);
		setCreativeTab(LKCreativeTabs.TAB_FOOD);
    }

    @Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        itemstack.stackSize--;
        entityplayer.getFoodStats().addStats(healAmount, saturationModifier);
        world.playSoundAtEntity(entityplayer, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        if (itemstack.stackSize <= 0) {
            return new ItemStack(mod_LionKing.jar);
        }
        return itemstack;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.drink;
    }
}
