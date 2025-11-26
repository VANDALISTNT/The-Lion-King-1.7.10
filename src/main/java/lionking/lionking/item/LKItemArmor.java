package lionking.item;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.client.LKCreativeTabs;

public class LKItemArmor extends ItemArmor {
    public LKItemArmor(ItemArmor.ArmorMaterial material, int renderIndex, int armorType) {
        super(material, renderIndex, armorType);
        setCreativeTab(LKCreativeTabs.TAB_COMBAT);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        String materialName = this.getArmorMaterial().name().substring(3).toLowerCase();
        String layerSuffix = (armorType == 2) ? "_2" : "_1"; 
        return "lionking:textures/models/armor/" + materialName + layerSuffix + ".png";
    }
}