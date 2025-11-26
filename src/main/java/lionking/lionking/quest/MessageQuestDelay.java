package lionking.quest;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lionking.quest.LKQuestBase;

public class MessageQuestDelay implements IMessage {
    private byte questIndex;
    private byte delayed;

    public MessageQuestDelay() {}

    public MessageQuestDelay(byte questIndex, byte delayed) {
        this.questIndex = questIndex;
        this.delayed = delayed;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        questIndex = buf.readByte();
        delayed = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(questIndex);
        buf.writeByte(delayed);
    }

    public static class Handler implements IMessageHandler<MessageQuestDelay, IMessage> {
        @Override
        public IMessage onMessage(MessageQuestDelay message, MessageContext ctx) {
            if (message.questIndex >= 0 && message.questIndex < LKQuestBase.allQuests.length) {
                LKQuestBase quest = LKQuestBase.allQuests[message.questIndex];
                if (quest != null) {
                    quest.stagesDelayed = message.delayed;
                }
            }
            return null;
        }
    }
}