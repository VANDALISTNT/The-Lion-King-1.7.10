package lionking;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.enchantment.Enchantment;

import java.util.Random;

import lionking.client.LKClientProxy;
import lionking.client.LKPacketHandlerClient;
import lionking.client.LKCreativeTabs;
import lionking.client.LKSimbaSit;
import lionking.client.LKSound;

import lionking.common.LKCommonProxy;
import lionking.common.LKTickHandlerServer;
import lionking.common.LKConnectionHandler;
import lionking.common.LKCharacterSpeech;
import lionking.common.LKPacketHandlerServer;
import lionking.common.LKEventHandler;

import lionking.tileentity.LKTileEntityFurRug;
import lionking.tileentity.LKTileEntityHyenaHead;
import lionking.tileentity.LKTileEntityMobSpawner;
import lionking.tileentity.LKTileEntityGrindingBowl;

import lionking.quest.MessageQuestCheck;
import lionking.quest.MessageQuestStage;
import lionking.quest.MessageQuestDelay;
import lionking.quest.MessageQuestCheckUpdate;

import lionking.client.*;
import lionking.common.*;
import lionking.world.*;
import lionking.biome.*;
import lionking.block.*;
import lionking.item.*;
import lionking.quest.*;
import lionking.entity.*;
import lionking.tileentity.*;
import lionking.enchantment.*;
import lionking.inventory.*;

import lionking.world.LKWorldProviderPrideLands;
import lionking.world.LKWorldProviderOutlands;
import lionking.world.LKWorldProviderUpendi;

import lionking.enchantment.LKEnchantmentTunnahDiggah;
import lionking.enchantment.LKEnchantmentRafikiThunder;
import lionking.enchantment.LKEnchantmentRafikiDurability;
import lionking.enchantment.LKEnchantmentRafikiDamage;
import lionking.enchantment.LKEnchantmentHyena;

@Mod(modid = "lionking", version = "v2.0 for Minecraft 1.7.10")
public class mod_LionKing {

    @SidedProxy(clientSide = "lionking.client.LKClientProxy", serverSide = "lionking.common.LKCommonProxy")
    public static LKCommonProxy proxy;

    @Mod.Instance("lionking")
    public static mod_LionKing instance;

    public static int lkMusicChance = 50;
    public static final int GUI_ID_QUIVER = 1;

    public static Item.ToolMaterial SILVER_TOOL = EnumHelper.addToolMaterial("SILVER", 2, 200, 6.0F, 2.0F, 14);
    public static ItemArmor.ArmorMaterial SILVER_ARMOR = EnumHelper.addArmorMaterial("SILVER", 15, new int[]{2, 6, 5, 2}, 9);
    public static ItemArmor.ArmorMaterial PEACOCK = EnumHelper.addArmorMaterial("PEACOCK", 33, new int[]{3, 8, 6, 3}, 10);
    public static ItemArmor.ArmorMaterial GEMSBOK = EnumHelper.addArmorMaterial("GEMSBOK", 7, new int[]{1, 3, 2, 1}, 25);
    public static ItemArmor.ArmorMaterial TICKET_LION_SUIT = EnumHelper.addArmorMaterial("TicketLionSuit_", 15, new int[]{2, 5, 4, 1}, 10);
    public static ItemArmor.ArmorMaterial armorSuit = EnumHelper.addArmorMaterial("AMULET", 5, new int[]{1, 2, 1, 1}, 15);
    public static ItemArmor.ArmorMaterial armorOutlandsHelm = EnumHelper.addArmorMaterial("OUTLANDS_HELM", 20, new int[]{2, 5, 4, 2}, 12);

    public static Enchantment diggahEnchantment;

    public static Block lionPortalFrame;
    public static Block lionPortal;
    public static Block woodSlabDouble;
    public static Block whiteFlower;
    public static Block forestLeaves;
    public static Block forestSapling;
    public static Block flowerTop;
    public static Block flowerBase;
    public static Block mangoLeaves;
    public static Block mangoSapling;
    public static Block grindBowl;
    public static Block rafikiWood;
    public static Block rafikiLeaves;
    public static Block woodSlabSingle;
    public static Block outlandsPortalFrame;
    public static Block outlandsPortal;
    public static Block bugTrap;
    public static Block pridestone;
    public static Block prideCoal;
    public static Block pridePillar;
    public static Block sapling;
    public static Block leaves;
    public static Block termite;
    public static Block stoneStairs;
    public static Block brickStairs;
    public static Block prideBrickMossy;
    public static Block oreSilver;
    public static Block outsand;
    public static Block outglass;
    public static Block outglassPane;
    public static Block starAltar;
    public static Block slabSingle;
    public static Block slabDouble;
    public static Block log;
    public static Block prideWood;
    public static Block blueFlower;
    public static Block drum;
    public static Block flowerVase;
    public static Block orePeacock;
    public static Block blockSilver;
    public static Block blockPeacock;
    public static Block rug;
    public static Block maize;
    public static Block stoneStairsCorrupt;
    public static Block brickStairsCorrupt;
    public static Block aridGrass;
    public static Block tilledSand;
    public static Block kiwanoBlock;
    public static Block kiwanoStem;
    public static Block pressurePlate;
    public static Block button;
    public static Block lever;
    public static Block outlandsPool;
    public static Block outshroom;
    public static Block outshroomGlowing;
    public static Block pumbaaBox;
    public static Block outlandsAltar;
    public static Block lily;
    public static Block stairsAcacia;
    public static Block stairsRainforest;
    public static Block stairsMango;
    public static Block blockBed;
    public static Block planks;
    public static Block hyenaHead;
    public static Block mountedShooter;
    public static Block passionSapling;
    public static Block passionLeaves;
    public static Block stairsPassion;
    public static Block hyenaTorch;
    public static Block wall;
    public static Block yamCrops;
    public static Block stairsBanana;
    public static Block prideWood2;
    public static Block bananaSapling;
    public static Block bananaLeaves;
    public static Block bananaCakeBlock;
    public static Block hangingBanana;
    public static Block stairsDeadwood;
    public static Block mobSpawner;
    public static Block driedMaizeBlock;
    public static Block driedMaizeSlabSingle;
    public static Block driedMaizeSlabDouble;
    public static Block stairsDriedMaize;
    public static Block prideBrick;

    public static Item ticket;
    public static Item hyenaBone;
    public static Item lionRaw;
    public static Item lionCooked;
    public static Item rafikiStick;
    public static Item purpleFlower;
    public static Item mango;
    public static Item featherBlue;
    public static Item featherYellow;
    public static Item featherRed;
    public static Item featherBlack;
    public static Item dartBlue;
    public static Item dartYellow;
    public static Item dartRed;
    public static Item dartShooter;
    public static Item hyenaBoneShard;
    public static Item zebraBoots;
    public static Item zebraHide;
    public static Item itemGrindingBowl;
    public static Item mangoDust;
    public static Item dartBlack;
    public static Item shovel;
    public static Item pickaxe;
    public static Item axe;
    public static Item sword;
    public static Item hoe;
    public static Item itemTermite;
    public static Item scarRug;
    public static Item jar;
    public static Item jarWater;
    public static Item silver;
    public static Item silverDartShooter;
    public static Item shovelSilver;
    public static Item pickaxeSilver;
    public static Item axeSilver;
    public static Item swordSilver;
    public static Item hoeSilver;
    public static Item rafikiCoin;
    public static Item termiteDust;
    public static Item lionDust;
    public static Item tunnahDiggah;
    public static Item crystal;
    public static Item bug;
    public static Item chocolateMufasa;
    public static Item pumbaaBomb;
    public static Item fur;
    public static Item jarMilk;
    public static Item zebraRaw;
    public static Item zebraCooked;
    public static Item rhinoRaw;
    public static Item rhinoCooked;
    public static Item helmetSilver;
    public static Item bodySilver;
    public static Item legsSilver;
    public static Item bootsSilver;
    public static Item vase;
    public static Item horn;
    public static Item hornGround;
    public static Item gemsbokHide;
    public static Item gemsbokHorn;
    public static Item gemsbokSpear;
    public static Item juice;
    public static Item helmetGemsbok;
    public static Item bodyGemsbok;
    public static Item legsGemsbok;
    public static Item bootsGemsbok;
    public static Item jarLava;
    public static Item peacockGem;
    public static Item shovelPeacock;
    public static Item pickaxePeacock;
    public static Item axePeacock;
    public static Item swordPeacock;
    public static Item hoePeacock;
    public static Item helmetPeacock;
    public static Item bodyPeacock;
    public static Item legsPeacock;
    public static Item bootsPeacock;
    public static Item rugDye;
    public static Item wings;
    public static Item corn;
    public static Item cornStalk;
    public static Item popcorn;
    public static Item nukaShard;
    public static Item outlanderFur;
    public static Item outlanderMeat;
    public static Item passionFruit;
    public static Item redFlower;
    public static Item kivulite;
    public static Item shovelKivulite;
    public static Item pickaxeKivulite;
    public static Item axeKivulite;
    public static Item swordKivulite;
    public static Item bugStew;
    public static Item crocodileMeat;
    public static Item poison;
    public static Item poisonedSpear;
    public static Item xpGrub;
    public static Item shovelCorrupt;
    public static Item pickaxeCorrupt;
    public static Item axeCorrupt;
    public static Item swordCorrupt;
    public static Item charm;
    public static Item zazuEgg;
    public static Item kiwano;
    public static Item kiwanoSeeds;
    public static Item ticketLionHead;
    public static Item ticketLionSuit;
    public static Item ticketLionLegs;
    public static Item ticketLionFeet;
    public static Item questBook;
    public static Item outlandsHelm;
    public static Item dartQuiver;
    public static Item outlandsFeather;
    public static Item ziraRug;
    public static Item ziraCoin;
    public static Item hyenaHeadItem;
    public static Item amulet;
    public static Item mountedShooterItem;
    public static Item staff;
    public static Item note;
    public static Item giraffeSaddle;
    public static Item tie;
    public static Item yam;
    public static Item roastYam;
    public static Item banana;
    public static Item bananaCake;
    public static Item featherPink;
    public static Item dartPink;
    public static Item bananaBread;
    public static Item hyenaMeal;
    public static Item cornKernels;
    public static Item driedMaize;
    public static Item spawnEgg;
    public static Item giraffeTie;

    public static int idBiomeSavannah;
    public static int idBiomeRainforest;
    public static int idBiomeMountains;
    public static int idBiomeRainforestHills;
    public static int idBiomeRiver;
    public static int idBiomeAridSavannah;
    public static int idBiomeOutlands;
    public static int idBiomeOutlandsMountains;
    public static int idBiomeOutlandsRiver;
    public static int idBiomeUpendi;
    public static int idBiomeGrasslandSavannah;
    public static int idBiomeWoodedSavannah;
    public static int idBiomeBananaForest;

    public static int idPrideLands;
    public static int idOutlands;
    public static int idUpendi;

    public static int boothLimit = 1000; 
    public static boolean randomBooths = true;
    public static final int GUI_ID_SIMBA = 1;
    public static final int GUI_ID_TIMON = 2;
    public static SimpleNetworkWrapper networkGame;
    public static SimpleNetworkWrapper networkQuests;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        LKCreativeTabs.initialiseTabs();
     
        networkGame = NetworkRegistry.INSTANCE.newSimpleChannel("LionKingNetwork");
        networkQuests = NetworkRegistry.INSTANCE.newSimpleChannel("LionKingQuests");
        
        networkQuests.registerMessage(MessageQuestCheck.Handler.class, MessageQuestCheck.class, 0, Side.SERVER);
        networkQuests.registerMessage(MessageQuestStage.Handler.class, MessageQuestStage.class, 1, Side.CLIENT);
        networkQuests.registerMessage(MessageQuestDelay.Handler.class, MessageQuestDelay.class, 2, Side.CLIENT);
        networkQuests.registerMessage(MessageQuestCheckUpdate.Handler.class, MessageQuestCheckUpdate.class, 3, Side.CLIENT);

        networkGame.registerMessage(LKSimbaSit.Handler.class, LKSimbaSit.class, 4, Side.SERVER);
        networkGame.registerMessage(LKPacketHandlerServer.SimbaSitMessage.Handler.class, LKPacketHandlerServer.SimbaSitMessage.class, 5, Side.SERVER);
        networkGame.registerMessage(LKPacketHandlerServer.DamageItemMessage.Handler.class, LKPacketHandlerServer.DamageItemMessage.class, 6, Side.SERVER);
        networkGame.registerMessage(LKPacketHandlerServer.QuestCheckMessage.Handler.class, LKPacketHandlerServer.QuestCheckMessage.class, 7, Side.SERVER);

        networkGame.registerMessage(LKPacketHandlerClient.LoginMessage.Handler.class, LKPacketHandlerClient.LoginMessage.class, 8, Side.CLIENT);
        networkGame.registerMessage(LKPacketHandlerClient.HomePortalMessage.Handler.class, LKPacketHandlerClient.HomePortalMessage.class, 9, Side.CLIENT);
        networkGame.registerMessage(LKPacketHandlerClient.MoundMessage.Handler.class, LKPacketHandlerClient.MoundMessage.class, 10, Side.CLIENT);
        networkGame.registerMessage(LKPacketHandlerClient.SimpleFlagMessage.Handler.class, LKPacketHandlerClient.SimpleFlagMessage.class, 11, Side.CLIENT);
        networkGame.registerMessage(LKPacketHandlerServer.Message.Handler.class, LKPacketHandlerServer.Message.class, 12, Side.CLIENT);
        networkGame.registerMessage(LKPacketHandlerServer.CustomFX.Handler.class, LKPacketHandlerServer.CustomFX.class, 13, Side.CLIENT);
        networkGame.registerMessage(LKPacketHandlerServer.BreakItem.Handler.class, LKPacketHandlerServer.BreakItem.class, 14, Side.CLIENT);

        GameRegistry.registerWorldGenerator(new LKWorldGenerator(), 10);
        MinecraftForge.EVENT_BUS.register(new LKWorldGenerator.BiomeFixer());

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        planks = new LKBlockPlanks().setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setBlockName("planks").setBlockTextureName("lionking:planks");
        lionPortalFrame = new LKBlockPortalFrame().setStepSound(Block.soundTypeStone).setBlockName("portalFrame").setBlockTextureName("lionking:portalFrame");
        lionPortal = new LKBlockPortal().setBlockUnbreakable().setResistance(6000000F).setStepSound(Block.soundTypeGlass).setLightLevel(0.75F).setBlockName("portal").setBlockTextureName("lionking:portal");
        woodSlabDouble = new LKBlockWoodSlab(true).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setBlockName("woodSlabDouble").setBlockTextureName("lionking:woodSlabDouble");
        whiteFlower = new LKBlockFlower().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("whiteFlower").setBlockTextureName("lionking:whiteFlower");
        forestLeaves = new LKBlockLeaves().setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundTypeGrass).setBlockName("rainforestLeaves").setBlockTextureName("lionking:rainforestLeaves");
        forestSapling = new LKBlockSapling().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("rainforestSapling").setBlockTextureName("lionking:rainforestSapling");
        flowerTop = new LKBlockFlowerTop().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("tallFlowerTop").setBlockTextureName("lionking:tallFlowerTop");
        flowerBase = new LKBlockFlowerBase().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("tallFlowerBase").setBlockTextureName("lionking:tallFlowerBase");
        mangoLeaves = new LKBlockLeaves().setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundTypeGrass).setBlockName("mangoLeaves").setBlockTextureName("lionking:mangoLeaves");
        mangoSapling = new LKBlockSapling().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("mangoSapling").setBlockTextureName("lionking:mangoSapling");
        grindBowl = new LKBlockGrindingBowl().setHardness(1.5F).setStepSound(Block.soundTypeWood).setBlockName("bowlBlock").setBlockTextureName("lionking:bowlBlock");
        rafikiWood = new LKBlockRafikiWood().setBlockUnbreakable().setResistance(6000000F).setStepSound(Block.soundTypeWood).setBlockName("rafikiWood").setBlockTextureName("lionking:rafikiWood");
        rafikiLeaves = new LKBlockRafikiLeaves().setBlockUnbreakable().setResistance(6000000F).setLightOpacity(1).setStepSound(Block.soundTypeGrass).setBlockName("rafikiLeaves").setBlockTextureName("lionking:rafikiLeaves");
        woodSlabSingle = new LKBlockWoodSlab(false).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setBlockName("woodSlabSingle").setBlockTextureName("lionking:woodSlabSingle");
        outlandsPortalFrame = new LKBlockPortalFrame().setStepSound(Block.soundTypeStone).setBlockName("outlandsPortalFrame").setBlockTextureName("lionking:outlandsPortalFrame");
        outlandsPortal = new LKBlockOutlandsPortal().setBlockUnbreakable().setResistance(6000000F).setStepSound(Block.soundTypeGlass).setLightLevel(0.75F).setBlockName("outlandsPortal").setBlockTextureName("lionking:outlandsPortal");
        bugTrap = new LKBlockBugTrap().setHardness(1.0F).setStepSound(Block.soundTypeWood).setBlockName("bugTrap").setBlockTextureName("lionking:bugTrap");
        pridestone = new LKBlockPridestone().setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypeStone).setBlockName("pridestone").setBlockTextureName("lionking:pridestone");
        prideCoal = new LKBlockOre().setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypeStone).setBlockName("oreCoal").setBlockTextureName("lionking:oreCoal");
        prideBrick = new LKBlockPrideBrick().setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypeStone).setBlockName("pridestoneBrick").setBlockTextureName("lionking:pridestoneBrick");
        pridePillar = new LKBlockPillar().setHardness(1.2F).setResistance(8.0F).setStepSound(Block.soundTypeStone).setBlockName("pillar").setBlockTextureName("lionking:pillar");
        sapling = new LKBlockSapling().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("acaciaSapling").setBlockTextureName("lionking:acaciaSapling");
        leaves = new LKBlockLeaves().setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundTypeGrass).setBlockName("acaciaLeaves").setBlockTextureName("lionking:acaciaLeaves");
        termite = new LKBlockTermite().setHardness(0.5F).setResistance(3.0F).setBlockName("termiteMound").setBlockTextureName("lionking:termiteMound");
        stoneStairs = new LKBlockStairs(pridestone, 0).setBlockName("stairsStone").setBlockTextureName("lionking:stairsStone");
        brickStairs = new LKBlockStairs(prideBrick, 0).setBlockName("stairsBrick").setBlockTextureName("lionking:stairsBrick");
        prideBrickMossy = new LKBlockPrideBrickVariant().setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypeStone).setBlockName("pridestoneBrickMossy").setBlockTextureName("lionking:pridestoneBrickMossy");
        oreSilver = new LKBlockOre().setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypeStone).setBlockName("oreSilver").setBlockTextureName("lionking:oreSilver");
        outsand = new LKBlockOutsand().setHardness(0.7F).setStepSound(Block.soundTypeSand).setBlockName("outsand").setBlockTextureName("lionking:outsand");
        outglass = new LKBlockGlass(Material.glass, false).setHardness(0.4F).setStepSound(Block.soundTypeGlass).setBlockName("outglass").setBlockTextureName("lionking:outglass");
        outglassPane = new LKBlockPane("lionking:outglass", "lionking:outsand", Material.glass, true).setHardness(0.4F).setStepSound(Block.soundTypeGlass).setBlockName("outglassPane").setBlockTextureName("lionking:outglassPane");
        starAltar = new LKBlockStarAltar().setHardness(2.0F).setResistance(15.0F).setStepSound(Block.soundTypeStone).setBlockName("altar").setBlockTextureName("lionking:altar");
        slabSingle = new LKBlockSlab(false).setHardness(2.0F).setResistance(8.0F).setStepSound(Block.soundTypeStone).setBlockName("slab").setBlockTextureName("lionking:slab");
        slabDouble = new LKBlockSlab(true).setHardness(2.0F).setResistance(8.0F).setStepSound(Block.soundTypeStone).setBlockName("slabDouble").setBlockTextureName("lionking:slabDouble");
        log = new LKBlockLog().setHardness(2.0F).setStepSound(Block.soundTypeWood).setBlockName("log").setBlockTextureName("lionking:log");
        prideWood = new LKBlockWood().setHardness(2.0F).setStepSound(Block.soundTypeWood).setBlockName("wood").setBlockTextureName("lionking:wood");
        blueFlower = new LKBlockFlower().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("blueFlower").setBlockTextureName("lionking:blueFlower");
        drum = new LKBlockDrum().setHardness(1.5F).setStepSound(Block.soundTypeWood).setBlockName("drum").setBlockTextureName("lionking:drum");
        flowerVase = new LKBlockVase().setHardness(0.0F).setStepSound(Block.soundTypeStone).setBlockName("vaseBlock").setBlockTextureName("lionking:vaseBlock");
        orePeacock = new LKBlockOre().setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypeStone).setBlockName("orePeacock").setBlockTextureName("lionking:orePeacock");
        blockSilver = new LKBlockOreStorage().setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal).setBlockName("blockSilver").setBlockTextureName("lionking:blockSilver");
        blockPeacock = new LKBlockOreStorage().setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal).setBlockName("blockPeacock").setBlockTextureName("lionking:blockPeacock");
        rug = new LKBlockRug().setHardness(0.3F).setStepSound(Block.soundTypeCloth).setBlockName("rug").setBlockTextureName("lionking:rug");
        maize = new LKBlockMaize().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("maize").setBlockTextureName("lionking:maize");
        stoneStairsCorrupt = new LKBlockStairs(pridestone, 1).setBlockName("stairsStoneCorrupt").setBlockTextureName("lionking:stairsStoneCorrupt");
        brickStairsCorrupt = new LKBlockStairs(prideBrick, 1).setBlockName("stairsBrickCorrupt").setBlockTextureName("lionking:stairsBrickCorrupt");
        aridGrass = new LKBlockAridGrass().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("aridGrass").setBlockTextureName("lionking:aridGrass");
        tilledSand = new LKBlockTilledSand().setHardness(0.6F).setStepSound(Block.soundTypeGravel).setBlockName("tilledSand").setBlockTextureName("lionking:tilledSand");
        kiwanoBlock = new LKBlockKiwano().setHardness(1.0F).setStepSound(Block.soundTypeWood).setBlockName("kiwanoBlock").setBlockTextureName("lionking:kiwanoBlock");
        kiwanoStem = new LKBlockKiwanoStem().setHardness(0.0F).setStepSound(Block.soundTypeWood).setBlockName("stemKiwano").setBlockTextureName("lionking:stemKiwano");
        pressurePlate = new LKBlockPressurePlate("lionking:pridestone", BlockPressurePlate.Sensitivity.everything, Material.rock).setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("pressurePlate").setBlockTextureName("lionking:pressurePlate");
        button = new LKBlockButton().setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("button").setBlockTextureName("lionking:button");
        lever = new LKBlockLever().setHardness(0.5F).setStepSound(Block.soundTypeWood).setBlockName("lever").setBlockTextureName("lionking:lever");
        outlandsPool = new LKBlockOutlandsPool().setHardness(-1.0F).setResistance(6000000F).setLightOpacity(3).setBlockName("pool").setBlockTextureName("lionking:pool");
        outshroom = new LKBlockOutshroom(false).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("outshroom").setBlockTextureName("lionking:outshroom");
        outshroomGlowing = new LKBlockOutshroom(true).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("outshroomGlowing").setBlockTextureName("lionking:outshroomGlowing");
        pumbaaBox = new LKBlockPumbaaBox().setHardness(1.0F).setStepSound(Block.soundTypeWood).setBlockName("box").setBlockTextureName("lionking:box");
        outlandsAltar = new LKBlockOutlandsAltar().setBlockUnbreakable().setResistance(6000000F).setStepSound(Block.soundTypeStone).setBlockName("outlandsAltar").setBlockTextureName("lionking:outlandsAltar");
        lily = new LKBlockLily().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("lily").setBlockTextureName("lionking:lily");
        stairsAcacia = new LKBlockStairs(planks, 0).setBlockName("stairsAcacia").setBlockTextureName("lionking:stairsAcacia");
        stairsRainforest = new LKBlockStairs(planks, 1).setBlockName("stairsRainforest").setBlockTextureName("lionking:stairsRainforest");
        stairsMango = new LKBlockStairs(planks, 2).setBlockName("stairsMango").setBlockTextureName("lionking:stairsMango");
        blockBed = new LKBlockBed().setHardness(0.2F).setBlockName("bed").setBlockTextureName("lionking:bed");
        hyenaHead = new LKBlockHyenaHead().setHardness(1.0F).setStepSound(Block.soundTypeStone).setBlockName("hyenaHead").setBlockTextureName("lionking:hyenaHead");
        mountedShooter = new LKBlockMountedShooter().setHardness(0.5F).setStepSound(Block.soundTypeWood).setBlockName("mountedShooter").setBlockTextureName("lionking:mountedShooter");
        passionSapling = new LKBlockSapling().setHardness(0.0F).setLightLevel(0.75F).setStepSound(Block.soundTypeGrass).setBlockName("passionSapling").setBlockTextureName("lionking:passionSapling");
        passionLeaves = new LKBlockLeaves().setHardness(0.2F).setLightOpacity(1).setLightLevel(0.75F).setStepSound(Block.soundTypeGrass).setBlockName("passionLeaves").setBlockTextureName("lionking:passionLeaves");
        stairsPassion = new LKBlockStairs(planks, 3).setBlockName("stairsPassion").setBlockTextureName("lionking:stairsPassion");
        hyenaTorch = new LKBlockHyenaTorch().setHardness(0.0F).setLightLevel(0.875F).setStepSound(Block.soundTypeStone).setBlockName("hyenaBoneTorch").setBlockTextureName("lionking:hyenaBoneTorch");
        wall = new LKBlockWall().setBlockName("stoneWall").setBlockTextureName("lionking:stoneWall");
        yamCrops = new LKBlockYam().setBlockName("yam").setBlockTextureName("lionking:yam");
        stairsBanana = new LKBlockStairs(planks, 4).setBlockName("stairsBanana").setBlockTextureName("lionking:stairsBanana");
        prideWood2 = new LKBlockWood2().setHardness(2.0F).setStepSound(Block.soundTypeWood).setBlockName("wood2").setBlockTextureName("lionking:wood2");
        bananaSapling = new LKBlockSapling().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("bananaSapling").setBlockTextureName("lionking:bananaSapling");
        bananaLeaves = new LKBlockLeaves().setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundTypeGrass).setBlockName("bananaLeaves").setBlockTextureName("lionking:bananaLeaves");
        bananaCakeBlock = new LKBlockBananaCake().setHardness(0.5F).setStepSound(Block.soundTypeCloth).setBlockName("bananaCake").setBlockTextureName("lionking:bananaCake");
        hangingBanana = new LKBlockHangingBanana().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("hangingBanana").setBlockTextureName("lionking:hangingBanana");
        stairsDeadwood = new LKBlockStairs(planks, 5).setBlockName("stairsDeadwood").setBlockTextureName("lionking:stairsDeadwood");
        mobSpawner = new LKBlockMobSpawner().setHardness(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("mobSpawner").setBlockTextureName("lionking.mobSpawner");
        driedMaizeBlock = new LKBlockDriedMaize().setHardness(0.5F).setStepSound(Block.soundTypeGrass).setBlockName("driedMaize").setBlockTextureName("lionking:driedMaize");
        driedMaizeSlabSingle = new LKBlockMaizeSlab(false).setHardness(0.5F).setStepSound(Block.soundTypeGrass).setBlockName("driedMaizeSlabSingle").setBlockTextureName("lionking:slabDriedMaizeSingle");
        driedMaizeSlabDouble = new LKBlockMaizeSlab(true).setHardness(0.5F).setStepSound(Block.soundTypeGrass).setBlockName("driedMaizeSlabDouble").setBlockTextureName("lionking:slabDriedMaizeDouble");
        stairsDriedMaize = new LKBlockDriedMaizeStairs().setHardness(0.5F).setStepSound(Block.soundTypeGrass).setBlockName("stairsDriedMaize").setBlockTextureName("lionking:stairsDriedMaize");

        spawnEgg = new LKItemSpawnEgg().setUnlocalizedName("spawnEgg").setTextureName("monsterPlacer");
        ticket = new LKItemTicket().setUnlocalizedName("ticket").setTextureName("lionking:ticket");
        hyenaBone = new LKItem().setUnlocalizedName("hyenaBone").setTextureName("lionking:hyenaBone");
        lionRaw = new LKItemFood(3, 0.3F, true).setUnlocalizedName("lionRaw").setTextureName("lionking:lionRaw");
        lionCooked = new LKItemFood(8, 0.8F, true).setUnlocalizedName("lionCooked").setTextureName("lionking:lionCooked");
        rafikiStick = new LKItem().setUnlocalizedName("rafikiStick").setTextureName("lionking:rafikiStick");
        purpleFlower = new LKItem().setUnlocalizedName("purpleFlower").setTextureName("lionking:purpleFlower");
        mango = new LKItemFood(4, 0.3F, false).setUnlocalizedName("mango").setTextureName("lionking:mango");
        featherBlue = new LKItem().setUnlocalizedName("featherBlue").setTextureName("lionking:featherBlue");
        featherYellow = new LKItem().setUnlocalizedName("featherYellow").setTextureName("lionking:featherYellow");
        featherRed = new LKItem().setUnlocalizedName("featherRed").setTextureName("lionking:featherRed");
        dartBlue = new LKItem().setUnlocalizedName("dartBlue").setTextureName("lionking:dartBlue");
        dartYellow = new LKItem().setUnlocalizedName("dartYellow").setTextureName("lionking:dartYellow");
        dartRed = new LKItem().setUnlocalizedName("dartRed").setTextureName("lionking:dartRed");
        dartShooter = new LKItem().setUnlocalizedName("dartShooter").setTextureName("lionking:dartShooter");
        hyenaBoneShard = new LKItem().setUnlocalizedName("hyenaBoneShard").setTextureName("lionking:hyenaBoneShard");
        zebraBoots = new LKItemSpecialArmor(ItemArmor.ArmorMaterial.CLOTH, 0, 3).setUnlocalizedName("zebraBoots").setTextureName("lionking:zebraBoots");
        zebraHide = new LKItem().setUnlocalizedName("zebraHide").setTextureName("lionking:zebraHide");
        itemGrindingBowl = new LKItem().setUnlocalizedName("grindingBowl").setTextureName("lionking:grindingBowl");
        mangoDust = new LKItem().setUnlocalizedName("mangoDust").setTextureName("lionking:mangoGround");
        dartBlack = new LKItem().setUnlocalizedName("dartBlack").setTextureName("lionking:dartOutlandish");
        featherBlack = new LKItem().setUnlocalizedName("featherBlack").setTextureName("lionking:featherVulture");
        shovel = new LKItemShovel(Item.ToolMaterial.STONE).setUnlocalizedName("shovel").setTextureName("lionking:shovelPridestone");
        pickaxe = new LKItemPickaxe(Item.ToolMaterial.STONE).setUnlocalizedName("pickaxe").setTextureName("lionking:pickaxePridestone");
        axe = new LKItemAxe(Item.ToolMaterial.STONE).setUnlocalizedName("axe").setTextureName("lionking:axePridestone");
        sword = new LKItemSword(Item.ToolMaterial.STONE).setUnlocalizedName("sword").setTextureName("lionking:swordPridestone");
        hoe = new LKItemHoe(Item.ToolMaterial.STONE).setUnlocalizedName("hoe").setTextureName("lionking:hoePridestone");
        itemTermite = new LKItem().setUnlocalizedName("itemTermite").setTextureName("lionking:termite");
        scarRug = new LKItem().setUnlocalizedName("scarRug").setTextureName("lionking:scarRug");
        jar = new LKItem().setUnlocalizedName("jar").setTextureName("lionking:jar");
        jarWater = new LKItem().setUnlocalizedName("jarWater").setContainerItem(jar).setTextureName("lionking:jarWater");
        silver = new LKItem().setUnlocalizedName("silver").setTextureName("lionking:silver");
        silverDartShooter = new LKItem().setUnlocalizedName("silverDartShooter").setTextureName("lionking:dartShooterSilver");
        shovelSilver = new LKItemShovel(SILVER_TOOL).setUnlocalizedName("shovelSilver").setTextureName("lionking:shovelSilver");
        pickaxeSilver = new LKItemPickaxe(SILVER_TOOL).setUnlocalizedName("pickaxeSilver").setTextureName("lionking:pickaxeSilver");
        axeSilver = new LKItemAxe(SILVER_TOOL).setUnlocalizedName("axeSilver").setTextureName("lionking:axeSilver");
        swordSilver = new LKItemSword(SILVER_TOOL).setUnlocalizedName("swordSilver").setTextureName("lionking:swordSilver");
        hoeSilver = new LKItemHoe(SILVER_TOOL).setUnlocalizedName("hoeSilver").setTextureName("lionking:hoeSilver");
        rafikiCoin = new LKItem().setUnlocalizedName("rafikiCoin").setTextureName("lionking:rafikiCoin");
        termiteDust = new LKItem().setUnlocalizedName("termiteDust").setTextureName("lionking:termiteGround");
        lionDust = new LKItem().setUnlocalizedName("lionDust").setTextureName("lionking:rafikiDust");
        tunnahDiggah = new LKItem().setUnlocalizedName("tunnahDiggah").setTextureName("lionking:tunnahDiggah");
        crystal = new LKItem().setUnlocalizedName("crystal").setTextureName("lionking:crystal");
        bug = new LKItem().setUnlocalizedName("bug").setTextureName("lionking:bug");
        chocolateMufasa = new LKItem().setUnlocalizedName("chocolateMufasa").setTextureName("lionking:chocolateMufasa");
        pumbaaBomb = new LKItem().setUnlocalizedName("pumbaaBomb").setTextureName("lionking:pumbaaFlatulence");
        fur = new LKItem().setUnlocalizedName("fur").setTextureName("lionking:lionFur");
        jarMilk = new LKItem().setUnlocalizedName("jarMilk").setTextureName("lionking:jarMilk");
        zebraRaw = new LKItemFood(3, 0.3F, true).setUnlocalizedName("zebraRaw").setTextureName("lionking:zebraRaw");
        zebraCooked = new LKItemFood(8, 0.8F, true).setUnlocalizedName("zebraCooked").setTextureName("lionking:zebraCooked");
        rhinoRaw = new LKItemFood(3, 0.3F, true).setUnlocalizedName("rhinoRaw").setTextureName("lionking:rhinoRaw");
        rhinoCooked = new LKItemFood(8, 0.8F, true).setUnlocalizedName("rhinoCooked").setTextureName("lionking:rhinoCooked");
        helmetSilver = new LKItemSpecialArmor(SILVER_ARMOR, 0, 0).setUnlocalizedName("helmetSilver").setTextureName("lionking:Silver_helmet");
        bodySilver = new LKItemSpecialArmor(SILVER_ARMOR, 0, 1).setUnlocalizedName("bodySilver").setTextureName("lionking:Silver_body");
        legsSilver = new LKItemSpecialArmor(SILVER_ARMOR, 0, 2).setUnlocalizedName("legsSilver").setTextureName("lionking:Silver_legs");
        bootsSilver = new LKItemSpecialArmor(SILVER_ARMOR, 0, 3).setUnlocalizedName("bootsSilver").setTextureName("lionking:Silver_boots");
        vase = new LKItem().setUnlocalizedName("vase").setTextureName("lionking:vase");
        horn = new LKItem().setUnlocalizedName("horn").setTextureName("lionking:rhinoHorn");
        hornGround = new LKItem().setUnlocalizedName("hornGround").setTextureName("lionking:rhinoHornGround");
        gemsbokHide = new LKItem().setUnlocalizedName("gemsbokHide").setTextureName("lionking:gemsbokHide");
        gemsbokHorn = new LKItem().setUnlocalizedName("gemsbokHorn").setTextureName("lionking:gemsbokHorn");
        gemsbokSpear = new LKItem().setUnlocalizedName("gemsbokSpear").setTextureName("lionking:gemsbokSpear");
        juice = new LKItem().setUnlocalizedName("MangoJuice").setTextureName("lionking:jarMangoJuice");
        helmetGemsbok = new LKItemSpecialArmor(GEMSBOK, 0, 0).setUnlocalizedName("helmetGemsbok").setTextureName("lionking:Gemsbok_helmet");
        bodyGemsbok = new LKItemSpecialArmor(GEMSBOK, 0, 1).setUnlocalizedName("bodyGemsbok").setTextureName("lionking:Gemsbok_body");
        legsGemsbok = new LKItemSpecialArmor(GEMSBOK, 0, 2).setUnlocalizedName("legsGemsbok").setTextureName("lionking:Gemsbok_legs");
        bootsGemsbok = new LKItemSpecialArmor(GEMSBOK, 0, 3).setUnlocalizedName("bootsGemsbok").setTextureName("lionking:Gemsbok_boots");
        jarLava = new LKItem().setUnlocalizedName("jarLava").setTextureName("lionking:jarLava");
        peacockGem = new LKItem().setUnlocalizedName("peacockGem").setTextureName("lionking:peacockGem");
        shovelPeacock = new LKItemShovel(Item.ToolMaterial.EMERALD).setUnlocalizedName("shovelPeacock").setTextureName("lionking:shovelPeacock");
        pickaxePeacock = new LKItemPickaxe(Item.ToolMaterial.EMERALD).setUnlocalizedName("pickaxePeacock").setTextureName("lionking:pickaxePeacock");
        axePeacock = new LKItemAxe(Item.ToolMaterial.EMERALD).setUnlocalizedName("axePeacock").setTextureName("lionking:axeCorruptPridestone");
        swordPeacock = new LKItemSword(Item.ToolMaterial.EMERALD).setUnlocalizedName("swordPeacock").setTextureName("lionking:swordPeacock");
        hoePeacock = new LKItemHoe(Item.ToolMaterial.EMERALD).setUnlocalizedName("hoePeacock").setTextureName("lionking:hoePeacock");
        helmetPeacock = new LKItemSpecialArmor(PEACOCK, 0, 0).setUnlocalizedName("helmetPeacock").setTextureName("lionking:Peacock_helmet");
        bodyPeacock = new LKItemSpecialArmor(PEACOCK, 0, 1).setUnlocalizedName("bodyPeacock").setTextureName("lionking:Peacock_body");
        legsPeacock = new LKItemSpecialArmor(PEACOCK, 0, 2).setUnlocalizedName("legsPeacock").setTextureName("lionking:Peacock_legs");
        bootsPeacock = new LKItemSpecialArmor(PEACOCK, 0, 3).setUnlocalizedName("bootsPeacock").setTextureName("lionking:Peacock_boots");
        rugDye = new LKItem().setUnlocalizedName("rugDye").setTextureName("lionking:dye");
        wings = new LKItem().setUnlocalizedName("wings").setTextureName("lionking:peacockWings");
        corn = new LKItem().setUnlocalizedName("corn").setTextureName("lionking:corn");
        cornStalk = new LKItem().setUnlocalizedName("cornStalk").setTextureName("lionking:maizeStalks");
        popcorn = new LKItem().setUnlocalizedName("popcorn").setTextureName("lionking:popcorn");
        nukaShard = new LKItem().setUnlocalizedName("nukaShard").setTextureName("lionking:nukaShard");
        outlanderFur = new LKItem().setUnlocalizedName("outlanderFur").setTextureName("lionking:outlanderFur");
        outlanderMeat = new LKItem().setUnlocalizedName("outlanderMeat").setTextureName("lionking:outlanderMeat");
        passionFruit = new LKItem().setUnlocalizedName("passionFruit").setTextureName("lionking:passionFruit");
        redFlower = new LKItem().setUnlocalizedName("redFlower").setTextureName("lionking:redFlower");
        kivulite = new LKItem().setUnlocalizedName("kivulite").setTextureName("lionking:kivulite");
        shovelKivulite = new LKItemShovel(Item.ToolMaterial.IRON).setUnlocalizedName("shovelKivulite").setTextureName("lionking:shovelKivulite");
        pickaxeKivulite = new LKItemPickaxe(Item.ToolMaterial.IRON).setUnlocalizedName("pickaxeKivulite").setTextureName("lionking:pickaxeKivulite");
        axeKivulite = new LKItemAxe(Item.ToolMaterial.IRON).setUnlocalizedName("axeKivulite").setTextureName("lionking:axeKivulite");
        swordKivulite = new LKItemSword(Item.ToolMaterial.IRON).setUnlocalizedName("swordKivulite").setTextureName("lionking:swordKivulite");
        bugStew = new LKItem().setUnlocalizedName("bugStew").setTextureName("lionking:bugStew");
        crocodileMeat = new LKItem().setUnlocalizedName("crocodileMeat").setTextureName("lionking:crocodileMeat");
        poison = new LKItem().setUnlocalizedName("poison").setTextureName("lionking:poison");
        poisonedSpear = new LKItem().setUnlocalizedName("poisonedSpear").setTextureName("lionking:gemsbokSpearPoisoned");
        xpGrub = new LKItem().setUnlocalizedName("xpGrub").setTextureName("lionking:experienceGrub");
        shovelCorrupt = new LKItemShovel(Item.ToolMaterial.STONE).setUnlocalizedName("shovelCorrupt").setTextureName("lionking:shovelCorruptPridestone");
        pickaxeCorrupt = new LKItemPickaxe(Item.ToolMaterial.STONE).setUnlocalizedName("pickaxeCorrupt").setTextureName("lionking:pickaxeCorruptPridestone");
        axeCorrupt = new LKItemAxe(Item.ToolMaterial.STONE).setUnlocalizedName("axeCorrupt").setTextureName("lionking:axeCorruptPridestone");
        swordCorrupt = new LKItemSword(Item.ToolMaterial.STONE).setUnlocalizedName("swordCorrupt").setTextureName("lionking:swordCorruptPridestone");
        charm = new LKItem().setUnlocalizedName("charm").setTextureName("lionking:charm");
        zazuEgg = new LKItem().setUnlocalizedName("zazuEgg").setTextureName("lionking:zazuEgg");
        kiwano = new LKItem().setUnlocalizedName("kiwano").setTextureName("lionking:kiwano");
        kiwanoSeeds = new LKItem().setUnlocalizedName("kiwanoSeeds").setTextureName("lionking:kiwanoSeeds");
        ticketLionHead = new LKItem().setUnlocalizedName("ticketLionHead").setTextureName("lionking:TicketLionSuit_helmet");
        ticketLionSuit = new LKItem().setUnlocalizedName("ticketLionSuit").setTextureName("lionking:TicketLionSuit_body");
        ticketLionLegs = new LKItem().setUnlocalizedName("ticketLionLegs").setTextureName("lionking:TicketLionSuit_legs");
        ticketLionFeet = new LKItem().setUnlocalizedName("ticketLionFeet").setTextureName("lionking:TicketLionSuit_boots");
        questBook = new LKItem().setUnlocalizedName("questBook").setTextureName("lionking:questBook");
        outlandsHelm = new LKItem().setUnlocalizedName("outlandsHelm").setTextureName("lionking:outlandishHelm");
        dartQuiver = new LKItem().setUnlocalizedName("dartQuiver").setTextureName("lionking:quiver");
        outlandsFeather = new LKItem().setUnlocalizedName("outlandsFeather").setTextureName("lionking:waywardFeather");
        ziraRug = new LKItem().setUnlocalizedName("ziraRug").setTextureName("lionking:ziraRug");
        ziraCoin = new LKItem().setUnlocalizedName("ziraCoin").setTextureName("lionking:ziraCoin");
        hyenaHeadItem = new LKItem().setUnlocalizedName("hyenaHeadItem").setTextureName("lionking:hyenaHead");
        amulet = new LKItem().setUnlocalizedName("amulet").setTextureName("lionking:amulet");
        mountedShooterItem = new LKItem().setUnlocalizedName("mountedShooterItem").setTextureName("lionking:mountedShooter");
        staff = new LKItem().setUnlocalizedName("staff").setTextureName("lionking:rhythmStaff");
        note = new LKItem().setUnlocalizedName("note").setTextureName("lionking:note");
        giraffeSaddle = new LKItem().setUnlocalizedName("giraffeSaddle").setTextureName("lionking:giraffeSaddle");
        tie = new LKItemGiraffeTie().setUnlocalizedName("tie").setTextureName("lionking:giraffeTie");
        yam = new LKItem().setUnlocalizedName("yam_item").setTextureName("lionking:yam_item");
        roastYam = new LKItem().setUnlocalizedName("roastYam").setTextureName("lionking:yamRoast");
        banana = new LKItem().setUnlocalizedName("banana").setTextureName("lionking:banana");
        bananaCake = new LKItem().setUnlocalizedName("bananaCakeitem").setTextureName("lionking:bananaCake");
        featherPink = new LKItem().setUnlocalizedName("featherPink").setTextureName("lionking:featherFlamingo");
        dartPink = new LKItem().setUnlocalizedName("dartPink").setTextureName("lionking:dartFlamingo");
        bananaBread = new LKItem().setUnlocalizedName("bananaBread").setTextureName("lionking:bananaBread");
        hyenaMeal = new LKItem().setUnlocalizedName("hyenaMeal").setTextureName("lionking:hyenaMeal");
        cornKernels = new LKItem().setUnlocalizedName("cornKernels").setTextureName("lionking:cornKernels");
        driedMaize = new LKItem().setUnlocalizedName("driedMaize_item").setTextureName("lionking:driedMaize");
        giraffeTie = new LKItemGiraffeTie().setUnlocalizedName("giraffe_tie");

        Enchantment tunnahDiggahEnchant = new LKEnchantmentTunnahDiggah(100, 2);
        Enchantment.enchantmentsList[100] = tunnahDiggahEnchant;
        Enchantment rafikiThunder = new LKEnchantmentRafikiThunder(101, 2);
        Enchantment.enchantmentsList[101] = rafikiThunder;
        Enchantment rafikiDurability = new LKEnchantmentRafikiDurability(102, 5);
        Enchantment.enchantmentsList[102] = rafikiDurability;
        Enchantment rafikiDamage = new LKEnchantmentRafikiDamage(103, 10);
        Enchantment.enchantmentsList[103] = rafikiDamage;
        Enchantment hyena = new LKEnchantmentHyena(104, 5);
        Enchantment.enchantmentsList[104] = hyena;

        GameRegistry.registerBlock(planks, "planks");
        GameRegistry.registerBlock(lionPortalFrame, "portalFrame");
        GameRegistry.registerBlock(lionPortal, "portal");
        GameRegistry.registerBlock(woodSlabDouble, "woodSlabDouble");
        GameRegistry.registerBlock(whiteFlower, "whiteFlower");
        GameRegistry.registerBlock(forestLeaves, "rainforestLeaves");
        GameRegistry.registerBlock(forestSapling, "rainforestSapling");
        GameRegistry.registerBlock(flowerTop, "tallFlowerTop");
        GameRegistry.registerBlock(flowerBase, "tallFlowerBase");
        GameRegistry.registerBlock(mangoLeaves, "mangoLeaves");
        GameRegistry.registerBlock(mangoSapling, "mangoSapling");
        GameRegistry.registerBlock(grindBowl, "bowlBlock");
        GameRegistry.registerBlock(rafikiWood, "rafikiWood");
        GameRegistry.registerBlock(rafikiLeaves, "rafikiLeaves");
        GameRegistry.registerBlock(woodSlabSingle, "woodSlabSingle");
        GameRegistry.registerBlock(outlandsPortalFrame, "outlandsPortalFrame");
        GameRegistry.registerBlock(outlandsPortal, "outlandsPortal");
        GameRegistry.registerBlock(bugTrap, "bugTrap");
        GameRegistry.registerBlock(pridestone, "pridestone");
        GameRegistry.registerBlock(prideCoal, "oreCoal");
        GameRegistry.registerBlock(prideBrick, "pridestoneBrick");
        GameRegistry.registerBlock(pridePillar, "pillar");
        GameRegistry.registerBlock(sapling, "acaciaSapling");
        GameRegistry.registerBlock(leaves, "acaciaLeaves");
        GameRegistry.registerBlock(termite, "termiteMound");
        GameRegistry.registerBlock(stoneStairs, "stairsStone");
        GameRegistry.registerBlock(brickStairs, "stairsBrick");
        GameRegistry.registerBlock(prideBrickMossy, "pridestoneBrickMossy");
        GameRegistry.registerBlock(oreSilver, "oreSilver");
        GameRegistry.registerBlock(outsand, "outsand");
        GameRegistry.registerBlock(outglass, "outglass");
        GameRegistry.registerBlock(outglassPane, "outglassPane");
        GameRegistry.registerBlock(starAltar, "altar");
        GameRegistry.registerBlock(slabSingle, "slab");
        GameRegistry.registerBlock(slabDouble, "slabDouble");
        GameRegistry.registerBlock(log, "log");
        GameRegistry.registerBlock(prideWood, "wood");
        GameRegistry.registerBlock(blueFlower, "blueFlower");
        GameRegistry.registerBlock(drum, "drum");
        GameRegistry.registerBlock(flowerVase, "vaseBlock");
        GameRegistry.registerBlock(orePeacock, "orePeacock");
        GameRegistry.registerBlock(blockSilver, "blockSilver");
        GameRegistry.registerBlock(blockPeacock, "blockPeacock");
        GameRegistry.registerBlock(rug, "rug");
        GameRegistry.registerBlock(maize, "maize");
        GameRegistry.registerBlock(stoneStairsCorrupt, "stairsStoneCorrupt");
        GameRegistry.registerBlock(brickStairsCorrupt, "stairsBrickCorrupt");
        GameRegistry.registerBlock(aridGrass, "aridGrass");
        GameRegistry.registerBlock(tilledSand, "tilledSand");
        GameRegistry.registerBlock(kiwanoBlock, "kiwanoBlock");
        GameRegistry.registerBlock(kiwanoStem, "stemKiwano");
        GameRegistry.registerBlock(pressurePlate, "pressurePlate");
        GameRegistry.registerBlock(button, "button");
        GameRegistry.registerBlock(lever, "lever");
        GameRegistry.registerBlock(outlandsPool, "pool");
        GameRegistry.registerBlock(outshroom, "outshroom");
        GameRegistry.registerBlock(outshroomGlowing, "outshroomGlowing");
        GameRegistry.registerBlock(pumbaaBox, "box");
        GameRegistry.registerBlock(outlandsAltar, "outlandsAltar");
        GameRegistry.registerBlock(lily, "lily");
        GameRegistry.registerBlock(stairsAcacia, "stairsAcacia");
        GameRegistry.registerBlock(stairsRainforest, "stairsRainforest");
        GameRegistry.registerBlock(stairsMango, "stairsMango");
        GameRegistry.registerBlock(blockBed, "bed");
        GameRegistry.registerBlock(hyenaHead, "hyenaHead");
        GameRegistry.registerBlock(mountedShooter, "mountedShooter");
        GameRegistry.registerBlock(passionSapling, "passionSapling");
        GameRegistry.registerBlock(passionLeaves, "passionLeaves");
        GameRegistry.registerBlock(stairsPassion, "stairsPassion");
        GameRegistry.registerBlock(hyenaTorch, "hyenaBoneTorch");
        GameRegistry.registerBlock(wall, "stoneWall");
        GameRegistry.registerBlock(yamCrops, "yam");
        GameRegistry.registerBlock(stairsBanana, "stairsBanana");
        GameRegistry.registerBlock(prideWood2, "wood2");
        GameRegistry.registerBlock(bananaSapling, "bananaSapling");
        GameRegistry.registerBlock(bananaLeaves, "bananaLeaves");
        GameRegistry.registerBlock(bananaCakeBlock, "bananaCake");
        GameRegistry.registerBlock(hangingBanana, "hangingBanana");
        GameRegistry.registerBlock(stairsDeadwood, "stairsDeadwood");
        GameRegistry.registerBlock(mobSpawner, "mobSpawner");
        GameRegistry.registerBlock(driedMaizeBlock, "driedMaize");
        GameRegistry.registerBlock(driedMaizeSlabSingle, "driedMaizeSlabSingle");
        GameRegistry.registerBlock(driedMaizeSlabDouble, "driedMaizeSlabDouble");
        GameRegistry.registerBlock(stairsDriedMaize, "stairsDriedMaize");

        GameRegistry.registerItem(spawnEgg, "spawnEgg");
        GameRegistry.registerItem(ticket, "ticket");
        GameRegistry.registerItem(hyenaBone, "hyenaBone");
        GameRegistry.registerItem(lionRaw, "lionRaw");
        GameRegistry.registerItem(lionCooked, "lionCooked");
        GameRegistry.registerItem(rafikiStick, "rafikiStick");
        GameRegistry.registerItem(purpleFlower, "purpleFlower");
        GameRegistry.registerItem(mango, "mango");
        GameRegistry.registerItem(featherBlue, "featherBlue");
        GameRegistry.registerItem(featherYellow, "featherYellow");
        GameRegistry.registerItem(featherRed, "featherRed");
        GameRegistry.registerItem(dartBlue, "dartBlue");
        GameRegistry.registerItem(dartYellow, "dartYellow");
        GameRegistry.registerItem(dartRed, "dartRed");
        GameRegistry.registerItem(dartShooter, "dartShooter");
        GameRegistry.registerItem(hyenaBoneShard, "hyenaBoneShard");
        GameRegistry.registerItem(zebraBoots, "zebraBoots");
        GameRegistry.registerItem(zebraHide, "zebraHide");
        GameRegistry.registerItem(itemGrindingBowl, "grindingBowl");
        GameRegistry.registerItem(mangoDust, "mangoDust");
        GameRegistry.registerItem(dartBlack, "dartBlack");
        GameRegistry.registerItem(featherBlack, "featherBlack");
        GameRegistry.registerItem(shovel, "shovel");
        GameRegistry.registerItem(pickaxe, "pickaxe");
        GameRegistry.registerItem(axe, "axe");
        GameRegistry.registerItem(sword, "sword");
        GameRegistry.registerItem(hoe, "hoe");
        GameRegistry.registerItem(itemTermite, "itemTermite");
        GameRegistry.registerItem(scarRug, "scarRug");
        GameRegistry.registerItem(jar, "jar");
        GameRegistry.registerItem(jarWater, "jarWater");
        GameRegistry.registerItem(silver, "silver");
        GameRegistry.registerItem(silverDartShooter, "silverDartShooter");
        GameRegistry.registerItem(shovelSilver, "shovelSilver");
        GameRegistry.registerItem(pickaxeSilver, "pickaxeSilver");
        GameRegistry.registerItem(axeSilver, "axeSilver");
        GameRegistry.registerItem(swordSilver, "swordSilver");
        GameRegistry.registerItem(hoeSilver, "hoeSilver");
        GameRegistry.registerItem(rafikiCoin, "rafikiCoin");
        GameRegistry.registerItem(termiteDust, "termiteDust");
        GameRegistry.registerItem(lionDust, "lionDust");
        GameRegistry.registerItem(tunnahDiggah, "tunnahDiggah");
        GameRegistry.registerItem(crystal, "crystal");
        GameRegistry.registerItem(bug, "bug");
        GameRegistry.registerItem(chocolateMufasa, "chocolateMufasa");
        GameRegistry.registerItem(pumbaaBomb, "pumbaaBomb");
        GameRegistry.registerItem(fur, "fur");
        GameRegistry.registerItem(jarMilk, "jarMilk");
        GameRegistry.registerItem(zebraRaw, "zebraRaw");
        GameRegistry.registerItem(zebraCooked, "zebraCooked");
        GameRegistry.registerItem(rhinoRaw, "rhinoRaw");
        GameRegistry.registerItem(rhinoCooked, "rhinoCooked");
        GameRegistry.registerItem(helmetSilver, "helmetSilver");
        GameRegistry.registerItem(bodySilver, "bodySilver");
        GameRegistry.registerItem(legsSilver, "legsSilver");
        GameRegistry.registerItem(bootsSilver, "bootsSilver");
        GameRegistry.registerItem(vase, "vase");
        GameRegistry.registerItem(horn, "horn");
        GameRegistry.registerItem(hornGround, "hornGround");
        GameRegistry.registerItem(gemsbokHide, "gemsbokHide");
        GameRegistry.registerItem(gemsbokHorn, "gemsbokHorn");
        GameRegistry.registerItem(gemsbokSpear, "gemsbokSpear");
        GameRegistry.registerItem(juice, "MangoJuice");
        GameRegistry.registerItem(helmetGemsbok, "helmetGemsbok");
        GameRegistry.registerItem(bodyGemsbok, "bodyGemsbok");
        GameRegistry.registerItem(legsGemsbok, "legsGemsbok");
        GameRegistry.registerItem(bootsGemsbok, "bootsGemsbok");
        GameRegistry.registerItem(jarLava, "jarLava");
        GameRegistry.registerItem(peacockGem, "peacockGem");
        GameRegistry.registerItem(shovelPeacock, "shovelPeacock");
        GameRegistry.registerItem(pickaxePeacock, "pickaxePeacock");
        GameRegistry.registerItem(axePeacock, "axePeacock");
        GameRegistry.registerItem(swordPeacock, "swordPeacock");
        GameRegistry.registerItem(hoePeacock, "hoePeacock");
        GameRegistry.registerItem(helmetPeacock, "helmetPeacock");
        GameRegistry.registerItem(bodyPeacock, "bodyPeacock");
        GameRegistry.registerItem(legsPeacock, "legsPeacock");
        GameRegistry.registerItem(bootsPeacock, "bootsPeacock");
        GameRegistry.registerItem(rugDye, "rugDye");
        GameRegistry.registerItem(wings, "wings");
        GameRegistry.registerItem(corn, "corn");
        GameRegistry.registerItem(cornStalk, "cornStalk");
        GameRegistry.registerItem(popcorn, "popcorn");
        GameRegistry.registerItem(nukaShard, "nukaShard");
        GameRegistry.registerItem(outlanderFur, "outlanderFur");
        GameRegistry.registerItem(outlanderMeat, "outlanderMeat");
        GameRegistry.registerItem(passionFruit, "passionFruit");
        GameRegistry.registerItem(redFlower, "redFlower");
        GameRegistry.registerItem(kivulite, "kivulite");
        GameRegistry.registerItem(shovelKivulite, "shovelKivulite");
        GameRegistry.registerItem(pickaxeKivulite, "pickaxeKivulite");
        GameRegistry.registerItem(axeKivulite, "axeKivulite");
        GameRegistry.registerItem(swordKivulite, "swordKivulite");
        GameRegistry.registerItem(bugStew, "bugStew");
        GameRegistry.registerItem(crocodileMeat, "crocodileMeat");
        GameRegistry.registerItem(poison, "poison");
        GameRegistry.registerItem(poisonedSpear, "poisonedSpear");
        GameRegistry.registerItem(xpGrub, "xpGrub");
        GameRegistry.registerItem(shovelCorrupt, "shovelCorrupt");
        GameRegistry.registerItem(pickaxeCorrupt, "pickaxeCorrupt");
        GameRegistry.registerItem(axeCorrupt, "axeCorrupt");
        GameRegistry.registerItem(swordCorrupt, "swordCorrupt");
        GameRegistry.registerItem(charm, "charm");
        GameRegistry.registerItem(zazuEgg, "zazuEgg");
        GameRegistry.registerItem(kiwano, "kiwano");
        GameRegistry.registerItem(kiwanoSeeds, "kiwanoSeeds");
        GameRegistry.registerItem(ticketLionHead, "ticketLionHead");
        GameRegistry.registerItem(ticketLionSuit, "ticketLionSuit");
        GameRegistry.registerItem(ticketLionLegs, "ticketLionLegs");
        GameRegistry.registerItem(ticketLionFeet, "ticketLionFeet");
        GameRegistry.registerItem(questBook, "questBook");
        GameRegistry.registerItem(outlandsHelm, "outlandsHelm");
        GameRegistry.registerItem(dartQuiver, "dartQuiver");
        GameRegistry.registerItem(outlandsFeather, "outlandsFeather");
        GameRegistry.registerItem(ziraRug, "ziraRug");
        GameRegistry.registerItem(ziraCoin, "ziraCoin");
        GameRegistry.registerItem(hyenaHeadItem, "hyenaHeadItem");
        GameRegistry.registerItem(amulet, "amulet");
        GameRegistry.registerItem(mountedShooterItem, "mountedShooterItem");
        GameRegistry.registerItem(staff, "staff");
        GameRegistry.registerItem(note, "note");
        GameRegistry.registerItem(giraffeSaddle, "giraffeSaddle");
        GameRegistry.registerItem(tie, "tie");
        GameRegistry.registerItem(yam, "yam_item");
        GameRegistry.registerItem(roastYam, "roastYam");
        GameRegistry.registerItem(banana, "banana");
        GameRegistry.registerItem(bananaCake, "bananaCakeitem");
        GameRegistry.registerItem(featherPink, "featherPink");
        GameRegistry.registerItem(dartPink, "dartPink");
        GameRegistry.registerItem(bananaBread, "bananaBread");
        GameRegistry.registerItem(hyenaMeal, "hyenaMeal");
        GameRegistry.registerItem(cornKernels, "cornKernels");
        GameRegistry.registerItem(driedMaize, "driedMaize_item");
        GameRegistry.registerItem(giraffeTie, "giraffe_tie");

        addRecipes();
        LKCharacterSpeech.registerLocalizations();
        config.save();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
     
        GameRegistry.registerWorldGenerator(new LKWorldGenerator(), 0);
        FMLCommonHandler.instance().bus().register(new LKEventHandler());

        LKPrideLandsBiome.savannah = new LKBiomeGenSavannah(idBiomeSavannah).setBiomeName("Savannah").setTemperatureRainfall(1.2F, 0.3F).setHeight(new BiomeGenBase.Height(0.1F, 0.25F));
        LKPrideLandsBiome.rainforest = new LKBiomeGenRainforest(idBiomeRainforest).setBiomeName("Rainforest").setTemperatureRainfall(1.0F, 0.9F).setHeight(new BiomeGenBase.Height(0.05F, 0.55F));
        LKPrideLandsBiome.mountains = new LKBiomeGenMountains(idBiomeMountains).setBiomeName("Mountains").setTemperatureRainfall(0.8F, 0.4F).setHeight(new BiomeGenBase.Height(0.7F, 1.1F));
        LKPrideLandsBiome.aridSavannah = new LKBiomeGenAridSavannah(idBiomeAridSavannah).setBiomeName("Arid Savannah").setTemperatureRainfall(1.3F, 0.1F).setHeight(new BiomeGenBase.Height(0.08F, 0.13F));
    
        LKPrideLandsBiome.desert = new LKBiomeGenDesert(idBiomeAridSavannah + 50).setBiomeName("Pride Lands Desert").setTemperatureRainfall(1.5F, 0.0F).setHeight(new BiomeGenBase.Height(0.16F, 0.2F));
    
        LKPrideLandsBiome.grasslandSavannah = new LKBiomeGenGrasslandSavannah(idBiomeGrasslandSavannah).setBiomeName("Grassland Savannah").setTemperatureRainfall(1.1F, 0.4F).setHeight(new BiomeGenBase.Height(0.1F, 0.3F));
        LKPrideLandsBiome.woodedSavannah = new LKBiomeGenWoodedSavannah(idBiomeWoodedSavannah).setBiomeName("Wooded Savannah").setTemperatureRainfall(1.1F, 0.5F).setHeight(new BiomeGenBase.Height(0.2F, 0.4F));
        LKPrideLandsBiome.bananaForest = new LKBiomeGenBananaForest(idBiomeBananaForest).setBiomeName("Banana Forest").setTemperatureRainfall(1.0F, 0.8F).setHeight(new BiomeGenBase.Height(0.3F, 0.5F));
        LKPrideLandsBiome.rainforestHills = new LKBiomeGenRainforest(idBiomeRainforestHills).setBiomeName("Rainforest Hills").setTemperatureRainfall(1.0F, 0.8F).setHeight(new BiomeGenBase.Height(0.6F, 1.6F));
        LKPrideLandsBiome.river = new LKBiomeGenRiver(idBiomeRiver).setBiomeName("River").setTemperatureRainfall(0.8F, 0.6F).setHeight(new BiomeGenBase.Height(-0.5F, 0.0F));

        LKOutlandsBiome.outlands = new LKBiomeGenOutlands(idBiomeOutlands).setBiomeName("Outlands").setTemperatureRainfall(1.3F, 0.2F).setHeight(new BiomeGenBase.Height(0.16F, 0.2F));
        LKOutlandsBiome.outlandsMountains = new LKBiomeGenOutlandsMountains(idBiomeOutlandsMountains).setBiomeName("Outlands Mountains").setTemperatureRainfall(1.1F, 0.3F).setHeight(new BiomeGenBase.Height(0.8F, 2.0F));
        LKOutlandsBiome.outlandsRiver = new LKBiomeGenOutlandsRiver(idBiomeOutlandsRiver).setBiomeName("Outlands River").setTemperatureRainfall(0.9F, 0.5F).setHeight(new BiomeGenBase.Height(-0.5F, 0.0F));
        LKBiomeGenUpendi.upendi = new LKBiomeGenUpendi(idBiomeUpendi).setBiomeName("Upendi").setTemperatureRainfall(1.0F, 1.0F).setHeight(new BiomeGenBase.Height(-0.05F, 2.0F));

        if (!DimensionManager.isDimensionRegistered(idPrideLands)) {
            DimensionManager.registerProviderType(idPrideLands, LKWorldProviderPrideLands.class, false);
            DimensionManager.registerDimension(idPrideLands, idPrideLands);
        }
       
        if (!DimensionManager.isDimensionRegistered(idOutlands)) {
            DimensionManager.registerProviderType(idOutlands, LKWorldProviderOutlands.class, false);
            DimensionManager.registerDimension(idOutlands, idOutlands);
        }
       
        if (!DimensionManager.isDimensionRegistered(idUpendi)) {
            DimensionManager.registerProviderType(idUpendi, LKWorldProviderUpendi.class, false);
            DimensionManager.registerDimension(idUpendi, idUpendi);
        }

        BiomeManager.addSpawnBiome(LKBiomeGenSavannah.savannah);
        BiomeManager.addSpawnBiome(LKBiomeGenRainforest.rainforest);
        BiomeManager.addSpawnBiome(LKBiomeGenAridSavannah.aridSavannah);
        BiomeManager.addSpawnBiome(LKBiomeGenGrasslandSavannah.grasslandSavannah);
        BiomeManager.addSpawnBiome(LKBiomeGenWoodedSavannah.woodedSavannah);
        BiomeManager.addSpawnBiome(LKBiomeGenBananaForest.bananaForest);

        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(LKBiomeGenSavannah.savannah, 30));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(LKBiomeGenGrasslandSavannah.grasslandSavannah, 25));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(LKBiomeGenWoodedSavannah.woodedSavannah, 20));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(LKBiomeGenAridSavannah.aridSavannah, 15));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(LKBiomeGenRainforest.rainforest, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(LKBiomeGenBananaForest.bananaForest, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(LKBiomeGenRiver.river, 15));
       
        GameRegistry.registerTileEntity(LKTileEntityGrindingBowl.class, "lionking:GrindingBowl");
	    GameRegistry.registerTileEntity((Class)LKTileEntityBugTrap.class, "lionking:BugTrap");
	    GameRegistry.registerTileEntity((Class)LKTileEntityDrum.class, "lionking:BongoDrum");
	    GameRegistry.registerTileEntity((Class)LKTileEntityOutlandsPool.class, "lionking:OutlandsPool");
	    GameRegistry.registerTileEntity((Class)LKTileEntityHyenaHead.class, "lionking:HyenaHead");
	    GameRegistry.registerTileEntity((Class)LKTileEntityMountedShooter.class, "LKMountedShooter");
	    GameRegistry.registerTileEntity(LKTileEntityFurRug.class, "lionking:FurRug");
        GameRegistry.registerTileEntity(LKTileEntityMobSpawner.class, "lionking:MobSpawner");   
        
        MinecraftForge.EVENT_BUS.register(new LKSound());
        MinecraftForge.EVENT_BUS.register(new LKTickHandlerServer());
        MinecraftForge.EVENT_BUS.register(new LKConnectionHandler());

        proxy.onLoad();
    }

    private void registerBlock(Block block) {
        GameRegistry.registerBlock(block, block.getUnlocalizedName().substring(5));
    }

    private void registerItem(Item item) {
        GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
    }

    private void addRecipes() {
        
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.dartBlue, 3), "X", "Y", "Z", 'X', mod_LionKing.hyenaBoneShard, 'Y', Items.stick, 'Z', mod_LionKing.featherBlue);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.dartYellow, 3), "X", "Y", "Z", 'X', mod_LionKing.hyenaBoneShard, 'Y', Items.stick, 'Z', mod_LionKing.featherYellow);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.dartRed, 3), "X", "Y", "Z", 'X', mod_LionKing.hyenaBoneShard, 'Y', Items.stick, 'Z', mod_LionKing.featherRed);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.dartBlack, 3), "X", "Y", "Z", 'X', mod_LionKing.itemTermite, 'Y', Items.stick, 'Z', mod_LionKing.featherBlack);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.dartPink, 3), "X", "Y", "Z", 'X', mod_LionKing.hyenaBoneShard, 'Y', Items.stick, 'Z', mod_LionKing.featherPink);

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.dartShooter), "XYY", 'X', mod_LionKing.mangoDust, 'Y', mod_LionKing.planks);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.silverDartShooter), "XYY", 'X', mod_LionKing.mangoDust, 'Y', mod_LionKing.silver);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.mountedShooterItem, 1, 0), " X ", "Y Y", 'X', mod_LionKing.dartShooter, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.mountedShooterItem, 1, 1), " X ", "Y Y", 'X', mod_LionKing.silverDartShooter, 'Y', Items.stick);

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.shovel), "X", "Y", "Y", 'X', new ItemStack(mod_LionKing.pridestone, 1, 0), 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.pickaxe), "XXX", " Y ", " Y ", 'X', new ItemStack(mod_LionKing.pridestone, 1, 0), 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.axe), "XX", "XY", " Y", 'X', new ItemStack(mod_LionKing.pridestone, 1, 0), 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.sword), "X", "X", "Y", 'X', new ItemStack(mod_LionKing.pridestone, 1, 0), 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.hoe), "XX", " Y", " Y", 'X', new ItemStack(mod_LionKing.pridestone, 1, 0), 'Y', Items.stick);

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.shovelSilver), "X", "Y", "Y", 'X', mod_LionKing.silver, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.pickaxeSilver), "XXX", " Y ", " Y ", 'X', mod_LionKing.silver, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.axeSilver), "XX", "XY", " Y", 'X', mod_LionKing.silver, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.swordSilver), "X", "X", "Y", 'X', mod_LionKing.silver, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.hoeSilver), "XX", " Y", " Y", 'X', mod_LionKing.silver, 'Y', Items.stick);

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.shovelPeacock), "X", "Y", "Y", 'X', mod_LionKing.peacockGem, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.pickaxePeacock), "XXX", " Y ", " Y ", 'X', mod_LionKing.peacockGem, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.axePeacock), "XX", "XY", " Y", 'X', mod_LionKing.peacockGem, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.swordPeacock), "X", "X", "Y", 'X', mod_LionKing.peacockGem, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.hoePeacock), "XX", " Y", " Y", 'X', mod_LionKing.peacockGem, 'Y', Items.stick);

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.shovelKivulite), "X", "Y", "Y", 'X', mod_LionKing.kivulite, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.pickaxeKivulite), "XXX", " Y ", " Y ", 'X', mod_LionKing.kivulite, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.axeKivulite), "XX", "XY", " Y", 'X', mod_LionKing.kivulite, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.swordKivulite), "X", "X", "Y", 'X', mod_LionKing.kivulite, 'Y', Items.stick);

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.shovelCorrupt), "X", "Y", "Y", 'X', new ItemStack(mod_LionKing.pridestone, 1, 1), 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.pickaxeCorrupt), "XXX", " Y ", " Y ", 'X', new ItemStack(mod_LionKing.pridestone, 1, 1), 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.axeCorrupt), "XX", "XY", " Y", 'X', new ItemStack(mod_LionKing.pridestone, 1, 1), 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.swordCorrupt), "X", "X", "Y", 'X', new ItemStack(mod_LionKing.pridestone, 1, 1), 'Y', Items.stick);

        GameRegistry.addRecipe(new ItemStack(Items.wooden_shovel), "X", "Y", "Y", 'X', mod_LionKing.planks, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(Items.wooden_pickaxe), "XXX", " Y ", " Y ", 'X', mod_LionKing.planks, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(Items.wooden_axe), "XX", "XY", " Y", 'X', mod_LionKing.planks, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(Items.wooden_sword), "X", "X", "Y", 'X', mod_LionKing.planks, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(Items.wooden_hoe), "XX", " Y", " Y", 'X', mod_LionKing.planks, 'Y', Items.stick);

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.zebraBoots), "  X", "XXX", "YYY", 'X', mod_LionKing.zebraHide, 'Y', mod_LionKing.hyenaBone);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.helmetSilver), "XXX", "X X", 'X', mod_LionKing.silver);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.bodySilver), "X X", "XXX", "XXX", 'X', mod_LionKing.silver);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.legsSilver), "XXX", "X X", "X X", 'X', mod_LionKing.silver);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.bootsSilver), "X X", "X X", 'X', mod_LionKing.silver);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.helmetGemsbok), "XXX", "X X", 'X', mod_LionKing.gemsbokHide);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.bodyGemsbok), "X X", "XXX", "XXX", 'X', mod_LionKing.gemsbokHide);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.legsGemsbok), "XXX", "X X", "X X", 'X', mod_LionKing.gemsbokHide);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.bootsGemsbok), "X X", "X X", 'X', mod_LionKing.gemsbokHide);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.helmetPeacock), "XXX", "X X", 'X', mod_LionKing.peacockGem);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.bodyPeacock), "X X", "XXX", "XXX", 'X', mod_LionKing.peacockGem);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.legsPeacock), "XXX", "X X", "X X", 'X', mod_LionKing.peacockGem);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.bootsPeacock), "X X", "X X", 'X', mod_LionKing.peacockGem);

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.prideBrick, 4, 0), "XX", "XX", 'X', new ItemStack(mod_LionKing.pridestone, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.prideBrick, 4, 1), "XX", "XX", 'X', new ItemStack(mod_LionKing.pridestone, 1, 1));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.pridePillar, 3, 0), "X", "X", "X", 'X', new ItemStack(mod_LionKing.pridestone, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.pridePillar, 3, 4), "X", "X", "X", 'X', new ItemStack(mod_LionKing.pridestone, 1, 1));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.blockSilver, 1, 0), "XXX", "XXX", "XXX", 'X', mod_LionKing.silver);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.silver, 9), "X", 'X', new ItemStack(mod_LionKing.blockSilver, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.blockSilver, 1, 1), "XXX", "XXX", "XXX", 'X', mod_LionKing.kivulite);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.kivulite, 9), "X", 'X', new ItemStack(mod_LionKing.blockSilver, 1, 1));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.blockPeacock), "XXX", "XXX", "XXX", 'X', mod_LionKing.peacockGem);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.peacockGem, 9), "X", 'X', mod_LionKing.blockPeacock);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.kiwanoBlock), "XXX", "XXX", "XXX", 'X', mod_LionKing.kiwano);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.driedMaizeBlock), "XX", "XX", 'X', mod_LionKing.driedMaize);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.outglassPane, 16), "XXX", "XXX", 'X', mod_LionKing.outglass);

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.slabSingle, 6, 0), "XXX", 'X', new ItemStack(mod_LionKing.pridestone, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.slabSingle, 6, 1), "XXX", 'X', new ItemStack(mod_LionKing.prideBrick, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.slabSingle, 6, 2), "XXX", 'X', new ItemStack(mod_LionKing.pridePillar, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.slabSingle, 6, 3), "XXX", 'X', new ItemStack(mod_LionKing.pridestone, 1, 1));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.slabSingle, 6, 4), "XXX", 'X', new ItemStack(mod_LionKing.prideBrick, 1, 1));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.slabSingle, 6, 5), "XXX", 'X', new ItemStack(mod_LionKing.pridePillar, 1, 4));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.driedMaizeSlabSingle, 6, 0), "XXX", 'X', mod_LionKing.driedMaizeBlock);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.woodSlabSingle, 6, 0), "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.woodSlabSingle, 6, 1), "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 1));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.woodSlabSingle, 6, 2), "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 2));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.woodSlabSingle, 6, 3), "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 3));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.woodSlabSingle, 6, 4), "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 4));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.woodSlabSingle, 6, 5), "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 5));

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.stoneStairs, 4), "X  ", "XX ", "XXX", 'X', new ItemStack(mod_LionKing.pridestone, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.brickStairs, 4), "X  ", "XX ", "XXX", 'X', new ItemStack(mod_LionKing.prideBrick, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.stoneStairsCorrupt, 4), "X  ", "XX ", "XXX", 'X', new ItemStack(mod_LionKing.pridestone, 1, 1));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.brickStairsCorrupt, 4), "X  ", "XX ", "XXX", 'X', new ItemStack(mod_LionKing.prideBrick, 1, 1));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.stairsAcacia, 4), "X  ", "XX ", "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.stairsRainforest, 4), "X  ", "XX ", "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 1));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.stairsMango, 4), "X  ", "XX ", "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 2));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.stairsPassion, 4), "X  ", "XX ", "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 3));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.stairsBanana, 4), "X  ", "XX ", "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 4));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.stairsDeadwood, 4), "X  ", "XX ", "XXX", 'X', new ItemStack(mod_LionKing.planks, 1, 5));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.stairsDriedMaize, 4), "X  ", "XX ", "XXX", 'X', mod_LionKing.driedMaizeBlock);

        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.planks, 4, 0), new ItemStack(mod_LionKing.prideWood, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.planks, 4, 1), new ItemStack(mod_LionKing.prideWood, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.planks, 4, 2), new ItemStack(mod_LionKing.prideWood, 1, 2));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.planks, 4, 3), new ItemStack(mod_LionKing.prideWood, 1, 3));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.planks, 4, 4), new ItemStack(mod_LionKing.prideWood2, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.planks, 4, 5), new ItemStack(mod_LionKing.prideWood2, 1, 1));

        for (int i = 0; i < 4; ++i) {
            GameRegistry.addRecipe(new ItemStack(mod_LionKing.drum, 1, i), "XXX", "Y Y", "YYY", 'X', mod_LionKing.zebraHide, 'Y', new ItemStack(mod_LionKing.prideWood, 1, i));
        }
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.drum, 1, 4), "XXX", "Y Y", "YYY", 'X', mod_LionKing.zebraHide, 'Y', new ItemStack(mod_LionKing.prideWood2, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.drum, 1, 5), "XXX", "Y Y", "YYY", 'X', mod_LionKing.zebraHide, 'Y', new ItemStack(mod_LionKing.prideWood2, 1, 1));

        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.button), new ItemStack(mod_LionKing.pridestone, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.lever), "X", "Y", 'X', Items.stick, 'Y', new ItemStack(mod_LionKing.pridestone, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.pressurePlate), "XX", 'X', new ItemStack(mod_LionKing.pridestone, 1, 0));
        GameRegistry.addRecipe(new ItemStack(Blocks.wooden_pressure_plate), "XX", "XX", 'X', mod_LionKing.planks);
        GameRegistry.addRecipe(new ItemStack(Blocks.fence_gate), "XYX", "YXY", "XYX", 'X', mod_LionKing.termiteDust, 'Y', Blocks.planks);
        GameRegistry.addRecipe(new ItemStack(Blocks.fence), "XX", 'X', mod_LionKing.planks);
        GameRegistry.addRecipe(new ItemStack(Blocks.wooden_door), "XX", "XX", 'X', mod_LionKing.planks);
        GameRegistry.addRecipe(new ItemStack(Items.sign, 3), "X X", "XXX", 'X', mod_LionKing.planks);
        GameRegistry.addRecipe(new ItemStack(Blocks.trapdoor, 2), "XXX", "XXX", 'X', mod_LionKing.planks);
        GameRegistry.addRecipe(new ItemStack(Blocks.ladder), "YXY", "YXY", 'X', mod_LionKing.planks, 'Y', Items.stick);

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.wall, 1, 0), "XXX", "XXX", 'X', new ItemStack(mod_LionKing.pridestone, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.wall, 1, 1), "XXX", "XXX", 'X', new ItemStack(mod_LionKing.prideBrick, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.wall, 1, 2), "XXX", "XXX", 'X', new ItemStack(mod_LionKing.prideBrickMossy, 1, 0));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.wall, 1, 3), "XXX", "XXX", 'X', new ItemStack(mod_LionKing.pridestone, 1, 1));
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.wall, 1, 4), "XXX", "XXX", 'X', new ItemStack(mod_LionKing.prideBrick, 1, 1));

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.jar), "X X", "X X", " X ", 'X', mod_LionKing.pridestone);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.vase, 1, 0), "X", "Y", 'X', mod_LionKing.whiteFlower, 'Y', mod_LionKing.jarWater);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.vase, 1, 1), "X", "Y", 'X', mod_LionKing.blueFlower, 'Y', mod_LionKing.jarWater);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.vase, 1, 2), "X", "Y", 'X', mod_LionKing.purpleFlower, 'Y', mod_LionKing.jarWater);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.vase, 1, 3), "X", "Y", 'X', mod_LionKing.redFlower, 'Y', mod_LionKing.jarWater);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.vase, 1, 4), "X", "Y", 'X', mod_LionKing.sapling, 'Y', mod_LionKing.jarWater);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.vase, 1, 5), "X", "Y", 'X', mod_LionKing.forestSapling, 'Y', mod_LionKing.jarWater);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.vase, 1, 6), "X", "Y", 'X', mod_LionKing.mangoSapling, 'Y', mod_LionKing.jarWater);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.vase, 1, 7), "X", "Y", 'X', mod_LionKing.outshroom, 'Y', mod_LionKing.jarWater);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.vase, 1, 8), "X", "Y", 'X', mod_LionKing.outshroomGlowing, 'Y', mod_LionKing.jarWater);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.vase, 1, 9), "X", "Y", 'X', mod_LionKing.passionSapling, 'Y', mod_LionKing.jarWater);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.vase, 1, 10), "X", "Y", 'X', mod_LionKing.bananaSapling, 'Y', mod_LionKing.jarWater);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.rug, 4, 0), "XX", "XX", 'X', mod_LionKing.fur);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.rug, 4, 12), "XX", "XX", 'X', mod_LionKing.outlanderFur);
        for (int i = 0; i <= 15; ++i) {
            if (i != 1) {
                GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 1), new ItemStack(mod_LionKing.rug, 1, i), new ItemStack(mod_LionKing.rugDye, 1, 0));
            }
        }
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 2), new ItemStack(mod_LionKing.rug, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 3), new ItemStack(mod_LionKing.rug, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 2));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 4), new ItemStack(mod_LionKing.rug, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 3));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 5), new ItemStack(mod_LionKing.rug, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 4));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 6), new ItemStack(mod_LionKing.rug, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 5));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 7), new ItemStack(mod_LionKing.rug, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 6));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 8), new ItemStack(mod_LionKing.rug, 1, 1), mod_LionKing.mangoDust);
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 9), new ItemStack(mod_LionKing.rug, 1, 1), mod_LionKing.hornGround);
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 10), new ItemStack(mod_LionKing.rug, 1, 1), mod_LionKing.termiteDust);
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 11), new ItemStack(mod_LionKing.rug, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 7));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 13), new ItemStack(mod_LionKing.rug, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 8));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 14), new ItemStack(mod_LionKing.rug, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 9));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.rug, 1, 15), new ItemStack(mod_LionKing.rug, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 10));
        GameRegistry.addRecipe(new ItemStack(Blocks.bed), "XXX", "YYY", 'X', mod_LionKing.fur, 'Y', mod_LionKing.planks);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.wings), "PRP", "YPY", "BPB", 'P', mod_LionKing.peacockGem, 'R', mod_LionKing.featherRed, 'Y', mod_LionKing.featherYellow, 'B', mod_LionKing.featherBlue);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.hyenaTorch, 4), "X", "Y", 'X', Items.coal, 'Y', mod_LionKing.hyenaBone);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.hyenaTorch, 4), "X", "Y", 'X', new ItemStack(Items.coal, 1, 1), 'Y', mod_LionKing.hyenaBone);

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.gemsbokSpear), "  Y", " Y ", "X  ", 'X', Items.stick, 'Y', mod_LionKing.gemsbokHorn);
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.poisonedSpear), mod_LionKing.gemsbokSpear, mod_LionKing.poison, mod_LionKing.poison);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.staff), "X", "Y", "Y", 'X', mod_LionKing.peacockGem, 'Y', mod_LionKing.gemsbokHorn);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.itemGrindingBowl), " A ", "B B", "BBB", 'A', Items.stick, 'B', mod_LionKing.planks);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.bugTrap), "XXX", "YZY", "XBX", 'X', new ItemStack(mod_LionKing.prideWood, 1, 0), 'Y', mod_LionKing.silver, 'Z', Items.stick, 'B', mod_LionKing.bug);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.starAltar), "XXX", "YYY", 'X', mod_LionKing.lionDust, 'Y', mod_LionKing.silver);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.charm), " X ", "XYX", " X ", 'X', mod_LionKing.silver, 'Y', mod_LionKing.lionDust);

        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.mangoDust), new ItemStack(mod_LionKing.mango), new ItemStack(mod_LionKing.itemGrindingBowl));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.jarWater), new ItemStack(mod_LionKing.jar), new ItemStack(Items.water_bucket));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.juice), new ItemStack(mod_LionKing.mango), new ItemStack(mod_LionKing.mango), new ItemStack(mod_LionKing.jarWater));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.bugStew), Items.bowl, mod_LionKing.bug, mod_LionKing.mango, mod_LionKing.jarMilk);
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.hyenaMeal, 3), mod_LionKing.hyenaBone);
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.cornKernels, 2), mod_LionKing.corn);
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.kiwanoSeeds), mod_LionKing.kiwano);
        GameRegistry.addShapelessRecipe(new ItemStack(Items.bread), Items.egg, Items.egg, Items.egg, mod_LionKing.gemsbokHide);
        GameRegistry.addShapelessRecipe(new ItemStack(Items.mushroom_stew), Items.bowl, mod_LionKing.outshroom, mod_LionKing.outshroom);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.bananaCake), "AAA", "BCB", "DDD", 'A', mod_LionKing.jarMilk, 'B', mod_LionKing.banana, 'C', mod_LionKing.zazuEgg, 'D', Items.sugar);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.bananaBread), "XYX", 'X', Items.sugar, 'Y', mod_LionKing.banana);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.outshroomGlowing), "X", "Y", 'X', mod_LionKing.nukaShard, 'Y', mod_LionKing.outshroom);
        GameRegistry.addRecipe(new ItemStack(mod_LionKing.termite, 1, 1), " X ", "XYX", " X ", 'X', mod_LionKing.itemTermite, 'Y', new ItemStack(mod_LionKing.pridestone, 1, 1));

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.tie, 1, 0), "X", "X", "X", 'X', mod_LionKing.fur);
        for (int i = 0; i <= 7; ++i) {
            if (i != 1) {
                GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.tie, 1, 1), new ItemStack(mod_LionKing.tie, 1, i), new ItemStack(mod_LionKing.rugDye, 1, 0));
            }
        }
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.tie, 1, 2), new ItemStack(mod_LionKing.tie, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.tie, 1, 3), new ItemStack(mod_LionKing.tie, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 2));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.tie, 1, 4), new ItemStack(mod_LionKing.tie, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 3));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.tie, 1, 5), new ItemStack(mod_LionKing.tie, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 4));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.tie, 1, 6), new ItemStack(mod_LionKing.tie, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 6));
        GameRegistry.addShapelessRecipe(new ItemStack(mod_LionKing.tie, 1, 7), new ItemStack(mod_LionKing.tie, 1, 1), new ItemStack(mod_LionKing.rugDye, 1, 7));

        GameRegistry.addRecipe(new ItemStack(mod_LionKing.giraffeSaddle), "XXX", "Y Y", 'X', mod_LionKing.gemsbokHide, 'Y', mod_LionKing.silver);
        GameRegistry.addRecipe(new ItemStack(Items.saddle), "XXX", "XYX", "XXX", 'X', Items.stick, 'Y', mod_LionKing.zebraHide);

        GameRegistry.addRecipe(new ItemStack(Items.stick, 4), "X", "X", 'X', mod_LionKing.planks);
        GameRegistry.addRecipe(new ItemStack(Items.bowl, 4), "X X", " X ", 'X', mod_LionKing.planks);
        GameRegistry.addRecipe(new ItemStack(Blocks.chest), "XXX", "X X", "XXX", 'X', mod_LionKing.planks);
        GameRegistry.addRecipe(new ItemStack(Blocks.crafting_table), "XXX", "YYY", "XXX", 'X', mod_LionKing.planks, 'Y', Items.bread);
        GameRegistry.addRecipe(new ItemStack(Items.boat, 3), "XXX", "XXX", " Y ", 'X', mod_LionKing.planks, 'Y', Items.stick);
        GameRegistry.addRecipe(new ItemStack(Blocks.furnace), "XXX", "XYX", "XXX", 'X', mod_LionKing.pridestone, 'Y', mod_LionKing.dartShooter);
        GameRegistry.addRecipe(new ItemStack(Items.egg, 3), "XXX", 'X', mod_LionKing.cornStalk);
        GameRegistry.addRecipe(new ItemStack(planks, 4, 0), "W", 'W', prideWood);
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.fence), mod_LionKing.planks);

        GameRegistry.addSmelting(mod_LionKing.lionRaw, new ItemStack(mod_LionKing.lionCooked), 0.35F);
        GameRegistry.addSmelting(mod_LionKing.zebraRaw, new ItemStack(mod_LionKing.zebraCooked), 0.35F);
        GameRegistry.addSmelting(mod_LionKing.rhinoRaw, new ItemStack(mod_LionKing.rhinoCooked), 0.35F);
        GameRegistry.addSmelting(mod_LionKing.oreSilver, new ItemStack(mod_LionKing.silver), 0.7F);
        GameRegistry.addSmelting(mod_LionKing.orePeacock, new ItemStack(mod_LionKing.peacockGem), 1.0F);

        GameRegistry.addShapelessRecipe(new ItemStack(rafikiCoin), silver, lionDust);
        GameRegistry.addShapelessRecipe(new ItemStack(chocolateMufasa), new ItemStack(Items.dye, 1, 3), jarMilk, Items.sugar);
        GameRegistry.addShapelessRecipe(new ItemStack(pumbaaBomb), bug, Items.gunpowder);
        GameRegistry.addShapelessRecipe(new ItemStack(questBook), Items.book, featherBlue, lionDust);
        GameRegistry.addSmelting(yam, new ItemStack(roastYam), 0.35F);

        LKEntities.registerCreature(LKEntityLion.class, "Lion", 1, 15248168, 11092992);
        LKEntities.registerCreature(LKEntityLioness.class, "Lioness", 2, 14527033, 13851933);
        LKEntities.registerCreature(LKEntityZebra.class, "Zebra", 3, 14869218, 1447446);
        LKEntities.registerCreature(LKEntityZazu.class, "Zazu", 4, 943330, 12178939);
        LKEntities.registerCreature(LKEntityRafiki.class, "Rafiki", 5, 1644825, 15805738);
        LKEntities.registerCreature(LKEntityTimon.class, "Timon", 6, 14717499, 4926487);
        LKEntities.registerCreature(LKEntityPumbaa.class, "Pumbaa", 7, 10232079, 2952204);
        LKEntities.registerCreature(LKEntityBug.class, "Bug", 8, 69988, 12696344);
        LKEntities.registerCreature(LKEntitySimba.class, "Simba", 9, 16688407, 12399104);
        LKEntities.registerCreature(LKEntityHyena.class, "Hyena", 10, 4473411, 1313815);
        LKEntities.registerCreature(LKEntityScar.class, "Scar", 11, 7087883, 657930);
        LKEntities.registerCreature(LKEntityOutlander.class, "Outlander", 12, 13283425, 2694163);
        LKEntities.registerCreature(LKEntityVulture.class, "Vulture", 13, 1250067, 13079056);
        LKEntities.registerCreature(LKEntityTermite.class, "Exploding Termite", 14, 1512461, 3091236);
        LKEntities.registerCreature(LKEntityTicketLion.class, "Ticket Lion", 15, 12215054, 1118481);
        LKEntities.registerCreature(LKEntityRhino.class, "Rhino", 16, 6118481, 12171165);
        LKEntities.registerCreature(LKEntityOutlandess.class, "Outlandess", 17, 11505738, 2102029);
        LKEntities.registerCreature(LKEntityGemsbok.class, "Gemsbok", 18, 11759423, 15920343);
        LKEntities.registerCreature(LKEntityCrocodile.class, "Crocodile", 19, 2896659, 986886);
        LKEntities.registerCreature(LKEntityZira.class, "Zira", 20, 11508815, 7995392);
        LKEntities.registerCreature(LKEntityTermiteQueen.class, "Termite Queen", 21, 1512461, 3091236);
        LKEntities.registerCreature(LKEntityGiraffe.class, "Giraffe", 22, 15905848, 6307846);
        LKEntities.registerCreature(LKEntitySkeletalHyena.class, "Skeletal Hyena", 23, 13683370, 7762784);
        LKEntities.registerCreature(LKEntitySkeletalHyenaHead.class, "Skeletal Hyena Head", 24, 13683370, 7762784);
        LKEntities.registerCreature(LKEntityDikdik.class, "Dik-dik", 25, 12023867, 6833961);
        LKEntities.registerCreature(LKEntityFlamingo.class, "Flamingo", 26, 16087966, 16374243);
        LKEntities.registerEntity(LKEntityBlueDart.class, "Blue Dart", 100);
        LKEntities.registerEntity(LKEntityYellowDart.class, "Yellow Dart", 101);
        LKEntities.registerEntity(LKEntityRedDart.class, "Red Dart", 102);
        LKEntities.registerEntity(LKEntityBlackDart.class, "Outlandish Dart", 103);
        LKEntities.registerEntity(LKEntityCoin.class, "Thrown Rafiki Coin", 104);
        LKEntities.registerEntity(LKEntityThrownTermite.class, "Thrown Termite", 105);
        LKEntities.registerEntity(LKEntityOutsand.class, "Falling Outsand", 106);
        LKEntities.registerEntity(LKEntityPumbaaBomb.class, "Thrown Pumbaa Bomb", 107);
        LKEntities.registerEntity(LKEntityGemsbokSpear.class, "Gemsbok Spear", 108);
        LKEntities.registerEntity(LKEntityPoisonedSpear.class, "Poisoned Gemsbok Spear", 109);
        LKEntities.registerEntity(LKEntityZazuEgg.class, "Zazu Egg", 110);
        LKEntities.registerEntity(LKEntityScarRug.class, "Scar Rug", 111);
        LKEntities.registerEntity(LKEntityPinkDart.class, "Flamingo Dart", 112);
            }

    private static void loadAndSaveConfig(Configuration config) {
     config.load();
     try {
        idBiomeSavannah = config.get("biome", "Savannah", 130).getInt();
        idBiomeRainforest = config.get("biome", "Rainforest", 131).getInt();
        idBiomeMountains = config.get("biome", "Mountains", 132).getInt();
        idBiomeRainforestHills = config.get("biome", "Rainforest Hills", 133).getInt();
        idBiomeRiver = config.get("biome", "River", 134).getInt();
        idBiomeAridSavannah = config.get("biome", "Arid Savannah", 135).getInt();
        idBiomeOutlands = config.get("biome", "Outlands", 136).getInt();
        idBiomeOutlandsMountains = config.get("biome", "Outlands Mountains", 137).getInt();
        idBiomeOutlandsRiver = config.get("biome", "Outlands River", 138).getInt();
        idBiomeUpendi = config.get("biome", "Upendi", 139).getInt();
        idBiomeGrasslandSavannah = config.get("biome", "Grassland Savannah", 140).getInt();
        idBiomeWoodedSavannah = config.get("biome", "Wooded Savannah", 141).getInt();
        idBiomeBananaForest = config.get("biome", "Banana Forest", 142).getInt();

        idPrideLands = config.get("dimension", "Pride Lands", 20).getInt();
        idOutlands = config.get("dimension", "Outlands", 21).getInt();
        idUpendi = config.get("dimension", "Upendi", 22).getInt();

        lkMusicChance = MathHelper.clamp_int(config.get("general", "Lion King music percentage chance", 40).getInt(), 0, 100);
        boothLimit = Math.max(config.get("general", "Minimum distance between ticket booths", 250).getInt(), 1);
        randomBooths = config.get("general", "Random ticket booths", false).getBoolean(false);

        } catch (Exception e) {
            FMLLog.severe("Failed to load Lion King mod config: %s", e.getMessage());
        }

        if (config.hasChanged()) {
            config.save();
        }
    }
        
    private static int ensureSafeBiomeID(int originalID, int fallback) {
        if (originalID < 128) {
            FMLLog.info("Biome ID %d is in Overworld range (<128) ? automatically changed to %d", originalID, fallback);
            return fallback;
        }
        if (originalID < 0 || originalID >= 256) {
            FMLLog.warning("Invalid biome ID %d ? using fallback %d", originalID, fallback);
            return fallback;
        }
        return originalID;
    }
}
