package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKItemSwordFire extends LKItemSword {
    public LKItemSwordFire(Item.ToolMaterial material) {
        super(material);
	   setCreativeTab(LKCreativeTabs.TAB_COMBAT);
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!target.isImmuneToFire() && target.getHealth() > 0) {
            if (!target.worldObj.isRemote) {
                target.setFire(3 + target.worldObj.rand.nextInt(3));

                for (int i = 0; i < 8; i++) {
                    double d = target.worldObj.rand.nextGaussian() * 0.02D;
                    double d1 = target.worldObj.rand.nextGaussian() * 0.02D;
                    double d2 = target.worldObj.rand.nextGaussian() * 0.02D;
                    target.worldObj.spawnParticle("flame",
                        target.posX + (target.worldObj.rand.nextFloat() * target.width * 2.0F - target.width) * 0.75F,
                        target.posY + 0.25F + (target.worldObj.rand.nextFloat() * target.height),
                        target.posZ + (target.worldObj.rand.nextFloat() * target.width * 2.0F - target.width) * 0.75F,
                        d, d1, d2);
                }
                itemStack.damageItem(1, attacker);
            }
            return true;
        }
        return super.hitEntity(itemStack, target, attacker);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);

        if (block == Blocks.snow) {
            side = 1;
        } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush &&
                   (block != null && !block.isReplaceable(world, x, y, z))) {
            switch (side) {
                case 0: --y; break;
                case 1: ++y; break;
                case 2: --z; break;
                case 3: ++z; break;
                case 4: --x; break;
                case 5: ++x; break;
            }
        }

        if (!player.canPlayerEdit(x, y, z, side, itemStack)) {
            return false;
        }

        if (world.isAirBlock(x, y, z)) {
            if (!world.isRemote) {
                world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
                world.setBlock(x, y, z, Blocks.fire, 0, 3);
                itemStack.damageItem(1, player);
            }
            return true;
        }
        return false;
    }
}
