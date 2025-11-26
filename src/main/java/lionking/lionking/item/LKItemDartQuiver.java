package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;
import lionking.inventory.LKInventoryQuiver;

public class LKItemDartQuiver extends LKItem {
    @SideOnly(Side.CLIENT)
    private IIcon dartQuiverIcon;

    public LKItemDartQuiver() {
        super();
        setMaxStackSize(1);
        setCreativeTab(LKCreativeTabs.TAB_COMBAT);
        setUnlocalizedName("dartQuiver");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            player.openGui(mod_LionKing.instance, mod_LionKing.proxy.GUI_ID_QUIVER, world, itemStack.getItemDamage(), 0, 0);
        }
        return itemStack;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isSelected) {
        if (world.isRemote) {
            ensureQuiverInventoryExists(world, itemStack.getItemDamage());
        }
    }

    public static LKInventoryQuiver getQuiverInventory(int damage, World world) {
        String key = "lk.quiver_" + damage;
        LKInventoryQuiver inventory = (LKInventoryQuiver) world.loadItemData(LKInventoryQuiver.class, key);
        if (inventory == null) {
            inventory = new LKInventoryQuiver(key);
            inventory.markDirty();
            world.setItemData(key, inventory);
        }
        return inventory;
    }

    private void ensureQuiverInventoryExists(World world, int damage) {
        String key = "lk.quiver_" + damage;
        LKInventoryQuiver inventory = (LKInventoryQuiver) world.loadItemData(LKInventoryQuiver.class, key);
        if (inventory == null) {
            inventory = new LKInventoryQuiver(key);
            inventory.markDirty();
            world.setItemData(key, inventory);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        dartQuiverIcon = iconRegister.registerIcon("lionking:dartQuiver");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return dartQuiverIcon;
    }
}