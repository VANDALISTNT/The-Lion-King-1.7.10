package lionking.block;

import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockPressurePlate.Sensitivity;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

public class LKBlockPressurePlate extends BlockPressurePlate {
    public LKBlockPressurePlate(String textureName, Sensitivity triggerType, Material material) {
        super(textureName, material, triggerType);
        setCreativeTab(LKCreativeTabs.tabMisc);
        setHardness(0.5F);
        setStepSound(material == Material.wood ? soundTypeWood : soundTypeStone);
        setBlockName("pressurePlate");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return mod_LionKing.pridestone.getIcon(side, 0);
    }
}
