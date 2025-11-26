package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import lionking.client.LKCreativeTabs;

public class LKBlock extends Block {
    public LKBlock(Material material) {
        super(material); 
        setCreativeTab(LKCreativeTabs.tabBlock);
    }
}