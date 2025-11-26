package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.client.LKCreativeTabs;

import java.util.List;

public class LKBlockWood2 extends LKBlockWood {
    @SideOnly(Side.CLIENT)
    private IIcon[][] woodIcons;

    private static final String[] WOOD_TYPES = {"banana", "deadwood"};

    public LKBlockWood2() { 
        super(); 
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(Block.soundTypeWood);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        int orientation = metadata & 12;
        int woodType = metadata & 3;

        if (woodType >= WOOD_TYPES.length) {
            woodType = 0;
        }

        if ((orientation == 0 && (side == 0 || side == 1)) ||
            (orientation == 4 && (side == 4 || side == 5)) ||
            (orientation == 8 && (side == 2 || side == 3))) {
            return woodIcons[woodType][0];
        }
        return woodIcons[woodType][1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        woodIcons = new IIcon[WOOD_TYPES.length][2];
        for (int i = 0; i < WOOD_TYPES.length; i++) {
            woodIcons[i][0] = iconRegister.registerIcon("lionking:wood_" + WOOD_TYPES[i] + "_top");
            woodIcons[i][1] = iconRegister.registerIcon("lionking:wood_" + WOOD_TYPES[i] + "_side");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) { 
        for (int i = 0; i < WOOD_TYPES.length; i++) {
            list.add(new ItemStack(this, 1, i)); 
        }
    }
}