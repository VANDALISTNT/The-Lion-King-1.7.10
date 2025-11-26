package lionking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumRarity;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

public class LKItemOutlandsHelm extends LKItemArmor {
    public LKItemOutlandsHelm() {
        super(mod_LionKing.armorOutlandsHelm, 0, 0); 
        setMaxDamage(0);
        setMaxStackSize(1);
        setCreativeTab(LKCreativeTabs.TAB_QUEST);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "lionking:textures/armor/special.png"; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }
}
