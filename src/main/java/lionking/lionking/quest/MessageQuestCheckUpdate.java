package lionking.quest;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lionking.quest.LKQuestBase;

public class MessageQuestCheckUpdate implements IMessage {
    private byte questIndex;
    private byte checked;

    public MessageQuestCheckUpdate() {}
    public MessageQuestCheckUpdate(byte questIndex, byte checked) {
        this.questIndex = questIndex;
        this.checked = checked;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        questIndex = buf.readByte();
        checked = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(questIndex);
        buf.writeByte(checked);
    }

    public static class Handler implements IMessageHandler<MessageQuestCheckUpdate, IMessage> {
        @Override
        public IMessage onMessage(MessageQuestCheckUpdate message, MessageContext ctx) {
            if (message.questIndex >= 0 && message.questIndex < LKQuestBase.allQuests.length) {
                LKQuestBase quest = LKQuestBase.allQuests[message.questIndex];
                if (quest != null) {
                    quest.checked = message.checked;
                }
            }
            return null;
        }
    }
}