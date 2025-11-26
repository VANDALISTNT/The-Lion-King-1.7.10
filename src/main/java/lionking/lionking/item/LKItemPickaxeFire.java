package lionking.item;

import net.minecraftforge.common.ForgeHooks;
import net.minecraft.world.World;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.block.Block;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;
import lionking.common.LKAchievementList;

public class LKItemPickaxeFire extends LKItemPickaxe {
    public LKItemPickaxeFire(Item.ToolMaterial material) {
        super(material);
		setCreativeTab(LKCreativeTabs.TAB_TOOLS);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, int x, int y, int z, EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            return false;
        }

        World world = player.worldObj;
        Block block = world.getBlock(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);

        if (ForgeHooks.isToolEffective(itemStack, block, blockMeta)) {
            ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(block, 1, blockMeta));
            if (smeltingResult != null) {
                if (!world.isRemote) {
                    player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
                    player.addExhaustion(0.025F);
                    int dropCount = block.quantityDropped(world.rand);
                    for (int i = 0; i < dropCount; i++) {
                        ItemStack drop = new ItemStack(smeltingResult.getItem(), 1, smeltingResult.getItemDamage());
                        EntityItem entityItem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, drop);
                        entityItem.delayBeforeCanPickup = 10;
                        world.spawnEntityInWorld(entityItem);
                    }
                    world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (blockMeta << 12));
                    itemStack.damageItem(1, player);
                    world.setBlockToAir(x, y, z);
                    player.addStat(LKAchievementList.fireTool, 1);
                }

                for (int i = 0; i < 6; i++) {
                    double px = x + world.rand.nextFloat();
                    double py = y + world.rand.nextFloat();
                    double pz = z + world.rand.nextFloat();
                    world.spawnParticle("flame", px, py, pz, 0.0D, 0.0D, 0.0D);
                }
                return true;
            }
        }
        return false;
    }
}
