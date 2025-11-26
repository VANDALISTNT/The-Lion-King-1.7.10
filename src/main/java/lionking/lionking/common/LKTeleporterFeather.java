package lionking.common;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.Teleporter;

import java.util.List;
import java.util.Random;

import lionking.entity.LKEntitySimba;

public class LKTeleporterFeather extends Teleporter {
    private final Random random = new Random();
    private final WorldServer world;
    private final List<NBTTagCompound> simbaData;

    public LKTeleporterFeather(WorldServer world, List<NBTTagCompound> simbaData) {
        super(world);
        this.world = world;
        this.simbaData = simbaData;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float yaw) {
        int baseX = MathHelper.floor_double(entity.posX);
        int baseZ = MathHelper.floor_double(entity.posZ);

        int targetX = baseX - 32 + random.nextInt(65);
        int targetZ = baseZ - 32 + random.nextInt(65);
        int targetY = world.getTopSolidOrLiquidBlock(targetX, targetZ) + random.nextInt(12);

        entity.setLocationAndAngles(targetX + 0.5D, targetY + 1.0D, targetZ + 0.5D, entity.rotationYaw, 0.0F);

        spawnSimbas(entity, targetX, targetY, targetZ);
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