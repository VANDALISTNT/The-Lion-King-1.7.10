package lionking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKItemShovelCorrupt extends LKItemShovel {
    public LKItemShovelCorrupt(Item.ToolMaterial material) {
        super(material);
		setCreativeTab(LKCreativeTabs.TAB_TOOLS);
    }

    @Override
    public float func_150893_a(ItemStack itemStack, Block block) {
        int currentDamage = Math.max(itemStack.getItemDamage(), 0);

        float efficiency = 0.15F + ((float)(itemStack.getMaxDamage() - currentDamage) / itemStack.getMaxDamage()) * 0.85F;
        efficiency *= super.func_150893_a(itemStack, block);

        return Math.max(efficiency, 1.0F);
    }
}
