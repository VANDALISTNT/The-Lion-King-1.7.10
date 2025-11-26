package lionking.item;

import net.minecraft.world.World;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items; 

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKItemBugStew extends LKItemFood {
    private static final int HUNGER_RESTORE = 8;
    private static final float SATURATION = 0.5F;
    private static final float NAUSEA_CHANCE = 0.4F;
    private static final float POISON_CHANCE = 0.3F;

    public LKItemBugStew() {
        super(HUNGER_RESTORE, SATURATION, false); 
        setContainerItem(Items.bowl); 
        setMaxStackSize(1);
	   setCreativeTab(LKCreativeTabs.TAB_FOOD);
    }

    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
        super.onEaten(itemStack, world, player);

        if (!world.isRemote) {
            applyRandomEffects(world, player);
        }

        return new ItemStack(Items.bowl); 
    }

    private void applyRandomEffects(World world, EntityPlayer player) {
        if (world.rand.nextFloat() < NAUSEA_CHANCE) {
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, 160 + world.rand.nextInt(100), 0));
            if (world.rand.nextFloat() < POISON_CHANCE) {
                player.addPotionEffect(new PotionEffect(Potion.poison.id, 40 + world.rand.nextInt(40), 0));
            }
        }
    }
}
