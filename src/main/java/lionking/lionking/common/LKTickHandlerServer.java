package lionking.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.List;

import lionking.mod_LionKing;
import lionking.quest.LKQuestBase;
import lionking.entity.LKEntityZira;
import lionking.entity.LKEntityLightning;
import lionking.tileentity.LKTileEntityOutlandsPool;

public class LKTickHandlerServer {
    public static final HashMap<EntityPlayer, Integer> playersInPortals = new HashMap<>();
    public static final HashMap<EntityPlayer, Integer> playersInOutPortals = new HashMap<>();

    public LKTickHandlerServer() {
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            World world = event.world;
            handleWorldTick(world);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            handlePlayerTick(player);
        }
    }

    private void handleWorldTick(World world) {
        if (world == DimensionManager.getWorld(0)) {
            handleOverworldTick(world);
        } else if (world == DimensionManager.getWorld(mod_LionKing.idPrideLands)) {
            handlePrideLandsTick((WorldServer) world);
        } else if (world == DimensionManager.getWorld(mod_LionKing.idOutlands)) {
            handleOutlandsTick(world);
        }
    }

    private void handleOverworldTick(World world) {
        if (LKLevelData.needsLoad) {
            LKLevelData.load();
            LKLevelData.needsLoad = false;
        }

        LKQuestBase.updateAllQuests();
        LKIngame.runMainWorldTick(world);

        if (LKLevelData.needsSave) {
            LKLevelData.save();
            LKLevelData.needsSave = false;
        }

        if (world.getWorldTime() % 600L == 0L) {
            LKLevelData.save();
        }
    }

    private void handlePrideLandsTick(WorldServer world) {
        List<EntityPlayer> players = world.playerEntities;
        if (!players.isEmpty()) {
            removePortalsAroundPlayers(world, players);
            handleSleepLogic(world, players);
        }

        if (!world.isRemote && LKLevelData.ziraStage == 13 && world.getChunkProvider().chunkExists(0, 0)) {
            LKLevelData.setZiraStage(14);
            LKEntityZira zira = new LKEntityZira(world);
            zira.setLocationAndAngles(0, 103, 0, 0.0F, 0.0F);
            world.spawnEntityInWorld(zira);
            zira.spawnOutlandersInTree();
        }
    }

    private void handleOutlandsTick(World world) {
        List<EntityPlayer> players = world.playerEntities;
        if (!players.isEmpty()) {
            removePortalsAroundPlayers(world, players);
            if (world.rand.nextInt(120) == 0) {
                LKIngame.generateOutsand(players.get(0), world, world.rand);
            }
        }

        LKTileEntityOutlandsPool.updateInventory(world);

        if (LKLevelData.ziraStage == 22 && !players.isEmpty()) {
            spawnZiraInOutlands(world, players.get(0));
        }
    }

    private void removePortalsAroundPlayers(World world, List<EntityPlayer> players) {
        for (EntityPlayer player : players) {
            int x = MathHelper.floor_double(player.posX);
            int y = MathHelper.floor_double(player.posY);
            int z = MathHelper.floor_double(player.posZ);
            for (int dx = -8; dx <= 8; dx++) {
                for (int dy = -8; dy <= 8; dy++) {
                    for (int dz = -8; dz <= 8; dz++) {
                        if (world.getBlock(x + dx, y + dy, z + dz) == Blocks.portal) {
                            world.setBlockToAir(x + dx, y + dy, z + dz);
                        }
                    }
                }
            }
        }
    }

    private void handleSleepLogic(WorldServer world, List<EntityPlayer> players) {
        if (!world.areAllPlayersAsleep()) return;

        boolean allFullyAsleep = players.stream().allMatch(EntityPlayer::isPlayerFullyAsleep);
        if (allFullyAsleep) {
            World overWorld = DimensionManager.getWorld(0);
            long time = world.getWorldInfo().getWorldTime() + 24000L;
            overWorld.getWorldInfo().setWorldTime(time - time % 24000L);
            overWorld.getWorldInfo().setRainTime(0);
            overWorld.getWorldInfo().setRaining(false);
            overWorld.getWorldInfo().setThunderTime(0);
            overWorld.getWorldInfo().setThundering(false);

            for (EntityPlayer player : players) {
                if (player.isPlayerSleeping()) {
                    player.wakeUpPlayer(false, false, true);
                }
            }
            overWorld.provider.resetRainAndThunder();
        }
    }

    private void spawnZiraInOutlands(World world, EntityPlayer player) {
        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.boundingBox.minY);
        int z = MathHelper.floor_double(player.posZ);

        if (world.canBlockSeeTheSky(x, y, z) && y == world.getHeightValue(x, z)) {
            int spawnX = x - 8 + world.rand.nextInt(17);
            int spawnZ = z - 8 + world.rand.nextInt(17);
            int spawnY = world.getHeightValue(spawnX, spawnZ);

            LKEntityZira zira = new LKEntityZira(world);
            zira.setSelfTalkTick(0);
            zira.setPosition(spawnX, spawnY, spawnZ);
            zira.getLookHelper().setLookPosition(player.posX, player.posY + player.getEyeHeight(), player.posZ, 10.0F, zira.getVerticalFaceSpeed());
            world.spawnEntityInWorld(zira);
            world.spawnEntityInWorld(new LKEntityLightning(player, world, spawnX, spawnY, spawnZ, 0));

            LKLevelData.setZiraStage(23);
        }
    }

    private void handlePlayerTick(EntityPlayer player) {
        if (player == null || player.worldObj == null) return;

        awardDimensionAchievements(player);
        checkWingsAchievement(player);
        handlePortalTeleportation(player, playersInPortals, true);
        handlePortalTeleportation(player, playersInOutPortals, false);
    }

    private void awardDimensionAchievements(EntityPlayer player) {
        if (player.dimension == mod_LionKing.idOutlands) {
            player.triggerAchievement(LKAchievementList.enterOutlands);
        } else if (player.dimension == mod_LionKing.idUpendi) {
            player.triggerAchievement(LKAchievementList.upendi);
        }
    }

    private void checkWingsAchievement(EntityPlayer player) {
        ItemStack[] armor = player.inventory.armorInventory;
        if (armor[2] != null && armor[2].getItem() == mod_LionKing.wings && !player.onGround) {
            player.triggerAchievement(LKAchievementList.wings);
        }
    }

    private void handlePortalTeleportation(EntityPlayer player, HashMap<EntityPlayer, Integer> portalMap, boolean isPrideLandsPortal) {
        World world = player.worldObj;
        int targetDimension = getTargetDimension(player.dimension, isPrideLandsPortal);

        if (targetDimension == -1 || !portalMap.containsKey(player)) return;

        if (LKIngame.isPlayerInLionPortal(player, isPrideLandsPortal)) {
            int timeInPortal = portalMap.get(player) + 1;
            portalMap.put(player, timeInPortal);

            if (timeInPortal >= 100 && player instanceof EntityPlayerMP && world instanceof WorldServer) {
                MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(
                        (EntityPlayerMP) player, targetDimension,
                        new LKTeleporter(DimensionManager.getWorld(targetDimension), isPrideLandsPortal, LKIngame.getSimbas(player))
                );
                LKIngame.doAdditionalPortalActions(player, targetDimension, isPrideLandsPortal);
                portalMap.remove(player);
            }
        } else {
            portalMap.remove(player);
        }
    }

    private int getTargetDimension(int currentDimension, boolean isPrideLandsPortal) {
        if (isPrideLandsPortal) {
            if (currentDimension == 0) return mod_LionKing.idPrideLands;
            if (currentDimension == mod_LionKing.idPrideLands) return 0;
        } else {
            if (currentDimension == mod_LionKing.idOutlands) return mod_LionKing.idPrideLands;
            if (currentDimension == mod_LionKing.idPrideLands) return mod_LionKing.idOutlands;
        }
        return -1;
    }
}
