package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

import lionking.mod_LionKing; 
import lionking.common.LKAchievementList;
import lionking.client.LKCreativeTabs;
import lionking.entity.LKEntityDart;
import lionking.entity.LKEntityPinkDart; 
import lionking.entity.LKEntityBlackDart; 
import lionking.entity.LKEntityRedDart; 
import lionking.entity.LKEntityYellowDart; 
import lionking.entity.LKEntityBlueDart; 

public class LKItemDartShooter extends LKItem {
    private int shootTick;
    private final boolean isSilver;
    private static final int REGULAR_MAX_DAMAGE = 214;
    private static final int SILVER_MAX_DAMAGE = 286;
    private static final int REGULAR_COOLDOWN = 20;
    private static final int SILVER_COOLDOWN = 12;

    public LKItemDartShooter(boolean isSilver) {
        super(); 
        this.isSilver = isSilver;
        maxStackSize = 1;
        setMaxDamage(isSilver ? SILVER_MAX_DAMAGE : REGULAR_MAX_DAMAGE);
        shootTick = REGULAR_COOLDOWN;
        setCreativeTab(LKCreativeTabs.TAB_COMBAT);
        setFull3D();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (tryShootDart(itemStack, player, world, mod_LionKing.dartPink, LKEntityPinkDart.class, 2.0F) ||
            tryShootDart(itemStack, player, world, mod_LionKing.dartBlack, LKEntityBlackDart.class, 2.0F) ||
            tryShootDart(itemStack, player, world, mod_LionKing.dartRed, LKEntityRedDart.class, 2.0F) ||
            tryShootDart(itemStack, player, world, mod_LionKing.dartYellow, LKEntityYellowDart.class, 2.0F) ||
            tryShootDart(itemStack, player, world, mod_LionKing.dartBlue, LKEntityBlueDart.class, 1.5F)) {
            return itemStack;
        }
        return itemStack;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isSelected) {
        if (shootTick < REGULAR_COOLDOWN) {
            shootTick++;
        }
    }

    private boolean canShoot() {
        return isSilver ? shootTick >= SILVER_COOLDOWN : shootTick >= REGULAR_COOLDOWN;
    }

    private boolean tryShootDart(ItemStack itemStack, EntityPlayer player, World world, Item dartItem, Class<? extends LKEntityDart> dartClass, float speed) {
        if (!player.inventory.hasItem(dartItem) || !canShoot()) {
            return false;
        }

        LKEntityDart dart = createDart(dartClass, world, player, speed);
        if (dart == null) {
            return false;
        }

        world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);

        if (!player.capabilities.isCreativeMode) {
            player.inventory.consumeInventoryItem(dartItem);
            itemStack.damageItem(1, player);
        }

        if (!world.isRemote) {
            world.spawnEntityInWorld(dart);
            player.addStat(LKAchievementList.shootDart, 1); 
            shootTick = 0;
        }
        return true;
    }

    private LKEntityDart createDart(Class<? extends LKEntityDart> dartClass, World world, EntityPlayer player, float speed) {
        try {
            return dartClass.getConstructor(World.class, EntityPlayer.class, float.class, boolean.class)
                    .newInstance(world, player, speed, isSilver);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
