package lionking.item;

import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.block.material.Material; 
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.client.LKCreativeTabs; 
import lionking.mod_LionKing; 
import lionking.tileentity.LKTileEntityHyenaHead; 

import java.util.List;

public class LKItemHyenaHead extends LKItem {
    @SideOnly(Side.CLIENT)
    private IIcon[] headIcons;

    private static final int MAX_SUBTYPES = 4;

    public LKItemHyenaHead() {
        super(); 
        setCreativeTab(LKCreativeTabs.TAB_DECORATIONS);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return headIcons[Math.min(damage, MAX_SUBTYPES - 1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        headIcons = new IIcon[MAX_SUBTYPES]; 
        for (int i = 0; i < MAX_SUBTYPES; i++) {
            headIcons[i] = iconRegister.registerIcon("lionking:hyenaHead_" + i);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName() + "." + itemStack.getItemDamage();
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (side == 0 || !world.getBlock(x, y, z).getMaterial().isSolid()) { 
            return false;
        }

        int[] coords = adjustCoordinates(x, y, z, side);
        x = coords[0];
        y = coords[1];
        z = coords[2];

        if (!canPlaceHead(player, world, x, y, z, side, itemStack)) {
            return false;
        }

        placeHyenaHead(world, x, y, z, side, itemStack.getItemDamage(), player.rotationYaw);

        itemStack.stackSize--;
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) { 
        for (int i = 0; i < MAX_SUBTYPES; i++) {
            list.add(new ItemStack(this, 1, i)); 
        }
    }

    private int[] adjustCoordinates(int x, int y, int z, int side) {
        int[] coords = {x, y, z};
        switch (side) {
            case 1: coords[1]++; break;
            case 2: coords[2]--; break;
            case 3: coords[2]++; break;
            case 4: coords[0]--; break;
            case 5: coords[0]++; break;
        }
        return coords;
    }

    private boolean canPlaceHead(EntityPlayer player, World world, int x, int y, int z, int side, ItemStack stack) {
        return player.canPlayerEdit(x, y, z, side, stack) && mod_LionKing.hyenaHead.canPlaceBlockAt(world, x, y, z);
    }

    private void placeHyenaHead(World world, int x, int y, int z, int side, int hyenaType, float playerYaw) {
        world.setBlock(x, y, z, mod_LionKing.hyenaHead, side, 3); 
        int rotation = side == 1 ? MathHelper.floor_double(playerYaw * 16.0F / 360.0F + 0.5D) & 15 : 0;

        TileEntity tileEntity = world.getTileEntity(x, y, z); 
        if (tileEntity instanceof LKTileEntityHyenaHead) {
            LKTileEntityHyenaHead hyenaHead = (LKTileEntityHyenaHead) tileEntity;
            hyenaHead.setHyenaType(hyenaType);
            hyenaHead.setRotation(rotation);
        }
    }
}
