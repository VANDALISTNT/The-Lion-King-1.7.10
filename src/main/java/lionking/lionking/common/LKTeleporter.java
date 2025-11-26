package lionking.common;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lionking.entity.LKEntitySimba;
import lionking.mod_LionKing;

public class LKTeleporter extends Teleporter {
    private final WorldServer world;
    private final Random random;
    private final Block frameBlock;
    private final Block portalBlock;
    private final boolean isPrideLands;
    private final List<NBTTagCompound> simbaData;
    private final LongHashMap portals = new LongHashMap();
    private final List<Long> portalList = new ArrayList<>();

    public LKTeleporter(WorldServer world, boolean isPrideLands, List<NBTTagCompound> simbaData) {
        super(world);
        this.world = world;
        this.random = new Random(world.getSeed());
        this.isPrideLands = isPrideLands;
        this.frameBlock = isPrideLands ? mod_LionKing.lionPortalFrame : mod_LionKing.outlandsPortalFrame;
        this.portalBlock = isPrideLands ? mod_LionKing.lionPortal : mod_LionKing.outlandsPortal;
        this.simbaData = simbaData;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float yaw) {
        if (!placeInExistingLionPortal(entity, x, y, z, yaw)) {
            createLionPortal(entity);
            placeInExistingLionPortal(entity, x, y, z, yaw);
        }
    }

    public boolean placeInExistingLionPortal(Entity entity, double x, double y, double z, float yaw) {
        double nearestDistance = -1.0D;
        int portalX = 0, portalY = 0, portalZ = 0;
        int entityX = MathHelper.floor_double(entity.posX);
        int entityZ = MathHelper.floor_double(entity.posZ);
        long chunkKey = ChunkCoordIntPair.chunkXZ2Int(entityX, entityZ);
        boolean isNewPortal = true;

        if (portals.containsItem(chunkKey)) {
            PortalPosition portal = (PortalPosition) portals.getValueByKey(chunkKey);
            nearestDistance = 0.0D;
            portalX = portal.posX;
            portalY = portal.posY;
            portalZ = portal.posZ;
            portal.lastUpdateTime = world.getTotalWorldTime();
            isNewPortal = false;
        } else {
            for (int searchX = entityX - 128; searchX <= entityX + 128; searchX++) {
                double dx = searchX + 0.5D - entity.posX;
                for (int searchZ = entityZ - 128; searchZ <= entityZ + 128; searchZ++) {
                    double dz = searchZ + 0.5D - entity.posZ;
                    for (int searchY = world.getActualHeight() - 1; searchY >= 0; searchY--) {
                        if (world.getBlock(searchX, searchY, searchZ) == portalBlock) {
                            while (world.getBlock(searchX, searchY - 1, searchZ) == portalBlock) {
                                searchY--;
                            }
                            double dy = searchY + 0.5D - entity.posY;
                            double distance = dx * dx + dy * dy + dz * dz;
                            if (nearestDistance < 0.0D || distance < nearestDistance) {
                                nearestDistance = distance;
                                portalX = searchX;
                                portalY = searchY;
                                portalZ = searchZ;
                            }
                        }
                    }
                }
            }
        }

        if (nearestDistance >= 0.0D) {
            if (isNewPortal) {
                portals.add(chunkKey, new PortalPosition(portalX, portalY, portalZ, world.getTotalWorldTime()));
                portalList.add(chunkKey);
            }

            double finalX = portalX + 0.5D;
            double finalY = portalY + 0.5D;
            double finalZ = portalZ + 0.5D;
            int portalDirection = determinePortalDirection(portalX, portalY, portalZ);

            adjustEntityPositionAndMotion(entity, portalDirection, finalX, finalY, finalZ, yaw);
            spawnSimbas(entity, finalX, finalY, finalZ);

            return true;
        }
        return false;
    }

    private int determinePortalDirection(int x, int y, int z) {
        if (world.getBlock(x - 1, y, z) == portalBlock) return 2;
        if (world.getBlock(x + 1, y, z) == portalBlock) return 0;
        if (world.getBlock(x, y, z - 1) == portalBlock) return 3;
        if (world.getBlock(x, y, z + 1) == portalBlock) return 1;
        return -1;
    }

    private void adjustEntityPositionAndMotion(Entity entity, int direction, double x, double y, double z, float yaw) {
        if (direction == -1) {
            entity.motionX = entity.motionY = entity.motionZ = 0.0D;
            entity.setLocationAndAngles(x, y, z, yaw, entity.rotationPitch);
            return;
        }

        int leftDirection = Direction.rotateLeft[direction];
        int dx = Direction.offsetX[direction];
        int dz = Direction.offsetZ[direction];
        int dxLeft = Direction.offsetX[leftDirection];
        int dzLeft = Direction.offsetZ[leftDirection];

        int checkXLeft = MathHelper.floor_double(x + dx + dxLeft);
        int checkZLeft = MathHelper.floor_double(z + dz + dzLeft);
        int checkXStraight = MathHelper.floor_double(x + dx);
        int checkZStraight = MathHelper.floor_double(z + dz);

        boolean isBlockedLeft = !world.isAirBlock(checkXLeft, MathHelper.floor_double(y), checkZLeft) ||
                                !world.isAirBlock(checkXLeft, MathHelper.floor_double(y) + 1, checkZLeft);
        boolean isBlockedStraight = !world.isAirBlock(checkXStraight, MathHelper.floor_double(y), checkZStraight) ||
                                   !world.isAirBlock(checkXStraight, MathHelper.floor_double(y) + 1, checkZStraight);

        if (isBlockedLeft && isBlockedStraight) {
            direction = Direction.rotateOpposite[direction];
            leftDirection = Direction.rotateOpposite[leftDirection];
            dx = Direction.offsetX[direction];
            dz = Direction.offsetZ[direction];
            dxLeft = Direction.offsetX[leftDirection];
            dzLeft = Direction.offsetZ[leftDirection];
            x -= dxLeft;
            z -= dzLeft;

            checkXLeft = MathHelper.floor_double(x + dx + dxLeft);
            checkZLeft = MathHelper.floor_double(z + dz + dzLeft);
            checkXStraight = MathHelper.floor_double(x + dx);
            checkZStraight = MathHelper.floor_double(z + dz);

            isBlockedLeft = !world.isAirBlock(checkXLeft, MathHelper.floor_double(y), checkZLeft) ||
                            !world.isAirBlock(checkXLeft, MathHelper.floor_double(y) + 1, checkZLeft);
            isBlockedStraight = !world.isAirBlock(checkXStraight, MathHelper.floor_double(y), checkZStraight) ||
                               !world.isAirBlock(checkXStraight, MathHelper.floor_double(y) + 1, checkZStraight);
        }

        float offsetX = isBlockedLeft && !isBlockedStraight ? 1.0F : (isBlockedStraight && !isBlockedLeft ? 0.0F : 0.5F);
        float offsetZ = (isBlockedLeft && isBlockedStraight) ? 0.0F : 0.5F;

        x += dxLeft * offsetX + offsetZ * dx;
        z += dzLeft * offsetX + offsetZ * dz;

        int teleportDirection = entity.getTeleportDirection();
        float motionXFactor = (direction == teleportDirection || direction == Direction.rotateOpposite[teleportDirection]) ? (direction == teleportDirection ? 1.0F : -1.0F) : 0.0F;
        float motionZFactor = (direction == Direction.rotateRight[teleportDirection] || direction == Direction.rotateLeft[teleportDirection]) ? (direction == Direction.rotateRight[teleportDirection] ? 1.0F : -1.0F) : 0.0F;

        double originalMotionX = entity.motionX;
        double originalMotionZ = entity.motionZ;
        entity.motionX = originalMotionX * motionXFactor + originalMotionZ * (direction == Direction.rotateRight[teleportDirection] ? 1.0F : -1.0F);
        entity.motionZ = originalMotionX * motionZFactor + originalMotionZ * (direction == teleportDirection ? 1.0F : -1.0F);
        entity.rotationYaw = yaw - (teleportDirection * 90) + (direction * 90);

        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
    }

    private void spawnSimbas(Entity entity, double x, double y, double z) {
        for (NBTTagCompound simbaNBT : simbaData) {
            LKEntitySimba simba = new LKEntitySimba(world);
            simba.readFromNBT(simbaNBT);
            simba.setLocationAndAngles(x, y, z, entity.rotationYaw, 0.0F);
            simba.motionX = simba.motionY = simba.motionZ = 0.0D;
            world.spawnEntityInWorld(simba);
            simba.applyTeleportationEffects(entity);
        }
    }

    public boolean createLionPortal(Entity entity) {
        int searchRadius = 16;
        double bestDistance = -1.0D;
        int baseX = MathHelper.floor_double(entity.posX);
        int baseY = MathHelper.floor_double(entity.posY);
        int baseZ = MathHelper.floor_double(entity.posZ);
        int portalX = baseX, portalY = baseY, portalZ = baseZ;
        int direction = random.nextInt(4);

        for (int x = baseX - searchRadius; x <= baseX + searchRadius; x++) {
            double dx = x + 0.5D - entity.posX;
            for (int z = baseZ - searchRadius; z <= baseZ + searchRadius; z++) {
                double dz = z + 0.5D - entity.posZ;
                for (int y = world.getActualHeight() - 1; y >= 0; y--) {
                    if (world.isAirBlock(x, y, z)) {
                        while (y > 0 && world.isAirBlock(x, y - 1, z)) y--;
                        if (isValidPortalLocation(x, y, z, direction)) {
                            double dy = y + 0.5D - entity.posY;
                            double distance = dx * dx + dy * dy + dz * dz;
                            if (bestDistance < 0.0D || distance < bestDistance) {
                                bestDistance = distance;
                                portalX = x;
                                portalY = y;
                                portalZ = z;
                            }
                        }
                    }
                }
            }
        }

        if (bestDistance < 0.0D) {
            portalY = MathHelper.clamp_int(baseY, 70, world.getActualHeight() - 10);
            buildPortalFrame(portalX, portalY, portalZ, direction, true);
        }

        buildPortalFrame(portalX, portalY, portalZ, direction, false);
        return true;
    }

    private boolean isValidPortalLocation(int x, int y, int z, int direction) {
        int dx = direction % 2;
        int dz = 1 - dx;
        if (direction % 4 >= 2) {
            dx = -dx;
            dz = -dz;
        }

        for (int width = 0; width < 3; width++) {
            for (int depth = 0; depth < 4; depth++) {
                for (int height = -1; height < 4; height++) {
                    int checkX = x + (depth - 1) * dx + width * dz;
                    int checkY = y + height;
                    int checkZ = z + (depth - 1) * dz - width * dx;
                    if (height < 0 && !world.getBlock(checkX, checkY, checkZ).getMaterial().isSolid() ||
                        height >= 0 && !world.isAirBlock(checkX, checkY, checkZ)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void buildPortalFrame(int x, int y, int z, int direction, boolean forceAir) {
        int dx = direction % 2;
        int dz = 1 - dx;
        if (direction % 4 >= 2) {
            dx = -dx;
            dz = -dz;
        }

        for (int width = 0; width < 4; width++) {
            for (int height = -1; height < 4; height++) {
                int frameX = x + (width - 1) * dx;
                int frameY = y + height;
                int frameZ = z + (width - 1) * dz;
                boolean isFrame = width == 0 || width == 3 || height == -1 || height == 3;
                world.setBlock(frameX, frameY, frameZ, forceAir && height < 0 ? frameBlock : (isFrame ? frameBlock : portalBlock), 0, 2);
                if (!forceAir) world.notifyBlocksOfNeighborChange(frameX, frameY, frameZ, world.getBlock(frameX, frameY, frameZ));
            }
        }
    }

    @Override
    public void removeStalePortalLocations(long time) {
        if (time % 100L == 0L) {
            long staleTime = time - 600L;
            Iterator<Long> iterator = portalList.iterator();
            while (iterator.hasNext()) {
                long chunkKey = iterator.next();
                PortalPosition pos = (PortalPosition) portals.getValueByKey(chunkKey);
                if (pos == null || pos.lastUpdateTime < staleTime) {
                    iterator.remove();
                    portals.remove(chunkKey);
                }
            }
        }
    }
}