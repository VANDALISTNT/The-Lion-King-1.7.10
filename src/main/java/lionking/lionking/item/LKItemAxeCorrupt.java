package lionking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKItemAxeCorrupt extends LKItemAxe {
    private static final float BASE_EFFICIENCY = 0.15F;
    private static final float MAX_EFFICIENCY_BOOST = 0.85F;
    private static final float MIN_EFFICIENCY = 1.0F;

    public LKItemAxeCorrupt(Item.ToolMaterial material) {
        super(material);
	   setCreativeTab(LKCreativeTabs.TAB_TOOLS);
    }

    @Override
    public float func_150893_a(ItemStack itemStack, Block block) {
        int currentDamage = Math.max(itemStack.getItemDamage(), 0);
        int maxDamage = itemStack.getMaxDamage();

        float efficiency = BASE_EFFICIENCY + ((float) (maxDamage - currentDamage) / maxDamage * MAX_EFFICIENCY_BOOST);
        efficiency *= super.func_150893_a(itemStack, block);

        return Math.max(efficiency, MIN_EFFICIENCY);
    }
}
