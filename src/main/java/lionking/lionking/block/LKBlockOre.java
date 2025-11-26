package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.init.Items;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs; 

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class LKBlockOre extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon nukaIcon;

    @SideOnly(Side.CLIENT)
    private IIcon kivuliteIcon;

    public LKBlockOre() {
        super(Material.rock); 
        setBlockName("ore"); 
        setCreativeTab(LKCreativeTabs.TAB_BLOCK); 
        setHardness(3.0F);
        setResistance(5.0F);
        setStepSound(soundTypeStone);
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        if (this == mod_LionKing.prideCoal) {
            return metadata == 0 ? Items.coal : mod_LionKing.nukaShard;
        }
        return Item.getItemFromBlock(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (this == mod_LionKing.prideCoal && metadata == 1) {
            return nukaIcon;
        }
        if (this == mod_LionKing.oreSilver && metadata == 1) {
            return kivuliteIcon;
        }
        return blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("lionking:oreCoal");
        if (this == mod_LionKing.prideCoal) {
            nukaIcon = iconRegister.registerIcon("lionking:oreNuka");
        }
        if (this == mod_LionKing.oreSilver) {
            this.blockIcon = iconRegister.registerIcon("lionking:oreSilver");
            kivuliteIcon = iconRegister.registerIcon("lionking:oreKivulite");
        }
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float dropChance, int fortune) {
        super.dropBlockAsItemWithChance(world, x, y, z, metadata, dropChance, fortune);
        int xpAmount = 0;
        if (this == mod_LionKing.prideCoal) {
            xpAmount = MathHelper.getRandomIntegerInRange(world.rand, 0, metadata == 0 ? 2 : 1);
        }
        if (xpAmount > 0) {
            this.dropXpOnBlockBreak(world, x, y, z, xpAmount);
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        if (this == mod_LionKing.prideCoal && metadata == 1 && !EnchantmentHelper.getSilkTouchModifier(player)) {
            Random random = world.rand;
            int dropCount = random.nextInt(10) == 0 ? 1 : (random.nextInt(3) == 0 ? 2 : 3);
            for (int i = 0; i < dropCount; i++) {
                this.dropBlockAsItem(world, x, y, z, new ItemStack(mod_LionKing.nukaShard, 1, 0));
            }
        } else {
            super.harvestBlock(world, player, x, y, z, metadata);
        }
    }

    @Override
    public int damageDropped(int metadata) {
        return (this == mod_LionKing.oreSilver) ? metadata : 0;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune > 0 && this == mod_LionKing.prideCoal) {
            int bonus = random.nextInt(fortune + 2) - 1;
            return quantityDropped(random) * (Math.max(bonus, 0) + 1);
        }
        return quantityDropped(random);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        if (this == mod_LionKing.prideCoal || this == mod_LionKing.oreSilver) {
            for (int meta = 0; meta < 2; meta++) {
                list.add(new ItemStack(this, 1, meta));
            }
        } else {
            list.add(new ItemStack(this, 1, 0));
        }
    }

    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }
}
