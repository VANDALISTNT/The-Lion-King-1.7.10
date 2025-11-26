package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

import java.util.Random;

public class LKBlockStarAltar extends LKBlock {
    @SideOnly(Side.CLIENT)
    private IIcon[] altarIcons;

    public LKBlockStarAltar() {
        super(Material.iron);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        setLightLevel(0.5F);
        setTickRandomly(true);
        setCreativeTab(LKCreativeTabs.tabDecorations); 
        setHardness(3.0F);
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
        altarIcons[0] = iconRegister.registerIcon("lionking:starAltar_side");
        altarIcons[1] = iconRegister.registerIcon("lionking:starAltar_top");
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return canBlockStay(world, x, y, z) && super.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        if (!world.isRemote) {
            super.onNeighborBlockChange(world, x, y, z, neighborBlock);
            checkAltarChange(world, x, y, z);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            checkAltarChange(world, x, y, z);
        }
    }

    protected final void checkAltarChange(World world, int x, int y, int z) {
        if (!canBlockStay(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return (world.getBlock(x, y - 1, z).isNormalCube() || world.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) &&
               world.canBlockSeeTheSky(x, y + 1, z) &&
               isRoom(world, x, y, z) &&
               world.provider.dimensionId == mod_LionKing.idPrideLands;
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
    public int getRenderType() {
        return mod_LionKing.proxy.getStarAltarRenderID();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        double posX = x + 0.5D;
        double posY = y + random.nextFloat() * 0.75F;
        double posZ = z + 0.5D;
        double motionX = (random.nextFloat() - 0.5D) * 0.5D;
        double motionY = (random.nextFloat() - 0.5D) * 0.5D;
        double motionZ = (random.nextFloat() - 0.5D) * 0.5D;
        int direction = random.nextInt(2) * 2 - 1;
        posX += 0.25D * direction;
        motionX = random.nextFloat() * 2.0F * direction;
        world.spawnParticle("portal", posX, posY, posZ, motionX, motionY, motionZ);
    }

    private boolean isRoom(World world, int x, int y, int z) {
        for (int dx = x - 1; dx <= x + 1; dx++) {
            for (int dy = y + 1; dy <= y + 2; dy++) {
                for (int dz = z - 1; dz <= z + 1; dz++) {
                    if (!world.isAirBlock(dx, dy, dz)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }
}
