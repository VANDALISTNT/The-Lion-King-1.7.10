package lionking.entity;

import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.Random;
import java.util.List;
import java.util.Iterator;

import lionking.mod_LionKing;

public class LKEntityAIZazuMate extends EntityAIBase {
    private LKEntityZazu theZazu; 
    World theWorld; 
    private LKEntityZazu targetMate; 

    int layEggDelay = 0; 
    private double moveSpeed; 

    public LKEntityAIZazuMate(LKEntityZazu zazu, double d) {
        theZazu = zazu; 
        theWorld = zazu.worldObj; 
        moveSpeed = d; 
        setMutexBits(3); 
    }

    @Override
    public boolean shouldExecute() {
        if (!theZazu.isInLove()) { 
            return false;
        } else {
            targetMate = getNearbyMate(); 
            return targetMate != null; 
        }
    }

    @Override
    public boolean continueExecuting() {
        return targetMate.isEntityAlive() && targetMate.isInLove() && layEggDelay < 60; 
    }

    @Override
    public void resetTask() {
        targetMate = null; 
        layEggDelay = 0; 
    }

    @Override
    public void updateTask() {
        theZazu.getLookHelper().setLookPositionWithEntity(targetMate, 10.0F, (float) theZazu.getVerticalFaceSpeed()); 
        theZazu.getNavigator().tryMoveToEntityLiving(targetMate, moveSpeed); 
        ++layEggDelay; 

        if (layEggDelay == 60) { 
            layEggs(); 
        }
    }

    private LKEntityZazu getNearbyMate() {
        float f = 8.0F; 
        List list = theWorld.getEntitiesWithinAABB(LKEntityZazu.class, theZazu.boundingBox.expand((double) f, (double) f, (double) f)); 
        Iterator i = list.iterator(); 
        LKEntityZazu zazu;

        do {
            if (!i.hasNext()) { 
                return null;
            }
            zazu = (LKEntityZazu) i.next(); 
        } while (!theZazu.canMateWith(zazu)); 

        return zazu; 
    }

    private void layEggs() {
        theZazu.setGrowingAge(6000); 
        targetMate.setGrowingAge(6000); 
        theZazu.resetInLove(); 
        targetMate.resetInLove(); 

        Random rand = theZazu.getRNG(); 
        int eggs = rand.nextInt(3) + 1; 
        for (int i = 0; i < eggs; i++) {
            theZazu.dropItem(mod_LionKing.zazuEgg, 1); 
        }

        for (int i = 0; i < 7; i++) {
            double d = rand.nextGaussian() * 0.02D; 
            double d1 = rand.nextGaussian() * 0.02D; 
            double d2 = rand.nextGaussian() * 0.02D; 
            theWorld.spawnParticle("heart", theZazu.posX + (double) (rand.nextFloat() * theZazu.width * 2.0F) - (double) theZazu.width,
                    theZazu.posY + 0.5D + (double) (rand.nextFloat() * theZazu.height), theZazu.posZ + (double) (rand.nextFloat() * theZazu.width * 2.0F) - (double) theZazu.width,
                    d, d1, d2); 
        }
    }
}