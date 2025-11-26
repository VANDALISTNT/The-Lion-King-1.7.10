package lionking.block;

import net.minecraft.block.BlockBed;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon; 
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.mod_LionKing;

public class LKBlockBed extends BlockBed {
    @SideOnly(Side.CLIENT)
    private IIcon[] bedEndIcons; 

    @SideOnly(Side.CLIENT)
    private IIcon[] bedSideIcons; 

    @SideOnly(Side.CLIENT)
    private IIcon[] bedTopIcons; 

    public LKBlockBed() { 
        super(); 
        setHardness(0.2F); 
        setStepSound(soundTypeWood); 
        setBlockName("lionBed"); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) { 
        if (side == 0) {
            return mod_LionKing.prideWood.getIcon(2, 0); 
        }

        int direction = getDirection(metadata);
        int bedSide = Direction.bedDirection[direction][side];
        int headIndex = isBlockHeadOfBed(metadata) ? 1 : 0;

        if ((headIndex == 1 && bedSide == 2) || (headIndex == 0 && bedSide == 3)) {
            return bedEndIcons[headIndex]; 
        }
        return (bedSide == 5 || bedSide == 4) ? bedSideIcons[headIndex] : bedTopIcons[headIndex]; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        bedTopIcons = new IIcon[] {
            iconRegister.registerIcon("lionking:bed_feet_top"),
            iconRegister.registerIcon("lionking:bed_head_top")
        };
        bedEndIcons = new IIcon[] {
            iconRegister.registerIcon("lionking:bed_feet_end"),
            iconRegister.registerIcon("lionking:bed_head_end")
        };
        bedSideIcons = new IIcon[] {
            iconRegister.registerIcon("lionking:bed_feet_side"),
            iconRegister.registerIcon("lionking:bed_head_side")
        };
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        return isBlockHeadOfBed(metadata) ? null : Item.getItemFromBlock(mod_LionKing.blockBed); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) { 
        return Item.getItemFromBlock(mod_LionKing.blockBed); 
    }

    public boolean isBed(World world, int x, int y, int z, EntityLivingBase player) {
        return true; 
    }
}