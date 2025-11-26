package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.common.LKIngame;
import lionking.common.LKLevelData;
import lionking.quest.LKQuestBase;
import lionking.mod_LionKing;

public class LKBlockPumbaaBox extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] boxIcons;

    public LKBlockPumbaaBox() {
        super(Material.wood);
        setCreativeTab(null);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeWood);
        setBlockName("pumbaaBox");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return side < 2 ? boxIcons[1] : boxIcons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        boxIcons = new IIcon[2];
        boxIcons[0] = iconRegister.registerIcon("lionking:pumbbox_side");
        boxIcons[1] = iconRegister.registerIcon("lionking:pumbbox_top");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            spawnSmokeParticles(world, x, y, z);
            return true;
        }

        if (isExplosiveConditionMet(world, x, y, z)) {
            explode(world, x, y, z);
            return true;
        }

        spawnSmokeParticles(world, x, y, z);
        return true;
    }

    private boolean isExplosiveConditionMet(World world, int x, int y, int z) {
        return LKQuestBase.outlandsQuest.getQuestStage() == 8 &&
               LKLevelData.ziraStage == 18 &&
               world.provider.dimensionId == mod_LionKing.idPrideLands &&
               world.getBlock(x, y - 1, z) == mod_LionKing.rafikiWood &&
               world.getBlockMetadata(x, y - 1, z) == 2 &&
               y == 103 &&
               x > -4 && x < 15 &&
               z > -10 && z < 9;
    }

    private void explode(World world, int x, int y, int z) {
        world.setBlockToAir(x, y, z);
        world.spawnParticle("hugeexplosion", x + 0.5D, y + 2.0D, z + 0.5D, 0.0D, 0.0D, 0.0D);
        world.playSoundEffect(x + 0.5D, y + 2.0D, z + 0.5D, "lionking:flatulence", 4F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        if (!world.isRemote) {
            LKIngame.startFlatulenceExplosion(world);
        }
    }

    private void spawnSmokeParticles(World world, int x, int y, int z) {
        for (int i = 0; i < 8; i++) {
            double motionX = world.rand.nextGaussian() * 0.02D;
            double motionY = world.rand.nextGaussian() * 0.02D;
            double motionZ = world.rand.nextGaussian() * 0.02D;
            double posX = x + ((world.rand.nextFloat() * 2.0F) - 1.0D) * 0.75F + 0.5D;
            double posY = y + 0.9F + world.rand.nextFloat();
            double posZ = z + ((world.rand.nextFloat() * 2.0F) - 1.0D) * 0.75F + 0.5D;
            world.spawnParticle("smoke", posX, posY, posZ, motionX, motionY, motionZ);
        }
    }
}