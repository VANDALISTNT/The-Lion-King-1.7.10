package lionking.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.FMLLog;

import lionking.mod_LionKing;
import lionking.world.LKWorldChunkManagerOutlands;
import lionking.world.LKChunkProviderOutlands;

public class LKWorldProviderOutlands extends WorldProvider {
    private static final float CELESTIAL_ANGLE = 0.5F;
    private static final String DIMENSION_NAME = "Outlands";
    private static final String SAVE_FOLDER = "Outlands";
    private static final String WELCOME_KEY = "dimension.outlands.welcome";
    private static final String DEPART_KEY = "dimension.outlands.depart";

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new LKWorldChunkManagerOutlands(worldObj.getSeed(), worldObj.getWorldInfo().getTerrainType());
        this.dimensionId = mod_LionKing.idOutlands;
        FMLLog.info("Registered WorldChunkManager for Outlands dimension ID %d", this.dimensionId);
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new LKChunkProviderOutlands(worldObj, worldObj.getSeed());
    }

    @Override
    public float calculateCelestialAngle(long time, float partialTicks) {
        return CELESTIAL_ANGLE;
    }

    @Override
    public int getMoonPhase(long time) {
        return 0;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public String getWelcomeMessage() {
        String message = StatCollector.translateToLocal(WELCOME_KEY);
        if (message.equals(WELCOME_KEY)) {
            FMLLog.warning("Localization key %s not found, using default message", WELCOME_KEY);
            return "Welcome to the Outlands!";
        }
        return message;
    }

    @Override
    public String getDepartMessage() {
        String message = StatCollector.translateToLocal(DEPART_KEY);
        if (message.equals(DEPART_KEY)) {
            FMLLog.warning("Localization key %s not found, using default message", DEPART_KEY);
            return "Leaving the Outlands!";
        }
        return message;
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
    public boolean shouldMapSpin(String entity, double x, double y, double z) {
        return false;
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        return mod_LionKing.idPrideLands;
    }
}