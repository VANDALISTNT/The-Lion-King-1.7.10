package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.init.Blocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;
import lionking.common.LKIngame;
import lionking.biome.LKBiomeGenUpendi;
import lionking.world.LKWorldGenBananaTrees;
import lionking.world.LKWorldGenHugeRainforestTrees;
import lionking.world.LKWorldGenMangoTrees;
import lionking.world.LKWorldGenPassionTrees;
import lionking.world.LKWorldGenRainforestTrees;
import lionking.world.LKWorldGenTrees;

import java.util.Random;

public class LKBlockSapling extends LKBlockFlower {
    @SideOnly(Side.CLIENT)
    private IIcon[] saplingIcons;

    public LKBlockSapling() {
        super();
        float size = 0.4F;
        setBlockBounds(0.5F - size, 0.0F, 0.5F - size, 0.5F + size, size * 2.0F, 0.5F + size);
        setCreativeTab(LKCreativeTabs.tabDecorations); 
        setHardness(0.0F);
        setStepSound(soundTypeGrass);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (world.isRemote) return;

        WorldChunkManager chunkManager = world.getWorldChunkManager();
        if (chunkManager == null) return;

        boolean canGrow = LKIngame.isLKWorld(world.provider.dimensionId);
        if (this == mod_LionKing.passionSapling) {
            canGrow = chunkManager.getBiomeGenAt(x, z) instanceof LKBiomeGenUpendi;
        }

        if (!canGrow) return;

        super.updateTick(world, x, y, z, random);
        if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(7) == 0) {
            incrementGrowth(world, x, y, z, random);
        }
    }

    public void incrementGrowth(World world, int x, int y, int z, Random random) {
        int metadata = world.getBlockMetadata(x, y, z);
        if ((metadata & 8) == 0) {
            world.setBlockMetadataWithNotify(x, y, z, metadata | 8, 4);
        } else {
            growTree(world, x, y, z, random);
        }
    }

    public void growTree(World world, int x, int y, int z, Random random) {
        WorldGenerator tree = null;
        int offsetX = 0;
        int offsetZ = 0;
        boolean isHugeTree = false;

        if (this == mod_LionKing.bananaSapling) {
            tree = new LKWorldGenBananaTrees(true);
        } else if (this == mod_LionKing.passionSapling) {
            tree = new LKWorldGenPassionTrees(true);
        } else if (this == mod_LionKing.mangoSapling) {
            tree = new LKWorldGenMangoTrees(true);
        } else if (this == mod_LionKing.forestSapling || this == mod_LionKing.sapling) {
            for (int dx = 0; dx >= -1; dx--) {
                for (int dz = 0; dz >= -1; dz--) {
                    if (isSaplingSquare(world, x + dx, y, z + dz)) {
                        if (this == mod_LionKing.forestSapling) {
                            tree = new LKWorldGenHugeRainforestTrees(true);
                        } else {
                            tree = new LKWorldGenTrees(true).setLarge();
                        }
                        isHugeTree = true;
                        offsetX = dx;
                        offsetZ = dz;
                        break;
                    }
                }
                if (tree != null) break;
            }
            if (tree == null) {
                offsetX = 0;
                offsetZ = 0;
                if (this == mod_LionKing.forestSapling) {
                    tree = new LKWorldGenRainforestTrees(true);
                } else {
                    tree = new LKWorldGenTrees(true);
                }
            }
        }

        if (tree != null) {
            clearSaplingArea(world, x, y, z, offsetX, offsetZ, isHugeTree);
            if (!tree.generate(world, random, x + offsetX, y, z + offsetZ)) {
                restoreSaplingArea(world, x, y, z, offsetX, offsetZ, isHugeTree);
            }
        }
    }

    private boolean isSaplingSquare(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) == this &&
               world.getBlock(x + 1, y, z) == this &&
               world.getBlock(x, y, z + 1) == this &&
               world.getBlock(x + 1, y, z + 1) == this;
    }

    private void clearSaplingArea(World world, int x, int y, int z, int offsetX, int offsetZ, boolean isHugeTree) {
        if (isHugeTree) {
            world.setBlock(x + offsetX, y, z + offsetZ, Blocks.air, 0, 4);
            world.setBlock(x + offsetX + 1, y, z + offsetZ, Blocks.air, 0, 4);
            world.setBlock(x + offsetX, y, z + offsetZ + 1, Blocks.air, 0, 4);
            world.setBlock(x + offsetX + 1, y, z + offsetZ + 1, Blocks.air, 0, 4);
        } else {
            world.setBlock(x, y, z, Blocks.air, 0, 4);
        }
    }

    private void restoreSaplingArea(World world, int x, int y, int z, int offsetX, int offsetZ, boolean isHugeTree) {
        if (isHugeTree) {
            world.setBlock(x + offsetX, y, z + offsetZ, this, 0, 4);
            world.setBlock(x + offsetX + 1, y, z + offsetZ, this, 0, 4);
            world.setBlock(x + offsetX, y, z + offsetZ + 1, this, 0, 4);
            world.setBlock(x + offsetX + 1, y, z + offsetZ + 1, this, 0, 4);
        } else {
            world.setBlock(x, y, z, this, 0, 4);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (this == mod_LionKing.passionSapling) {
            for (int i = 0; i < 4; i++) {
                double posX = x + random.nextFloat();
                double posY = y + random.nextFloat() * 0.5F;
                double posZ = z + random.nextFloat();
                double motionX = (-0.5F + random.nextFloat()) * 0.01F;
                double motionY = random.nextFloat() * 0.01F;
                double motionZ = (-0.5F + random.nextFloat()) * 0.01F;
                mod_LionKing.proxy.spawnParticle("passion", posX, posY, posZ, motionX, motionY, motionZ);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (this == mod_LionKing.bananaSapling) return saplingIcons[0];
        if (this == mod_LionKing.passionSapling) return saplingIcons[1];
        if (this == mod_LionKing.mangoSapling) return saplingIcons[2];
        if (this == mod_LionKing.forestSapling) return saplingIcons[3];
        return saplingIcons[4];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        saplingIcons = new IIcon[5];
        saplingIcons[0] = iconRegister.registerIcon("lionking:sapling_banana");
        saplingIcons[1] = iconRegister.registerIcon("lionking:sapling_passion");
        saplingIcons[2] = iconRegister.registerIcon("lionking:sapling_mango");
        saplingIcons[3] = iconRegister.registerIcon("lionking:sapling_rainforest");
        saplingIcons[4] = iconRegister.registerIcon("lionking:sapling_acacia");
    }
}
