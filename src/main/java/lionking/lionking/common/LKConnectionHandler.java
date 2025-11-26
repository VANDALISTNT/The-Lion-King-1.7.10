package lionking.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.entity.player.EntityPlayerMP;
import lionking.client.LKClientProxy;
import lionking.client.LKPacketHandlerClient;
import lionking.common.LKLevelData;
import lionking.quest.LKQuestBase;

public class LKConnectionHandler {

    public LKConnectionHandler() {
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
        EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity; 
        LKLevelData.sendLoginPacket(player);
        LKClientProxy.NETWORK.sendTo(
            new LKPacketHandlerClient.LoginMessage(
                LKLevelData.homePortalX, LKLevelData.homePortalY, LKLevelData.homePortalZ,
                LKLevelData.moundX, LKLevelData.moundY, LKLevelData.moundZ,
                (byte) LKLevelData.defeatedScar, (byte) LKLevelData.ziraStage, (byte) LKLevelData.pumbaaStage, 
                (byte) LKLevelData.outlandersHostile, (byte) LKLevelData.flatulenceSoundsRemaining, 
                getQuestData()
            ),
            player
        );
    }

    private byte[][] getQuestData() {
        byte[][] questData = new byte[16][19];
        for (int i = 0; i < 16; i++) {
            LKQuestBase quest = LKQuestBase.allQuests[i];
            if (quest != null) {
                questData[i][0] = (byte) quest.stagesDelayed; 
                questData[i][1] = (byte) quest.currentStage; 
                questData[i][2] = (byte) (quest.checked != 0 ? 1 : 0); 
                for (int j = 0; j < Math.min(16, quest.stagesCompleted.length); j++) {
                    questData[i][3 + j] = (byte) quest.stagesCompleted[j]; 
                }
            }
        }
        return questData;
    }
}