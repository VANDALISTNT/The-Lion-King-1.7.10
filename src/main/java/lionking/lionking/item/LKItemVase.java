package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import java.util.List;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;
import lionking.common.LKIngame;
import lionking.common.LKCharacterSpeech;
import lionking.entity.LKEntityRafiki;

public class LKItemVase extends LKItem {
    @SideOnly(Side.CLIENT)
    private IIcon[] vaseIcons;
    private static final String[] vaseTypes = {
        "flowerWhite", "flowerBlue", "flowerPurple", "flowerRed", "acacia",
        "rainforest", "mango", "outshroom", "outshroomGlowing", "passion", "banana"
    };

    public LKItemVase() {
        super();
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(LKCreativeTabs.TAB_DECORATIONS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int i) {
        int index = Math.max(0, Math.min(i, vaseTypes.length - 1));
        return vaseIcons[index];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        vaseIcons = new IIcon[vaseTypes.length];
        for (int i = 0; i < vaseTypes.length; ++i) {
            vaseIcons[i] = iconRegister.registerIcon("lionking:vase_" + vaseTypes[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int j = 0; j < vaseTypes.length; ++j) {
            list.add(new ItemStack(item, 1, j));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        int damage = itemstack.getItemDamage();
        int index = Math.max(0, Math.min(damage, vaseTypes.length - 1));
        return "item.vase." + vaseTypes[index];
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        Block block = world.getBlock(i, j, k);
        if (block == Blocks.snow_layer) {
            l = 1;
        } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush &&
                   !block.isReplaceable(world, i, j, k)) {
            switch (l) {
                case 0: --j; break;
                case 1: ++j; break;
                case 2: --k; break;
                case 3: ++k; break;
                case 4: --i; break;
                case 5: ++i; break;
            }
        }

        if (!entityplayer.canPlayerEdit(i, j, k, l, itemstack)) {
            return false;
        }

        Block vaseBlock = mod_LionKing.flowerVase;
        if (world.canPlaceEntityOnSide(vaseBlock, i, j, k, false, l, entityplayer, itemstack)) {
            if (!world.isRemote) {
                world.setBlock(i, j, k, vaseBlock, itemstack.getItemDamage(), 3);
                world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, vaseBlock.stepSound.getBreakSound(),
                    (vaseBlock.stepSound.getVolume() + 1.0F) / 2.0F, vaseBlock.stepSound.getPitch() * 0.8F);
                --itemstack.stackSize;

                List nearbyEntities = world.getEntitiesWithinAABB(LKEntityRafiki.class,
                    entityplayer.boundingBox.expand(16F, 5F, 16F));
                if (!nearbyEntities.isEmpty() && itemstack.getItemDamage() < 4) {
                    LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.FLOWERS));
                    for (Object obj : nearbyEntities) {
                        ((LKEntityRafiki) obj).addHeartFX();
                    }
                }
                return true;
            }
        }
        return false;
    }
}