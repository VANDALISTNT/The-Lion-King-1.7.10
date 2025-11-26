package lionking.world;

import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.util.ChunkCoordinates;

import lionking.mod_LionKing; 
import lionking.common.LKLevelData; 
import lionking.world.LKChunkProviderPrideLands;
import lionking.world.LKWorldChunkManagerPrideLands;

public class LKWorldProviderPrideLands extends WorldProvider {
    private static final String DIMENSION_NAME = "Pride Lands";
    private static final String SAVE_FOLDER = "PrideLands";
    private static final String WELCOME_KEY = "dimension.pridelands.welcome";
    private static final String DEPART_KEY = "dimension.pridelands.depart";
    private static final ChunkCoordinates DEFAULT_SPAWN = new ChunkCoordinates(8, 64, 8);

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new LKWorldChunkManagerPrideLands(worldObj.getSeed(), worldObj.getWorldInfo().getTerrainType());
        this.dimensionId = mod_LionKing.idPrideLands;
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new LKChunkProviderPrideLands(worldObj, worldObj.getSeed());
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public String getWelcomeMessage() {
        return StatCollector.translateToLocal(WELCOME_KEY); 
    }

    @Override
    public String getDepartMessage() {
        return StatCollector.translateToLocal(DEPART_KEY); 
    }

    @Override
    public String getSaveFolder() {
        return SAVE_FOLDER;
    }

    @Override
    public String getDimensionName() {
        return DIMENSION_NAME;
    }

    @Override
    public ChunkCoordinates getSpawnPoint() {
        return new ChunkCoordinates(LKLevelData.homePortalX, LKLevelData.homePortalY, LKLevelData.homePortalZ);
    }

    @Override
    public void setSpawnPoint(int x, int y, int z) {
        if (!isDefaultSpawn(x, y, z)) {
            LKLevelData.setHomePortalLocation(x, y, z);
        }
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double y, double z) {
        return false;
    }

    private boolean isDefaultSpawn(int x, int y, int z) {
        return x == DEFAULT_SPAWN.posX && y == DEFAULT_SPAWN.posY && z == DEFAULT_SPAWN.posZ;
    }
}