package lionking.item;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumRarity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;
import lionking.common.LKIngame;
import lionking.common.LKLevelData;
import lionking.common.LKAchievementList;
import lionking.entity.LKEntityLightning;
import lionking.entity.LKEntitySimba;
import lionking.quest.LKQuestBase; 

public class LKItemDust extends LKItem {
    public LKItemDust() {
        super();
        setCreativeTab(LKCreativeTabs.TAB_QUEST); 
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (isUseBlocked(player)) {
            return false;
        }

        if (world.getBlock(x, y, z) != mod_LionKing.starAltar) {
            return false;
        }

        itemStack.stackSize--;
        spawnEffectsAndEntities(world, player, x, y, z);
        updateQuestProgress(player);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.uncommon;
    }

    private boolean isUseBlocked(EntityPlayer player) {
        return (LKQuestBase.rafikiQuest.getQuestStage() == 5 && LKQuestBase.rafikiQuest.isDelayed()) ||
               LKLevelData.hasSimba(player);
    }

    private void spawnEffectsAndEntities(World world, EntityPlayer player, int x, int y, int z) {
        LKEntityLightning lightning = new LKEntityLightning(player, world, x, y, z, 0);
        if (!world.isRemote) {
            world.spawnEntityInWorld(lightning);
        }

        world.createExplosion(player, x, y + 1, z, 0F, false);

        LKEntitySimba simba = new LKEntitySimba(world);
        simba.setLocationAndAngles(x + 0.5, y + 1, z + 0.5, 0F, 0F);
        simba.setAge(-36000);
        simba.setHealth(15);
        simba.setOwnerName(player.getCommandSenderName());
        if (!world.isRemote) {
            world.spawnEntityInWorld(simba);
        }

        player.addStat(LKAchievementList.simba, 1);
    }

    private void updateQuestProgress(EntityPlayer player) {
        if (LKQuestBase.rafikiQuest.getQuestStage() == 6) {
            LKQuestBase.rafikiQuest.progress(7);
            LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("message.lionking.rafiki.simba"));
        }
    }
}
