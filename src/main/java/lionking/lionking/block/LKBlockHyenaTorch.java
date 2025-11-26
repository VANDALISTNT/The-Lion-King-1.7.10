package lionking.block;

import net.minecraft.block.BlockTorch;
import lionking.client.LKCreativeTabs;

public class LKBlockHyenaTorch extends BlockTorch {
    public LKBlockHyenaTorch() { 
        super(); 
        setCreativeTab(LKCreativeTabs.tabDecorations); 
        setBlockName("hyenaTorch"); 
    }
}