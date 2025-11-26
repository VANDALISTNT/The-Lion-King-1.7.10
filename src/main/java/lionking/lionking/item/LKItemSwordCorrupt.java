package lionking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.client.LKCreativeTabs;

public class LKItemSwordCorrupt extends LKItemSword {
    public LKItemSwordCorrupt(Item.ToolMaterial material) {
        super(material);
        setCreativeTab(LKCreativeTabs.TAB_COMBAT);
        setUnlocalizedName("lionking:sword_corrupt_pridestone");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon("lionking:swordCorruptPridestone");
    }

    @Override
    public float func_150893_a(ItemStack itemStack, Block block) {
        int currentDamage = Math.max(itemStack.getItemDamage(), 0);
        float efficiency = 0.15F + ((float)(itemStack.getMaxDamage() - currentDamage) / itemStack.getMaxDamage()) * 0.85F;
        efficiency *= super.func_150893_a(itemStack, block);
        return Math.max(efficiency, 1.5F);
    }
}
