package lionking.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKItemLeaves extends ItemBlock {
    public LKItemLeaves(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
		setCreativeTab(LKCreativeTabs.TAB_BLOCK);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata | 4;
    }
}
