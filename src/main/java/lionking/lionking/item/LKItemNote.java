package lionking.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.client.LKCreativeTabs;

import java.util.List;

public class LKItemNote extends Item {
    @SideOnly(Side.CLIENT)
    private IIcon[] noteIcons;

    private static final String[] noteNames = {"a", "b", "c", "d", "e", "f", "g"};

    public LKItemNote() {
        super();
        setCreativeTab(LKCreativeTabs.TAB_MISC);
        setHasSubtypes(true);
        setUnlocalizedName("lionking:note");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        noteIcons = new IIcon[noteNames.length];
        for (int i = 0; i < noteNames.length; i++) {
            noteIcons[i] = iconRegister.registerIcon("lionking:note_" + noteNames[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        int index = damage < 0 || damage >= noteNames.length ? 0 : damage;
        return noteIcons[index];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int index = stack.getItemDamage();
        index = index < 0 || index >= noteNames.length ? 0 : index;
        return super.getUnlocalizedName() + "_" + noteNames[index];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < noteNames.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    public static int getNoteValue(int damage) {
        int index = damage < 0 || damage >= noteNames.length ? 0 : damage;
        return index;
    }
}