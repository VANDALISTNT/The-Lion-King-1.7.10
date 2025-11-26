package lionking.item;

import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.block.Block;
import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;
import lionking.tileentity.LKTileEntityMountedShooter;

public class LKItemMountedShooter extends LKItem {
    @SideOnly(Side.CLIENT)
    private IIcon[] shooterIcons;

    private static final String[] shooterTypes = {"wood", "silver"};

    public LKItemMountedShooter() {
        super();
        setCreativeTab(LKCreativeTabs.TAB_COMBAT);
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (l == 0 || !world.getBlock(i, j, k).getMaterial().isSolid()) {
            return false;
        }

        if (l == 1) j++;
        else if (l == 2) k--;
        else if (l == 3) k++;
        else if (l == 4) i--;
        else if (l == 5) i++;

        if (!entityplayer.canPlayerEdit(i, j, k, l, itemstack) || !mod_LionKing.mountedShooter.canPlaceBlockAt(world, i, j, k)) {
            return false;
        }

        Block block = mod_LionKing.mountedShooter;
        int rotation = MathHelper.floor_double((entityplayer.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        world.setBlock(i, j, k, block, rotation, 3);
        world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.func_150496_b(), 
                (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

        TileEntity tileentity = world.getTileEntity(i, j, k);
        if (tileentity instanceof LKTileEntityMountedShooter) {
            ((LKTileEntityMountedShooter) tileentity).setShooterType(itemstack.getItemDamage());
        }

        if (!entityplayer.capabilities.isCreativeMode) {
            itemstack.stackSize--;
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        int index = damage < 0 || damage >= shooterTypes.length ? 0 : damage;
        return shooterIcons[index];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        shooterIcons = new IIcon[shooterTypes.length];
        for (int i = 0; i < shooterTypes.length; i++) {
            shooterIcons[i] = iconRegister.registerIcon("lionking:mountedShooter_" + shooterTypes[i]);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        int damage = itemstack.getItemDamage();
        int index = damage < 0 || damage >= shooterTypes.length ? 0 : damage;
        return super.getUnlocalizedName() + "." + shooterTypes[index];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < shooterTypes.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}