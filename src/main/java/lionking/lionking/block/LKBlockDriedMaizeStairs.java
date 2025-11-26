package lionking.block;

import net.minecraft.block.Block;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKBlockDriedMaizeStairs extends LKBlockStairs {
    public LKBlockDriedMaizeStairs() {
        super(mod_LionKing.driedMaizeBlock, 0);
        setCreativeTab(LKCreativeTabs.tabBlock);
        setBlockName("stairsDriedMaize");
    }
}
