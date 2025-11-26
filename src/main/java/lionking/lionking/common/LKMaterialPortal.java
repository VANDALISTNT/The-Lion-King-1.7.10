package lionking.common;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class LKMaterialPortal extends Material {
    public static final Material PORTAL_MATERIAL = new Material(MapColor.emeraldColor) {
        @Override
        public boolean isSolid() {
            return false;
        }

        @Override
        public boolean getCanBlockGrass() {
            return false;
        }

        @Override
        public boolean blocksMovement() {
            return false;
        }

        @Override
        public boolean isOpaque() {
            return false;
        }
    };

    public LKMaterialPortal(MapColor color) {
        super(color);
    }
}