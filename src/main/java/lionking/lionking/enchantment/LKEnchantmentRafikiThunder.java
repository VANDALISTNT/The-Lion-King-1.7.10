package lionking.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import lionking.mod_LionKing;

public class LKEnchantmentRafikiThunder extends Enchantment {
    private static final int BASE_ENCHANTABILITY = 5;
    private static final int LEVEL_MULTIPLIER = 12;
    private static final int ENCHANTABILITY_RANGE = 50;

    public LKEnchantmentRafikiThunder(int id, int weight) {
        super(id, weight, EnumEnchantmentType.weapon);
        this.setName("rafikiThunder");
    }

    @Override
    public int getMinEnchantability(int level) {
        return BASE_ENCHANTABILITY + (level - 1) * LEVEL_MULTIPLIER;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return getMinEnchantability(level) + ENCHANTABILITY_RANGE;
    }

    @Override
    public int getMaxLevel() {
        return 3;
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