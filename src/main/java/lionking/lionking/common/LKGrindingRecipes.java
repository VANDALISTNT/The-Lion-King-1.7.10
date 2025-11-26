package lionking.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks; 
import java.util.HashMap;
import java.util.Map;
import lionking.mod_LionKing;

public class LKGrindingRecipes {
    private static final LKGrindingRecipes INSTANCE = new LKGrindingRecipes();
    private final Map<Integer, ItemStack> grindingList = new HashMap<>();
    private final Map<GrindingKey, ItemStack> metaGrindingList = new HashMap<>();

    public static LKGrindingRecipes getInstance() {
        return INSTANCE;
    }

    private LKGrindingRecipes() {
        addGrinding(Item.getIdFromItem(mod_LionKing.hyenaBone), new ItemStack(mod_LionKing.hyenaBoneShard));
        addGrinding(Item.getIdFromItem(mod_LionKing.mango), new ItemStack(mod_LionKing.mangoDust));
        addGrinding(Item.getIdFromItem(mod_LionKing.itemTermite), new ItemStack(mod_LionKing.termiteDust));
        addGrinding(Item.getIdFromItem(mod_LionKing.horn), new ItemStack(mod_LionKing.hornGround));
        addGrinding(Block.getIdFromBlock(mod_LionKing.whiteFlower), new ItemStack(mod_LionKing.rugDye, 1, 0));
        addGrinding(Item.getIdFromItem(mod_LionKing.featherBlue), new ItemStack(mod_LionKing.rugDye, 1, 1));
        addGrinding(Item.getIdFromItem(mod_LionKing.featherYellow), new ItemStack(mod_LionKing.rugDye, 1, 2));
        addGrinding(Item.getIdFromItem(mod_LionKing.featherRed), new ItemStack(mod_LionKing.rugDye, 1, 3));
        addGrinding(Item.getIdFromItem(mod_LionKing.purpleFlower), new ItemStack(mod_LionKing.rugDye, 1, 4));
        addGrinding(Block.getIdFromBlock(mod_LionKing.blueFlower), new ItemStack(mod_LionKing.rugDye, 1, 5));
        addGrinding(Block.getIdFromBlock(mod_LionKing.leaves), new ItemStack(mod_LionKing.rugDye, 1, 6));
        addGrinding(Block.getIdFromBlock(mod_LionKing.forestLeaves), new ItemStack(mod_LionKing.rugDye, 1, 6));
        addGrinding(Block.getIdFromBlock(mod_LionKing.mangoLeaves), new ItemStack(mod_LionKing.rugDye, 1, 6));
        addGrinding(Block.getIdFromBlock(mod_LionKing.bananaLeaves), new ItemStack(mod_LionKing.rugDye, 1, 6));
        addGrinding(Item.getIdFromItem(mod_LionKing.redFlower), new ItemStack(mod_LionKing.rugDye, 1, 3));
        addGrinding(Item.getIdFromItem(mod_LionKing.featherBlack), new ItemStack(mod_LionKing.rugDye, 1, 7));
        addGrinding(Item.getIdFromItem(mod_LionKing.nukaShard), new ItemStack(mod_LionKing.poison));
        addGrinding(Block.getIdFromBlock(mod_LionKing.pridestone), new ItemStack(Blocks.sand)); // Исправлено
        addGrinding(Item.getIdFromItem(mod_LionKing.featherPink), new ItemStack(mod_LionKing.rugDye, 1, 9));
        addGrinding(Block.getIdFromBlock(mod_LionKing.passionLeaves), new ItemStack(mod_LionKing.rugDye, 1, 10));

        addMetaGrinding(Block.getIdFromBlock(mod_LionKing.lily), 0, new ItemStack(mod_LionKing.rugDye, 1, 0));
        addMetaGrinding(Block.getIdFromBlock(mod_LionKing.lily), 1, new ItemStack(mod_LionKing.rugDye, 1, 8));
        addMetaGrinding(Block.getIdFromBlock(mod_LionKing.lily), 2, new ItemStack(mod_LionKing.rugDye, 1, 3));

        for (int meta = 0; meta <= 2; meta++) {
            addMetaGrinding(Block.getIdFromBlock(mod_LionKing.pridePillar), meta, new ItemStack(mod_LionKing.pridePillar, 1, meta + 1));
        }
        for (int meta = 4; meta <= 6; meta++) {
            addMetaGrinding(Block.getIdFromBlock(mod_LionKing.pridePillar), meta, new ItemStack(mod_LionKing.pridePillar, 1, meta + 1));
        }
    }

    private void addGrinding(int itemId, ItemStack result) {
        grindingList.put(itemId, result);
    }

    private void addMetaGrinding(int itemId, int meta, ItemStack result) {
        metaGrindingList.put(new GrindingKey(itemId, meta), result);
    }

    public ItemStack getGrindingResult(ItemStack input) {
        if (input == null) {
            return null;
        }

        ItemStack metaResult = metaGrindingList.get(new GrindingKey(Item.getIdFromItem(input.getItem()), input.getItemDamage()));
        if (metaResult != null) {
            return metaResult;
        }

        return grindingList.get(Item.getIdFromItem(input.getItem()));
    }

    private static class GrindingKey {
        private final int itemId;
        private final int metadata;

        GrindingKey(int itemId, int metadata) {
            this.itemId = itemId;
            this.metadata = metadata;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GrindingKey)) return false;
            GrindingKey key = (GrindingKey) o;
            return itemId == key.itemId && metadata == key.metadata;
        }

        @Override
        public int hashCode() {
            return 31 * itemId + metadata;
        }
    }
}