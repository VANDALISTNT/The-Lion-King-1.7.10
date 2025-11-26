package lionking.item;

import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Facing;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import java.util.List;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

import lionking.client.LKCreativeTabs;
import lionking.entity.LKEntities;
import lionking.entity.LKEntitySimba;
import lionking.entity.LKEntityEggInfo;

public class LKItemSpawnEgg extends LKItem {
    public LKItemSpawnEgg() {
        super();
        setHasSubtypes(true);
        setCreativeTab(LKCreativeTabs.TAB_MISC);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        String baseName = "item.spawnEgg";
        String entityName = LKEntities.getStringFromID(itemstack.getItemDamage());
        return entityName != null ? baseName + "." + entityName : baseName;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int renderPass) {
        LKEntityEggInfo info = (LKEntityEggInfo) LKEntities.creatures.get(itemstack.getItemDamage());
        if (info != null) {
            return (renderPass == 0) ? info.primaryColor : info.secondaryColor;
        }
        return (renderPass == 0) ? 0xFFFFFF : 0x000000;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        }

        Block block = world.getBlock(i, j, k);
        i += Facing.offsetsXForSide[l];
        j += Facing.offsetsYForSide[l];
        k += Facing.offsetsZForSide[l];
        double yOffset = (l == 1 && block != null && block.getRenderType() == 11) ? 0.5D : 0.0D;

        Entity entity = spawnCreature(world, itemstack.getItemDamage(), i + 0.5D, j + yOffset, k + 0.5D);
        if (entity != null) {
            if (entity instanceof EntityLiving && itemstack.hasDisplayName()) {
                ((EntityLiving) entity).setCustomNameTag(itemstack.getDisplayName());
            }
            if (entity instanceof LKEntitySimba) {
                ((LKEntitySimba) entity).setOwnerName(entityplayer.getCommandSenderName());
            }
            if (!entityplayer.capabilities.isCreativeMode) {
                --itemstack.stackSize;
            }
        }
        return true;
    }

    private Entity spawnCreature(World world, int id, double x, double y, double z) {
        if (!LKEntities.creatures.containsKey(id)) {
            return null;
        }

        Entity entity = LKEntities.createEntityByID(id, world);
        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;
            entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
            entityliving.rotationYawHead = entityliving.rotationYaw;
            entityliving.renderYawOffset = entityliving.rotationYaw;
            entityliving.onSpawnWithEgg(null);
            world.spawnEntityInWorld(entity);
            entityliving.playLivingSound();
        }
        return entity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
        return Items.spawn_egg.getIconFromDamageForRenderPass(damage, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (Object obj : LKEntities.creatures.values()) {
            if (obj instanceof LKEntityEggInfo) {
                LKEntityEggInfo info = (LKEntityEggInfo) obj;
                list.add(new ItemStack(item, 1, info.spawnedID));
            }
        }
    }
}
