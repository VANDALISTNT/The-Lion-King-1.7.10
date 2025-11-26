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
import lionking.entity.LKEntityTermite;

import java.util.List;
import java.util.Random;

public class LKBlockTermite extends LKBlock {
    @SideOnly(Side.CLIENT)
    private IIcon[] termiteIcons;

    public LKBlockTermite() {
        super(Material.ground);
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(0.5F);
        setResistance(2.5F);
        setStepSound(soundTypeGravel);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
        if (!world.isRemote && metadata == 0 && world.rand.nextBoolean()) {
            int termiteCount = world.rand.nextInt(3);
            for (int i = 0; i < termiteCount; i++) {
                spawnTermite(world, x, y, z);
            }
        }
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float dropChance, int fortune) {
        if (!world.isRemote && metadata == 0 && world.rand.nextBoolean()) {
            spawnTermite(world, x, y, z);
        }
        super.dropBlockAsItemWithChance(world, x, y, z, metadata, dropChance, fortune);
    }

    private void spawnTermite(World world, int x, int y, int z) {
        LKEntityTermite termite = new LKEntityTermite(world);
        termite.setLocationAndAngles(x + 0.5D, y, z + 0.5D, world.rand.nextFloat() * 360.0F, 0.0F);
        world.spawnEntityInWorld(termite);
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata == 1 ? 1 : 0;
    }

    @Override
    public int quantityDropped(int metadata, int fortune, Random random) {
        return metadata == 1 ? 1 : 0;
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        return metadata == 1 ? Item.getItemFromBlock(this) : null; 
    }

    @Override
    protected ItemStack createStackedBlock(int metadata) {
        return new ItemStack(this, 1, metadata);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) { 
        for (int i = 0; i < 2; i++) {
            list.add(new ItemStack(this, 1, i)); 
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return metadata == 1 ? termiteIcons[1] : termiteIcons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        termiteIcons = new IIcon[2];
        termiteIcons[0] = iconRegister.registerIcon("lionking:termite_mound");
        termiteIcons[1] = iconRegister.registerIcon("lionking:termite_mound_default");
    }
}
