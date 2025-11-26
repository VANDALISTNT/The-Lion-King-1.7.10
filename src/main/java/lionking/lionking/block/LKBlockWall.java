package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

import java.util.List;

public class LKBlockWall extends BlockWall {
    public LKBlockWall() {
        super(mod_LionKing.pridestone); 
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(1.5F);
        setResistance(10.0F);
        setStepSound(Block.soundTypeStone);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        switch (metadata) {
            case 1: return mod_LionKing.prideBrick.getIcon(side, 0);
            case 2: return mod_LionKing.prideBrickMossy.getIcon(side, 0);
            case 3: return mod_LionKing.pridestone.getIcon(side, 1);
            case 4: return mod_LionKing.prideBrick.getIcon(side, 1);
            case 0:
            default: return mod_LionKing.pridestone.getIcon(side, 0);
        }
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 3 || metadata == 4) {
            return 1.05F;
        }
        return 1.5F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) { 
        for (int i = 0; i <= 4; i++) {
            list.add(new ItemStack(this, 1, i)); 
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }
}