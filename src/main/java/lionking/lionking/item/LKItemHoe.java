package lionking.item;

import net.minecraft.item.ItemHoe;
import net.minecraft.item.Item;

import lionking.client.LKCreativeTabs;

public class LKItemHoe extends ItemHoe {
    public LKItemHoe(Item.ToolMaterial material) {
        super(material); 
        setCreativeTab(LKCreativeTabs.TAB_TOOLS);
        setUnlocalizedName("lkHoe." + material.name().toLowerCase());
    }
}
