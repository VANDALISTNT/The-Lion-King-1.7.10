package lionking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityLivingBase;
import com.google.common.collect.Multimap;
import lionking.client.LKCreativeTabs;

public class LKItemStaff extends LKItem {
    public LKItemStaff() {
        super();
        setCreativeTab(LKCreativeTabs.TAB_TOOLS);
        setFull3D();
        setMaxStackSize(1);
        setMaxDamage(600);
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitEntity, EntityLivingBase usingEntity) {
        itemstack.damageItem(1, usingEntity);
        return true;
    }

    @Override
    public Multimap getItemAttributeModifiers() {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
            new AttributeModifier(field_111210_e, "Weapon modifier", 5D, 0));
        return multimap;
    }
}