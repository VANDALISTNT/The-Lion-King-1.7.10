package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.common.LKIngame;
import lionking.common.LKLevelData;
import lionking.client.LKCreativeTabs;

import java.util.List;

public class LKBlockRafikiWood extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] treeIcons;

    @SideOnly(Side.CLIENT)
    private IIcon[] corruptTreeIcons;

    @SideOnly(Side.CLIENT)
    private IIcon[] christmasIcons;

    private static final String[] WOOD_TYPES = {"side", "log", "top"};

    public LKBlockRafikiWood() {
        super(Material.wood);
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeWood);
        setBlockName("rafikiWood");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 3; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        treeIcons = new IIcon[WOOD_TYPES.length];
        corruptTreeIcons = new IIcon[WOOD_TYPES.length];
        christmasIcons = new IIcon[2];
        for (int i = 0; i < WOOD_TYPES.length; i++) {
            treeIcons[i] = iconRegister.registerIcon("lionking:rafikiWood_" + WOOD_TYPES[i]);
            corruptTreeIcons[i] = iconRegister.registerIcon("lionking:rafikiWood_" + WOOD_TYPES[i] + "_corrupt");
        }
        christmasIcons[0] = iconRegister.registerIcon("lionking:rafikiWood_christmas_side");
        christmasIcons[1] = iconRegister.registerIcon("lionking:rafikiWood_christmas_top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        boolean isZiraOccupied = LKLevelData.ziraStage >= 14 && LKLevelData.ziraStage < 19;
        IIcon[] icons = isZiraOccupied ? corruptTreeIcons : treeIcons;

        if (metadata == 1) {
            if (side == 0 || side == 1) {
                return icons[1];
            }
        } else if (metadata == 2) {
            return icons[2];
        }
        return icons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int metadata = world.getBlockMetadata(x, y, z);
        boolean isZiraOccupied = LKLevelData.ziraStage >= 14 && LKLevelData.ziraStage < 19;
        IIcon[] icons = isZiraOccupied ? corruptTreeIcons : treeIcons;

        if (metadata == 1) {
            if (side == 0 || side == 1) {
                return icons[1];
            }
        } else if (metadata == 2) {
            if (isZiraOccupied) {
                return icons[2];
            }
            if (LKIngame.isChristmas()) {
                if (side == 1) {
                    return christmasIcons[1];
                }
                if (side > 1) {
                    return christmasIcons[0];
                }
            }
            return icons[2];
        }
        return icons[0];
    }

    @Override
    public int getMobilityFlag() {
        return 2;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }
}