package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.common.IShearable;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

import java.util.ArrayList;
import java.util.Random;

public class LKBlockAridGrass extends BlockFlower implements IShearable {
    public LKBlockAridGrass() {
        super(0);
        setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 0.5F, 0.9F);
        setCreativeTab(LKCreativeTabs.tabDecorations);
        setBlockName("aridGrass");
        this.blockResistance = 1.0F; 
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        Block blockBelow = world.getBlock(x, y - 1, z);
        return (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z)) &&
               (blockBelow == Blocks.sand || blockBelow == mod_LionKing.tilledSand);
    }

    @Override
    public boolean canReplace(World world, int x, int y, int z, int side, ItemStack stack) {
        return true;
    }

    @Override
    public int getRenderType() {
        return mod_LionKing.proxy.getAridGrassRenderID();
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        return random.nextInt(8) == 0 ? Items.wheat_seeds : null;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune > 0 && random.nextInt(2 + fortune) == 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(8) == 0 ? 1 : 0;
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(this));
        return drops;
    }
}
