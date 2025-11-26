package lionking.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.AchievementPage;
import io.netty.buffer.ByteBuf;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import lionking.mod_LionKing;
import lionking.entity.LKEntityScar;
import lionking.entity.LKEntityZira;
import lionking.entity.LKEntityCustomFX;
import lionking.block.LKBlockLeaves;
import lionking.block.LKBlockRafikiLeaves;
import lionking.client.LKGuiIngame;
import lionking.common.LKAchievementList;
import lionking.common.LKCommonProxy;
import lionking.common.LKIngame;
import lionking.common.LKLevelData;

public class LKTickHandlerClient {
    private GuiScreen lastGuiOpen;
    private boolean checkedUpdate;
    private boolean wingFlight;
    private int wingTicks;
    public static HashMap<EntityPlayer, Integer> playersInPortals = new HashMap<>();
    public static HashMap<EntityPlayer, Integer> playersInOutPortals = new HashMap<>();
    public static LKEntityScar scarBoss;
    public static LKEntityZira ziraBoss;

    public LKTickHandlerClient() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft minecraft = Minecraft.getMinecraft();
            runRenderTick(event.renderTickTime, minecraft);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft minecraft = Minecraft.getMinecraft();
            if (minecraft.currentScreen == null) {
                lastGuiOpen = null;
            }
            runClientTick(minecraft);

            ((LKBlockLeaves) mod_LionKing.forestLeaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);
            ((LKBlockLeaves) mod_LionKing.mangoLeaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);
            ((LKBlockLeaves) mod_LionKing.leaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);
            ((LKBlockLeaves) mod_LionKing.passionLeaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);
            ((LKBlockLeaves) mod_LionKing.bananaLeaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);
            ((LKBlockLeaves) mod_LionKing.rafikiLeaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);

            if (minecraft.thePlayer != null && minecraft.theWorld != null && !checkedUpdate) {
                checkForUpdates(minecraft);
                checkedUpdate = true;
            }

            handlePortals(minecraft);
            handleGui(minecraft);
        }
    }

    private void runRenderTick(float partialTicks, Minecraft minecraft) {
        if (minecraft.currentScreen == null) {
            if (scarBoss != null) {
                LKGuiIngame.drawBossHealth(minecraft, scarBoss);
            }
            if (ziraBoss != null) {
                LKGuiIngame.drawBossHealth(minecraft, ziraBoss);
            }
        }

        if (minecraft.thePlayer != null && minecraft.theWorld != null) {
            if (playersInPortals.containsKey(minecraft.thePlayer)) {
                int i = playersInPortals.get(minecraft.thePlayer);
                if (i > 0) {
                    LKGuiIngame.renderPortalOverlay(0.1F + ((float) i / 100F) * 0.6F, minecraft, true);
                }
            }
            if (playersInOutPortals.containsKey(minecraft.thePlayer)) {
                int i = playersInOutPortals.get(minecraft.thePlayer);
                if (i > 0) {
                    LKGuiIngame.renderPortalOverlay(0.1F + ((float) i / 100F) * 0.6F, minecraft, false);
                }
            }
        }

        if (LKIngame.flatulenceSoundTick > 0) {
            LKGuiIngame.renderFlatulenceOverlay(0.1F + ((float) LKIngame.flatulenceSoundTick / 25.0F) * 0.7F, minecraft);
        }

        if (LKIngame.loadRenderers) {
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
            LKIngame.loadRenderers = false;
        }
    }

    private void runClientTick(Minecraft minecraft) {
        EntityPlayerSP entityplayer = minecraft.thePlayer;
        World world = minecraft.theWorld;
        if (entityplayer != null && world != null) {
            Random random = world.rand;

            if (entityplayer.dimension == mod_LionKing.idPrideLands) {
                entityplayer.triggerAchievement(LKAchievementList.enterPrideLands);
            }

            ItemStack boots = entityplayer.inventory.armorItemInSlot(0);
            if (boots != null && boots.getItem() == mod_LionKing.zebraBoots) {
                damageZebraBoots(entityplayer, boots);
            }

            ItemStack body = entityplayer.inventory.armorItemInSlot(2);
            if (body != null && body.getItem() == mod_LionKing.wings) {
                applyWingFlight(entityplayer, body);
            } else {
                wingFlight = false;
            }

            ItemStack[] armor = entityplayer.inventory.armorInventory;
            if (armor[3] != null && armor[2] != null && armor[1] != null && armor[0] != null &&
                armor[3].getItem() == mod_LionKing.ticketLionHead && armor[2].getItem() == mod_LionKing.ticketLionSuit &&
                armor[1].getItem() == mod_LionKing.ticketLionLegs && armor[0].getItem() == mod_LionKing.ticketLionFeet) {
                entityplayer.triggerAchievement(LKAchievementList.ticketLionSuit);
                if (entityplayer.sprintingTicksLeft > 0 && random.nextInt(3) == 0) {
                    spawnTicketLionEffect(entityplayer, world, random);
                }
            }

            if (armor[3] != null && armor[3].getItem() == mod_LionKing.outlandsHelm && random.nextInt(3) == 0) {
                spawnOutlandsHelmFlame(entityplayer, world, random);
            }
        }
    }

    private void checkForUpdates(Minecraft minecraft) {
        try {
            URL updateURL = new URL("http://dl.dropbox.com/s/b9odiuz504iy6ol/version.txt");
            HttpURLConnection updateConnection = (HttpURLConnection) updateURL.openConnection();
            BufferedReader updateReader = new BufferedReader(new InputStreamReader(updateConnection.getInputStream()));
            String updateVersion = updateReader.readLine();
            updateReader.close();

            String version = "v2.0 for Minecraft 1.7.10";
            if (version != null && updateVersion != null && !updateVersion.equals(version)) {
                minecraft.thePlayer.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(
                    "lionking.update_available", updateVersion)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePortals(Minecraft minecraft) {
        EntityPlayer entityplayer = minecraft.thePlayer;
        if (entityplayer == null || minecraft.theWorld == null) return;

        if ((entityplayer.dimension == 0 || entityplayer.dimension == mod_LionKing.idPrideLands) && playersInPortals.containsKey(entityplayer)) {
            if (LKIngame.isPlayerInLionPortal(entityplayer, true)) {
                int i = playersInPortals.get(entityplayer);
                i++;
                playersInPortals.put(entityplayer, i);
                if (i >= 100) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(new net.minecraft.client.audio.PositionedSoundRecord(
                        new net.minecraft.util.ResourceLocation("portal.travel"), 1F, minecraft.theWorld.rand.nextFloat() * 0.4F + 0.8F,
                        (float)entityplayer.posX, (float)entityplayer.posY, (float)entityplayer.posZ));
                    playersInPortals.remove(entityplayer);
                }
            } else {
                playersInPortals.remove(entityplayer);
            }
        }

        if ((entityplayer.dimension == mod_LionKing.idOutlands || entityplayer.dimension == mod_LionKing.idPrideLands) && playersInOutPortals.containsKey(entityplayer)) {
            if (LKIngame.isPlayerInLionPortal(entityplayer, false)) {
                int i = playersInOutPortals.get(entityplayer);
                i++;
                playersInOutPortals.put(entityplayer, i);
                if (i >= 100) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(new net.minecraft.client.audio.PositionedSoundRecord(
                        new net.minecraft.util.ResourceLocation("portal.travel"), 1F, minecraft.theWorld.rand.nextFloat() * 0.4F + 0.8F,
                        (float)entityplayer.posX, (float)entityplayer.posY, (float)entityplayer.posZ));
                    playersInOutPortals.remove(entityplayer);
                }
            } else {
                playersInOutPortals.remove(entityplayer);
            }
        }
    }

    private void handleGui(Minecraft minecraft) {
        GuiScreen guiscreen = minecraft.currentScreen;
        if (guiscreen != null) {
            if (guiscreen instanceof GuiAchievements) {
                minecraft.thePlayer.openGui(mod_LionKing.instance, LKCommonProxy.GUI_ID_ACHIEVEMENTS, minecraft.theWorld, 0, 0, 0);
            }

            if (guiscreen instanceof LKGuiAchievements) {
                int page = ((LKGuiAchievements) guiscreen).getCurrentPage(); 
                if (!AchievementPage.getTitle(page).equals("Lion King")) {
                    GuiAchievements gui = new GuiAchievements(null, minecraft.thePlayer.getStatFileWriter()); 
                    FMLClientHandler.instance().displayGuiScreen(minecraft.thePlayer, gui);
                }
            }

            if (guiscreen instanceof GuiMainMenu && !(lastGuiOpen instanceof GuiMainMenu)) {
                LKLevelData.needsLoad = true;
            }

            lastGuiOpen = guiscreen;
        }
    }

    private void damageZebraBoots(EntityPlayerSP entityplayer, ItemStack itemstack) {
        if (!entityplayer.worldObj.isAnyLiquid(entityplayer.boundingBox) && entityplayer.movementInput.moveForward > 0.0F &&
            !(itemstack.getItemDamage() == itemstack.getMaxDamage()) && !wingFlight) {
            entityplayer.moveEntityWithHeading(entityplayer.movementInput.moveForward, 30F);
            sendDamageItemPacket(entityplayer, 0);
        }
    }

    private void applyWingFlight(EntityPlayerSP entityplayer, ItemStack itemstack) {
        if (entityplayer.onGround) {
            wingFlight = false;
        }
        if (entityplayer.posY < 0D) {
            wingFlight = false;
            wingTicks = 0;
        }
        if (wingTicks > 0) {
            wingTicks--;
            if (entityplayer.movementInput.sneak) {
                entityplayer.motionY = -0.22D;
            } else {
                entityplayer.motionY = ((double) (wingTicks / 12)) * 0.05D;
            }
        }
        if (!(itemstack.getItemDamage() == itemstack.getMaxDamage())) {
            if (entityplayer.motionY < 0D && !entityplayer.movementInput.sneak && wingFlight) {
                entityplayer.motionY = -0.22D;
            }
            if (entityplayer.movementInput.jump && wingTicks == 0 && entityplayer.posY < 240D && entityplayer.posY > 0D && !entityplayer.movementInput.sneak) {
                wingTicks = 50;
                entityplayer.motionY = 1D;
                sendDamageItemPacket(entityplayer, 1);
                wingFlight = false;
            }
        }
        if (itemstack.getItemDamage() == itemstack.getMaxDamage()) {
            wingTicks = 0;
            wingFlight = true;
        }
    }

    private void spawnTicketLionEffect(EntityPlayerSP entityplayer, World world, Random random) {
        double d = random.nextGaussian() * 0.02D;
        double d1 = random.nextGaussian() * 0.02D;
        double d2 = random.nextGaussian() * 0.02D;
        world.spawnEntityInWorld(new LKEntityCustomFX(world, 48, 32, false,
            (entityplayer.posX + (((double) (random.nextFloat() * entityplayer.width * 2.0F)) - (double) entityplayer.width) * 0.75F),
            entityplayer.posY - 1.0F + (double) (random.nextFloat() * entityplayer.height),
            (entityplayer.posZ + (((double) (random.nextFloat() * entityplayer.width * 2.0F)) - (double) entityplayer.width) * 0.75F),
            d, d1, d2));
    }

    private void spawnOutlandsHelmFlame(EntityPlayerSP entityplayer, World world, Random random) {
        double d = random.nextGaussian() * 0.01D;
        double d1 = random.nextGaussian() * 0.02D;
        if (d1 < 0.0D) {
            d1 *= -1D;
        }
        double d2 = random.nextGaussian() * 0.01D;
        world.spawnParticle("flame", entityplayer.posX + ((((double) (random.nextFloat() * entityplayer.width * 2.0F)) - (double) entityplayer.width) * 0.25F),
            entityplayer.boundingBox.maxY + 0.3F + (double) (random.nextFloat() * 0.25F),
            (entityplayer.posZ + (((double) (random.nextFloat() * entityplayer.width * 2.0F)) - (double) entityplayer.width) * 0.25F),
            d, d1, d2);
    }

    public static class DamageItemMessage implements IMessage {
        private int entityId;
        private byte dimension;
        private byte type;

        public DamageItemMessage() {}

        public DamageItemMessage(int entityId, byte dimension, byte type) {
            this.entityId = entityId;
            this.dimension = dimension;
            this.type = type;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            entityId = buf.readInt();
            dimension = buf.readByte();
            type = buf.readByte();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(entityId);
            buf.writeByte(dimension);
            buf.writeByte(type);
        }
    }

    public static class DamageItemHandler implements IMessageHandler<DamageItemMessage, IMessage> {
        @Override
        public IMessage onMessage(DamageItemMessage message, MessageContext ctx) {
            return null;
        }
    }

    private static void sendDamageItemPacket(EntityPlayer entityplayer, int type) {
        LKClientProxy.NETWORK.sendToServer(new DamageItemMessage(entityplayer.getEntityId(), (byte) entityplayer.dimension, (byte) type));
    }
}