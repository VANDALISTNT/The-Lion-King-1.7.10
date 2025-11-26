package lionking.item;

import net.minecraft.item.Item;

import lionking.client.LKCreativeTabs;

public class LKItem extends Item {
    public LKItem() {
        super();
        setCreativeTab(LKCreativeTabs.TAB_MISC);
        setUnlocalizedName("lkItem");
    }
}
