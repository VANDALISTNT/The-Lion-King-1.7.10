package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.client.LKCreativeTabs;

public class LKBlockStairs extends BlockStairs {
    private final Block baseBlock;
    private final int baseBlockMetadata;

    public LKBlockStairs(Block block, int metadata) {
        super(block, metadata);
        this.baseBlock = block;
        this.baseBlockMetadata = metadata;
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(block.getBlockHardness(null, 0, 0, 0));
        setResistance(block.getExplosionResistance(null));
        setStepSound(block.stepSound);
        setLightOpacity(255);
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return baseBlock.getBlockHardness(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return baseBlock.getIcon(side, baseBlockMetadata);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public int damageDropped(int metadata) {
        return baseBlockMetadata;
    }
}