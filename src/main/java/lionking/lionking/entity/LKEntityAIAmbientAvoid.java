package lionking.entity;

import net.minecraft.util.Vec3;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.Entity;

import lionking.common.LKAmbientPositionGenerator;

import java.util.List;

public class LKEntityAIAmbientAvoid extends EntityAIBase {
    private EntityAmbientCreature theEntity;
    private double farSpeed;
    private double nearSpeed;
    private Entity closestLivingEntity;
    private float distanceFromEntity;
    private PathEntity entityPathEntity;
    private PathNavigate entityPathNavigate;
    private Class targetEntityClass;

    public LKEntityAIAmbientAvoid(EntityAmbientCreature creature, Class cls, float f, double d, double d1) {
        theEntity = creature;
        targetEntityClass = cls;
        distanceFromEntity = f;
        farSpeed = d;
        nearSpeed = d1;
        entityPathNavigate = creature.getNavigator();
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        List list = theEntity.worldObj.getEntitiesWithinAABB(targetEntityClass, theEntity.boundingBox.expand((double) distanceFromEntity, 3.0D, (double) distanceFromEntity));

        if (list.isEmpty()) {
            return false;
        }

        closestLivingEntity = (Entity) list.get(0);

        if (!theEntity.getEntitySenses().canSee(closestLivingEntity)) {
            return false;
        } else {
            Vec3 v = LKAmbientPositionGenerator.findRandomTargetBlockAwayFrom(theEntity, 16, 7, Vec3.createVectorHelper(closestLivingEntity.posX, closestLivingEntity.posY, closestLivingEntity.posZ));

            if (v == null) {
                return false;
            } else if (closestLivingEntity.getDistanceSq(v.xCoord, v.yCoord, v.zCoord) < closestLivingEntity.getDistanceSqToEntity(theEntity)) {
                return false;
            } else {
                entityPathEntity = entityPathNavigate.getPathToXYZ(v.xCoord, v.yCoord, v.zCoord);
                return entityPathEntity != null && entityPathEntity.isDestinationSame(v);
            }
        }
    }

    @Override
    public boolean continueExecuting() {
        return !entityPathNavigate.noPath();
    }

    @Override
    public void startExecuting() {
        entityPathNavigate.setPath(entityPathEntity, farSpeed);
    }

    @Override
    public void resetTask() {
        closestLivingEntity = null;
    }

    @Override
    public void updateTask() {
        if (theEntity.getDistanceSqToEntity(closestLivingEntity) < 49.0D) {
            theEntity.getNavigator().setSpeed(nearSpeed);
        } else {
            theEntity.getNavigator().setSpeed(farSpeed);
        }
    }
}
