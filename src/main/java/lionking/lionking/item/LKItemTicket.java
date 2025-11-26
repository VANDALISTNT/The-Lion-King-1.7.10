package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumRarity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import lionking.client.LKCreativeTabs;
import lionking.block.LKBlockPortal; 
import lionking.block.LKBlockOutlandsPortal; 
import lionking.mod_LionKing; 

public class LKItemTicket extends LKItem {
    @SideOnly(Side.CLIENT)
    private IIcon crackerIcon;

    public LKItemTicket() {
        super();
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(LKCreativeTabs.TAB_QUEST);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return damage == 1 ? crackerIcon : itemIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
        crackerIcon = iconRegister.registerIcon("lionking:cracker");
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName() + "." + itemStack.getItemDamage();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (itemStack.getItemDamage() == 1) {
            if (!world.isRemote) {
                --itemStack.stackSize;
                player.inventory.addItemStackToInventory(new ItemStack(mod_LionKing.ticket));
                world.playSoundAtEntity(player, "lionking:pop", 1.0F, 1.0F);
            }
        }
        return itemStack;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (itemStack.getItemDamage() == 0) {
            if (!world.isRemote) {
                if (world.getBlock(x, y, z) == mod_LionKing.lionPortalFrame && world.isAirBlock(x, y + 1, z)) {
                    if (player.dimension == 0 || player.dimension == mod_LionKing.idPrideLands) {
                        if (LKBlockPortal.tryToCreatePortal(world, x, y + 1, z)) {
                            --itemStack.stackSize;
                            return true;
                        }
                    }
                }

                if (world.getBlock(x, y, z) == mod_LionKing.outlandsPortalFrame && world.isAirBlock(x, y + 1, z)) {
                    if (player.dimension == mod_LionKing.idOutlands || player.dimension == mod_LionKing.idPrideLands) {
                        if (LKBlockOutlandsPortal.tryToCreatePortal(world, x, y + 1, z)) {
                            --itemStack.stackSize;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.uncommon;
    }
}