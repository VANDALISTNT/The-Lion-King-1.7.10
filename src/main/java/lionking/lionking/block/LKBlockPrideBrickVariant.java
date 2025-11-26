package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.client.LKCreativeTabs;

import java.util.List;

public class LKBlockPrideBrickVariant extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon normalIcon;

    @SideOnly(Side.CLIENT)
    private IIcon crackedIcon;

    public LKBlockPrideBrickVariant() {
        super(Material.rock);
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(2.0F);
        setResistance(10.0F);
        setStepSound(soundTypeStone);
        setBlockName("prideBrickVariant");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return metadata == 1 ? crackedIcon : normalIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        normalIcon = iconRegister.registerIcon("lionking:pridestoneBrick");
        crackedIcon = iconRegister.registerIcon("lionking:pridestoneBrickCracked");
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        if (world == null) {
            return 2.0F;
        }
        return 2.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 2; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }
}