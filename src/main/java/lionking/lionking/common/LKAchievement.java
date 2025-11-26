package lionking.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.StatBase;
import net.minecraft.util.StatCollector;

import lionking.common.LKAchievementList;

public class LKAchievement extends Achievement {
    public final int displayColumn;
    public final int displayRow;
    public final LKAchievement parentAchievement;
    private final String achievementKey;
    private IStatStringFormat statStringFormatter;
    public final ItemStack theItemStack;
    private boolean isSpecial;
    public String lkAchievementTitle;

    public LKAchievement(int id, String key, int column, int row, Item item, LKAchievement parent) {
        this(id, key, column, row, new ItemStack(item), parent);
    }

    public LKAchievement(int id, String key, int column, int row, Block block, LKAchievement parent) {
        this(id, key, column, row, new ItemStack(block), parent);
    }

    public LKAchievement(int id, String key, int column, int row, ItemStack itemStack, LKAchievement parent) {
        super("lionking." + key, key, column, row, itemStack, parent == null ? null : parent);
        this.theItemStack = itemStack;
        this.achievementKey = key;
        this.displayColumn = column;
        this.displayRow = row;

        if (column < LKAchievementList.minDisplayColumn) {
            LKAchievementList.minDisplayColumn = column;
        }
        if (row < LKAchievementList.minDisplayRow) {
            LKAchievementList.minDisplayRow = row;
        }
        if (column > LKAchievementList.maxDisplayColumn) {
            LKAchievementList.maxDisplayColumn = column;
        }
        if (row > LKAchievementList.maxDisplayRow) {
            LKAchievementList.maxDisplayRow = row;
        }
        this.parentAchievement = parent;
    }

    public LKAchievement setIndependent() {
        this.isIndependent = true;
        return this;
    }

    public LKAchievement setSpecial() {
        this.isSpecial = true;
        return this;
    }

    public LKAchievement registerAchievement() {
        super.registerStat();
        AchievementList.achievementList.remove(this); 
        return this;
    }

    @Override
    public boolean isAchievement() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
        return StatCollector.translateToLocal(lkAchievementTitle);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getDescription() {
        String desc = StatCollector.translateToLocal(achievementKey + ".desc");
        return (statStringFormatter != null) ? statStringFormatter.formatString(desc) : desc;
    }

    public LKAchievement setStatStringFormatter(IStatStringFormat formatter) {
        this.statStringFormatter = formatter;
        return this;
    }

    @Override
    public boolean getSpecial() {
        return isSpecial;
    }

    @Override
    public Achievement registerStat() {
        return this.registerAchievement();
    }

    @Override
    public Achievement initIndependentStat() {
        return this.setIndependent();
    }

    @Override
    public String toString() {
        return StatCollector.translateToLocal(lkAchievementTitle);
    }
}
