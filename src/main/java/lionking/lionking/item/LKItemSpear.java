package lionking.item;

import net.minecraft.world.World;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityLivingBase;
import com.google.common.collect.Multimap;

import lionking.client.LKCreativeTabs;
import lionking.entity.LKEntityPoisonedSpear;
import lionking.entity.LKEntityGemsbokSpear;

public class LKItemSpear extends LKItem {
    private final boolean poisoned;

    public LKItemSpear(boolean flag) {
        super();
        maxStackSize = 1;
        setMaxDamage(160);
        poisoned = flag;
        setCreativeTab(LKCreativeTabs.TAB_COMBAT);
        setFull3D();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!world.isRemote) {
            Object spear = poisoned
                ? new LKEntityPoisonedSpear(world, entityplayer, 2.0F, itemstack.getItemDamage())
                : new LKEntityGemsbokSpear(world, entityplayer, 2.0F, itemstack.getItemDamage());

            world.spawnEntityInWorld((net.minecraft.entity.Entity) spear);
            world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
        }
        return itemstack;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitEntity, EntityLivingBase user) {
        if (!hitEntity.worldObj.isRemote) {
            itemstack.damageItem(1, user);
            if (poisoned && itemRand.nextInt(3) != 0) {
                hitEntity.addPotionEffect(new PotionEffect(Potion.poison.id, (itemRand.nextInt(3) + 1) * 20, 0));
            }
        }
        return true;
    }

    @Override
    public Multimap getItemAttributeModifiers() {
        Multimap multimap = super.getItemAttributeModifiers();
        double damage = poisoned ? 3D : 4D;
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
            new AttributeModifier(field_111210_e, "Weapon modifier", damage, 0));
        return multimap;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return poisoned ? "item.spear.poisoned" : "item.spear.normal";
    }
}