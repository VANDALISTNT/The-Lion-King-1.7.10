package lionking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

public class LKItemQuestBlock extends ItemBlock {
    public LKItemQuestBlock(Block block) {
        super(block);
		setCreativeTab(LKCreativeTabs.TAB_QUEST);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }
}
