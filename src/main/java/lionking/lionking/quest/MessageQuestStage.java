package lionking.quest;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lionking.quest.LKQuestBase;

public class MessageQuestStage implements IMessage {
    private byte questIndex;
    private byte stage;

    public MessageQuestStage() {}
    public MessageQuestStage(byte questIndex, byte stage) {
        this.questIndex = questIndex;
        this.stage = stage;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        questIndex = buf.readByte();
        stage = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(questIndex);
        buf.writeByte(stage);
    }

    public static class Handler implements IMessageHandler<MessageQuestStage, IMessage> {
        @Override
        public IMessage onMessage(MessageQuestStage message, MessageContext ctx) {
            if (message.questIndex >= 0 && message.questIndex < LKQuestBase.allQuests.length) {
                LKQuestBase quest = LKQuestBase.allQuests[message.questIndex];
                if (quest != null) {
                    quest.currentStage = message.stage;
                }
            }
            return null;
        }
    }
}