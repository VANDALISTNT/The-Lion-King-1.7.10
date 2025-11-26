package lionking.block;

import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;

import lionking.client.LKCreativeTabs;

public class LKBlockPane extends BlockPane {
    public LKBlockPane(String sideTexture, String topTexture, Material material, boolean canDrop) {
        super(sideTexture, topTexture, material, canDrop);
        setCreativeTab(LKCreativeTabs.tabDecorations);
        setBlockName("lkPane");
    }
}