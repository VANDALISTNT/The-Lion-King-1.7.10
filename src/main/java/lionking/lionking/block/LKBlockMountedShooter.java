package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.init.Blocks;

import lionking.entity.LKEntityBlackDart;
import lionking.entity.LKEntityBlueDart;
import lionking.entity.LKEntityPinkDart;
import lionking.entity.LKEntityRedDart;
import lionking.entity.LKEntityYellowDart;
import lionking.mod_LionKing;
import lionking.tileentity.LKTileEntityMountedShooter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.nio.ByteBuffer;
import java.util.Random;

public class LKBlockMountedShooter extends BlockContainer {
    public LKBlockMountedShooter() { 
        super(Material.circuits);
        setCreativeTab(null);
        setBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 0.75F, 1.0F);
        setHardness(0.5F);
        setStepSound(soundTypeWood);
        setBlockName("mountedShooter"); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return Blocks.planks.getIcon(side, 0); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata % 2 == 0) {
            setBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 0.75F, 1.0F);
        } else {
            setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 0.75F, 0.75F);
        }
    }

    @Override
    public int tickRate(World world) {
        return 4;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) { 
        if (!canBlockStay(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
            return;
        }
        if (neighbor != null && neighbor.canProvidePower()) { 
            if (world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z)) {
                world.scheduleBlockUpdate(x, y, z, this, tickRate(world)); 
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote && (world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z))) {
            tryFireDart(world, x, y, z, random);
        }
    }

    private void tryFireDart(World world, int x, int y, int z, Random random) {
        LKTileEntityMountedShooter shooter = (LKTileEntityMountedShooter) world.getTileEntity(x, y, z);
        if (shooter == null || shooter.dartID == 0 || shooter.dartStackSize <= 0) return; 

        int metadata = world.getBlockMetadata(x, y, z);
        int xVelocity = (metadata == 3 ? -1 : metadata == 1 ? 1 : 0);
        int zVelocity = (metadata == 2 ? 1 : metadata == 0 ? -1 : 0);

        double posX = x + xVelocity * 0.6D + 0.5D;
        double posY = y + 0.5D;
        double posZ = z + zVelocity * 0.6D + 0.5D;

        Item dartItem = Item.getItemById(shooter.dartID);
        if (dartItem == mod_LionKing.dartYellow) {
            LKEntityYellowDart dart = new LKEntityYellowDart(world, posX, posY, posZ);
            dart.setDartHeading(xVelocity * 1.5D, 0.05D, zVelocity * 1.5D, 2.0F, 1.0F);
            world.spawnEntityInWorld(dart);
        } else if (dartItem == mod_LionKing.dartRed) {
            LKEntityRedDart dart = new LKEntityRedDart(world, posX, posY, posZ);
            dart.setDartHeading(xVelocity * 1.5D, 0.05D, zVelocity * 1.5D, 2.0F, 1.0F);
            world.spawnEntityInWorld(dart);
        } else if (dartItem == mod_LionKing.dartBlack) {
            LKEntityBlackDart dart = new LKEntityBlackDart(world, posX, posY, posZ);
            dart.setDartHeading(xVelocity * 1.5D, 0.05D, zVelocity * 1.5D, 2.0F, 1.0F);
            world.spawnEntityInWorld(dart);
        } else if (dartItem == mod_LionKing.dartPink) {
            LKEntityPinkDart dart = new LKEntityPinkDart(world, posX, posY, posZ);
            dart.setDartHeading(xVelocity * 1.5D, 0.05D, zVelocity * 1.5D, 2.0F, 1.0F);
            world.spawnEntityInWorld(dart);
        } else {
            LKEntityBlueDart dart = new LKEntityBlueDart(world, posX, posY, posZ);
            dart.setDartHeading(xVelocity * 1.5D, 0.05D, zVelocity * 1.5D, 2.0F, 1.0F);
            world.spawnEntityInWorld(dart);
        }

        if (getDamageValue(world, x, y, z) == 1) {
        }

        shooter.dartStackSize--;
        if (shooter.dartStackSize == 0) shooter.dartID = 0;
        if (world instanceof WorldServer) updateClientFireCounter((WorldServer) world, x, y, z);
        world.playSoundEffect(posX, posY, posZ, "random.bow", 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + 0.25F);
    }

    private void updateClientFireCounter(WorldServer world, int x, int y, int z) {
        byte[] data = new byte[13];
        ByteBuffer.wrap(data, 0, 4).putInt(x);
        ByteBuffer.wrap(data, 4, 4).putInt(y);
        ByteBuffer.wrap(data, 8, 4).putInt(z);
        data[12] = (byte) -1;
        S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lk.tileEntity", data);
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet); 
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && super.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        LKTileEntityMountedShooter shooter = (LKTileEntityMountedShooter) world.getTileEntity(x, y, z);
        if (shooter == null) return false;

        ItemStack heldItem = player.inventory.getCurrentItem();
        if (shooter.dartID != 0) { 
            if (!world.isRemote) {
                dropBlockAsItem(world, x, y, z, new ItemStack(Item.getItemById(shooter.dartID), shooter.dartStackSize, 0)); 
                shooter.dartID = 0;
                shooter.dartStackSize = 0;
            }
            return true;
        } else if (shooter.dartID == 0 && shooter.dartStackSize == 0 && heldItem != null) {
            Item item = heldItem.getItem(); 
            if (item == mod_LionKing.dartBlue || item == mod_LionKing.dartYellow ||
                item == mod_LionKing.dartRed || item == mod_LionKing.dartBlack ||
                item == mod_LionKing.dartPink) {
                shooter.dartID = Item.getIdFromItem(item);
                shooter.dartStackSize = heldItem.stackSize;
                if (!world.isRemote) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        int direction = MathHelper.floor_double((placer.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, direction, 2);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) { 
        return new LKTileEntityMountedShooter();
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        return mod_LionKing.mountedShooterItem; 
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) { 
        return mod_LionKing.mountedShooterItem; 
    }

    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        return (tile instanceof LKTileEntityMountedShooter) ? ((LKTileEntityMountedShooter) tile).getShooterType() : 0;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float dropChance, int fortune) {
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            world.setBlockMetadataWithNotify(x, y, z, metadata | 8, 4);
        }
        super.onBlockHarvested(world, x, y, z, metadata, player);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) { 
        if (!world.isRemote) {
            dropBlockAsItem(world, x, y, z, new ItemStack(mod_LionKing.mountedShooterItem, 1, getDamageValue(world, x, y, z)));
            LKTileEntityMountedShooter shooter = (LKTileEntityMountedShooter) world.getTileEntity(x, y, z);
            if (shooter != null && shooter.dartID != 0) { 
                dropBlockAsItem(world, x, y, z, new ItemStack(Item.getItemById(shooter.dartID), shooter.dartStackSize, 0)); 
                shooter.dartID = 0;
                shooter.dartStackSize = 0;
            }
        }
        super.breakBlock(world, x, y, z, this, metadata); 
    }
}