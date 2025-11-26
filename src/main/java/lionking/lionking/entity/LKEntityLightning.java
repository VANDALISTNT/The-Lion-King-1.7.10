package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import lionking.common.LKCharacter;

import java.util.List;

public class LKEntityLightning extends EntityWeatherEffect {
    private int lightningState;
    public long boltVertex = 0L;
    private int boltLivingTime;
    private int power;
    private EntityPlayer thePlayer;

    public LKEntityLightning(EntityPlayer player, World world, double d, double d1, double d2, int level) {
        super(world);
        thePlayer = player;
        power = level;
        setLocationAndAngles(d, d1, d2, 0.0F, 0.0F);
        lightningState = 2;
        boltVertex = world.rand.nextLong();
        boltLivingTime = world.rand.nextInt(3) + 1;
        if (power > 0) {
            int x = MathHelper.floor_double(d);
            int y = MathHelper.floor_double(d1);
            int z = MathHelper.floor_double(d2);
            if (world.getBlock(x, y, z) == Blocks.air && Blocks.fire.canPlaceBlockAt(world, x, y, z)) {
                world.setBlock(x, y, z, Blocks.fire, 0, 3);
            }
            for (int i = 0; i < power * 3; ++i) {
                int xRand = x + world.rand.nextInt(3) - 1;
                int yRand = y + world.rand.nextInt(3) - 1;
                int zRand = z + world.rand.nextInt(3) - 1;
                if (world.getBlock(xRand, yRand, zRand) == Blocks.air && Blocks.fire.canPlaceBlockAt(world, xRand, yRand, zRand)) {
                    world.setBlock(xRand, yRand, zRand, Blocks.fire, 0, 3);
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (lightningState == 2) {
            worldObj.playSoundEffect(posX, posY, posZ, "ambient.weather.thunder", 10000.0F, 0.8F + rand.nextFloat() * 0.2F);
            worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 2.0F, 0.5F + rand.nextFloat() * 0.2F);
        }

        --lightningState;

        if (lightningState < 0) {
            if (boltLivingTime == 0) {
                setDead();
            } else if (lightningState < -rand.nextInt(10)) {
                --boltLivingTime;
                lightningState = 1;
                boltVertex = rand.nextLong();
            }
        }

        if (lightningState >= 0) {
            if (power > 0) {
                double radius = 3.0D;
                AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(
                    posX - radius, posY - radius, posZ - radius,
                    posX + radius, posY + 6.0D + radius, posZ + radius
                );
                List<Entity> entities = worldObj.getEntitiesWithinAABBExcludingEntity(this, aabb);
                for (Entity entity : entities) {
                    if ((thePlayer != null && entity == thePlayer) || entity instanceof LKCharacter || entity.isImmuneToFire()) {
                        continue;
                    }
                    entity.attackEntityFrom(thePlayer != null ? DamageSource.causePlayerDamage(thePlayer) : DamageSource.inFire, (power * 3) + (rand.nextInt(3) * 2));
                    entity.setFire(power + rand.nextInt(4));
                }
            }
            worldObj.lastLightningBolt = 2;
        }
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return lightningState >= 0;
    }
}