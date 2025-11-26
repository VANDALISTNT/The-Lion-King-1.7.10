package lionking.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import java.util.List;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;
import lionking.common.LKIngame;
import lionking.block.LKBlockStarAltar;
import lionking.entity.LKEntityLightning;

public class LKItemSimbaCharm extends LKItem {
    @SideOnly(Side.CLIENT)
    private IIcon[] charmIcons;
    private final String[] charmTypes = {"active", "inactive"};

    public LKItemSimbaCharm() {
        super();
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(LKCreativeTabs.TAB_QUEST); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int i) {
        return charmIcons[Math.min(i, charmTypes.length - 1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister) {
        charmIcons = new IIcon[charmTypes.length];
        for (int i = 0; i < charmTypes.length; i++) {
            charmIcons[i] = iconregister.registerIcon("lionking:charm_" + charmTypes[i]);
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (world.getBlock(i, j, k) != mod_LionKing.starAltar || itemstack.getItemDamage() != 1) {
            return false;
        }

        if (!world.isRemote) {
            --itemstack.stackSize;

            EntityItem item = new EntityItem(world,
                i + 0.25D + itemRand.nextFloat() / 2.0F,
                j,
                k + 0.25D + itemRand.nextFloat() / 2.0F,
                new ItemStack(this, 1, 0));
            item.delayBeforeCanPickup = 10;
            item.motionX = 0.0D;
            item.motionY = 0.4D + itemRand.nextFloat() / 10.0F;
            item.motionZ = 0.0D;
            world.spawnEntityInWorld(item);

            LKEntityLightning bolt = new LKEntityLightning(entityplayer, world, i, j, k, 0);
            world.spawnEntityInWorld(bolt);

            for (int j1 = 0; j1 < 64; j1++) {
                double x = i - 0.5F + world.rand.nextFloat() * 2.0F;
                double y = j - 0.5F + world.rand.nextFloat() * 2.0F;
                double z = k - 0.5F + world.rand.nextFloat() * 2.0F;
                double dx = (world.rand.nextFloat() - 0.5D) * 0.5D;
                double dy = (world.rand.nextFloat() - 0.5D) * 0.5D;
                double dz = (world.rand.nextFloat() - 0.5D) * 0.5D;
                LKIngame.spawnCustomFX(world, 64 + world.rand.nextInt(4), 16, true, x, y, z, dx, dy, dz);
            }
        }
        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        int damage = Math.min(itemstack.getItemDamage(), charmTypes.length - 1);
        return super.getUnlocalizedName() + "." + charmTypes[damage];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int j = 0; j < charmTypes.length; j++) {
            list.add(new ItemStack(this, 1, j));
        }
    }
}