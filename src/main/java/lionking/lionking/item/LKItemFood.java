package lionking.item;

import net.minecraft.item.ItemFood;

import lionking.client.LKCreativeTabs;

public class LKItemFood extends ItemFood {
    public LKItemFood(int hunger, float saturation, boolean isWolfFood) {
        super(hunger, saturation, isWolfFood);
        setCreativeTab(LKCreativeTabs.TAB_FOOD);
        setUnlocalizedName("lkFood");
    }
}