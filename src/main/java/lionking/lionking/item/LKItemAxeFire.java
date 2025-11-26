package lionking.item;

import net.minecraft.world.World;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.common.ForgeHooks;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;
import lionking.common.LKAchievementList;

public class LKItemAxeFire extends LKItemAxe {
    public LKItemAxeFire(Item.ToolMaterial material) {
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

        if (!isToolEffectiveAndSmeltable(itemStack, block, blockMeta)) {
            return false;
        }

        ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(block, 1, blockMeta));
        processBlockBreak(world, player, itemStack, x, y, z, block, smeltingResult);
        spawnFlameParticles(world, x, y, z);
        player.addStat(LKAchievementList.fireTool, 1);
        return true;
    }

    private boolean isToolEffectiveAndSmeltable(ItemStack itemStack, Block block, int blockMeta) {
        return block != null &&
               ForgeHooks.isToolEffective(itemStack, block, blockMeta) &&
               FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(block, 1, blockMeta)) != null;
    }

    private void processBlockBreak(World world, EntityPlayer player, ItemStack itemStack, int x, int y, int z, Block block, ItemStack smeltingResult) {
        if (!world.isRemote) {
            player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
            player.addExhaustion(0.025F);
            int dropCount = block.quantityDropped(world.rand);
            for (int i = 0; i < dropCount; i++) {
                ItemStack drop = new ItemStack(smeltingResult.getItem(), 1, smeltingResult.getItemDamage());
                EntityItem itemEntity = new EntityItem(world, x + 0.5D, y + 0.5D, z + 0.5D, drop);
                itemEntity.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(itemEntity);
            }
            world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (world.getBlockMetadata(x, y, z) << 12));
            itemStack.damageItem(1, player);
            world.setBlockToAir(x, y, z);
        }
    }

    private void spawnFlameParticles(World world, int x, int y, int z) {
        for (int i = 0; i < 6; i++) {
            double px = x + world.rand.nextFloat();
            double py = y + world.rand.nextFloat();
            double pz = z + world.rand.nextFloat();
            world.spawnParticle("flame", px, py, pz, 0.0D, 0.0D, 0.0D);
        }
    }
}
