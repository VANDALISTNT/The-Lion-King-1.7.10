package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.block.Block;

import lionking.tileentity.LKTileEntityBugTrap;
import lionking.common.LKCharacter;
import lionking.mod_LionKing;

public class LKEntityAIBugFindTrap extends EntityAIBase {
    private LKEntityBug entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private World worldObj;
    private LKTileEntityBugTrap theTrap;

    public LKEntityAIBugFindTrap(LKEntityBug bug, double d) {
        entity = bug;
        speed = d;
        worldObj = bug.worldObj;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (entity.getRNG().nextInt(5) != 0) {
            return false;
        } else {
            for (int i = -8; i <= 8; i++) {
                for (int j = -4; j <= 4; j++) {
                    for (int k = -8; k <= 8; k++) {
                        int i1 = i + MathHelper.floor_double(entity.posX);
                        int j1 = j + MathHelper.floor_double(entity.posY);
                        int k1 = k + MathHelper.floor_double(entity.posZ);
                        if (Block.getIdFromBlock(worldObj.getBlock(i1, j1, k1)) == Block.getIdFromBlock(mod_LionKing.bugTrap)) {
                            LKTileEntityBugTrap tileentity = (LKTileEntityBugTrap) worldObj.getTileEntity(i1, j1, k1);
                            if (tileentity != null) {
                                int l = entity.getRNG().nextInt(4);
                                if (tileentity.getAverageBaitSaturation() > 0.0F) {
                                    xPosition = i1;
                                    yPosition = j1;
                                    zPosition = k1;
                                    theTrap = tileentity;
                                    entity.targetTrap = tileentity;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean continueExecuting() {
        return !entity.getNavigator().noPath() && theTrap != null && (theTrap.getAverageBaitSaturation() > 0.0F || entity.trapTick > -1);
    }

    @Override
    public void startExecuting() {
        entity.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, speed);
    }
}
