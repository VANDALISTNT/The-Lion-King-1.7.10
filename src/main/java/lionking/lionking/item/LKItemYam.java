package lionking.item;

import net.minecraft.potion.Potion;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

public class LKItemYam extends ItemSeedFood {
    public LKItemYam() {
        super(1, 0.4F, mod_LionKing.yamCrops, Blocks.farmland);
        setPotionEffect(Potion.hunger.id, 15, 0, 0.4F);
        setCreativeTab(LKCreativeTabs.TAB_FOOD);
    }
}