package lionking;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import lionking.entity.LKEntitySimba;
import net.minecraft.entity.Entity;

public class LKSimbaSit {

    public static SimpleNetworkWrapper simbaNetwork;

    public static void register() {
        simbaNetwork = NetworkRegistry.INSTANCE.newSimpleChannel("lk.simbaSit");
        simbaNetwork.registerMessage(SimbaSitHandler.class, SimbaSitMessage.class, 0, Side.SERVER);
    }

    // Класс сообщения для SimbaSit
    public static class SimbaSitMessage implements IMessage {
        private int entityId;
        private byte dimension;

        public SimbaSitMessage() {}

        public SimbaSitMessage(int entityId, byte dimension) {
            this.entityId = entityId;
            this.dimension = dimension;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            entityId = buf.readInt();
            dimension = buf.readByte();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(entityId);
            buf.writeByte(dimension);
        }
    }

    // Обработчик сообщения SimbaSit
    public static class SimbaSitHandler implements IMessageHandler<SimbaSitMessage, IMessage> {
        @Override
        public IMessage onMessage(SimbaSitMessage message, MessageContext ctx) {
            net.minecraft.entity.player.EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if (player.dimension == message.dimension) {
                Entity entity = player.worldObj.getEntityByID(message.entityId);
                if (entity instanceof LKEntitySimba) {
                    LKEntitySimba simba = (LKEntitySimba) entity;
                    simba.setSitting(!simba.isSitting()); // Переключаем состояние сидения
                }
            }
            return null; // Нет ответа клиенту
        }
    }
}