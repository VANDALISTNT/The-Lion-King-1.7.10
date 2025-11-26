package lionking.entity;

import net.minecraft.util.Vec3;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.ai.EntityAIBase;

import lionking.common.LKAmbientPositionGenerator;

public class LKEntityAIAmbientWander extends EntityAIBase {
    private EntityAmbientCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;

    public LKEntityAIAmbientWander(EntityAmbientCreature creature, double d) {
        entity = creature;
        speed = d;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (entity.getAge() >= 100) {
            return false;
        } else if (entity.getRNG().nextInt(60) != 0) {
            return false;
        } else {
            Vec3 v = LKAmbientPositionGenerator.findRandomTarget(entity, 10, 7);

            if (v == null) {
                return false;
            } else {
                xPosition = v.xCoord;
                yPosition = v.yCoord;
                zPosition = v.zCoord;
                return true;
            }
        }
    }

    @Override
    public boolean continueExecuting() {
        return !entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        entity.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, speed);
    }
}
