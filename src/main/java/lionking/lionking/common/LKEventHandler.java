package lionking.common;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import lionking.enchantment.LKEnchantmentHyena;

public class LKEventHandler {
    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {
        if (event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            EntityLivingBase target = event.entityLiving;
            int hyenaLevel = EnchantmentHelper.getEnchantmentLevel(100, player.getHeldItem());
            if (hyenaLevel > 0 && target != null) {
                LKEnchantmentHyena hyenaEnchant = (LKEnchantmentHyena) Enchantment.enchantmentsList[104];
                float extraDamage = hyenaEnchant.func_77318_a(hyenaLevel, player.getHeldItem(), target);
                if (extraDamage > 0.0F) {
                    event.ammount += extraDamage;
                }
            }
        }
    }
}
