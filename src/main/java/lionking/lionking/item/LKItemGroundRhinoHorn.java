package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityAgeable;

import lionking.client.LKCreativeTabs;
import lionking.common.LKAngerable;
import lionking.common.LKAchievementList;

public class LKItemGroundRhinoHorn extends LKItem {
    public LKItemGroundRhinoHorn() {
        super();
        setCreativeTab(LKCreativeTabs.TAB_MATERIALS);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity) {
        if (entity.getHealth() <= 0) {
            return false;
        }

        if (!(entity instanceof EntityAnimal)) {
            return false;
        }

        EntityAnimal animal = (EntityAnimal) entity;
        if (entity instanceof LKAngerable || animal.isInLove()) {
            return processBreeding(itemStack, player, animal);
        }

        return false;
    }

    private boolean processBreeding(ItemStack itemStack, EntityPlayer player, EntityAnimal animal) {
        if (itemRand.nextInt(3) == 0) {
            spawnParticles(animal.worldObj, animal, "smoke", 7);
            itemStack.stackSize--;
            return false;
        }
        procreate(animal);
        itemStack.stackSize--;
        player.addStat(LKAchievementList.rhinoHorn, 1);
        return true;
    }

    private void procreate(EntityAnimal animal) {
        World world = animal.worldObj;
        EntityAgeable baby = animal.createChild(animal);
        if (baby != null) {
            baby.setGrowingAge(-24000);
            baby.setLocationAndAngles(animal.posX, animal.posY, animal.posZ, 0.0F, 0.0F);
            if (!world.isRemote) {
                world.spawnEntityInWorld(baby);
            }
            spawnParticles(world, animal, "heart", 7);
        }
    }

    private void spawnParticles(World world, EntityLivingBase entity, String particleType, int count) {
        for (int i = 0; i < count; i++) {
            double dx = itemRand.nextGaussian() * 0.02D;
            double dy = itemRand.nextGaussian() * 0.02D;
            double dz = itemRand.nextGaussian() * 0.02D;
            double x = entity.posX + (itemRand.nextFloat() * entity.width * 2.0F) - entity.width;
            double y = entity.posY + 0.5D + (itemRand.nextFloat() * entity.height);
            double z = entity.posZ + (itemRand.nextFloat() * entity.width * 2.0F) - entity.width;
            world.spawnParticle(particleType, x, y, z, dx, dy, dz);
        }
    }
}
