package lionking.block;

import net.minecraft.block.BlockLever;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKBlockLever extends BlockLever {
    public LKBlockLever() {
        super();
        setCreativeTab(LKCreativeTabs.tabMisc);
        setBlockName("lever");
    }

    @Override
    public int getRenderType() {
        return mod_LionKing.proxy.getLeverRenderID();
    }
}