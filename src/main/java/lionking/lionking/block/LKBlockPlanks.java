package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import lionking.client.LKCreativeTabs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

public class LKBlockPlanks extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] plankIcons;

    private static final String[] PLANK_TYPES = {"acacia", "rainforest", "mango", "passion", "banana", "deadwood"};

    public LKBlockPlanks() {
        super(Material.wood);
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(Block.soundTypeWood);
        setBlockName("planks");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (metadata >= PLANK_TYPES.length || metadata < 0) {
            metadata = 0;
        }
        return plankIcons[metadata];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        plankIcons = new IIcon[PLANK_TYPES.length];
        for (int i = 0; i < PLANK_TYPES.length; i++) {
            plankIcons[i] = iconRegister.registerIcon("lionking:planks_" + PLANK_TYPES[i]);
        }
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < PLANK_TYPES.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }
}
