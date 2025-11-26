package lionking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityGiraffe;
import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

import java.util.List;

public class LKItemGiraffeTie extends LKItem {
    @SideOnly(Side.CLIENT)
    private IIcon[] tieIcons;

    private static final String[] TIE_TYPES = {"base", "white", "blue", "yellow", "red", "purple", "green", "black"};
    private static final int MAX_SUBTYPES = TIE_TYPES.length;

    public LKItemGiraffeTie() {
        super(); 
        setMaxDamage(0);
        setHasSubtypes(true);
		setCreativeTab(LKCreativeTabs.TAB_MISC);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return tieIcons[Math.min(damage, MAX_SUBTYPES - 1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        tieIcons = new IIcon[MAX_SUBTYPES];
        for (int i = 0; i < MAX_SUBTYPES; i++) {
            tieIcons[i] = iconRegister.registerIcon("lionking:tie_" + TIE_TYPES[i]);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName() + "." + itemStack.getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) { 
        for (int i = 0; i < MAX_SUBTYPES; i++) {
            list.add(new ItemStack(this, 1, i)); 
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity) {
        if (!(entity instanceof LKEntityGiraffe)) {
            return false;
        }

        LKEntityGiraffe giraffe = (LKEntityGiraffe) entity;
        if (canEquipTie(giraffe)) {
            giraffe.setTie(itemStack.getItemDamage());
            itemStack.stackSize--;
        }
        return true;
    }

    private boolean canEquipTie(LKEntityGiraffe giraffe) {
        return giraffe.getTie() == -1 && !giraffe.isChild() && giraffe.getSaddled();
    }
}
