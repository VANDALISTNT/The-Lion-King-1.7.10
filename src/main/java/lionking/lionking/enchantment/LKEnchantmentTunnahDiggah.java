package lionking.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import lionking.mod_LionKing;

public class LKEnchantmentTunnahDiggah extends Enchantment {
    public LKEnchantmentTunnahDiggah(int id, int weight) {
        super(id, weight, EnumEnchantmentType.digger);
        this.setName("tunnahDiggah");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinEnchantability(int level) {
        return 5;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return getMinEnchantability(level) + 50;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack != null && Item.getIdFromItem(stack.getItem()) == Item.getIdFromItem(mod_LionKing.tunnahDiggah);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canApply(stack);
    }
}