package lionking.block;

import net.minecraft.block.BlockMobSpawner;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;

import lionking.tileentity.LKTileEntityMobSpawner;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LKBlockMobSpawner extends BlockMobSpawner {
    public LKBlockMobSpawner() {
        super();
        setCreativeTab(null);
        setHardness(5.0F);
        setStepSound(soundTypeMetal);
        setBlockName("mobSpawner");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return Blocks.mob_spawner.getIcon(side, metadata);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new LKTileEntityMobSpawner();
    }
}