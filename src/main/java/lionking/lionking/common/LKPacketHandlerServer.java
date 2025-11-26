package lionking.common;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraftforge.common.DimensionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import java.nio.ByteBuffer;

import lionking.entity.LKEntitySimba;
import lionking.quest.LKQuestBase;
import lionking.mod_LionKing;

public class LKPacketHandlerServer {

    public static abstract class LKServerMessage implements IMessage {
        protected abstract void process(MessageContext ctx);
    }

    public static class SimbaSitMessage extends LKServerMessage {
        private int entityId;
        private int dimension;

        public SimbaSitMessage() {}

        public SimbaSitMessage(int entityId, int dimension) {
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

        @Override
        protected void process(MessageContext ctx) {
            Entity entity = DimensionManager.getWorld(dimension).getEntityByID(entityId);
            if (entity instanceof LKEntitySimba) {
                LKEntitySimba simba = (LKEntitySimba) entity;
                simba.setSitting(!simba.isSitting());
            }
        }

        public static class Handler implements IMessageHandler<SimbaSitMessage, IMessage> {
            @Override
            public IMessage onMessage(SimbaSitMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    public static class DamageItemMessage extends LKServerMessage {
        private int entityId;
        private int dimension;
        private byte type;

        public DamageItemMessage() {}

        public DamageItemMessage(int entityId, int dimension, byte type) {
            this.entityId = entityId;
            this.dimension = dimension;
            this.type = type;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            entityId = buf.readInt();
            dimension = buf.readInt();
            type = buf.readByte();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(entityId);
            buf.writeInt(dimension);
            buf.writeByte(type);
        }

        @Override
        protected void process(MessageContext ctx) {
            Entity entity = DimensionManager.getWorld(dimension).getEntityByID(entityId);
            if (!(entity instanceof EntityPlayerMP)) return;

            EntityPlayerMP player = (EntityPlayerMP) entity;
            ItemStack item = getTargetItem(player, type);
            if (item != null && isValidItem(item, type)) {
                damageAndCheckItem(player, item, type);
            }
        }

        private ItemStack getTargetItem(EntityPlayerMP player, byte type) {
            return type == 0 ? player.inventory.armorItemInSlot(0) :
                   type == 1 ? player.inventory.armorItemInSlot(2) : null;
        }

        private boolean isValidItem(ItemStack item, byte type) {
            return (type == 0 && item.getItem() == mod_LionKing.zebraBoots) ||
                   (type == 1 && item.getItem() == mod_LionKing.wings);
        }

        private void damageAndCheckItem(EntityPlayerMP player, ItemStack item, byte type) {
            item.damageItem(1, player);
            if (item.getItemDamage() >= item.getMaxDamage()) {
                int slot = type == 0 ? 36 : 38;
                LKIngame.sendBreakItemPacket(player, type == 0 ? 0 : 1);
                player.inventory.setInventorySlotContents(slot, null);
                player.addStat(StatList.objectBreakStats[Item.getIdFromItem(item.getItem())], 1);
            }
        }

        public static class Handler implements IMessageHandler<DamageItemMessage, IMessage> {
            @Override
            public IMessage onMessage(DamageItemMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    public static class QuestCheckMessage extends LKServerMessage {
        private int questId;

        public QuestCheckMessage() {}

        public QuestCheckMessage(int questId) {
            this.questId = questId;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            questId = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(questId);
        }

        @Override
        protected void process(MessageContext ctx) {
            LKQuestBase quest = LKQuestBase.allQuests[questId];
            if (quest != null) {
                quest.setChecked(true);
            }
        }

        public static class Handler implements IMessageHandler<QuestCheckMessage, IMessage> {
            @Override
            public IMessage onMessage(QuestCheckMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    public static class Message implements IMessage {
        private String text;

        public Message() {}
        public Message(String text) { this.text = text; }

        @Override
        public void fromBytes(ByteBuf buf) {
            int length = buf.readInt();
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);
            text = new String(bytes);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            byte[] bytes = text.getBytes();
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }

        public static class Handler implements IMessageHandler<Message, IMessage> {
            @Override
            public IMessage onMessage(Message message, MessageContext ctx) {
                if (ctx.side.isClient()) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message.text));
                }
                return null;
            }
        }
    }

    public static class CustomFX implements IMessage {
        private byte[] data;

        public CustomFX() {}
        public CustomFX(byte[] data) { this.data = data; }

        @Override
        public void fromBytes(ByteBuf buf) {
            data = new byte[51];
            buf.readBytes(data);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeBytes(data);
        }

        public static class Handler implements IMessageHandler<CustomFX, IMessage> {
            @Override
            public IMessage onMessage(CustomFX message, MessageContext ctx) {
                if (ctx.side.isClient()) {
                    int texture = message.data[0] & 255;
                    int age = message.data[1] & 255;
                    boolean glow = message.data[2] == 1;
                    ByteBuffer buffer = ByteBuffer.wrap(message.data, 3, 48);
                    double posX = buffer.getDouble();
                    double posY = buffer.getDouble();
                    double posZ = buffer.getDouble();
                    double velX = buffer.getDouble();
                    double velY = buffer.getDouble();
                    double velZ = buffer.getDouble();
                    String particleType = glow ? "fireworksSpark" : "smoke";
                    for (int i = 0; i < Math.min(age, 10); i++) {
                        Minecraft.getMinecraft().theWorld.spawnParticle(
                            particleType, posX, posY, posZ, velX, velY, velZ
                        );
                    }
                }
                return null;
            }
        }
    }

    public static class BreakItem implements IMessage {
        private byte[] data;

        public BreakItem() {}
        public BreakItem(byte[] data) { this.data = data; }

        @Override
        public void fromBytes(ByteBuf buf) {
            data = new byte[6];
            buf.readBytes(data);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeBytes(data);
        }

        public static class Handler implements IMessageHandler<BreakItem, IMessage> {
            @Override
            public IMessage onMessage(BreakItem message, MessageContext ctx) {
                if (ctx.side.isClient()) {
                    ByteBuffer buffer = ByteBuffer.wrap(message.data);
                    int entityId = buffer.getInt();
                    int dimension = message.data[4];
                    int type = message.data[5];

                    Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityId);
                    if (entity != null) {
                        String particleType = "iconcrack_" + Item.getIdFromItem(
                            type == 0 ? mod_LionKing.zebraBoots : mod_LionKing.wings
                        );
                        for (int i = 0; i < 8; i++) {
                            Minecraft.getMinecraft().theWorld.spawnParticle(
                                particleType,
                                entity.posX + (Math.random() - 0.5) * 0.5,
                                entity.posY + 1.0,
                                entity.posZ + (Math.random() - 0.5) * 0.5,
                                (Math.random() - 0.5) * 0.1,
                                Math.random() * 0.1,
                                (Math.random() - 0.5) * 0.1
                            );
                        }
                    }
                }
                return null;
            }
        }
    }
}