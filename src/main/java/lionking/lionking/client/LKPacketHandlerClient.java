package lionking.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled; 
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import lionking.mod_LionKing; 
import lionking.tileentity.LKTileEntityBugTrap; 
import lionking.tileentity.LKTileEntityHyenaHead; 
import lionking.tileentity.LKTileEntityMountedShooter; 
import lionking.tileentity.LKTileEntityFurRug; 
import lionking.tileentity.LKTileEntityMobSpawner; 
import lionking.common.LKLevelData; 
import lionking.quest.LKQuestBase; 
import lionking.entity.LKEntityCustomFX; 

@SideOnly(Side.CLIENT)
public class LKPacketHandlerClient {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static abstract class LKMessage implements IMessage {
        protected abstract void process(MessageContext ctx);
    }

    public static class TileEntityMessage extends LKMessage {
        private int x, y, z;
        private byte[] data;

        public TileEntityMessage() {}

        public TileEntityMessage(int x, int y, int z, byte[] data) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.data = data;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
            data = new byte[buf.readableBytes()];
            buf.readBytes(data);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
            buf.writeBytes(data);
        }

        @Override
        protected void process(MessageContext ctx) {
            TileEntity tileEntity = mc.theWorld.getTileEntity(x, y, z);
            if (tileEntity == null) return;

            ByteBuf buffer = Unpooled.wrappedBuffer(data);
            if (tileEntity instanceof LKTileEntityBugTrap) {
                updateBugTrapInventory((LKTileEntityBugTrap) tileEntity, buffer);
            } else if (tileEntity instanceof LKTileEntityHyenaHead) {
                updateHyenaHead((LKTileEntityHyenaHead) tileEntity, buffer);
            } else if (tileEntity instanceof LKTileEntityMountedShooter) {
                updateMountedShooter((LKTileEntityMountedShooter) tileEntity, buffer);
            } else if (tileEntity instanceof LKTileEntityFurRug) {
                ((LKTileEntityFurRug) tileEntity).direction = buffer.readByte();
            } else if (tileEntity instanceof LKTileEntityMobSpawner) {
                updateMobSpawner((LKTileEntityMobSpawner) tileEntity, buffer);
            }
        }

        public static class Handler implements IMessageHandler<TileEntityMessage, IMessage> {
            @Override
            public IMessage onMessage(TileEntityMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    public static class ParticlesMessage extends LKMessage {
        private byte type, subtype;
        private boolean flag;
        private double posX, posY, posZ, velX, velY, velZ;

        public ParticlesMessage() {}

        public ParticlesMessage(byte type, byte subtype, boolean flag, double posX, double posY, double posZ, double velX, double velY, double velZ) {
            this.type = type;
            this.subtype = subtype;
            this.flag = flag;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.velX = velX;
            this.velY = velY;
            this.velZ = velZ;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            type = buf.readByte();
            subtype = buf.readByte();
            flag = buf.readBoolean();
            posX = buf.readDouble();
            posY = buf.readDouble();
            posZ = buf.readDouble();
            velX = buf.readDouble();
            velY = buf.readDouble();
            velZ = buf.readDouble();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeByte(type);
            buf.writeByte(subtype);
            buf.writeBoolean(flag);
            buf.writeDouble(posX);
            buf.writeDouble(posY);
            buf.writeDouble(posZ);
            buf.writeDouble(velX);
            buf.writeDouble(velY);
            buf.writeDouble(velZ);
        }

        @Override
        protected void process(MessageContext ctx) {
            World world = mc.theWorld;
            if (world != null) {
                world.spawnEntityInWorld(new LKEntityCustomFX(world, type, subtype, flag, posX, posY, posZ, velX, velY, velZ));
            }
        }

        public static class Handler implements IMessageHandler<ParticlesMessage, IMessage> {
            @Override
            public IMessage onMessage(ParticlesMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    public static class BreakItemMessage extends LKMessage {
        private int entityId;
        private byte type;

        public BreakItemMessage() {}

        public BreakItemMessage(int entityId, byte type) {
            this.entityId = entityId;
            this.type = type;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            entityId = buf.readInt();
            type = buf.readByte();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(entityId);
            buf.writeByte(type);
        }

        @Override
        protected void process(MessageContext ctx) {
            Entity entity = mod_LionKing.proxy.getEntityFromID(entityId, mc.theWorld);
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                ItemStack brokenItem = type == 0 ? new ItemStack(mod_LionKing.zebraBoots) : new ItemStack(mod_LionKing.wings);
                player.renderBrokenItemStack(brokenItem);
            }
        }

        public static class Handler implements IMessageHandler<BreakItemMessage, IMessage> {
            @Override
            public IMessage onMessage(BreakItemMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    public static class HomePortalMessage extends LKMessage {
        private int x, y, z;

        public HomePortalMessage() {}

        public HomePortalMessage(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
        }

        @Override
        protected void process(MessageContext ctx) {
            LKLevelData.homePortalX = x;
            LKLevelData.homePortalY = y;
            LKLevelData.homePortalZ = z;
        }

        public static class Handler implements IMessageHandler<HomePortalMessage, IMessage> {
            @Override
            public IMessage onMessage(HomePortalMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    public static class SimpleFlagMessage extends LKMessage {
        private byte value;
        private String flagType;

        public SimpleFlagMessage() {}

        public SimpleFlagMessage(byte value, String flagType) {
            this.value = value;
            this.flagType = flagType;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            value = buf.readByte();
            int length = buf.readInt();
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);
            flagType = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeByte(value);
            byte[] bytes = flagType.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }

        @Override
        protected void process(MessageContext ctx) {
            switch (flagType) {
                case "scar":
                    LKLevelData.defeatedScar = value;
                    break;
                case "outlanders":
                    LKLevelData.outlandersHostile = value;
                    break;
                case "zira":
                    LKLevelData.ziraStage = value;
                    break;
                case "pumbaa":
                    LKLevelData.pumbaaStage = value;
                    break;
                case "hasSimba":
                    LKLevelData.setHasSimba(mc.thePlayer, value == 1);
                    break;
                case "flatulence":
                    LKLevelData.flatulenceSoundsRemaining = value;
                    break;
            }
        }

        public static class Handler implements IMessageHandler<SimpleFlagMessage, IMessage> {
            @Override
            public IMessage onMessage(SimpleFlagMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    public static class MoundMessage extends LKMessage {
        private int x, y, z;

        public MoundMessage() {}

        public MoundMessage(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
        }

        @Override
        protected void process(MessageContext ctx) {
            LKLevelData.moundX = x;
            LKLevelData.moundY = y;
            LKLevelData.moundZ = z;
        }

        public static class Handler implements IMessageHandler<MoundMessage, IMessage> {
            @Override
            public IMessage onMessage(MoundMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    public static class QuestMessage extends LKMessage {
        private byte questId, value;
        private String questAction;

        public QuestMessage() {}

        public QuestMessage(byte questId, byte value, String questAction) {
            this.questId = questId;
            this.value = value;
            this.questAction = questAction;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            questId = buf.readByte();
            value = buf.readByte();
            int length = buf.readInt();
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);
            questAction = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeByte(questId);
            buf.writeByte(value);
            byte[] bytes = questAction.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }

        @Override
        protected void process(MessageContext ctx) {
            LKQuestBase quest = LKQuestBase.allQuests[questId];
            if (quest != null) {
                switch (questAction) {
                    case "doStage":
                        quest.stagesCompleted[value] = 1;
                        break;
                    case "delay":
                        quest.stagesDelayed = value;
                        break;
                    case "check":
                        quest.checked = value;
                        break;
                    case "stage":
                        quest.currentStage = value;
                        break;
                }
            }
        }

        public static class Handler implements IMessageHandler<QuestMessage, IMessage> {
            @Override
            public IMessage onMessage(QuestMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    public static class LoginMessage extends LKMessage {
        private int homeX, homeY, homeZ, moundX, moundY, moundZ;
        private byte defeatedScar, ziraStage, pumbaaStage, outlandersHostile, flatulenceSounds;
        private byte[][] questData;

        public LoginMessage() {}

        public LoginMessage(int homeX, int homeY, int homeZ, int moundX, int moundY, int moundZ,
                            byte defeatedScar, byte ziraStage, byte pumbaaStage, byte outlandersHostile, byte flatulenceSounds,
                            byte[][] questData) {
            this.homeX = homeX;
            this.homeY = homeY;
            this.homeZ = homeZ;
            this.moundX = moundX;
            this.moundY = moundY;
            this.moundZ = moundZ;
            this.defeatedScar = defeatedScar;
            this.ziraStage = ziraStage;
            this.pumbaaStage = pumbaaStage;
            this.outlandersHostile = outlandersHostile;
            this.flatulenceSounds = flatulenceSounds;
            this.questData = questData;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            homeX = buf.readInt();
            homeY = buf.readInt();
            homeZ = buf.readInt();
            moundX = buf.readInt();
            moundY = buf.readInt();
            moundZ = buf.readInt();
            defeatedScar = buf.readByte();
            ziraStage = buf.readByte();
            pumbaaStage = buf.readByte();
            outlandersHostile = buf.readByte();
            flatulenceSounds = buf.readByte();
            questData = new byte[16][];
            for (int i = 0; i < 16; i++) {
                questData[i] = new byte[19];
                buf.readBytes(questData[i]);
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(homeX);
            buf.writeInt(homeY);
            buf.writeInt(homeZ);
            buf.writeInt(moundX);
            buf.writeInt(moundY);
            buf.writeInt(moundZ);
            buf.writeByte(defeatedScar);
            buf.writeByte(ziraStage);
            buf.writeByte(pumbaaStage);
            buf.writeByte(outlandersHostile);
            buf.writeByte(flatulenceSounds);
            for (int i = 0; i < 16; i++) {
                buf.writeBytes(questData[i]);
            }
        }

        @Override
        protected void process(MessageContext ctx) {
            LKLevelData.homePortalX = homeX;
            LKLevelData.homePortalY = homeY;
            LKLevelData.homePortalZ = homeZ;
            LKLevelData.moundX = moundX;
            LKLevelData.moundY = moundY;
            LKLevelData.moundZ = moundZ;
            LKLevelData.defeatedScar = defeatedScar;
            LKLevelData.ziraStage = ziraStage;
            LKLevelData.pumbaaStage = pumbaaStage;
            LKLevelData.outlandersHostile = outlandersHostile;
            LKLevelData.flatulenceSoundsRemaining = flatulenceSounds;

            for (int i = 0; i < 16; i++) {
                LKQuestBase quest = LKQuestBase.allQuests[i];
                if (quest != null) {
                    ByteBuf buffer = Unpooled.wrappedBuffer(questData[i]);
                    quest.stagesDelayed = buffer.readByte();
                    quest.currentStage = buffer.readByte();
                    quest.checked = buffer.readByte();
                    for (int j = 0; j < Math.min(16, quest.stagesCompleted.length); j++) {
                        quest.stagesCompleted[j] = buffer.readByte();
                    }
                }
            }
        }

        public static class Handler implements IMessageHandler<LoginMessage, IMessage> {
            @Override
            public IMessage onMessage(LoginMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    public static class ChatMessage extends LKMessage {
        private String message;

        public ChatMessage() {}

        public ChatMessage(String message) {
            this.message = message;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            int length = buf.readInt();
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);
            message = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            byte[] bytes = message.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }

        @Override
        protected void process(MessageContext ctx) {
            EntityPlayer player = mc.thePlayer;
            if (player != null) {
                player.addChatMessage(new net.minecraft.util.ChatComponentText(message));
            }
        }

        public static class Handler implements IMessageHandler<ChatMessage, IMessage> {
            @Override
            public IMessage onMessage(ChatMessage message, MessageContext ctx) {
                message.process(ctx);
                return null;
            }
        }
    }

    private static void updateBugTrapInventory(LKTileEntityBugTrap bugTrap, ByteBuf buffer) {
        for (int slot = 0; slot < 4; slot++) {
            int id = buffer.readInt();
            int damage = buffer.readInt();
            Item item = (Item) Item.itemRegistry.getObjectById(id);
            if (item != null) {
                bugTrap.setInventorySlotContents(slot, new ItemStack(item, 1, damage));
            }
        }
    }

    private static void updateHyenaHead(LKTileEntityHyenaHead hyenaHead, ByteBuf buffer) {
        hyenaHead.setHyenaType(buffer.readByte());
        hyenaHead.setRotation(buffer.readByte());
    }

    private static void updateMountedShooter(LKTileEntityMountedShooter shooter, ByteBuf buffer) {
        byte type = buffer.readByte();
        if (type == -1) {
            shooter.fireCounter = 20;
        } else {
            shooter.setShooterType(type);
        }
    }

    private static void updateMobSpawner(LKTileEntityMobSpawner spawner, ByteBuf buffer) {
        spawner.yaw = buffer.readDouble();
        spawner.yaw2 = buffer.readDouble();
        spawner.setMobID(buffer.readInt());
    }
}