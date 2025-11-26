package lionking.common;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.Teleporter;

import java.util.List;

import lionking.entity.LKEntitySimba;

public class LKTeleporterUpendi extends Teleporter {
    private final WorldServer world;
    private final List<NBTTagCompound> simbaData;

    public LKTeleporterUpendi(WorldServer world, List<NBTTagCompound> simbaData) {
        super(world);
        this.world = world;
        this.simbaData = simbaData;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float yaw) {
        int posX = MathHelper.floor_double(entity.posX);
        int posZ = MathHelper.floor_double(entity.posZ);
        int posY = world.getTopSolidOrLiquidBlock(posX, posZ);

        entity.setLocationAndAngles(posX + 0.5D, posY + 1.0D, posZ + 0.5D, entity.rotationYaw, 0.0F);

        spawnSimbas(entity, posX, posY, posZ);
    }

    private void spawnSimbas(Entity entity, int x, int y, int z) {
        for (NBTTagCompound simbaNBT : simbaData) {
            LKEntitySimba simba = new LKEntitySimba(world);
            simba.readFromNBT(simbaNBT);

            simba.setLocationAndAngles(x + 0.5D, y + 1.0D, z + 0.5D, entity.rotationYaw, 0.0F);
            simba.setVelocity(0.0D, 0.0D, 0.0D);

            world.spawnEntityInWorld(simba);
            simba.applyTeleportationEffects(entity);
        }
    }
}