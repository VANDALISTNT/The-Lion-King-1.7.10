package lionking.item;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.Item;
import lionking.client.LKCreativeTabs;

public class LKItemAxe extends ItemAxe {
    public LKItemAxe(Item.ToolMaterial material) {
        super(material);
        setCreativeTab(LKCreativeTabs.TAB_TOOLS);
        setUnlocalizedName("lkAxe");
    }
}