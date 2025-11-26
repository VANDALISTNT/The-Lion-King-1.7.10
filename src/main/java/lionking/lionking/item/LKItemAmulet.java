package lionking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

public class LKItemAmulet extends LKItemArmor {
    public LKItemAmulet() {
        super(mod_LionKing.armorSuit, 0, 1);
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
        return "lionking:item/amulet.png";
    }
}
