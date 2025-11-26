package lionking.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import lionking.entity.LKEntitySimba;

public class LKSimbaSit implements IMessage {
    private int entityId;
    private int dimension;

    public LKSimbaSit() {}

    public LKSimbaSit(int entityId, int dimension) {
        this.entityId = entityId;
        this.dimension = dimension;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        dimension = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(dimension);
    }

    public static class Handler implements IMessageHandler<LKSimbaSit, IMessage> {
        @Override
        public IMessage onMessage(LKSimbaSit message, MessageContext ctx) {
            if (ctx.side.isServer()) {
                Entity entity = ctx.getServerHandler().playerEntity.worldObj.getEntityByID(message.entityId);
                if (entity instanceof LKEntitySimba) {
                    LKEntitySimba simba = (LKEntitySimba) entity;
                    simba.setSitting(!simba.isSitting()); 
                }
            }
            return null; 
        }
    }
}