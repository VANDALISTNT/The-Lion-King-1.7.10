package lionking.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

import lionking.mod_LionKing; 
import lionking.common.LKLevelData; 
import lionking.world.LKChunkProviderUpendi;
import lionking.world.LKWorldChunkManagerUpendi;

public class LKWorldProviderUpendi extends WorldProvider {
    private static final String DIMENSION_NAME = "Upendi";
    private static final String SAVE_FOLDER = "Upendi";
    private static final String WELCOME_KEY = "dimension.upendi.welcome";
    private static final String DEPART_KEY = "dimension.upendi.depart";
    private static final float FOG_R = 209.0F / 255.0F;
    private static final float FOG_G = 86.0F / 255.0F;
    private static final float FOG_B = 234.0F / 255.0F;

    @Override
    public void registerWorldChunkManager() {
        worldChunkMgr = new LKWorldChunkManagerUpendi();
        dimensionId = mod_LionKing.idUpendi;
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new LKChunkProviderUpendi(worldObj, worldObj.getSeed());
    }

    @Override
    public Vec3 getFogColor(float celestialAngle, float partialTicks) {
        return Vec3.createVectorHelper(FOG_R, FOG_G, FOG_B);
    }

    @Override
    public boolean canRespawnHere() {
        return false;
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
    public boolean shouldMapSpin(String entity, double x, double y, double z) {
        return false;
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        return mod_LionKing.idPrideLands;
    }
}