package lionking.common;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.init.Blocks; 

import java.util.Random;

public class LKAmbientPositionGenerator {
    private static final Vec3 STATIC_VECTOR = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);

    public static Vec3 findRandomTarget(EntityAmbientCreature creature, int rangeX, int rangeY) {
        return findRandomTargetBlock(creature, rangeX, rangeY, null);
    }

    public static Vec3 findRandomTargetBlockTowards(EntityAmbientCreature creature, int rangeX, int rangeY, Vec3 target) {
        STATIC_VECTOR.xCoord = target.xCoord - creature.posX;
        STATIC_VECTOR.yCoord = target.yCoord - creature.posY;
        STATIC_VECTOR.zCoord = target.zCoord - creature.posZ;
        return findRandomTargetBlock(creature, rangeX, rangeY, STATIC_VECTOR);
    }

    public static Vec3 findRandomTargetBlockAwayFrom(EntityAmbientCreature creature, int rangeX, int rangeY, Vec3 target) {
        STATIC_VECTOR.xCoord = creature.posX - target.xCoord;
        STATIC_VECTOR.yCoord = creature.posY - target.yCoord;
        STATIC_VECTOR.zCoord = creature.posZ - target.zCoord;
        return findRandomTargetBlock(creature, rangeX, rangeY, STATIC_VECTOR);
    }

    private static Vec3 findRandomTargetBlock(EntityAmbientCreature creature, int rangeX, int rangeY, Vec3 direction) {
        Random rand = creature.getRNG();
        boolean found = false;
        int targetX = 0;
        int targetY = 0;
        int targetZ = 0;
        float bestWeight = -99999.0F;

        for (int attempt = 0; attempt < 10; attempt++) {
            int offsetX = rand.nextInt(2 * rangeX) - rangeX;
            int offsetY = rand.nextInt(2 * rangeY) - rangeY;
            int offsetZ = rand.nextInt(2 * rangeX) - rangeX;

            if (direction == null || offsetX * direction.xCoord + offsetZ * direction.zCoord >= 0.0D) {
                int x = offsetX + MathHelper.floor_double(creature.posX);
                int y = offsetY + MathHelper.floor_double(creature.posY);
                int z = offsetZ + MathHelper.floor_double(creature.posZ);

                float weight = getBlockPathWeight(creature, x, y, z);
                if (weight > bestWeight) {
                    bestWeight = weight;
                    targetX = x;
                    targetY = y;
                    targetZ = z;
                    found = true;
                }
            }
        }

        return found ? Vec3.createVectorHelper(targetX, targetY, targetZ) : null;
    }

    private static float getBlockPathWeight(EntityAmbientCreature entity, int x, int y, int z) {
        return entity.worldObj.getBlock(x, y - 1, z) == Blocks.grass
                ? 10.0F
                : entity.worldObj.getLightBrightness(x, y, z) - 0.5F;
    }
}