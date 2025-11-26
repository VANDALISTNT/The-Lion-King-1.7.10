package lionking.item;

import net.minecraftforge.common.IShearable;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import com.google.common.collect.Multimap;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;
import lionking.common.LKIngame;
import lionking.biome.LKBiomeGenAridSavannah;
import lionking.biome.LKBiomeGenDesert;
import lionking.biome.LKBiomeGenRainforest;
import lionking.biome.LKBiomeGenUpendi;
import lionking.biome.LKBiomeGenMountains;
import lionking.biome.LKBiomeGenSavannahBase;
import lionking.biome.LKBiomeGenRiver;
import lionking.entity.LKEntityLightning;
import lionking.entity.LKEntityScar;
import lionking.block.LKBlockLeaves;
import lionking.block.LKBlockSapling;
import lionking.block.LKBlockAridGrass;
import lionking.block.LKBlockFlowerBase;
import lionking.block.LKBlockFlowerTop;
import lionking.block.LKBlockKiwanoStem;

public class LKItemRafikiStick extends LKItem {
    private int lastThunderUseTick;
    private static final UUID itemModifierUUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF"); // Standard UUID for weapon damage modifiers
    @SideOnly(Side.CLIENT)
    private IIcon[] thunderIcons;

    public LKItemRafikiStick() {
        super();
        setMaxStackSize(1);
        setMaxDamage(850);
        setNoRepair();
        lastThunderUseTick = 0;
        setCreativeTab(LKCreativeTabs.TAB_QUEST);
        setFull3D();
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (world.isRemote || !LKIngame.isLKWorld(world.provider.dimensionId) || !entityplayer.canPlayerEdit(i, j, k, l, itemstack)) {
            return false;
        }

        Block block = world.getBlock(i, j, k);
        int blockMeta = world.getBlockMetadata(i, j, k);

        if (block == mod_LionKing.sapling || block == mod_LionKing.forestSapling) {
            damageRafikiStick(itemstack, 4, entityplayer);
            ((LKBlockSapling) block).growTree(world, i, j, k, world.rand);
            return true;
        } else if (block == mod_LionKing.mangoSapling || block == mod_LionKing.bananaSapling) {
            damageRafikiStick(itemstack, 8, entityplayer);
            ((LKBlockSapling) block).growTree(world, i, j, k, world.rand);
            return true;
        } else if (block == mod_LionKing.passionSapling) {
            damageRafikiStick(itemstack, 10, entityplayer);
            ((LKBlockSapling) block).growTree(world, i, j, k, world.rand);
            return true;
        }

        if ((block == Blocks.wheat || block == mod_LionKing.yamCrops || block == mod_LionKing.kiwanoStem)
                && blockMeta < 7) {
            world.setBlockMetadataWithNotify(i, j, k, 7, 3);
            damageRafikiStick(itemstack, 6, entityplayer);
            return true;
        }

        if (block == Blocks.grass) {
            BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
            generateVegetationOnGrass(world, i, j, k, biome);
            damageRafikiStick(itemstack, 3, entityplayer);
            return true;
        }

        if (block == Blocks.sand && (world.getBiomeGenForCoords(i, k) instanceof LKBiomeGenAridSavannah
                || world.getBiomeGenForCoords(i, k) instanceof LKBiomeGenDesert)) {
            generateVegetationOnSand(world, i, j, k);
            damageRafikiStick(itemstack, 2, entityplayer);
            return true;
        }

        return false;
    }

    private void generateVegetationOnGrass(World world, int i, int j, int k, BiomeGenBase biome) {
        world.notifyBlocksOfNeighborChange(i, j, k, world.getBlock(i, j, k));
        outer:
        for (int l1 = 0; l1 < 128; l1++) {
            int x = i, y = j + 1, z = k;
            for (int l2 = 0; l2 < l1 / 16; l2++) {
                x += itemRand.nextInt(3) - 1;
                y += (itemRand.nextInt(3) - 1) * itemRand.nextInt(3) / 2;
                z += itemRand.nextInt(3) - 1;
                if (world.getBlock(x, y - 1, z) != Blocks.grass || world.getBlock(x, y, z).getMaterial().isSolid()) {
                    continue outer;
                }
            }
            if (world.isAirBlock(x, y, z)) {
                if (itemRand.nextInt(7) != 0 && Blocks.tallgrass.canPlaceBlockAt(world, x, y, z)) {
                    int meta = (biome instanceof LKBiomeGenRainforest || biome instanceof LKBiomeGenUpendi) && itemRand.nextInt(5) == 0 ? 2 : 1;
                    world.setBlock(x, y, z, Blocks.tallgrass, meta, 3);
                } else {
                    placeBiomeSpecificFlowers(world, x, y, z, biome);
                }
            }
        }
    }

    private void generateVegetationOnSand(World world, int i, int j, int k) {
        world.notifyBlocksOfNeighborChange(i, j, k, world.getBlock(i, j, k));
        outer:
        for (int l1 = 0; l1 < 128; l1++) {
            int x = i, y = j + 1, z = k;
            for (int l2 = 0; l2 < l1 / 16; l2++) {
                x += itemRand.nextInt(3) - 1;
                y += (itemRand.nextInt(3) - 1) * itemRand.nextInt(3) / 2;
                z += itemRand.nextInt(3) - 1;
                if (world.getBlock(x, y - 1, z) != Blocks.sand || world.getBlock(x, y, z).getMaterial().isSolid()) {
                    continue outer;
                }
            }
            if (world.isAirBlock(x, y, z)) {
                if (world.rand.nextInt(16) == 0 && Blocks.deadbush.canPlaceBlockAt(world, x, y, z)) {
                    world.setBlock(x, y, z, Blocks.deadbush, 0, 3);
                } else if (mod_LionKing.aridGrass.canPlaceBlockAt(world, x, y, z)) {
                    world.setBlock(x, y, z, mod_LionKing.aridGrass, 0, 3);
                }
            }
        }
    }

    private void placeBiomeSpecificFlowers(World world, int x, int y, int z, BiomeGenBase biome) {
        if (biome instanceof LKBiomeGenUpendi) {
            int chance = itemRand.nextInt(8);
            if (chance == 0 && world.isAirBlock(x, y + 1, z) && mod_LionKing.flowerBase.canPlaceBlockAt(world, x, y, z)) {
                world.setBlock(x, y, z, mod_LionKing.flowerBase, 1, 3);
                world.setBlock(x, y + 1, z, mod_LionKing.flowerTop, 1, 3);
            } else if (chance == 1 && mod_LionKing.whiteFlower.canPlaceBlockAt(world, x, y, z)) {
                world.setBlock(x, y, z, mod_LionKing.whiteFlower, 0, 3);
            } else if (chance > 5 && world.isAirBlock(x, y + 1, z) && mod_LionKing.flowerBase.canPlaceBlockAt(world, x, y, z)) {
                world.setBlock(x, y, z, mod_LionKing.flowerBase, 0, 3);
                world.setBlock(x, y + 1, z, mod_LionKing.flowerTop, 0, 3);
            }
        } else if (biome instanceof LKBiomeGenMountains && mod_LionKing.blueFlower.canPlaceBlockAt(world, x, y, z)) {
            world.setBlock(x, y, z, mod_LionKing.blueFlower, 0, 3);
        } else if (biome instanceof LKBiomeGenRainforest || biome instanceof LKBiomeGenSavannahBase
                || biome instanceof LKBiomeGenRiver || biome instanceof LKBiomeGenAridSavannah) {
            int chance = itemRand.nextInt(5);
            if (chance < (biome instanceof LKBiomeGenRainforest ? 2 : 1) && world.isAirBlock(x, y + 1, z)
                    && mod_LionKing.flowerBase.canPlaceBlockAt(world, x, y, z)) {
                world.setBlock(x, y, z, mod_LionKing.flowerBase, 0, 3);
                world.setBlock(x, y + 1, z, mod_LionKing.flowerTop, 0, 3);
            } else if (chance == (biome instanceof LKBiomeGenRainforest ? 2 : 1) && mod_LionKing.whiteFlower.canPlaceBlockAt(world, x, y, z)) {
                world.setBlock(x, y, z, mod_LionKing.whiteFlower, 0, 3);
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack);
        if (level > 0 && lastThunderUseTick == 0) {
            MovingObjectPosition m = entityplayer.rayTrace(5, 1F);
            if (m == null) {
                entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
            }
        }
        return itemstack;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int ticksLeft) {
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack);
        if (level <= 0) return;

        MovingObjectPosition target = entityplayer.rayTrace(2 + Math.pow(4, level + 1), 1F);
        MovingObjectPosition close = entityplayer.rayTrace(5, 1F);
        if (target != null && target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && close == null) {
            int x = target.blockX, y = target.blockY, z = target.blockZ;
            if (world.getBlock(x, y, z).isOpaqueCube() && world.isAirBlock(x, y + 1, z)) {
                LKEntityLightning bolt = new LKEntityLightning(entityplayer, world, x, y, z, level);
                if (!world.isRemote) {
                    world.spawnEntityInWorld(bolt);
                    damageRafikiStick(itemstack, 10, entityplayer);
                    lastThunderUseTick = 12;
                }
                return;
            }
        }

        for (int i = 0; i < 7; i++) {
            double dx = itemRand.nextGaussian() * 0.02D;
            double dy = itemRand.nextGaussian() * 0.02D;
            double dz = itemRand.nextGaussian() * 0.02D;
            world.spawnParticle("smoke", entityplayer.posX + (itemRand.nextFloat() * entityplayer.width * 2F) - entityplayer.width,
                    entityplayer.posY - 0.5D + (itemRand.nextFloat() * (entityplayer.height / 2)),
                    entityplayer.posZ + (itemRand.nextFloat() * entityplayer.width * 2F) - entityplayer.width, dx, dy, dz);
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 72000;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack itemstack, int renderPass, EntityPlayer entityplayer, ItemStack usingItem, int useRemaining) {
        if (lastThunderUseTick > 0) lastThunderUseTick--;
        return (usingItem == itemstack) ? thunderIcons[itemRand.nextInt(6)] : itemIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister) {
        super.registerIcons(iconregister);
        thunderIcons = new IIcon[6];
        for (int i = 0; i < 6; i++) {
            thunderIcons[i] = iconregister.registerIcon("lionking:rafikiStick_glow_" + i);
        }
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!(target instanceof LKEntityScar)) {
            damageRafikiStick(itemstack, 2, attacker);
        }
        return true;
    }

    @Override
    public Multimap getItemAttributeModifiers() {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
                new AttributeModifier(itemModifierUUID, "Weapon modifier", 5D, 0));
        return multimap;
    }

    @Override
    public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
        if (block instanceof LKBlockLeaves || block instanceof BlockTallGrass || block instanceof BlockDeadBush
                || block instanceof LKBlockAridGrass) {
            return 15F;
        }
        return super.getDigSpeed(itemstack, block, metadata);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if (block instanceof LKBlockLeaves) {
            damageRafikiStick(itemstack, 1, entity);
            return true;
        }
        return super.onBlockDestroyed(itemstack, world, block, x, y, z, entity);
    }

    private void damageRafikiStick(ItemStack itemstack, int damage, EntityLivingBase entity) {
        if (!itemstack.isItemStackDamageable()) return;
        if (damage > 0 && entity instanceof EntityPlayer) {
            int durabilityLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemstack);
            if (durabilityLevel > 0 && entity.worldObj.rand.nextInt(durabilityLevel + 1) > 0) {
                return;
            }
        }
        int currentDamage = itemstack.getItemDamage();
        itemstack.setItemDamage(currentDamage + damage);
        if (itemstack.getItemDamage() > itemstack.getMaxDamage()) {
            entity.renderBrokenItemStack(itemstack);
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).addStat(StatList.objectBreakStats[Item.getIdFromItem(itemstack.getItem())], 1);
            }
            itemstack.stackSize--;
            if (itemstack.stackSize < 0) itemstack.stackSize = 0;
            itemstack.setItemDamage(0);
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player) {
        if (player.worldObj.isRemote) return false;

        Block block = player.worldObj.getBlock(x, y, z);
        if (block instanceof IShearable && ((IShearable) block).isShearable(itemstack, player.worldObj, x, y, z)) {
            ArrayList<ItemStack> drops = ((IShearable) block).onSheared(itemstack, player.worldObj, x, y, z,
                    EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
            dropItems(player.worldObj, x, y, z, drops, player);
            damageRafikiStick(itemstack, 1, player);
            player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
        } else if (block == Blocks.deadbush) {
            ItemStack drop = new ItemStack(Blocks.deadbush);
            dropItems(player.worldObj, x, y, z, java.util.Collections.singletonList(drop), player);
            damageRafikiStick(itemstack, 1, player);
            player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
        }
        return false;
    }

    private void dropItems(World world, int x, int y, int z, List<ItemStack> drops, EntityPlayer player) {
        float f = 0.7F;
        for (ItemStack stack : drops) {
            double dx = player.getRNG().nextFloat() * f + (1.0F - f) * 0.5D;
            double dy = player.getRNG().nextFloat() * f + (1.0F - f) * 0.5D;
            double dz = player.getRNG().nextFloat() * f + (1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, x + dx, y + dy, z + dz, stack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }
}