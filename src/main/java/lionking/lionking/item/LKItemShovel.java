package lionking.item;

import net.minecraft.item.ItemSpade;
import net.minecraft.item.Item;

import lionking.client.LKCreativeTabs;

public class LKItemShovel extends ItemSpade {
    public LKItemShovel(Item.ToolMaterial material) {
        super(material);
        setCreativeTab(LKCreativeTabs.TAB_TOOLS); 
    }
}
