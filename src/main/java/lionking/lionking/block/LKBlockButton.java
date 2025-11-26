package lionking.block;

import net.minecraft.block.BlockButtonStone;
import net.minecraft.util.IIcon; 
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKBlockButton extends BlockButtonStone {
    public LKBlockButton() { 
        super(); 
        setCreativeTab(LKCreativeTabs.tabMisc);
        setHardness(0.5F); 
        setStepSound(soundTypeStone); 
        setBlockName("prideButton"); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) { 
        return mod_LionKing.pridestone.getIcon(side, 0); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
    }
}