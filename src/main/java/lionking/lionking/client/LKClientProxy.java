package lionking.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import lionking.entity.LKEntityLionBase;
import lionking.entity.LKEntityHyena;
import lionking.entity.LKEntityTicketLion;
import lionking.entity.LKEntityZazu;
import lionking.entity.LKEntityBlueDart;
import lionking.entity.LKEntityYellowDart;
import lionking.entity.LKEntityRedDart;
import lionking.entity.LKEntityBlackDart;
import lionking.entity.LKEntityZebra;
import lionking.entity.LKEntityRafiki;
import lionking.entity.LKEntityThrownTermite;
import lionking.entity.LKEntityScar;
import lionking.entity.LKEntityCoin;
import lionking.entity.LKEntityLightning;
import lionking.entity.LKEntitySimba;
import lionking.entity.LKEntityOutlander;
import lionking.entity.LKEntityVulture;
import lionking.entity.LKEntityTermite;
import lionking.entity.LKEntityBug;
import lionking.entity.LKEntityOutsand;
import lionking.entity.LKEntityPumbaa;
import lionking.entity.LKEntityTimon;
import lionking.entity.LKEntityPumbaaBomb;
import lionking.entity.LKEntityRhino;
import lionking.entity.LKEntityGemsbok;
import lionking.entity.LKEntityGemsbokSpear;
import lionking.entity.LKEntityPoisonedSpear;
import lionking.entity.LKEntityCrocodile;
import lionking.entity.LKEntityZazuEgg;
import lionking.entity.LKEntityScarRug;
import lionking.entity.LKEntityZira;
import lionking.entity.LKEntityTermiteQueen;
import lionking.entity.LKEntityGiraffe;
import lionking.entity.LKEntitySkeletalHyenaHead;
import lionking.entity.LKEntityCustomFX;
import lionking.entity.LKEntityDikdik;
import lionking.entity.LKEntityPinkDart;
import lionking.entity.LKEntityFlamingo;
import lionking.entity.LKEntityPortalFX;
import lionking.entity.LKEntityPassionFX;

import lionking.tileentity.LKTileEntityDrum;
import lionking.tileentity.LKTileEntityGrindingBowl;
import lionking.tileentity.LKTileEntityBugTrap;
import lionking.tileentity.LKTileEntityHyenaHead;
import lionking.tileentity.LKTileEntityMountedShooter;
import lionking.tileentity.LKTileEntityMobSpawner;
import lionking.tileentity.LKTileEntityBugTrapRenderer;
import lionking.tileentity.LKTileEntityHyenaHeadRenderer;
import lionking.tileentity.LKTileEntityMountedShooterRenderer;
import lionking.tileentity.LKTileEntityMobSpawnerRenderer;

import lionking.common.LKTickHandlerServer;
import lionking.common.LKCommonProxy;

import lionking.item.LKItemDartQuiver;

import lionking.inventory.LKContainerGrindingBowl;
import lionking.inventory.LKContainerSimba;
import lionking.inventory.LKContainerTimon;
import lionking.inventory.LKInventoryQuiver;
import lionking.inventory.LKContainerQuiver;
import lionking.inventory.LKContainerItemInfo;
import lionking.inventory.LKContainerBugTrap;
import lionking.inventory.LKContainerDrum;

import lionking.client.LKPacketHandlerClient;
import lionking.client.LKSound;
import lionking.client.LKTickHandlerClient;
import lionking.client.LKRenderLion;
import lionking.client.LKRenderHyena;
import lionking.client.LKRenderTicketLion;
import lionking.client.LKRenderZazu;
import lionking.client.LKModelZazu;
import lionking.client.LKRenderDart;
import lionking.client.LKRenderZebra;
import lionking.client.LKRenderRafiki;
import lionking.client.LKModelRafiki;
import lionking.client.LKRenderScar;
import lionking.client.LKRenderThrownCoin;
import lionking.client.LKRenderLightning;
import lionking.client.LKRenderSimba;
import lionking.client.LKRenderOutlander;
import lionking.client.LKRenderVulture;
import lionking.client.LKModelVulture;
import lionking.client.LKRenderTermite;
import lionking.client.LKRenderBug;
import lionking.client.LKRenderOutsand;
import lionking.client.LKRenderPumbaa;
import lionking.client.LKRenderTimon;
import lionking.client.LKRenderRhino;
import lionking.client.LKRenderGemsbok;
import lionking.client.LKRenderSpear;
import lionking.client.LKRenderCrocodile;
import lionking.client.LKRenderScarRug;
import lionking.client.LKRenderZira;
import lionking.client.LKRenderTermiteQueen;
import lionking.client.LKRenderGiraffe;
import lionking.client.LKModelGiraffe;
import lionking.client.LKRenderSkeletalHyenaHead;
import lionking.client.LKRenderCustomFX;
import lionking.client.LKRenderDikdik;
import lionking.client.LKRenderFlamingo;
import lionking.client.LKModelFlamingo;
import lionking.client.LKRenderGrindingStick;
import lionking.client.LKRenderBlocks;
import lionking.client.LKRenderLargeItem;

import lionking.mod_LionKing;

@SideOnly(Side.CLIENT)
public class LKClientProxy extends LKCommonProxy {
    public static final ResourceLocation ICONS_TEXTURE = new ResourceLocation("lionking:gui/icons.png");
    public static final ResourceLocation ITEM_GLINT_TEXTURE = new ResourceLocation("minecraft:textures/misc/enchanted_item_glint.png");

    private final LKTickHandlerClient tickHandler = new LKTickHandlerClient();
    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("LionKing");

    private int grindingBowlRenderID;
    private int pillarRenderID;
    private int starAltarRenderID;
    private int vaseRenderID;
    private int bugTrapRenderID;
    private int aridGrassRenderID;
    private int kiwanoBlockRenderID;
    private int kiwanoStemRenderID;
    private int leverRenderID;
    private int lilyRenderID;
    private int rugRenderID;

    @Override
    public void onPreload() {
        MinecraftForge.EVENT_BUS.register(new LKSound());
    }

    @Override
    public void onLoad() {
        FMLCommonHandler.instance().bus().register(tickHandler);
        registerPacketChannels();
        registerEntityRenderers();
        registerTileEntityRenderers();
        registerBlockRenderers();
        MinecraftForgeClient.registerItemRenderer(mod_LionKing.tunnahDiggah, new LKRenderLargeItem());
    }

    private void registerPacketChannels() {
        NETWORK.registerMessage(LKPacketHandlerClient.TileEntityMessage.Handler.class, LKPacketHandlerClient.TileEntityMessage.class, 0, Side.CLIENT);
        NETWORK.registerMessage(LKPacketHandlerClient.ParticlesMessage.Handler.class, LKPacketHandlerClient.ParticlesMessage.class, 1, Side.CLIENT);
        NETWORK.registerMessage(LKPacketHandlerClient.BreakItemMessage.Handler.class, LKPacketHandlerClient.BreakItemMessage.class, 2, Side.CLIENT);
        NETWORK.registerMessage(LKPacketHandlerClient.HomePortalMessage.Handler.class, LKPacketHandlerClient.HomePortalMessage.class, 3, Side.CLIENT);
        NETWORK.registerMessage(LKPacketHandlerClient.SimpleFlagMessage.Handler.class, LKPacketHandlerClient.SimpleFlagMessage.class, 4, Side.CLIENT);
        NETWORK.registerMessage(LKPacketHandlerClient.MoundMessage.Handler.class, LKPacketHandlerClient.MoundMessage.class, 5, Side.CLIENT);
        NETWORK.registerMessage(LKPacketHandlerClient.QuestMessage.Handler.class, LKPacketHandlerClient.QuestMessage.class, 6, Side.CLIENT);
        NETWORK.registerMessage(LKPacketHandlerClient.LoginMessage.Handler.class, LKPacketHandlerClient.LoginMessage.class, 7, Side.CLIENT);
        NETWORK.registerMessage(LKPacketHandlerClient.ChatMessage.Handler.class, LKPacketHandlerClient.ChatMessage.class, 8, Side.CLIENT);
    }

    private void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(LKEntityLionBase.class, new LKRenderLion());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityHyena.class, new LKRenderHyena());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityTicketLion.class, new LKRenderTicketLion());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityZazu.class, new LKRenderZazu(new LKModelZazu(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityBlueDart.class, new LKRenderDart("blue"));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityYellowDart.class, new LKRenderDart("yellow"));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityRedDart.class, new LKRenderDart("red"));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityBlackDart.class, new LKRenderDart("black"));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityZebra.class, new LKRenderZebra());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityRafiki.class, new LKRenderRafiki());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityThrownTermite.class, new RenderSnowball(mod_LionKing.itemTermite));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityScar.class, new LKRenderScar());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityCoin.class, new LKRenderThrownCoin());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityLightning.class, new LKRenderLightning());
        RenderingRegistry.registerEntityRenderingHandler(LKEntitySimba.class, new LKRenderSimba());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityOutlander.class, new LKRenderOutlander());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityVulture.class, new LKRenderVulture(new LKModelVulture(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityTermite.class, new LKRenderTermite());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityBug.class, new LKRenderBug());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityOutsand.class, new LKRenderOutsand());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityPumbaa.class, new LKRenderPumbaa());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityTimon.class, new LKRenderTimon());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityPumbaaBomb.class, new RenderSnowball(mod_LionKing.pumbaaBomb));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityRhino.class, new LKRenderRhino());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityGemsbok.class, new LKRenderGemsbok());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityGemsbokSpear.class, new LKRenderSpear(false));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityPoisonedSpear.class, new LKRenderSpear(true));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityCrocodile.class, new LKRenderCrocodile());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityZazuEgg.class, new RenderSnowball(mod_LionKing.zazuEgg));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityScarRug.class, new LKRenderScarRug());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityZira.class, new LKRenderZira());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityTermiteQueen.class, new LKRenderTermiteQueen());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityGiraffe.class, new LKRenderGiraffe(new LKModelGiraffe(0F), new LKModelGiraffe(0.5F)));
        RenderingRegistry.registerEntityRenderingHandler(LKEntitySkeletalHyenaHead.class, new LKRenderSkeletalHyenaHead());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityCustomFX.class, new LKRenderCustomFX());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityDikdik.class, new LKRenderDikdik());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityPinkDart.class, new LKRenderDart("pink"));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityFlamingo.class, new LKRenderFlamingo(new LKModelFlamingo()));
    }

    private void registerTileEntityRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(LKTileEntityGrindingBowl.class, new LKRenderGrindingStick());
        ClientRegistry.bindTileEntitySpecialRenderer(LKTileEntityBugTrap.class, new LKTileEntityBugTrapRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(LKTileEntityHyenaHead.class, new LKTileEntityHyenaHeadRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(LKTileEntityMountedShooter.class, new LKTileEntityMountedShooterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(LKTileEntityMobSpawner.class, new LKTileEntityMobSpawnerRenderer());
    }

    private void registerBlockRenderers() {
        grindingBowlRenderID = RenderingRegistry.getNextAvailableRenderId();
        pillarRenderID = RenderingRegistry.getNextAvailableRenderId();
        starAltarRenderID = RenderingRegistry.getNextAvailableRenderId();
        vaseRenderID = RenderingRegistry.getNextAvailableRenderId();
        bugTrapRenderID = RenderingRegistry.getNextAvailableRenderId();
        aridGrassRenderID = RenderingRegistry.getNextAvailableRenderId();
        kiwanoBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
        kiwanoStemRenderID = RenderingRegistry.getNextAvailableRenderId();
        leverRenderID = RenderingRegistry.getNextAvailableRenderId();
        lilyRenderID = RenderingRegistry.getNextAvailableRenderId();
        rugRenderID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(grindingBowlRenderID, new LKRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(pillarRenderID, new LKRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(starAltarRenderID, new LKRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(vaseRenderID, new LKRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(bugTrapRenderID, new LKRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(aridGrassRenderID, new LKRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(kiwanoBlockRenderID, new LKRenderBlocks(true));
        RenderingRegistry.registerBlockHandler(kiwanoStemRenderID, new LKRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(leverRenderID, new LKRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(lilyRenderID, new LKRenderBlocks(false));
        RenderingRegistry.registerBlockHandler(rugRenderID, new LKRenderBlocks(true));
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case GUI_ID_BOWL:
                TileEntity bowl = world.getTileEntity(x, y, z); 
                if (bowl instanceof LKTileEntityGrindingBowl) {
                    return new LKGuiGrindingBowl(player, (LKTileEntityGrindingBowl) bowl);
                }
                break;
            case GUI_ID_SIMBA:
                Entity simba = getEntityFromID(x, world);
                if (simba instanceof LKEntitySimba) {
                    return new LKGuiSimba(player, (LKEntitySimba) simba);
                }
                break;
            case GUI_ID_TIMON:
                Entity timon = getEntityFromID(x, world);
                if (timon instanceof LKEntityTimon) {
                    return new LKGuiTimon(player, (LKEntityTimon) timon);
                }
                break;
            case GUI_ID_QUIVER:
                LKInventoryQuiver quiver = LKItemDartQuiver.getQuiverInventory(x, world);
                if (quiver != null) {
                    return new LKGuiQuiver(player, quiver);
                }
                break;
            case GUI_ID_QUESTS:
                return new LKGuiQuests(player);
            case GUI_ID_ACHIEVEMENTS:
                return new LKGuiAchievements(Minecraft.getMinecraft().thePlayer.getStatFileWriter(), Minecraft.getMinecraft().currentScreen);
            case GUI_ID_TRAP:
                TileEntity trap = world.getTileEntity(x, y, z); 
                if (trap instanceof LKTileEntityBugTrap) {
                    return new LKGuiBugTrap(player, (LKTileEntityBugTrap) trap);
                }
                break;
            case GUI_ID_DRUM:
                TileEntity drum = world.getTileEntity(x, y, z); 
                if (drum instanceof LKTileEntityDrum) {
                    LKContainerDrum container = new LKContainerDrum(player, world, (LKTileEntityDrum) drum); 
                    return new LKGuiDrum(player, container); 
                }
                break;
        }
        return null;
    }

    @Override
    public int getGrindingBowlRenderID() { return grindingBowlRenderID; }
    @Override
    public int getPillarRenderID() { return pillarRenderID; }
    @Override
    public int getStarAltarRenderID() { return starAltarRenderID; }
    @Override
    public int getVaseRenderID() { return vaseRenderID; }
    @Override
    public int getBugTrapRenderID() { return bugTrapRenderID; }
    @Override
    public int getAridGrassRenderID() { return aridGrassRenderID; }
    @Override
    public int getKiwanoBlockRenderID() { return kiwanoBlockRenderID; }
    @Override
    public int getKiwanoStemRenderID() { return kiwanoStemRenderID; }
    @Override
    public int getLeverRenderID() { return leverRenderID; }
    @Override
    public int getLilyRenderID() { return lilyRenderID; }
    @Override
    public int getRugRenderID() { return rugRenderID; }

    @Override
    public void setInPrideLandsPortal(EntityPlayer player) {
        LKTickHandlerClient.playersInPortals.putIfAbsent(player, 0);
        if (Minecraft.getMinecraft().isSingleplayer()) {
            LKTickHandlerServer.playersInPortals.putIfAbsent(player, 0);
        }
    }

    @Override
    public void setInOutlandsPortal(EntityPlayer player) {
        LKTickHandlerClient.playersInOutPortals.putIfAbsent(player, 0);
        if (Minecraft.getMinecraft().isSingleplayer()) {
            LKTickHandlerServer.playersInOutPortals.putIfAbsent(player, 0);
        }
    }

    @Override
    public void playPortalFXForUpendi(World world) {
        Minecraft.getMinecraft().getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation("portal.travel"), 1.0F, world.rand.nextFloat() * 0.4F + 0.8F, 0, 0, 0));
    }

    @Override
    public void spawnParticle(String type, double x, double y, double z, double motionX, double motionY, double motionZ) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.renderViewEntity == null || mc.theWorld == null) return;

        World world = mc.theWorld;
        int particleSetting = mc.gameSettings.particleSetting;
        if (particleSetting == 1 && world.rand.nextInt(3) == 0) particleSetting = 2;
        if (particleSetting > 1) return;

        switch (type) {
            case "outlandsPortal":
                mc.effectRenderer.addEffect(new LKEntityPortalFX(world, x, y, z, motionX, motionY, motionZ, false));
                break;
            case "passion":
                mc.effectRenderer.addEffect(new LKEntityPassionFX(world, x, y, z, motionX, motionY, motionZ));
                break;
            case "prideLandsPortal":
                mc.effectRenderer.addEffect(new LKEntityPortalFX(world, x, y, z, motionX, motionY, motionZ, true));
                break;
        }
    }

    @Override
    public EntityPlayer getSPPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}