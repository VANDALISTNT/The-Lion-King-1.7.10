package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.client.LKCreativeTabs;
import lionking.common.LKCompatibility;
import java.util.List;

public class LKBlockWood extends LKBlock {
    @SideOnly(Side.CLIENT)
    private IIcon[][] woodIcons;
    private static final String[] WOOD_TYPES = {"acacia", "rainforest", "mango", "passion"};

    public LKBlockWood() {
        super(Material.wood);
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeWood);
        setBlockName("wood");
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (!world.isRemote) {
            if (LKCompatibility.isTimberModInstalled) {
                LKCompatibility.timber(world, x, y, z, Block.getIdFromBlock(this));
            }

            final int radius = 4;
            final int range = radius + 1;
            if (world.checkChunksExist(x - range, y - range, z - range, x + range, y + range, z + range)) {
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        for (int dz = -radius; dz <= radius; dz++) {
                            Block nearbyBlock = world.getBlock(x + dx, y + dy, z + dz);
                            if (nearbyBlock instanceof LKBlockLeaves) {
                                int nearbyMetadata = world.getBlockMetadata(x + dx, y + dy, z + dz);
                                if ((nearbyMetadata & 8) == 0) {
                                    world.setBlockMetadataWithNotify(x + dx, y + dy, z + dz, nearbyMetadata | 8, 4);
                                }
                            }
                        }
                    }
                }
            }
        }
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
    public int getRenderType() {
        return 31;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        int woodType = metadata & 3;
        int orientation;
        switch (side) {
            case 0:
            case 1:
                orientation = 0;
                break;
            case 2:
            case 3:
                orientation = 8;
                break;
            case 4:
            case 5:
                orientation = 4;
                break;
            default:
                orientation = 0;
        }
        return woodType | orientation;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata & 3;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < WOOD_TYPES.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    protected ItemStack createStackedBlock(int metadata) {
        return new ItemStack(this, 1, damageDropped(metadata));
    }
}
