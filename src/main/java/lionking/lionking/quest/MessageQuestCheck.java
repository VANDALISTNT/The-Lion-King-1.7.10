package lionking.quest;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

import lionking.mod_LionKing;
import lionking.common.LKLevelData;

public class MessageQuestCheck implements IMessage {
    private byte questIndex;

    public MessageQuestCheck() {}
    public MessageQuestCheck(byte index) {
        this.questIndex = index;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        questIndex = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(questIndex);
    }

    public static class Handler implements IMessageHandler<MessageQuestCheck, IMessage> {
        @Override
        public IMessage onMessage(MessageQuestCheck message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if (player != null) {
                if (message.questIndex >= 0 && message.questIndex < LKQuestBase.orderedQuests.size()) {
                    LKQuestBase quest = (LKQuestBase) LKQuestBase.orderedQuests.get(message.questIndex);
                    if (quest != null && quest.canStart()) {
                        int currentStage = quest.getQuestStage();
                        if (currentStage < quest.getNumStages()) {
                            LKLevelData.setQuestStage(player, message.questIndex, currentStage + 1);
                            quest.progress(currentStage + 1);
                            System.out.println("Server updated quest " + message.questIndex + " for player " + player.getCommandSenderName() + " to stage " + (currentStage + 1));
                            mod_LionKing.networkQuests.sendToAll(new MessageQuestStage((byte) message.questIndex, (byte) (currentStage + 1)));
                        }
                    }
                } else {
                    FMLLog.info("Invalid quest index %d for player %s", message.questIndex, player.getCommandSenderName());
                }
            }
            return null;
        }
    }
}