package lionking.item;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.EnumHelper;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

public class LKItemTicketLionSuit extends LKItemArmor {

    public LKItemTicketLionSuit(int armorType) {
        super(mod_LionKing.TICKET_LION_SUIT, 0, armorType);
        setMaxDamage(0);
        setMaxStackSize(1);
        setCreativeTab(LKCreativeTabs.TAB_MISC);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        switch (this.armorType) {
            case 0: 
                return "lionking:textures/items/TicketLionSuit_helmet.png";
            case 1: 
                return "lionking:textures/items/TicketLionSuit_body.png";
            case 2: 
                return "lionking:textures/items/TicketLionSuit_legs.png";
            case 3:
                return "lionking:textures/items/TicketLionSuit_boots.png";
        }
        return null;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        switch (armorType) {
            case 0: return "item.helmetTicketLion";
            case 1: return "item.bodyTicketLion";
            case 2: return "item.legsTicketLion";
            case 3: return "item.bootsTicketLion";
            default: return "item.helmetTicketLion";
        }
    }
}
