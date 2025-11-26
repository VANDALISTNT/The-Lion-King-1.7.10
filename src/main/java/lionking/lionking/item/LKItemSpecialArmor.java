package lionking.item;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;

import lionking.client.LKCreativeTabs;

public class LKItemSpecialArmor extends ItemArmor {
    public LKItemSpecialArmor(ItemArmor.ArmorMaterial material, int renderIndex, int armorType) {
        super(material, renderIndex, armorType);
        setCreativeTab(LKCreativeTabs.TAB_COMBAT);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        String materialName = this.getArmorMaterial().name().toLowerCase();
        switch (this.armorType) {
            case 0: 
                return "lionking:textures/items/" + materialName + "_helmet.png";
            case 1: 
                return "lionking:textures/items/" + materialName + "_body.png";
            case 2: 
                return "lionking:textures/items/" + materialName + "_legs.png";
            case 3: 
                return "lionking:textures/items/" + materialName + "_boots.png";
        }        
        return null;
    }
}
