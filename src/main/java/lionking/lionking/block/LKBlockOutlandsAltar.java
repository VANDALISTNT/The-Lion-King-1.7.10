package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs; 

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

public class LKBlockOutlandsAltar extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] altarIcons;

    public LKBlockOutlandsAltar() {
        super(Material.iron); 
        setBlockName("outlandsAltar"); 
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        setLightLevel(0.75F);
        setCreativeTab(LKCreativeTabs.TAB_DECORATIONS); 
        setHardness(5.0F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return side == 1 ? altarIcons[1] : altarIcons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        altarIcons = new IIcon[2];
        altarIcons[0] = iconRegister.registerIcon("lionking:poolFocus_side");
        altarIcons[1] = iconRegister.registerIcon("lionking:poolFocus_top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        double posX = x + 0.5D + (random.nextFloat() - 0.5D) * 0.5D;
        double posY = y + random.nextFloat();
        double posZ = z + 0.5D + (random.nextFloat() - 0.5D) * 0.5D;
        double motionX = (random.nextFloat() - 0.5D) * 0.5D;
        double motionY = (random.nextFloat() - 0.5D) * 0.5D;
        double motionZ = (random.nextFloat() - 0.5D) * 0.5D;
        mod_LionKing.proxy.spawnParticle("outlandsPortal", posX, posY, posZ, motionX, motionY, motionZ);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getMobilityFlag() {
        return 2;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return true; 
    }
}