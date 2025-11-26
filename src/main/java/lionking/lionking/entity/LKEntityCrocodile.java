package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem; 
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity; 

import lionking.mod_LionKing;
import lionking.common.LKLevelData;
import lionking.tileentity.LKTileEntityMobSpawner; 

import java.util.List;

public class LKEntityCrocodile extends EntityMob {
    private static Item[] preyDropsTable = new Item[] {
        mod_LionKing.zebraRaw, mod_LionKing.zebraRaw, mod_LionKing.zebraRaw, mod_LionKing.zebraHide,
        mod_LionKing.rhinoRaw, mod_LionKing.rhinoRaw, mod_LionKing.horn, mod_LionKing.gemsbokHide,
        mod_LionKing.gemsbokHide, mod_LionKing.gemsbokHide, mod_LionKing.featherBlue, mod_LionKing.featherBlue,
        mod_LionKing.featherYellow, Items.fish, Items.fish, Items.fish, Items.rotten_flesh, Items.rotten_flesh
    }; 

    public LKEntityCrocodile(World world) {
        super(world); 
        setSize(1.7F, 0.57F); 
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(18D); 
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D); 
        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2D); 
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(20, Integer.valueOf(0)); 
    }

    public int getSnapTime() {
        return dataWatcher.getWatchableObjectInt(20); 
    }

    public void setSnapTime(int i) {
        dataWatcher.updateObject(20, Integer.valueOf(i)); 
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true; 
    }

    @Override
    protected Entity findPlayerToAttack() {
        float f = getBrightness(1.0F); 
        if (f < 0.5F) {
            double d = 16.0D; 
            return worldObj.getClosestVulnerablePlayerToEntity(this, d); 
        } else {
            return null; 
        }
    }

    @Override
    protected String getLivingSound() {
        return "lionking:crocodile"; 
    }

    @Override
    protected String getDeathSound() {
        return "lionking:crocodiledeath"; 
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!worldObj.isRemote) {
            setSnapTime(attackTime); 

            if (inWater) {
                getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4D); 
            } else {
                getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2D); 
            }
        }

        if (entityToAttack == null && !hasPath() && worldObj.rand.nextInt(800) == 0) { 
            List list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(10D, 4D, 10D)); 
            if (!list.isEmpty()) {
                EntityAnimal entityanimal = (EntityAnimal) list.get(getRNG().nextInt(list.size()));
                boolean canAttack = true;
                if (entityanimal instanceof LKEntityLionBase) {
                    canAttack = false; 
                }
                if (entityanimal instanceof LKEntityGiraffe && ((LKEntityGiraffe) entityanimal).getSaddled()) {
                    canAttack = false; 
                }
                if (canAttack) {
                    setTarget(entityanimal); 
                }
            }
        }
    }

    protected int getDropItemId() { 
        return Item.getIdFromItem(mod_LionKing.crocodileMeat); 
    }

    @Override
    protected void dropFewItems(boolean flag, int i) {
        super.dropFewItems(flag, i);
        if (getRNG().nextInt(5) == 0) { 
            for (int j = 0; j < 1 + getRNG().nextInt(2) + getRNG().nextInt(1 + i); j++) {
                dropItems(preyDropsTable[getRNG().nextInt(preyDropsTable.length)], 1); 
            }
        }
    }

    private void dropItems(Item item, int count) {
        if (!worldObj.isRemote) {
            for (int i = 0; i < count; i++) {
                EntityItem entityItem = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(item, 1));
                worldObj.spawnEntityInWorld(entityItem);
            }
        }
    }

    @Override
    protected void attackEntity(Entity entity, float f) {
        if (attackTime <= 0 && f < 2.6F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY) {
            attackTime = 20; 
            attackEntityAsMob(entity); 
            worldObj.playSoundAtEntity(this, "lionking:crocodilesnap", getSoundVolume(), getSoundPitch()); 
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        if (worldObj.checkNoEntityCollision(boundingBox) && isValidLightLevel() && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0) {
            for (int i = -6; i < 7; i++) {
                for (int j = -6; j < 7; j++) {
                    for (int k = -6; k < 7; k++) {
                        int i1 = MathHelper.floor_double(posX) + i;
                        int j1 = MathHelper.floor_double(posY) + j;
                        int k1 = MathHelper.floor_double(posZ) + k;
                        Block block = worldObj.getBlock(i1, j1, k1); 
                        if (block != null && block.getMaterial() == Material.water) { 
                            if (posY > 60) {
                                return true; 
                            } else if (isCrocodileSpawnerNearby()) {
                                return true; 
                            } else if (getRNG().nextInt(3) == 0) {
                                return true; 
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isCrocodileSpawnerNearby() {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posY) - 1;
        int k = MathHelper.floor_double(posZ);
        for (int i1 = i - 8; i1 < i + 9; i1++) {
            for (int j1 = j - 4; j1 < j + 5; j1++) {
                for (int k1 = k - 8; k1 < k + 9; k1++) {
                    if (worldObj.getBlock(i1, j1, k1) == mod_LionKing.mobSpawner) { 
                        TileEntity tileEntity = worldObj.getTileEntity(i1, j1, k1); 
                        if (tileEntity instanceof LKTileEntityMobSpawner) {
                            LKTileEntityMobSpawner spawner = (LKTileEntityMobSpawner) tileEntity;
                            if (spawner.getMobID() == LKEntities.getEntityIDFromClass(getClass())) {
                                return true; 
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public float getBlockPathWeight(int i, int j, int k) {
        Block block = worldObj.getBlock(i, j - 1, k); 
        Block block1 = worldObj.getBlock(i, j, k); 
        if ((block != null && block.getMaterial() == Material.water) || (block1 != null && block1.getMaterial() == Material.water)) {
            return 20.0F; 
        } else {
            if (isInWater()) {
                return getRNG().nextInt(6) == 0 ? -99999.0F : 20.0F; 
            } else {
                return getRNG().nextInt(3) == 0 ? -99999.0F : 20.0F; 
            }
        }
    }

    @Override
    public void moveEntityWithHeading(float f, float f1) {
        if (isInWater() && entityToAttack != null) {
            double d = posY;
            moveFlying(f, f1, isAIEnabled() ? 0.04F : 0.02F); 
            moveEntity(motionX, motionY, motionZ);
            if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d, motionZ)) {
                motionY = 0.30000001192092896D; 
            }
        } else {
            super.moveEntityWithHeading(f, f1); 
        }
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this)); 
    }
}