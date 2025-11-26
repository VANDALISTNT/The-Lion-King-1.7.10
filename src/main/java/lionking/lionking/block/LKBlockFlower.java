package lionking.block;

import net.minecraft.block.BlockFlower;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.List;
import lionking.client.LKCreativeTabs;

public class LKBlockFlower extends BlockFlower {
    public LKBlockFlower() { 
        super(0); 
        setCreativeTab(LKCreativeTabs.tabDecorations); 
        setBlockName("lionFlower"); 
    }
	
	@Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }
	
}