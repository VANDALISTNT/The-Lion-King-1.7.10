package lionking.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class LKTileEntityMobSpawner extends TileEntity {

    private int delay = 20;
    private int mobID = 1;

    public double yaw;
    public double yaw2 = 0.0D;

    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 800;
    private int spawnCount = 4;
    private int maxNearbyEntities = 6;
    private int requiredPlayerRange = 16;
    private int spawnRange = 4;

    private Entity cachedMobEntity;

    public LKTileEntityMobSpawner() {
        delay = 20;
    }

    public int getMobID() {
        return mobID;
    }

    public void setMobID(int id) {
        this.mobID = id;
        cachedMobEntity = null;
        if (!worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    private boolean isPlayerInRange() {
        return worldObj.getClosestPlayer(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, requiredPlayerRange) != null;
    }

    @Override
    public void updateEntity() {
        if (worldObj == null || !isPlayerInRange()) {
            return;
        }

        if (worldObj.isRemote) {
            spawnParticles();
            yaw2 = yaw;
            double speed = 1000.0D / (delay + 200.0D);
            yaw = (yaw + speed) % 360.0D;

            if (delay > 0) {
                delay--;
            }
        } else {
            if (delay == -1) {
                resetDelay();
            }

            if (delay > 0) {
                delay--;
                return;
            }

            boolean spawnedAtLeastOne = false;
            for (int i = 0; i < spawnCount; i++) {
                if (trySpawnMob()) {
                    spawnedAtLeastOne = true;
                }
            }

            if (spawnedAtLeastOne) {
                worldObj.playAuxSFX(2004, xCoord, yCoord, zCoord, 0);
            }

            resetDelay();
        }
    }

    private void spawnParticles() {
        for (int i = 0; i < 2; ++i) {
            double px = xCoord + worldObj.rand.nextFloat();
            double py = yCoord + worldObj.rand.nextFloat();
            double pz = zCoord + worldObj.rand.nextFloat();
            worldObj.spawnParticle("smoke", px, py, pz, 0.0D, 0.0D, 0.0D);
            worldObj.spawnParticle("flame", px, py, pz, 0.0D, 0.0D, 0.0D);
        }
    }

    private boolean trySpawnMob() {
        int nearby = worldObj.getEntitiesWithinAABB(EntityLiving.class,
                AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1)
                        .expand(spawnRange * 2, 4.0D, spawnRange * 2)).size();

        if (nearby >= maxNearbyEntities) {
            resetDelay();
            return false;
        }

        Entity entity = createMobEntity();
        if (entity == null) {
            return false;
        }

        double spawnX = xCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * spawnRange + 0.5D;
        double spawnY = yCoord + worldObj.rand.nextInt(3) - 1;
        double spawnZ = zCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * spawnRange + 0.5D;

        entity.setLocationAndAngles(spawnX, spawnY, spawnZ, worldObj.rand.nextFloat() * 360.0F, 0.0F);

        if (entity instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) entity;
            living.onSpawnWithEgg(null);
            worldObj.spawnEntityInWorld(living);
            living.spawnExplosionParticle();
        }

        return true;
    }

    private Entity createMobEntity() {
        Entity entity = EntityList.createEntityByID(mobID, worldObj);

        if (entity == null) {
            String name = EntityList.getStringFromID(mobID);
            if (name != null) {
                entity = EntityList.createEntityByName(name, worldObj);
            }
        }
        return entity;
    }

    private void resetDelay() {
        if (maxSpawnDelay <= minSpawnDelay) {
            delay = minSpawnDelay;
        } else {
            int range = maxSpawnDelay - minSpawnDelay;
            delay = minSpawnDelay + worldObj.rand.nextInt(range);
        }
    }

    public Entity getMobEntity() {
        if (cachedMobEntity == null && worldObj != null) {
            cachedMobEntity = createMobEntity();
            if (cachedMobEntity != null) {
                cachedMobEntity.setWorld(worldObj);
                if (cachedMobEntity instanceof EntityLiving) {
                    ((EntityLiving) cachedMobEntity).onSpawnWithEgg(null);
                }
            }
        }
        return cachedMobEntity;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        mobID = nbt.getInteger("EntityId");
        delay = nbt.getShort("Delay");
        minSpawnDelay = nbt.getShort("MinSpawnDelay");
        maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
        spawnCount = nbt.getShort("SpawnCount");
        maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
        requiredPlayerRange = nbt.getShort("RequiredPlayerRange");
        spawnRange = nbt.getShort("SpawnRange");

        cachedMobEntity = null;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("EntityId", mobID);
        nbt.setShort("Delay", (short) delay);
        nbt.setShort("MinSpawnDelay", (short) minSpawnDelay);
        nbt.setShort("MaxSpawnDelay", (short) maxSpawnDelay);
        nbt.setShort("SpawnCount", (short) spawnCount);
        nbt.setShort("MaxNearbyEntities", (short) maxNearbyEntities);
        nbt.setShort("RequiredPlayerRange", (short) requiredPlayerRange);
        nbt.setShort("SpawnRange", (short) spawnRange);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    @Override
    public boolean receiveClientEvent(int eventID, int eventParam) {
        if (eventID == 1 && worldObj != null && worldObj.isRemote) {
            delay = minSpawnDelay;
            return true;
        }
        return super.receiveClientEvent(eventID, eventParam);
    }
}