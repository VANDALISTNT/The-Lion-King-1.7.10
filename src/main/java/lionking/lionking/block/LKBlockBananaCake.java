package lionking.block;

import net.minecraft.block.BlockCake;
import net.minecraft.util.IIcon; 
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.mod_LionKing;

public class LKBlockBananaCake extends BlockCake {
    @SideOnly(Side.CLIENT)
    private IIcon iconBottom; 

    @SideOnly(Side.CLIENT)
    private IIcon iconTop; 

    @SideOnly(Side.CLIENT)
    private IIcon iconSide; 

    @SideOnly(Side.CLIENT)
    private IIcon iconEaten; 

    public LKBlockBananaCake() { 
        super(); 
        setHardness(0.5F); 
        setStepSound(soundTypeCloth); 
        setBlockName("bananaCake"); 
        setBlockTextureName("lionking:bananaCake");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) { 
        if (side == 0) return iconBottom; 
        if (side == 1) return iconTop;    
        if (metadata > 0 && side == 4) return iconEaten; 
        return iconSide; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        iconBottom = iconRegister.registerIcon(getTextureName() + "_bottom");
        iconTop = iconRegister.registerIcon(getTextureName() + "_top");
        iconSide = iconRegister.registerIcon(getTextureName() + "_side");
        iconEaten = iconRegister.registerIcon(getTextureName() + "_eaten");
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        return mod_LionKing.bananaCake; 
    }
}
