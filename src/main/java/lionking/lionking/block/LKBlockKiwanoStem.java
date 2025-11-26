package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import lionking.common.LKIngame;
import lionking.mod_LionKing;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

public class LKBlockKiwanoStem extends BlockFlower {
    private final Block fruitType;

    public LKBlockKiwanoStem() {
        super(0);
        fruitType = mod_LionKing.kiwanoBlock;
        setTickRandomly(true);
        float width = 0.125F;
        setBlockBounds(0.5F - width, 0.0F, 0.5F - width, 0.5F + width, 0.25F, 0.5F + width);
        setCreativeTab(null);
        setBlockName("kiwanoStem");
    }

    @Override
    protected boolean canPlaceBlockOn(Block block) {
        return block == mod_LionKing.tilledSand;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);

        if (!LKIngame.isLKWorld(world.provider.dimensionId) || world.getBlockLightValue(x, y + 1, z) < 9) return;

        float growthModifier = getGrowthModifier(world, x, y, z);
        if (random.nextInt((int) (25.0F / growthModifier) + 1) != 0) return;

        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata < 7) {
            world.setBlockMetadataWithNotify(x, y, z, metadata + 1, 3);
        } else {
            int[] directions = {x - 1, x + 1, z - 1, z + 1};
            for (int i = 0; i < 4; i++) {
                int fruitX = i < 2 ? directions[i] : x;
                int fruitZ = i >= 2 ? directions[i] : z;
                if (world.getBlock(fruitX, y, fruitZ) == fruitType) return;
            }

            int direction = random.nextInt(4);
            int fruitX = direction == 0 ? x - 1 : direction == 1 ? x + 1 : x;
            int fruitZ = direction == 2 ? z - 1 : direction == 3 ? z + 1 : z;

            Block groundBlock = world.getBlock(fruitX, y - 1, fruitZ);
            if (world.isAirBlock(fruitX, y, fruitZ) &&
                (groundBlock == mod_LionKing.tilledSand || groundBlock == Blocks.sand ||
                 groundBlock == Blocks.dirt || groundBlock == Blocks.grass)) {
                world.setBlock(fruitX, y, fruitZ, fruitType, 0, 3);
            }
        }
    }

    public void fertilize(World world, int x, int y, int z) {
        world.setBlockMetadataWithNotify(x, y, z, 7, 3);
    }

    public void fertilizePartially(World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z) + MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
        world.setBlockMetadataWithNotify(x, y, z, Math.min(metadata, 7), 2);
    }

    private float getGrowthModifier(World world, int x, int y, int z) {
        float growth = 0.8F;
        Block[] adjacentBlocks = {
            world.getBlock(x, y, z - 1), world.getBlock(x, y, z + 1),
            world.getBlock(x - 1, y, z), world.getBlock(x + 1, y, z),
            world.getBlock(x - 1, y, z - 1), world.getBlock(x + 1, y, z - 1),
            world.getBlock(x + 1, y, z + 1), world.getBlock(x - 1, y, z + 1)
        };

        boolean adjacentX = adjacentBlocks[2] == this || adjacentBlocks[3] == this;
        boolean adjacentZ = adjacentBlocks[0] == this || adjacentBlocks[1] == this;
        boolean diagonal = adjacentBlocks[4] == this || adjacentBlocks[5] == this ||
                          adjacentBlocks[6] == this || adjacentBlocks[7] == this;

        for (int dx = x - 1; dx <= x + 1; dx++) {
            for (int dz = z - 1; dz <= z + 1; dz++) {
                Block groundBlock = world.getBlock(dx, y - 1, dz);
                float bonus = (groundBlock == mod_LionKing.tilledSand)
                    ? (world.getBlockMetadata(dx, y - 1, dz) > 0 ? 3.0F : 1.0F)
                    : 0.0F;

                if (dx != x || dz != z) bonus /= 4.0F;
                growth += bonus;
            }
        }

        if (diagonal || (adjacentX && adjacentZ)) growth /= 2.0F;
        return growth;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int metadata) {
        int red = metadata * 32;
        int green = 255 - metadata * 8;
        int blue = metadata * 4;
        return (red << 16) | (green << 8) | blue;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return getRenderColor(world.getBlockMetadata(x, y, z));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return Blocks.pumpkin_stem.getIcon(side, metadata);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float width = 0.125F;
        setBlockBounds(0.5F - width, 0.0F, 0.5F - width, 0.5F + width, 0.25F, 0.5F + width);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        float maxY = (world.getBlockMetadata(x, y, z) * 2 + 2) / 16.0F;
        float width = 0.125F;
        setBlockBounds(0.5F - width, 0.0F, 0.5F - width, 0.5F + width, maxY, 0.5F + width);
    }

    @Override
    public int getRenderType() {
        return mod_LionKing.proxy.getKiwanoStemRenderID();
    }

    public int getState(IBlockAccess world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata < 7) return -1;
        if (world.getBlock(x - 1, y, z) == fruitType) return 0;
        if (world.getBlock(x + 1, y, z) == fruitType) return 1;
        if (world.getBlock(x, y, z - 1) == fruitType) return 2;
        if (world.getBlock(x, y, z + 1) == fruitType) return 3;
        return -1;
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        return mod_LionKing.kiwanoSeeds;
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(3) == 0 ? 1 : 0; 
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return mod_LionKing.kiwanoSeeds;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    }
}