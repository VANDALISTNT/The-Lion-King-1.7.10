package lionking.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import lionking.entity.LKEntityHyena;
import lionking.entity.LKEntitySkeletalHyenaHead;

public class LKEnchantmentHyena extends EnchantmentDamage {
    private static final int BASE_ENCHANTABILITY = 5;
    private static final int LEVEL_MULTIPLIER = 8;
    private static final int ENCHANTABILITY_RANGE = 20;
    private static final float DAMAGE_MULTIPLIER = 4.0F;

    public LKEnchantmentHyena(int id, int weight) {
        super(id, weight, 0);
        this.setName("hyena");
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
        return 5;
    }

    public float func_77318_a(int level, ItemStack stack, EntityLivingBase entity) {
        if (entity instanceof LKEntityHyena || entity instanceof LKEntitySkeletalHyenaHead) {
            return level * DAMAGE_MULTIPLIER;
        }
        return 0.0F;
    }

    @Override
    public boolean canApplyTogether(Enchantment other) {
        return !(other instanceof EnchantmentDamage);
    }
}
