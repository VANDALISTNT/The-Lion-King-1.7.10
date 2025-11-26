package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.item.Item;

import lionking.entity.LKEntityBug;
import lionking.mod_LionKing;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

public class LKBlockLog extends Block {
    public LKBlockLog() {
        super(Material.wood);
        setCreativeTab(null);
        setHardness(2.0F);
        setStepSound(soundTypeWood);
        setBlockName("log");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return mod_LionKing.prideWood.getIcon(side, 8);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        return Item.getItemFromBlock(mod_LionKing.prideWood);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
        if (!world.isRemote && world.rand.nextBoolean()) {
            LKEntityBug bug = new LKEntityBug(world);
            bug.setLocationAndAngles(x + 0.5D, y, z + 0.5D, world.rand.nextFloat() * 360.0F, 0.0F);
            world.spawnEntityInWorld(bug);
        }
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(mod_LionKing.prideWood);
    }
}