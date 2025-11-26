package lionking.quest;

import net.minecraft.item.ItemStack;

import lionking.mod_LionKing;
import net.minecraft.util.StatCollector;

public class LKQuestRafiki extends LKQuestBase {
    public LKQuestRafiki(int i) {
        super(i); 
    }

    
    @Override
    public boolean canStart() {
        return true; 
    }

    
    @Override
    public String[] getRequirements() {
        return null; 
    }

    
    @Override
    public int getNumStages() {
        return 7; 
    }

    
    @Override
    public ItemStack getIcon() {
        return new ItemStack(mod_LionKing.rafikiStick); 
    }

    
    @Override
    public String getObjectiveByStage(int i) {
        switch (i) {
            default: return StatCollector.translateToLocal("quest.rafiki.stage.default"); 
            case 0: return StatCollector.translateToLocal("quest.rafiki.stage.0"); 
            case 1: return StatCollector.translateToLocal("quest.rafiki.stage.1"); 
            case 2: return StatCollector.translateToLocal("quest.rafiki.stage.2"); 
            case 3: return StatCollector.translateToLocal("quest.rafiki.stage.3"); 
            case 4: return StatCollector.translateToLocal("quest.rafiki.stage.4"); 
            case 5: return StatCollector.translateToLocal("quest.rafiki.stage.5"); 
            case 6: return StatCollector.translateToLocal("quest.rafiki.stage.6"); 
            case 7: return StatCollector.translateToLocal("quest.rafiki.stage.7"); 
        }
    }
}
