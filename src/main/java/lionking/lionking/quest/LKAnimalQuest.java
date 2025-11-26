package lionking.quest;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item; 
import net.minecraft.entity.DataWatcher;
import net.minecraft.util.StatCollector;

import java.util.Random;

public class LKAnimalQuest {
    private static String[] questStart = {
        "quest.animal.start.0", 
        "quest.animal.start.1", 
        "quest.animal.start.2", 
        "quest.animal.start.3", 
        "quest.animal.start.4", 
        "quest.animal.start.5"  
    };

    private static String[] questEnd = {
        "quest.animal.end.0",   
        "quest.animal.end.1",   
        "quest.animal.end.2",   
        "quest.animal.end.3"    
    };

    private static Random rand = new Random();
    private DataWatcher dataWatcher;

    public void init(DataWatcher datawatcher) {
        if (datawatcher == null) {
            System.out.println("Error: dataWatcher is null in LKAnimalQuest.init");
            return;
        }
        dataWatcher = datawatcher;
        dataWatcher.addObject(25, Integer.valueOf(0));
        dataWatcher.addObject(26, Integer.valueOf(0));
        dataWatcher.addObject(27, Integer.valueOf(0));
        dataWatcher.addObject(28, Byte.valueOf((byte) 0));
    }

    public void setQuest(ItemStack itemstack) {
        if (dataWatcher == null) {
            System.out.println("Error: dataWatcher is null in LKAnimalQuest.setQuest");
            return;
        }
        dataWatcher.updateObject(25, Integer.valueOf(Item.getIdFromItem(itemstack.getItem()))); 
        dataWatcher.updateObject(26, Integer.valueOf(itemstack.stackSize));
        dataWatcher.updateObject(27, Integer.valueOf(itemstack.getItemDamage()));
        setHasQuest(true);
    }

    public boolean isRequiredItem(ItemStack itemstack) {
        if (dataWatcher == null) {
            System.out.println("Error: dataWatcher is null in LKAnimalQuest.isRequiredItem");
            return false;
        }
        return Item.getIdFromItem(itemstack.getItem()) == dataWatcher.getWatchableObjectInt(25) && 
               itemstack.stackSize >= dataWatcher.getWatchableObjectInt(26) &&
               itemstack.getItemDamage() == dataWatcher.getWatchableObjectInt(27);
    }

    public ItemStack getRequiredItem() {
        if (dataWatcher == null) {
            System.out.println("Error: dataWatcher is null in LKAnimalQuest.getRequiredItem");
            return null;
        }
        Item item = Item.getItemById(dataWatcher.getWatchableObjectInt(25));
        return item != null ? new ItemStack(item,
                             dataWatcher.getWatchableObjectInt(26),
                             dataWatcher.getWatchableObjectInt(27)) : null;
    }

    public static String startQuest(String animal, ItemStack itemstack) {
        String localizedAnimal = StatCollector.translateToLocal("entity." + animal + ".name");
        String s = "§e" + localizedAnimal + "§f" + StatCollector.translateToLocal(questStart[rand.nextInt(questStart.length)]);
        String numberKey = "quest.number." + numbers[itemstack.stackSize]; 
        String localizedNumber = StatCollector.translateToLocal(numberKey);
        return replace(s, "#", localizedNumber + " " + itemstack.getDisplayName()); 
    }

    public static String endQuest(String animal) {
        String localizedAnimal = StatCollector.translateToLocal("entity." + animal + ".name");
        return "§e" + localizedAnimal + "§f" + StatCollector.translateToLocal(questEnd[rand.nextInt(questEnd.length)]);
    }

    public boolean hasQuest() {
        if (dataWatcher == null) {
            System.out.println("Error: dataWatcher is null in LKAnimalQuest.hasQuest");
            return false;
        }
        return dataWatcher.getWatchableObjectByte(28) == (byte) 1;
    }

    public void setHasQuest(boolean flag) {
        if (dataWatcher == null) {
            System.out.println("Error: dataWatcher is null in LKAnimalQuest.setHasQuest");
            return;
        }
        dataWatcher.updateObject(28, flag ? (byte) 1 : (byte) 0);
    }

    public void writeToNBT(NBTTagCompound nbt) {
        if (dataWatcher == null) {
            System.out.println("Error: dataWatcher is null in LKAnimalQuest.writeToNBT");
            return;
        }
        nbt.setInteger("QuestID", dataWatcher.getWatchableObjectInt(25));
        nbt.setInteger("QuestAmount", dataWatcher.getWatchableObjectInt(26));
        nbt.setInteger("QuestDamage", dataWatcher.getWatchableObjectInt(27));
        nbt.setBoolean("HasQuest", hasQuest());
    }

    public void readFromNBT(NBTTagCompound nbt) {
        if (dataWatcher == null) {
            System.out.println("Error: dataWatcher is null in LKAnimalQuest.readFromNBT");
            return;
        }
        dataWatcher.updateObject(25, Integer.valueOf(nbt.getInteger("QuestID")));
        dataWatcher.updateObject(26, Integer.valueOf(nbt.getInteger("QuestAmount")));
        dataWatcher.updateObject(27, Integer.valueOf(nbt.getInteger("QuestDamage")));
        dataWatcher.updateObject(28, nbt.getBoolean("HasQuest") ? (byte) 1 : (byte) 0);
    }

    private static String replace(String text, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer newText = new StringBuffer();

        while ((e = text.indexOf(pattern, s)) >= 0) {
            newText.append(text.substring(s, e));
            newText.append(replace);
            s = e + pattern.length();
        }

        newText.append(text.substring(s));
        return newText.toString();
    }

    private static String[] numbers = {
        "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
        "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
        "twenty", "twenty-one", "twenty-two", "twenty-three", "twenty-four", "twenty-five", "twenty-six", "twenty-seven", "twenty-eight", "twenty-nine",
        "thirty", "thirty-one", "thirty-two", "thirty-three", "thirty-four", "thirty-five", "thirty-six", "thirty-seven", "thirty-eight", "thirty-nine",
        "forty", "forty-one", "forty-two", "forty-three", "forty-four", "forty-five", "forty-six", "forty-seven", "forty-eight", "forty-nine",
        "fifty", "fifty-one", "fifty-two", "fifty-three", "fifty-four", "fifty-five", "fifty-six", "fifty-seven", "fifty-eight", "fifty-nine",
        "sixty", "sixty-one", "sixty-two", "sixty-three", "sixty-four"
    };
}