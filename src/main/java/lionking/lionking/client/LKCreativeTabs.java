package lionking.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.mod_LionKing;

@SideOnly(Side.CLIENT)
public class LKCreativeTabs extends CreativeTabs{
     
    public static final LKCreativeTabs TAB_BLOCK = new LKCreativeTabs("Lion King Blocks", safeStack(mod_LionKing.prideBrick != null ? mod_LionKing.prideBrick : Blocks.brick_block));
    public static final LKCreativeTabs TAB_DECORATIONS = new LKCreativeTabs("Lion King Decorations", safeStack(mod_LionKing.sapling != null ? mod_LionKing.sapling : Blocks.sapling));
    public static final LKCreativeTabs TAB_FOOD = new LKCreativeTabs("Lion King Foodstuffs", safeStack(mod_LionKing.zebraRaw != null ? mod_LionKing.zebraRaw : Items.beef));
    public static final LKCreativeTabs TAB_MATERIALS = new LKCreativeTabs("Lion King Materials", safeStack(mod_LionKing.hyenaBone != null ? mod_LionKing.hyenaBone : Items.bone));
    public static final LKCreativeTabs TAB_MISC = new LKCreativeTabs("Lion King Miscellaneous", safeStack(mod_LionKing.rafikiStick != null ? mod_LionKing.rafikiStick : Items.stick));
    public static final LKCreativeTabs TAB_TOOLS = new LKCreativeTabs("Lion King Tools", safeStack(mod_LionKing.rafikiStick != null ? mod_LionKing.rafikiStick : Items.stick));
    public static final LKCreativeTabs TAB_COMBAT = new LKCreativeTabs("Lion King Combat", safeStack(mod_LionKing.dartShooter != null ? mod_LionKing.dartShooter : Items.bow));
    public static final LKCreativeTabs TAB_QUEST = new LKCreativeTabs("Lion King Quest Items", safeStack(mod_LionKing.questBook   != null ? mod_LionKing.questBook : Items.book));

    private final ItemStack iconStack;

    private LKCreativeTabs(String label, ItemStack icon){
     
        super(label);
        this.iconStack = icon;
    }

    private static ItemStack safeStack(Object obj){
     
        if (obj instanceof Item)   return new ItemStack((Item)obj);
        if (obj instanceof net.minecraft.block.Block) return new ItemStack((net.minecraft.block.Block)obj);
        return new ItemStack(Items.stick); 
    }

    @Override
    public ItemStack getIconItemStack(){
     
        return iconStack;
    }

    @Override
    public Item getTabIconItem(){
     
        return iconStack.getItem();
    }

    @Override
    public String getTranslatedTabLabel(){
     
        return "itemGroup." + getTabLabel();
    }

    public static void initialiseTabs() {
    }
}
