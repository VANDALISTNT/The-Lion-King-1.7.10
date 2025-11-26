package lionking.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.EntityCreature;

import lionking.common.LKAngerable;
import lionking.entity.LKEntityOutlander;
import lionking.mod_LionKing; 

public class LKEntityAIAngerableAttackableTarget extends EntityAINearestAttackableTarget {
    public LKEntityAIAngerableAttackableTarget(EntityCreature entity, Class entityclass, int i, boolean flag) {
        super(entity, entityclass, i, flag);
    }

    @Override
    public boolean shouldExecute() {
        if (((LKAngerable) taskOwner).isHostile()) {
            if (taskOwner instanceof LKEntityOutlander && !((LKEntityOutlander) taskOwner).inMound) {
                if (super.shouldExecute()) {
                    if (taskOwner.getAttackTarget() instanceof EntityPlayer) {
                        EntityPlayer entityplayer = (EntityPlayer) taskOwner.getAttackTarget();
                        ItemStack helmet = entityplayer.inventory.armorItemInSlot(3);
                        if (helmet != null && Item.getIdFromItem(helmet.getItem()) == Item.getIdFromItem(mod_LionKing.outlandsHelm)) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
            return super.shouldExecute();
        }
        return false;
    }
}