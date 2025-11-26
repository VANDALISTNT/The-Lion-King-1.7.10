package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.quest.LKQuestBase;
import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

public class LKBlockPortalFrame extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon normalIcon;

    @SideOnly(Side.CLIENT)
    private IIcon gateIcon;

    public LKBlockPortalFrame() {
        super(Material.rock);
        setBlockUnbreakable();
        setResistance(6000000F);
        setCreativeTab(LKCreativeTabs.tabBlock);
        setStepSound(soundTypeStone);
        setBlockName("portalFrame");
    }

    @Override
    public int getMobilityFlag() {
        return 2;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (this == mod_LionKing.outlandsPortalFrame) {
            if (metadata == 1) {
                return mod_LionKing.termite.getIcon(0, 0);
            }
            if (metadata == 2) {
                return gateIcon;
            }
        }
        return normalIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        normalIcon = iconRegister.registerIcon("lionking:portalFrame");
        gateIcon = iconRegister.registerIcon("lionking:ziraMoundGate");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }

        ItemStack heldItem = player.inventory.getCurrentItem();
        if (isActivationConditionMet(world, x, y, z, heldItem)) {
            activatePortalFrame(world, x, y, z, player, side, hitX, hitY, hitZ);
            return true;
        }
        return false;
    }

    private boolean isActivationConditionMet(World world, int x, int y, int z, ItemStack heldItem) {
        return LKQuestBase.outlandsQuest.canStart() &&
               heldItem != null &&
               heldItem.getItem() == mod_LionKing.rafikiStick &&
               this == mod_LionKing.outlandsPortalFrame &&
               world.getBlockMetadata(x, y, z) == 2;
    }

    private void activatePortalFrame(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (metadata << 12));
        world.setBlockToAir(x, y, z);

        int[][] directions = {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};
        for (int[] dir : directions) {
            int adjX = x + dir[0];
            int adjY = y + dir[1];
            int adjZ = z + dir[2];
            if (world.getBlock(adjX, adjY, adjZ) == this && world.getBlockMetadata(adjX, adjY, adjZ) == 2) {
                onBlockActivated(world, adjX, adjY, adjZ, player, side, hitX, hitY, hitZ);
            }
        }

        LKQuestBase.outlandsQuest.setDelayed(true);
        LKQuestBase.outlandsQuest.progress(1);
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }
}