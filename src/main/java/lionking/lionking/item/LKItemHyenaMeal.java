package lionking.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockSapling;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;
import lionking.common.LKIngame;
import lionking.block.LKBlockKiwanoStem;
import lionking.block.LKBlockFlowerBase;
import lionking.block.LKBlockFlowerTop;
import lionking.block.LKBlockAridGrass;
import lionking.biome.LKBiomeGenRainforest;
import lionking.biome.LKBiomeGenUpendi;
import lionking.biome.LKBiomeGenMountains;
import lionking.biome.LKBiomeGenSavannahBase;
import lionking.biome.LKBiomeGenRiver;
import lionking.biome.LKBiomeGenAridSavannah;
import lionking.biome.LKBiomeGenDesert;

public class LKItemHyenaMeal extends LKItem {
    public LKItemHyenaMeal() {
        super();
        setCreativeTab(LKCreativeTabs.TAB_MATERIALS);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (useHyenaMeal(itemstack, entityplayer, world, i, j, k, l)) {
            if (!world.isRemote) {
                world.playAuxSFX(2005, i, j, k, 0);
            }
            return true;
        }
        return false;
    }

    private boolean useHyenaMeal(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
        if (world.isRemote || !LKIngame.isLKWorld(world.provider.dimensionId) || !entityplayer.canPlayerEdit(i, j, k, l, itemstack)) {
            return false;
        }

        Block block = world.getBlock(i, j, k);
        if (block == mod_LionKing.sapling || block == mod_LionKing.forestSapling || block == mod_LionKing.mangoSapling ||
            block == mod_LionKing.passionSapling || block == mod_LionKing.bananaSapling) {
            if (!entityplayer.capabilities.isCreativeMode) {
                itemstack.stackSize--;
            }
            if (world.rand.nextFloat() < 0.45F) {
                ((BlockSapling) block).func_149878_d(world, i, j, k, world.rand); 
            }
            return true;
        }

        if ((block == Blocks.wheat || block == mod_LionKing.yamCrops) && world.getBlockMetadata(i, j, k) < 7) {
            if (!entityplayer.capabilities.isCreativeMode) {
                itemstack.stackSize--;
            }
            if (block instanceof BlockCrops) {
                int meta = world.getBlockMetadata(i, j, k);
                world.setBlockMetadataWithNotify(i, j, k, Math.min(meta + 1, 7), 2);
            }
            return true;
        }

        if (block == mod_LionKing.kiwanoStem && world.getBlockMetadata(i, j, k) < 7) {
            if (!entityplayer.capabilities.isCreativeMode) {
                itemstack.stackSize--;
            }
            ((LKBlockKiwanoStem) block).fertilizePartially(world, i, j, k);
            return true;
        }

        if (block == Blocks.grass) {
            BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
            world.notifyBlockChange(i, j, k, block);
            label0:
            for (int l1 = 0; l1 < 128; ++l1) {
                int i2 = i;
                int j2 = j + 1;
                int k2 = k;
                for (int l2 = 0; l2 < l1 / 16; ++l2) {
                    i2 += world.rand.nextInt(3) - 1;
                    j2 += (world.rand.nextInt(3) - 1) * world.rand.nextInt(3) / 2;
                    k2 += world.rand.nextInt(3) - 1;
                    if (world.getBlock(i2, j2 - 1, k2) != Blocks.grass || world.getBlock(i2, j2, k2).isOpaqueCube()) {
                        continue label0;
                    }
                }
                if (world.isAirBlock(i2, j2, k2)) {
                    if (world.rand.nextInt(7) != 0 && Blocks.tallgrass.canBlockStay(world, i2, j2, k2)) {
                        int j3 = 1;
                        if ((biome instanceof LKBiomeGenRainforest || biome instanceof LKBiomeGenUpendi) && world.rand.nextInt(5) == 0) {
                            j3 = 2;
                        }
                        world.setBlock(i2, j2, k2, Blocks.tallgrass, j3, 3);
                    } else {
                        if (biome instanceof LKBiomeGenUpendi) {
                            int i3 = world.rand.nextInt(8);
                            if (i3 == 0 && world.isAirBlock(i2, j2 + 1, k2) && mod_LionKing.flowerBase.canBlockStay(world, i2, j2, k2)) {
                                world.setBlock(i2, j2, k2, mod_LionKing.flowerBase, 1, 3);
                                world.setBlock(i2, j2 + 1, k2, mod_LionKing.flowerTop, 1, 3);
                            } else if (i3 == 1 && mod_LionKing.whiteFlower.canBlockStay(world, i2, j2, k2)) {
                                world.setBlock(i2, j2, k2, mod_LionKing.whiteFlower, 0, 3);
                            } else if (i3 > 5 && world.isAirBlock(i2, j2 + 1, k2) && mod_LionKing.flowerBase.canBlockStay(world, i2, j2, k2)) {
                                world.setBlock(i2, j2, k2, mod_LionKing.flowerBase, 0, 3);
                                world.setBlock(i2, j2 + 1, k2, mod_LionKing.flowerTop, 0, 3);
                            }
                        }
                        if (biome instanceof LKBiomeGenMountains) {
                            int i3 = world.rand.nextInt(5);
                            if (i3 == 0 && mod_LionKing.blueFlower.canBlockStay(world, i2, j2, k2)) {
                                world.setBlock(i2, j2, k2, mod_LionKing.blueFlower, 0, 3);
                            }
                        }
                        if (biome instanceof LKBiomeGenRainforest) {
                            int i3 = world.rand.nextInt(5);
                            if (i3 < 2 && world.isAirBlock(i2, j2 + 1, k2) && mod_LionKing.flowerBase.canBlockStay(world, i2, j2, k2)) {
                                world.setBlock(i2, j2, k2, mod_LionKing.flowerBase, 0, 3);
                                world.setBlock(i2, j2 + 1, k2, mod_LionKing.flowerTop, 0, 3);
                            } else if (i3 == 2 && mod_LionKing.whiteFlower.canBlockStay(world, i2, j2, k2)) {
                                world.setBlock(i2, j2, k2, mod_LionKing.whiteFlower, 0, 3);
                            }
                        }
                        if (biome instanceof LKBiomeGenSavannahBase || biome instanceof LKBiomeGenRiver || biome instanceof LKBiomeGenAridSavannah) {
                            int i3 = world.rand.nextInt(5);
                            if (i3 == 0 && world.isAirBlock(i2, j2 + 1, k2) && mod_LionKing.flowerBase.canBlockStay(world, i2, j2, k2)) {
                                world.setBlock(i2, j2, k2, mod_LionKing.flowerBase, 0, 3);
                                world.setBlock(i2, j2 + 1, k2, mod_LionKing.flowerTop, 0, 3);
                            } else if (i3 == 1 && mod_LionKing.whiteFlower.canBlockStay(world, i2, j2, k2)) {
                                world.setBlock(i2, j2, k2, mod_LionKing.whiteFlower, 0, 3);
                            }
                        }
                    }
                }
            }
            if (!entityplayer.capabilities.isCreativeMode) {
                itemstack.stackSize--;
            }
            return true;
        }

        if (block == Blocks.sand) {
            BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
            if (biome instanceof LKBiomeGenAridSavannah || biome instanceof LKBiomeGenDesert) {
                world.notifyBlockChange(i, j, k, block);
                label0:
                for (int l1 = 0; l1 < 128; ++l1) {
                    int i2 = i;
                    int j2 = j + 1;
                    int k2 = k;
                    for (int l2 = 0; l2 < l1 / 16; ++l2) {
                        i2 += world.rand.nextInt(3) - 1;
                        j2 += (world.rand.nextInt(3) - 1) * world.rand.nextInt(3) / 2;
                        k2 += world.rand.nextInt(3) - 1;
                        if (world.getBlock(i2, j2 - 1, k2) != Blocks.sand || world.getBlock(i2, j2, k2).isOpaqueCube()) {
                            continue label0;
                        }
                    }
                    if (world.isAirBlock(i2, j2, k2)) {
                        if (world.rand.nextInt(16) == 0 && Blocks.deadbush.canBlockStay(world, i2, j2, k2)) {
                            world.setBlock(i2, j2, k2, Blocks.deadbush, 0, 3);
                        } else if (mod_LionKing.aridGrass.canBlockStay(world, i2, j2, k2)) {
                            world.setBlock(i2, j2, k2, mod_LionKing.aridGrass, 0, 3);
                        }
                    }
                }
                if (!entityplayer.capabilities.isCreativeMode) {
                    itemstack.stackSize--;
                }
                return true;
            }
        }

        return false;
    }
}