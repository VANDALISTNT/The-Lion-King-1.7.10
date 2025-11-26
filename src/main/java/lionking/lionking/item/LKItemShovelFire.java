package lionking.item;

import net.minecraftforge.common.ForgeHooks;
import net.minecraft.world.World;
import net.minecraft.stats.StatList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.entity.item.EntityItem;

import lionking.common.LKAchievementList;
import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKItemShovelFire extends LKItemShovel {
    public LKItemShovelFire(Item.ToolMaterial material) {
        super(material);
		setCreativeTab(LKCreativeTabs.TAB_TOOLS);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int i, int j, int k, EntityPlayer entityplayer) {
        if (entityplayer.capabilities.isCreativeMode) {
            return false;
        }

        World world = entityplayer.worldObj;
        Block block = world.getBlock(i, j, k);
        int blockMeta = world.getBlockMetadata(i, j, k);

        if (block != null && ForgeHooks.isToolEffective(itemstack, block, blockMeta)) {
            ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(block, 1, blockMeta));
            if (smeltingResult != null) {
                processBlockBreak(world, entityplayer, itemstack, i, j, k, smeltingResult);
                return true;
            }
        }

        if (block == Blocks.clay) {
            processBlockBreak(world, entityplayer, itemstack, i, j, k, new ItemStack(Blocks.brick_block));
            return true;
        }

        return false;
    }

    private void processBlockBreak(World world, EntityPlayer entityplayer, ItemStack itemstack, int i, int j, int k, ItemStack drop) {
        if (!world.isRemote) {
            Block block = world.getBlock(i, j, k);
            int blockMeta = world.getBlockMetadata(i, j, k);
            int blockId = Block.getIdFromBlock(block);

            entityplayer.addStat(StatList.mineBlockStatArray[blockId], 1);
            entityplayer.addExhaustion(0.025F);
            if (block != null) {
                EntityItem entityItem = new EntityItem(world, i + 0.5, j + 0.5, k + 0.5, drop.copy());
                entityItem.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(entityItem);
                world.setBlockToAir(i, j, k);
            }

            world.playAuxSFX(2001, i, j, k, blockId + (blockMeta << 12));
            itemstack.damageItem(1, entityplayer);
            entityplayer.addStat(LKAchievementList.fireTool, 1);
        }

        for (int l = 0; l < 6; l++) {
            double x = i + world.rand.nextFloat();
            double y = j + world.rand.nextFloat();
            double z = k + world.rand.nextFloat();
            world.spawnParticle("flame", x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
