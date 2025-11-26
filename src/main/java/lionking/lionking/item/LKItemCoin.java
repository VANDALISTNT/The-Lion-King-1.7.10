package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.client.LKCreativeTabs; 
import lionking.mod_LionKing; 
import lionking.entity.LKEntityCoin; 

public class LKItemCoin extends LKItem {
    private final byte type;
    private static final int MAX_STACK_SIZE = 16;

    public LKItemCoin(byte type) {
        super(); 
        this.type = type;
        setMaxStackSize(MAX_STACK_SIZE);
        setCreativeTab(LKCreativeTabs.TAB_QUEST); 
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!world.isRemote && canUseCoin(player.dimension)) {
            spawnCoin(world, player);
            playCoinSound(world, player);
            if (!player.capabilities.isCreativeMode) {
                itemStack.stackSize--;
            }
        }
        return itemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.uncommon;
    }

    private boolean canUseCoin(int dimension) {
        return (type == 0 && dimension == mod_LionKing.idPrideLands) ||
               (type == 1 && dimension == mod_LionKing.idOutlands);
    }

    private void spawnCoin(World world, EntityPlayer player) {
        LKEntityCoin coin = new LKEntityCoin(world, player, type);
        world.spawnEntityInWorld(coin);
    }

    private void playCoinSound(World world, EntityPlayer player) {
        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    }
}