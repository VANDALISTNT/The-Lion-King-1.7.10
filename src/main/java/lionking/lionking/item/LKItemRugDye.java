package lionking.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.client.LKCreativeTabs;

public class LKItemRugDye extends LKItem {
    @SideOnly(Side.CLIENT)
    private IIcon[] dyeIcons;

    private static final String[] dyeTypes = {
        "white", "blue", "yellow", "red", "purple", "lightBlue",
        "green", "black", "violet", "pink", "lightGreen"
    };

    public LKItemRugDye() {
        super();
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(LKCreativeTabs.TAB_MATERIALS);
        setUnlocalizedName("lionking:dye");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        int index = damage < 0 || damage >= dyeTypes.length ? 0 : damage;
        return dyeIcons[index];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        dyeIcons = new IIcon[dyeTypes.length];
        for (int i = 0; i < dyeTypes.length; i++) {
            dyeIcons[i] = iconRegister.registerIcon("lionking:rug_" + dyeTypes[i]);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int damage = itemStack.getItemDamage();
        int index = damage < 0 || damage >= dyeTypes.length ? 0 : damage;
        return super.getUnlocalizedName() + "_" + dyeTypes[index];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < dyeTypes.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }
}