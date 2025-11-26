package lionking.quest;

import net.minecraft.item.ItemStack;

import lionking.mod_LionKing;
import net.minecraft.util.StatCollector;

public class LKQuestOutlands extends LKQuestBase {
    public LKQuestOutlands(int i) {
        super(i); 
    }

    
    @Override
    public boolean canStart() {
        return LKQuestBase.rafikiQuest.isComplete(); 
    }

    
    @Override
    public String[] getRequirements() {
        return new String[] {"Complete Rafiki's Quest"}; 
    }

    
    @Override
    public int getNumStages() {
        return 10; 
    }

    
    @Override
    public ItemStack getIcon() {
        return new ItemStack(mod_LionKing.outlandsFeather); 
    }

    
    @Override
    public String getObjectiveByStage(int i) {
        switch (i) {
            default: return StatCollector.translateToLocal("quest.outlands.stage.default"); 
            case 0: return StatCollector.translateToLocal("quest.outlands.stage.0"); 
            case 1: return StatCollector.translateToLocal("quest.outlands.stage.1"); 
            case 2: return StatCollector.translateToLocal("quest.outlands.stage.2"); 
            case 3: return StatCollector.translateToLocal("quest.outlands.stage.3");
            case 4: return StatCollector.translateToLocal("quest.outlands.stage.4");
            case 5: return StatCollector.translateToLocal("quest.outlands.stage.5"); 
            case 6: return StatCollector.translateToLocal("quest.outlands.stage.6"); 
            case 7: return StatCollector.translateToLocal("quest.outlands.stage.7"); 
            case 8: return StatCollector.translateToLocal("quest.outlands.stage.8"); 
            case 9: return StatCollector.translateToLocal("quest.outlands.stage.9"); 
            case 10: return StatCollector.translateToLocal("quest.outlands.stage.10"); 
        }
    }

    
    public String getQuestName() {
        return StatCollector.translateToLocal("quest.outlands.name");
    }

}
