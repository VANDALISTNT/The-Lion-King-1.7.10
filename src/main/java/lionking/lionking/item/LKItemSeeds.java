package lionking.item;

import net.minecraft.item.ItemSeeds;
import net.minecraft.block.Block;

import lionking.client.LKCreativeTabs;

public class LKItemSeeds extends ItemSeeds {
    public LKItemSeeds(Block crop, Block soil) {
        super(crop, soil);
        setCreativeTab(LKCreativeTabs.TAB_MATERIALS); 
    }
}
