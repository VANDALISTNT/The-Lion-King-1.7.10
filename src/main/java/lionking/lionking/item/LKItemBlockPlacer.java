package lionking.item;

import net.minecraft.item.ItemReed;
import net.minecraft.block.Block;

import lionking.client.LKCreativeTabs;

public class LKItemBlockPlacer extends ItemReed {
    public LKItemBlockPlacer(Block block) {
        super(block); 
        setCreativeTab(LKCreativeTabs.TAB_DECORATIONS);
        setUnlocalizedName("lkBlockPlacer");
    }
}