package lionking.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.DimensionManager;

import lionking.quest.LKQuestBase;
import lionking.client.LKClientProxy;
import lionking.client.LKPacketHandlerClient;
import lionking.tileentity.LKTileEntityOutlandsPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LKLevelData {
    public static final List<ChunkCoordinates> ticketBoothLocations = new ArrayList<>();
    public static int receivedQuestBook;
    public static int generatedMound;
    public static int quiverDamage;
    public static int homePortalX, homePortalY, homePortalZ;
    public static int defeatedScar;
    public static int moundX, moundY, moundZ;
    public static int ziraStage;
    public static int pumbaaStage;
    public static int outlandersHostile;
    public static int flatulenceSoundsRemaining;
    public static final Map<String, Integer> simbas = new HashMap<>();
    public static boolean needsLoad = true;
    public static boolean needsSave = false;

    public static void setHomePortalLocation(int x, int y, int z) {
        homePortalX = x;
        homePortalY = y;
        homePortalZ = z;
        LKClientProxy.NETWORK.sendToAll(new LKPacketHandlerClient.HomePortalMessage(x, y, z));
        needsSave = true;
    }

    public static void markMoundLocation(int x, int y, int z) {
        moundX = x;
        moundY = y;
        moundZ = z;
        generatedMound = 1;
        LKClientProxy.NETWORK.sendToAll(new LKPacketHandlerClient.MoundMessage(x, y, z));
        needsSave = true;
    }

    public static void setDefeatedScar(boolean defeated) {
        defeatedScar = defeated ? 1 : 0;
        LKClientProxy.NETWORK.sendToAll(new LKPacketHandlerClient.SimpleFlagMessage((byte) defeatedScar, "scar"));
        needsSave = true;
    }

    public static void setOutlandersHostile(int value) {
        outlandersHostile = value;
        LKClientProxy.NETWORK.sendToAll(new LKPacketHandlerClient.SimpleFlagMessage((byte) value, "outlanders"));
        needsSave = true;
    }

    public static void setZiraStage(int stage) {
        ziraStage = stage;
        LKClientProxy.NETWORK.sendToAll(new LKPacketHandlerClient.SimpleFlagMessage((byte) stage, "zira"));
        needsSave = true;
    }

    public static void setPumbaaStage(int stage) {
        pumbaaStage = stage;
        LKClientProxy.NETWORK.sendToAll(new LKPacketHandlerClient.SimpleFlagMessage((byte) stage, "pumbaa"));
        needsSave = true;
    }

    public static void setFlatulenceSoundsRemaining(int count) {
        flatulenceSoundsRemaining = count;
        LKClientProxy.NETWORK.sendToAll(new LKPacketHandlerClient.SimpleFlagMessage((byte) count, "flatulence"));
        needsSave = true;
    }

    public static boolean hasSimba(EntityPlayer player) {
        Integer value = simbas.get(player.getCommandSenderName());
        return value != null && value == 1;
    }

    public static void setHasSimba(EntityPlayer player, boolean hasSimba) {
        int value = hasSimba ? 1 : 0;
        simbas.put(player.getCommandSenderName(), value);
        LKClientProxy.NETWORK.sendTo(
            new LKPacketHandlerClient.SimpleFlagMessage((byte) value, "hasSimba"),
            (EntityPlayerMP) player
        );
        needsSave = true;
    }

    public static void setQuestStage(EntityPlayerMP player, byte questIndex, int stage) {
        if (questIndex >= 0 && questIndex < LKQuestBase.allQuests.length && LKQuestBase.allQuests[questIndex] != null) {
            LKQuestBase quest = LKQuestBase.allQuests[questIndex];
            quest.currentStage = stage;
            LKClientProxy.NETWORK.sendTo(
                new LKPacketHandlerClient.QuestMessage(questIndex, (byte) stage, ""),
                player
            );
            needsSave = true;
        }
    }

    public static void save() {
        try {
            File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "LionKing.dat");
            ensureFileExists(file);

            NBTTagCompound levelData = new NBTTagCompound();
            writeLevelData(levelData);
            writeSimbasToNBT(levelData);
            writeTicketBoothsToNBT(levelData);

            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                CompressedStreamTools.writeCompressed(levelData, outputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "LionKing.dat");
            ensureFileExists(file);

            try (FileInputStream inputStream = new FileInputStream(file)) {
                NBTTagCompound levelData = CompressedStreamTools.readCompressed(inputStream);
                readLevelData(levelData);
                readSimbasFromNBT(levelData);
                readTicketBoothsFromNBT(levelData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendLoginPacket(EntityPlayerMP player) {
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
        LKClientProxy.NETWORK.sendTo(
            new LKPacketHandlerClient.LoginMessage(
                homePortalX, homePortalY, homePortalZ,
                moundX, moundY, moundZ,
                (byte) defeatedScar, (byte) ziraStage, (byte) pumbaaStage,
                (byte) outlandersHostile, (byte) flatulenceSoundsRemaining,
                questData
            ),
            player
        );
    }

    private static void ensureFileExists(File file) throws IOException {
        if (!file.exists()) {
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                CompressedStreamTools.writeCompressed(new NBTTagCompound(), outputStream);
            }
        }
    }

    private static void writeLevelData(NBTTagCompound levelData) {
        levelData.setInteger("HomePortalX", homePortalX);
        levelData.setInteger("HomePortalY", homePortalY);
        levelData.setInteger("HomePortalZ", homePortalZ);
        levelData.setInteger("DefeatedScar", defeatedScar);
        levelData.setInteger("Book", receivedQuestBook);
        levelData.setInteger("Mound", generatedMound);
        levelData.setInteger("MoundX", moundX);
        levelData.setInteger("MoundY", moundY);
        levelData.setInteger("MoundZ", moundZ);
        levelData.setInteger("OutlandersHostile", outlandersHostile);
        levelData.setInteger("ZiraStage", ziraStage);
        levelData.setInteger("PumbaaStage", pumbaaStage);
        levelData.setInteger("FlatulenceRemaining", flatulenceSoundsRemaining);
        levelData.setInteger("QuiverDamage", quiverDamage);
        LKQuestBase.writeAllQuestsToNBT(levelData);
        LKTileEntityOutlandsPool.writeInventoryToNBT(levelData);
    }

    private static void readLevelData(NBTTagCompound levelData) {
        homePortalX = levelData.getInteger("HomePortalX");
        homePortalY = levelData.getInteger("HomePortalY");
        homePortalZ = levelData.getInteger("HomePortalZ");
        defeatedScar = levelData.getInteger("DefeatedScar");
        receivedQuestBook = levelData.getInteger("Book");
        generatedMound = levelData.getInteger("Mound");
        moundX = levelData.getInteger("MoundX");
        moundY = levelData.getInteger("MoundY");
        moundZ = levelData.getInteger("MoundZ");
        outlandersHostile = levelData.getInteger("OutlandersHostile");
        ziraStage = levelData.getInteger("ZiraStage");
        pumbaaStage = levelData.getInteger("PumbaaStage");
        flatulenceSoundsRemaining = levelData.getInteger("FlatulenceRemaining");
        quiverDamage = levelData.getInteger("QuiverDamage");
        LKQuestBase.readAllQuestsFromNBT(levelData);
        LKTileEntityOutlandsPool.readInventoryFromNBT(levelData);
    }

    private static void writeSimbasToNBT(NBTTagCompound levelData) {
        NBTTagList simbaList = new NBTTagList();
        for (Map.Entry<String, Integer> entry : simbas.entrySet()) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("Username", entry.getKey());
            nbt.setInteger("HasSimba", entry.getValue());
            simbaList.appendTag(nbt);
        }
        levelData.setTag("Simbas", simbaList);
    }

    private static void readSimbasFromNBT(NBTTagCompound levelData) {
        simbas.clear();
        NBTTagList simbaList = levelData.getTagList("Simbas", 10);
        for (int i = 0; i < simbaList.tagCount(); i++) {
            NBTTagCompound nbt = simbaList.getCompoundTagAt(i);
            simbas.put(nbt.getString("Username"), nbt.getInteger("HasSimba"));
        }
    }

    private static void writeTicketBoothsToNBT(NBTTagCompound levelData) {
        NBTTagList boothList = new NBTTagList();
        for (ChunkCoordinates coords : ticketBoothLocations) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("XPos", coords.posX);
            nbt.setInteger("YPos", coords.posY);
            nbt.setInteger("ZPos", coords.posZ);
            boothList.appendTag(nbt);
        }
        levelData.setTag("BoothLocations", boothList);
    }

    private static void readTicketBoothsFromNBT(NBTTagCompound levelData) {
        ticketBoothLocations.clear();
        NBTTagList boothList = levelData.getTagList("BoothLocations", 10);
        for (int i = 0; i < boothList.tagCount(); i++) {
            NBTTagCompound nbt = boothList.getCompoundTagAt(i);
            ticketBoothLocations.add(new ChunkCoordinates(
                nbt.getInteger("XPos"),
                nbt.getInteger("YPos"),
                nbt.getInteger("ZPos")
            ));
        }
    }
}