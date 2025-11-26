package lionking.common;

import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import lionking.mod_LionKing;
import java.nio.ByteBuffer;
import java.util.*;

import lionking.quest.LKQuestBase;
import lionking.world.LKWorldGenOutsand;
import lionking.world.LKWorldGenZiraMound;
import lionking.entity.LKEntitySimba;
import lionking.entity.LKEntityRafiki;
import lionking.common.LKPacketHandlerServer;
import cpw.mods.fml.common.network.NetworkRegistry;

public class LKIngame {
    public static int flatulenceSoundTick;
    public static boolean loadRenderers;

    public static void runMainWorldTick(World world) {
        Random random = world.rand;
        List<EntityPlayer> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;

        for (EntityPlayer player : players) {
            handlePlayerEffects(player, world, random);
        }

        updateRafikiQuest(world);
        handleFlatulenceEffects(world, random);
    }

    private static void handlePlayerEffects(EntityPlayer player, World world, Random random) {
        ItemStack bodyArmor = player.inventory.armorItemInSlot(2);
        if (bodyArmor != null && bodyArmor.getItem() == mod_LionKing.wings) {
            player.fallDistance = 0F;
        }

        if (player.getHealth() > 0F && player.getHealth() < 5F) {
            activateCrystals(player, world, random);
        }

        checkInventory(player);
    }

    private static void updateRafikiQuest(World world) {
        if (LKLevelData.defeatedScar == 1 && LKQuestBase.rafikiQuest.getQuestStage() == 2) {
            LKQuestBase.rafikiQuest.progress(3);
            sendMessageToAllPlayers(StatCollector.translateToLocal("message.lionking.rafiki_quest_complete"));
        }
    }

    private static void handleFlatulenceEffects(World world, Random random) {
        World prideLands = DimensionManager.getWorld(mod_LionKing.idPrideLands);
        if (flatulenceSoundTick > 0) {
            flatulenceSoundTick--;
            spawnExplosionParticle(prideLands, random);
        }

        if (LKLevelData.flatulenceSoundsRemaining > 0 && flatulenceSoundTick <= random.nextInt(16)) {
            LKLevelData.setFlatulenceSoundsRemaining(LKLevelData.flatulenceSoundsRemaining - 1);
            playFlatulenceSound(prideLands, random);
            flatulenceSoundTick = 25;
            if (LKLevelData.flatulenceSoundsRemaining == 0) {
                LKLevelData.setZiraStage(20);
            }
        }
    }

    public static boolean isPlayerInLionPortal(EntityPlayer player, boolean isPrideLands) {
        AxisAlignedBB bb = player.boundingBox;
        int minX = MathHelper.floor_double(bb.minX);
        int maxX = MathHelper.floor_double(bb.maxX + 1.0D);
        int minY = MathHelper.floor_double(bb.minY);
        int maxY = MathHelper.floor_double(bb.maxY + 1.0D);
        int minZ = MathHelper.floor_double(bb.minZ);
        int maxZ = MathHelper.floor_double(bb.maxZ + 1.0D);

        Block portalBlock = isPrideLands ? mod_LionKing.lionPortal : mod_LionKing.outlandsPortal;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    if (player.worldObj.getBlock(x, y, z) == portalBlock) {
                        if (isPrideLands && player.dimension == mod_LionKing.idPrideLands) {
                            player.worldObj.provider.setSpawnPoint(x, y, z);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void generateOutsand(EntityPlayer player, World world, Random random) {
        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);

        int targetX = x + random.nextInt(100) - random.nextInt(100);
        int targetY = y + random.nextInt(80) - random.nextInt(80);
        int targetZ = z + random.nextInt(100) - random.nextInt(100);

        if (targetY > 1 && targetY < 256) {
            LKWorldGenOutsand outsandGen = new LKWorldGenOutsand();
            if (outsandGen.isRadiusClearOfOutsand(world, targetX, targetY, targetZ, 64) &&
                world.getBlock(targetX, targetY, targetZ).isOpaqueCube() &&
                world.canBlockSeeTheSky(targetX, targetY + 1, targetZ)) {
                if (!world.isRemote) {
                    world.spawnEntityInWorld(new EntityLightningBolt(world, targetX, targetY, targetZ));
                }
                outsandGen.generate(world, random, targetX, targetY, targetZ);
            }
        }
    }

    private static void activateCrystals(EntityPlayer player, World world, Random random) {
        int y = MathHelper.floor_double(player.posY);
        if (y > 0 && player.inventory.hasItem(mod_LionKing.crystal)) { 
            player.inventory.consumeInventoryItem(mod_LionKing.crystal);
            player.setHealth(player.getMaxHealth());
            player.triggerAchievement(LKAchievementList.crystal);
            if (!world.isRemote) {
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("message.lionking.crystal_heal")));
            }
            spawnHealingParticles(player, world, random);
            world.playSoundAtEntity(player, "random.glass", 1F, ((random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }
    }

    private static void checkInventory(EntityPlayer player) {
        boolean[] rugFlags = new boolean[16];
        boolean[] headFlags = new boolean[3];
        for (int i = 0; i < 36; i++) {
            ItemStack item = player.inventory.getStackInSlot(i);
            if (item == null) continue;

            if (Item.getIdFromItem(item.getItem()) == Item.getIdFromItem(mod_LionKing.chocolateMufasa)) {
                player.triggerAchievement(LKAchievementList.chocolateMufasa);
            }
            if (Item.getIdFromItem(item.getItem()) == Item.getIdFromItem(mod_LionKing.tunnahDiggah) &&
                EnchantmentHelper.getEnchantmentLevel(mod_LionKing.diggahEnchantment.effectId, item) == 1) {
                player.triggerAchievement(LKAchievementList.enchantDiggah);
            }
            if (Item.getIdFromItem(item.getItem()) == Block.getIdFromBlock(mod_LionKing.rug) && item.getItemDamage() < 16) {
                rugFlags[item.getItemDamage()] = true;
            }
            if (Item.getIdFromItem(item.getItem()) == Item.getIdFromItem(mod_LionKing.hyenaHeadItem) && item.getItemDamage() < 3) {
                headFlags[item.getItemDamage()] = true;
            }
        }
        checkCollectionAchievements(player, rugFlags, headFlags);
    }

    public static List<NBTTagCompound> getSimbas(EntityPlayer player) {
        List<NBTTagCompound> simbaData = new ArrayList<>();
        AxisAlignedBB aabb = player.boundingBox.expand(16F, 16F, 16F);
        List<LKEntitySimba> simbas = player.worldObj.getEntitiesWithinAABB(LKEntitySimba.class, aabb);
        for (LKEntitySimba simba : simbas) {
            if (simba.canUsePortal(player)) {
                NBTTagCompound data = new NBTTagCompound();
                simba.writeToNBT(data);
                simbaData.add(data);
                if (!player.worldObj.isRemote) {
                    simba.setDead();
                }
            }
        }
        return simbaData;
    }

    public static void doAdditionalPortalActions(EntityPlayer player, int dimension, boolean isPrideLands) {
        if (isPrideLands && dimension == mod_LionKing.idPrideLands && LKLevelData.receivedQuestBook == 0) {
            generateQuestBookForPlayer(player);
        }
        if (dimension == mod_LionKing.idOutlands && LKLevelData.generatedMound == 0) {
            generateZiraMound(player);
        }
    }

    private static void spawnExplosionParticle(World world, Random random) {
        double x = -5.5D + random.nextFloat() * 21.0F;
        double y = 104.0D + random.nextFloat() * 8.0F;
        double z = -12.5D + random.nextFloat() * 21.0F;
        world.spawnParticle("hugeexplosion", x, y, z, 0.0D, 0.0D, 0.0D);
    }

    private static void playFlatulenceSound(World world, Random random) {
        double x = -5.5D + random.nextFloat() * 21.0F;
        double y = 104.0D + random.nextFloat() * 8.0F;
        double z = -12.5D + random.nextFloat() * 21.0F;
        world.playSoundEffect(x, y, z, "lionking:flatulence", 4F, (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F);
    }

    private static void spawnHealingParticles(EntityPlayer player, World world, Random random) {
        for (int i = 0; i < 15; i++) {
            double velX = random.nextGaussian() * 0.02D;
            double velY = random.nextGaussian() * 0.02D;
            double velZ = random.nextGaussian() * 0.02D;
            double x = player.posX + random.nextFloat() * player.width * 2.0F - player.width;
            double y = player.posY - 0.5D + random.nextFloat() * (player.height / 2);
            double z = player.posZ + random.nextFloat() * player.width * 2.0F - player.width;
            world.spawnParticle("heart", x, y, z, velX, velY, velZ);
        }
    }

    private static void checkCollectionAchievements(EntityPlayer player, boolean[] rugFlags, boolean[] headFlags) {
        boolean allRugs = true;
        for (boolean flag : rugFlags) {
            if (!flag) {
                allRugs = false;
                break;
            }
        }
        if (allRugs) {
            player.triggerAchievement(LKAchievementList.rugs);
        }

        boolean allHeads = true;
        for (boolean flag : headFlags) {
            if (!flag) {
                allHeads = false;
                break;
            }
        }
        if (allHeads) {
            player.triggerAchievement(LKAchievementList.heads);
        }
    }

    private static void generateQuestBookForPlayer(EntityPlayer player) {
        Random rand = player.getRNG();
        while (LKLevelData.receivedQuestBook == 0) {
            int x = MathHelper.floor_double(player.posX) + rand.nextInt(16) - rand.nextInt(16);
            int y = MathHelper.floor_double(player.posY) + rand.nextInt(8) - rand.nextInt(8);
            int z = MathHelper.floor_double(player.posZ) + rand.nextInt(16) - rand.nextInt(16);
            if (generateQuestBook(player.worldObj, x, y, z)) {
                LKLevelData.receivedQuestBook = 1;
            }
        }
    }

    private static boolean generateQuestBook(World world, int x, int y, int z) {
        if (world.getBlock(x, y - 1, z).getMaterial() == Material.leaves ||
            !world.getBlock(x, y - 1, z).isOpaqueCube() || !world.isAirBlock(x, y, z) ||
            !world.isAirBlock(x, y + 1, z) || !isAirAround(world, x, y + 1, z)) {
            return false;
        }

        world.setBlock(x, y, z, mod_LionKing.pridePillar, 1, 3);
        world.setBlock(x, y + 1, z, mod_LionKing.prideBrick, 0, 3);
        placeTorchesAround(world, x, y + 1, z);
        world.setBlock(x, y + 2, z, Blocks.chest, 2 + world.rand.nextInt(4), 3);

        TileEntityChest chest = (TileEntityChest) world.getTileEntity(x, y + 2, z);
        if (chest != null) {
            chest.setInventorySlotContents(13, new ItemStack(mod_LionKing.questBook));
        }
        return true;
    }

    private static boolean isAirAround(World world, int x, int y, int z) {
        return world.isAirBlock(x - 1, y, z) && world.isAirBlock(x + 1, y, z) &&
               world.isAirBlock(x, y, z - 1) && world.isAirBlock(x, y, z + 1);
    }

    private static void placeTorchesAround(World world, int x, int y, int z) {
        world.setBlock(x - 1, y, z, Blocks.torch, 0, 3);
        world.setBlock(x + 1, y, z, Blocks.torch, 0, 3);
        world.setBlock(x, y, z - 1, Blocks.torch, 0, 3);
        world.setBlock(x, y, z + 1, Blocks.torch, 0, 3);
    }

    private static void generateZiraMound(EntityPlayer player) {
        Random rand = player.getRNG();
        int attempts = 0;
        boolean generated = false;
        int x = MathHelper.floor_double(player.posX);
        int z = MathHelper.floor_double(player.posZ);

        while (attempts < 256 && !generated) {
            int offsetX = 20 + rand.nextInt(180);
            int offsetZ = 20 + rand.nextInt(180);
            x += rand.nextBoolean() ? offsetX : -offsetX;
            z += rand.nextBoolean() ? offsetZ : -offsetZ;
            int y = player.worldObj.getHeightValue(x, z);

            if (y > 62 && y < 70 && !player.worldObj.isRemote) {
                new LKWorldGenZiraMound().generate(player.worldObj, rand, x, y - 50, z);
                generated = true;
            }
            attempts++;
        }

        if (!generated && !player.worldObj.isRemote) {
            int offsetX = 20 + rand.nextInt(180);
            int offsetZ = 20 + rand.nextInt(180);
            x += rand.nextBoolean() ? offsetX : -offsetX;
            z += rand.nextBoolean() ? offsetZ : -offsetZ;
            new LKWorldGenZiraMound().generate(player.worldObj, rand, x, 14, z);
        }
        LKLevelData.generatedMound = 1;
    }

    public static void startFlatulenceExplosion(World world) {
        LKLevelData.setZiraStage(19);
        LKQuestBase.outlandsQuest.setDelayed(true);
        LKQuestBase.outlandsQuest.progress(9);

        LKEntityRafiki rafiki = new LKEntityRafiki(world);
        rafiki.setLocationAndAngles(0, 103, 0, 0.0F, 0.0F);
        rafiki.isThisTheRealRafiki = true;
        for (int i = 17; i <= 31; i++) {
            rafiki.getDataWatcher().updateObject(i, (byte) 1);
        }
        world.spawnEntityInWorld(rafiki);

        LKLevelData.setFlatulenceSoundsRemaining(10);
        flatulenceSoundTick = 25;
        loadRenderers = true;
    }

    public static boolean hasAmulet(EntityPlayer player) {
        ItemStack chestplate = player.inventory.armorItemInSlot(2);
        return chestplate != null && Item.getIdFromItem(chestplate.getItem()) == Item.getIdFromItem(mod_LionKing.amulet);
    }

    public static boolean isLKWorld(int dimension) {
        return dimension == mod_LionKing.idPrideLands || dimension == mod_LionKing.idOutlands || dimension == mod_LionKing.idUpendi;
    }

    public static boolean isChristmas() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DAY_OF_MONTH) == 25;
    }

    public static void sendMessageToAllPlayers(String message) {
        mod_LionKing.networkGame.sendToAll(new LKPacketHandlerServer.Message(message));
    }

    public static void spawnCustomFX(World world, int texture, int age, boolean glow, double posX, double posY, double posZ, double velX, double velY, double velZ) {
        if (!(world instanceof WorldServer)) return;

        byte[] data = new byte[51];
        data[0] = (byte) (texture & 255);
        data[1] = (byte) (age & 255);
        data[2] = (byte) (glow ? 1 : 0);
        encodeDoublesToByteArray(data, 3, posX, posY, posZ, velX, velY, velZ);

        mod_LionKing.networkGame.sendToAllAround(new LKPacketHandlerServer.CustomFX(data), 
            new NetworkRegistry.TargetPoint(world.provider.dimensionId, posX, posY, posZ, 64.0D));
    }

    public static void sendBreakItemPacket(EntityPlayer player, int type) {
        World world = player.worldObj;
        if (!(world instanceof WorldServer)) return;

        byte[] data = new byte[6];
        ByteBuffer.wrap(data, 0, 4).putInt(player.getEntityId());
        data[4] = (byte) player.dimension;
        data[5] = (byte) type;

        mod_LionKing.networkGame.sendToAllAround(new LKPacketHandlerServer.BreakItem(data), 
            new NetworkRegistry.TargetPoint(world.provider.dimensionId, player.posX, player.posY, player.posZ, 64.0D));
    }

    private static void encodeDoublesToByteArray(byte[] data, int offset, double... values) {
        for (int i = 0; i < values.length; i++) {
            byte[] bytes = ByteBuffer.allocate(8).putDouble(values[i]).array();
            System.arraycopy(bytes, 0, data, offset + i * 8, 8);
        }
    }
}