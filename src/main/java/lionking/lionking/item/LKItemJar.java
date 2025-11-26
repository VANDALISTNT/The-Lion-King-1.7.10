package lionking.item;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import lionking.mod_LionKing; 
import lionking.entity.LKEntityZebra;
import lionking.client.LKCreativeTabs;

public class LKItemJar extends LKItem {
    private final Block containedBlock; 

    public LKItemJar(Block block) {
        super();
        setMaxStackSize(1);
		setCreativeTab(LKCreativeTabs.TAB_MISC);
        this.containedBlock = block;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        MovingObjectPosition m = getMovingObjectPositionFromPlayer(world, entityplayer, containedBlock == null);

        if (m == null) {
            return itemstack;
        }

        if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int x = m.blockX;
            int y = m.blockY;
            int z = m.blockZ;

            if (!world.canMineBlock(entityplayer, x, y, z)) {
                return itemstack;
            }

            if (containedBlock == null) {
                if (!entityplayer.canPlayerEdit(x, y, z, m.sideHit, itemstack)) {
                    return itemstack;
                }

                Material material = world.getBlock(x, y, z).getMaterial();
                int meta = world.getBlockMetadata(x, y, z);

                if (material == Material.water && meta == 0) {
                    world.setBlockToAir(x, y, z);
                    return entityplayer.capabilities.isCreativeMode ? itemstack : new ItemStack(mod_LionKing.jarWater);
                } else if (material == Material.lava && meta == 0) {
                    world.setBlockToAir(x, y, z);
                    return entityplayer.capabilities.isCreativeMode ? itemstack : new ItemStack(mod_LionKing.jarLava);
                }
            } else {

                switch (m.sideHit) {
                    case 0: y--; break;
                    case 1: y++; break;
                    case 2: z--; break;
                    case 3: z++; break;
                    case 4: x--; break;
                    case 5: x++; break;
                }

                if (!entityplayer.canPlayerEdit(x, y, z, m.sideHit, itemstack)) {
                    return itemstack;
                }

                if (world.isAirBlock(x, y, z) || !world.getBlock(x, y, z).getMaterial().isSolid()) {
                    if (world.provider.isHellWorld && containedBlock == Blocks.flowing_water) {
                        world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.fizz", 0.5F,
                                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                        for (int l = 0; l < 8; l++) {
                            world.spawnParticle("largesmoke", x + Math.random(), y + Math.random(), z + Math.random(),
                                    0.0D, 0.0D, 0.0D);
                        }
                    } else {
                        world.setBlock(x, y, z, containedBlock, 0, 3);
                    }
                    return entityplayer.capabilities.isCreativeMode ? itemstack : new ItemStack(mod_LionKing.jar);
                }
            }
        } else if (containedBlock == null && m.entityHit instanceof LKEntityZebra) {
            return new ItemStack(mod_LionKing.jarMilk);
        }

        return itemstack;
    }
}
