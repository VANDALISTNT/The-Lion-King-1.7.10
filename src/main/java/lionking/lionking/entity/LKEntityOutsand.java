package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import lionking.mod_LionKing;

public class LKEntityOutsand extends Entity {
    public Block block;
    public int fallTime;

    public LKEntityOutsand(World world) {
        super(world);
        fallTime = 0;
    }

    public LKEntityOutsand(World world, double d, double d1, double d2, Block block) {
        super(world);
        fallTime = 0;
        this.block = block;
        preventEntitySpawning = true;
        setSize(0.98F, 0.98F);
        yOffset = height / 2.0F;
        setPosition(d, d1, d2);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = d;
        prevPosY = d1;
        prevPosZ = d2;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isDead;
    }

    @Override
    public void onUpdate() {
        if (block == null) {
            setDead();
            return;
        }
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        fallTime++;
        motionY -= 0.039999999105930328D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.98000001907348633D;
        motionY *= 0.98000001907348633D;
        motionZ *= 0.98000001907348633D;
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posY);
        int k = MathHelper.floor_double(posZ);

        if (fallTime == 1 && worldObj.getBlock(i, j, k) == block) {
            worldObj.setBlockToAir(i, j, k);
        } else if (!worldObj.isRemote && fallTime == 1) {
            setDead();
        }

        if (onGround) {
            motionX *= 0.69999998807907104D;
            motionZ *= 0.69999998807907104D;
            motionY *= -0.5D;
            if (worldObj.getBlock(i, j, k) != Blocks.piston_extension) {
                setDead();
                if (!canPlaceBlockAt(worldObj, i, j, k, block) && !worldObj.isRemote) {
                    dropItem(Item.getItemFromBlock(block), 1);
                }
            }
        } else if (fallTime > 100 && !worldObj.isRemote) {
            dropItem(Item.getItemFromBlock(block), 1);
            setDead();
        }
    }

    private boolean canPlaceBlockAt(World world, int x, int y, int z, Block block) {
        if (!world.isAirBlock(x, y - 1, z) && !isReplaceable(world, x, y - 1, z)) {
            return world.setBlock(x, y, z, block, 0, 3);
        }
        return false;
    }

    private boolean isReplaceable(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == Blocks.air || block.getMaterial().isLiquid() || block.getMaterial().isReplaceable();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("TileID", Block.getIdFromBlock(block));
        nbt.setInteger("Time", fallTime);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        block = Block.getBlockById(nbt.getInteger("TileID"));
        fallTime = nbt.getInteger("Time");
        if (block == null) {
            block = mod_LionKing.outsand;
        }
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    public World getWorld() {
        return worldObj;
    }
}