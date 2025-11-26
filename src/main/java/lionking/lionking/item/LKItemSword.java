package lionking.item;

import net.minecraft.item.ItemSword;
import net.minecraft.item.Item;

import lionking.client.LKCreativeTabs;

public class LKItemSword extends ItemSword {
    public LKItemSword(Item.ToolMaterial material) {
        super(material);
        setCreativeTab(LKCreativeTabs.TAB_COMBAT);
    }
}
