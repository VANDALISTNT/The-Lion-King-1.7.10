package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

public class LKItemTunnahDiggah extends LKItemPickaxe {
    public LKItemTunnahDiggah() {
        super(Item.ToolMaterial.IRON);
        setMaxDamage(690);
        setCreativeTab(LKCreativeTabs.TAB_QUEST);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return block.getMaterial() == Material.rock || block == Blocks.dirt || block == Blocks.grass;
    }

    private boolean shouldDamageWithLevel(int level) {
        return level == 0 || itemRand.nextFloat() < 0.82F;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int j, int k, int l, EntityLivingBase entityliving) {
        itemstack.damageItem(1, entityliving);

        if (block == Blocks.dirt || block == Blocks.grass || block == mod_LionKing.pridestone ||
            block == Blocks.stone || block == Blocks.netherrack || block == Blocks.end_stone) {
            
            int level = EnchantmentHelper.getEnchantmentLevel(mod_LionKing.diggahEnchantment.effectId, itemstack) + 1;
            for (int j1 = -level; j1 <= level; j1++) {
                for (int k1 = -level; k1 <= level; k1++) {
                    for (int l1 = -level; l1 <= level; l1++) {
                        int x = j + j1, y = k + k1, z = l + l1;
                        Block currentBlock = world.getBlock(x, y, z);

                        if (currentBlock == Blocks.dirt || currentBlock == Blocks.netherrack || currentBlock == Blocks.end_stone) {
                            world.setBlockToAir(x, y, z);
                            dropBlock(itemstack, world, x, y, z, currentBlock);
                            if (shouldDamageWithLevel(level)) {
                                itemstack.damageItem(1, entityliving);
                            }
                        } else if (currentBlock == mod_LionKing.pridestone) {
                            int metadata = world.getBlockMetadata(x, y, z);
                            world.setBlockToAir(x, y, z);
                            dropBlock(itemstack, world, x, y, z, mod_LionKing.pridestone, metadata);
                            if (shouldDamageWithLevel(level)) {
                                itemstack.damageItem(1, entityliving);
                            }
                        } else if (currentBlock == Blocks.grass) {
                            world.setBlockToAir(x, y, z);
                            boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, itemstack) > 0;
                            dropBlock(itemstack, world, x, y, z, silkTouch ? Blocks.grass : Blocks.dirt);
                            if (shouldDamageWithLevel(level)) {
                                itemstack.damageItem(1, entityliving);
                            }
                        } else if (currentBlock == Blocks.stone) {
                            world.setBlockToAir(x, y, z);
                            boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, itemstack) > 0;
                            dropBlock(itemstack, world, x, y, z, silkTouch ? Blocks.stone : Blocks.cobblestone);
                            if (shouldDamageWithLevel(level)) {
                                itemstack.damageItem(1, entityliving);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private void dropBlock(ItemStack itemstack, World world, int i, int j, int k, Block block) {
        dropBlock(itemstack, world, i, j, k, block, 0);
    }

    private void dropBlock(ItemStack itemstack, World world, int i, int j, int k, Block block, int metadata) {
        if (world.isRemote) {
            return;
        }

        boolean drop = itemRand.nextInt(3) == 0;
        if (drop) {
            float f = 0.7F;
            double d = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, i + d, j + d1, k + d2, new ItemStack(block, 1, metadata));
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }
}