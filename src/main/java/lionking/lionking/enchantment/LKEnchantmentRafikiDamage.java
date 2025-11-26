package lionking.enchantment;

import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import lionking.mod_LionKing;

public class LKEnchantmentRafikiDamage extends EnchantmentDamage {
    public LKEnchantmentRafikiDamage(int id, int weight) {
        super(id, weight, 0);
        this.setName("rafikiDamage");
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack != null && Item.getIdFromItem(stack.getItem()) == Item.getIdFromItem(mod_LionKing.rafikiStick);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canApply(stack);
    }
}