package lionking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.util.StatCollector;

public class LKItemInfo {
     
    public static String[] getItemInfo(ItemStack itemstack) {
        if (itemstack == null) {
            return null; 
        }

        String baseKey = itemstack.getItem().getUnlocalizedName(itemstack);
        String infoKey = baseKey + ".info";
        String info = StatCollector.translateToLocal(infoKey);

        if (!info.equals(infoKey)) {
            return info.split("\n");
        }

        return null;
    }
}
