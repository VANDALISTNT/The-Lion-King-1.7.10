package lionking.block;

     import net.minecraft.block.BlockLeavesBase;
     import net.minecraft.block.material.Material;
     import net.minecraft.entity.EnumCreatureType;
     import net.minecraft.util.IIcon;
     import net.minecraft.client.renderer.texture.IIconRegister;
     import net.minecraft.world.IBlockAccess;
     import net.minecraft.world.World;
     import cpw.mods.fml.relauncher.Side;
     import cpw.mods.fml.relauncher.SideOnly;
     import lionking.common.LKIngame;
     import lionking.common.LKLevelData;
     import lionking.client.LKCreativeTabs;

     import java.util.Random;

     public class LKBlockRafikiLeaves extends LKBlockLeaves {
         private int leafMode;
         private boolean isFancyGraphics;
         private final Random textureRand = new Random();

         @SideOnly(Side.CLIENT)
         private IIcon[] leafIcons;

         @SideOnly(Side.CLIENT)
         private IIcon[] corruptLeafIcons;

         @SideOnly(Side.CLIENT)
         private IIcon[][] christmasLeafIcons;

         private static final String[] LEAF_MODES = {"fast", "fancy"};

         public LKBlockRafikiLeaves() {
             super(); 
             setCreativeTab(LKCreativeTabs.tabDecorations);
             setHardness(0.2F);
             setResistance(1.0F);
             setStepSound(soundTypeGrass);
             setLightOpacity(1);
             setBlockName("rafikiLeaves");
             isFancyGraphics = false;
             leafMode = 0;
         }

         @Override
         public boolean isOpaqueCube() {
             return !isFancyGraphics;
         }

         @Override
         @SideOnly(Side.CLIENT)
         public void registerBlockIcons(IIconRegister iconRegister) {
             leafIcons = new IIcon[LEAF_MODES.length];
             corruptLeafIcons = new IIcon[LEAF_MODES.length];
             christmasLeafIcons = new IIcon[LEAF_MODES.length][4];
             for (int i = 0; i < LEAF_MODES.length; i++) {
                 leafIcons[i] = iconRegister.registerIcon("lionking:rafikiLeaves_" + LEAF_MODES[i]);
                 corruptLeafIcons[i] = iconRegister.registerIcon("lionking:rafikiLeaves_" + LEAF_MODES[i] + "_corrupt");
                 for (int j = 0; j < 4; j++) {
                     christmasLeafIcons[i][j] = iconRegister.registerIcon("lionking:rafikiLeaves_" + LEAF_MODES[i] + "_christmas_" + j);
                 }
             }
         }

         @Override
         @SideOnly(Side.CLIENT)
         public IIcon getIcon(int side, int metadata) {
             boolean isZiraOccupied = LKLevelData.ziraStage >= 14 && LKLevelData.ziraStage < 19;
             return isZiraOccupied ? corruptLeafIcons[leafMode] : leafIcons[leafMode];
         }

         @Override
         @SideOnly(Side.CLIENT)
         public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
             boolean isZiraOccupied = LKLevelData.ziraStage >= 14 && LKLevelData.ziraStage < 19;
             if (isZiraOccupied) {
                 return corruptLeafIcons[leafMode];
             }
             if (LKIngame.isChristmas() && side > 1) {
                 textureRand.setSeed((long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y ^ (long)side);
                 int variant = textureRand.nextInt(10);
                 if (variant < 4) {
                     return christmasLeafIcons[leafMode][variant];
                 }
             }
             return leafIcons[leafMode];
         }

         @Override
         public int getLightValue(IBlockAccess world, int x, int y, int z) {
             return LKIngame.isChristmas() ? 13 : 0;
         }

         @Override
         @SideOnly(Side.CLIENT)
         public void randomDisplayTick(World world, int x, int y, int z, Random random) {
             if (world.canLightningStrikeAt(x, y + 1, z) && !world.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && random.nextInt(15) == 1) {
                 double posX = x + random.nextFloat();
                 double posY = y - 0.05D;
                 double posZ = z + random.nextFloat();
                 world.spawnParticle("dripWater", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
             }

             if (LKIngame.isChristmas() && random.nextInt(4) == 0) {
                 double posX = x - 0.5F + (2F * random.nextFloat());
                 double posY = y + 0.5D;
                 double posZ = z - 0.5F + (2F * random.nextFloat());
                 world.spawnParticle("snowshovel", posX, posY, posZ, 0D, -0.05D, 0D);
             }
         }

         @Override
         public int getMobilityFlag() {
             return 2;
         }

         @Override
         public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
             return false;
         }

         public void setFancyGraphics(boolean isFancy) {
             this.isFancyGraphics = isFancy;
             this.leafMode = isFancy ? 1 : 0;
         }
     }