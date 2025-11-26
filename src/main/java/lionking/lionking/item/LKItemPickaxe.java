package lionking.item;

import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.Item;

import lionking.client.LKCreativeTabs;

public class LKItemPickaxe extends ItemPickaxe {
    public LKItemPickaxe(Item.ToolMaterial material) {
        super(material); 
        setCreativeTab(LKCreativeTabs.TAB_TOOLS); 
    }
}
