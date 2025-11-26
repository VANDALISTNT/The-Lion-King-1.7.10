package lionking.quest;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import lionking.mod_LionKing;
import lionking.quest.MessageQuestStage;
import lionking.quest.MessageQuestCheckUpdate;
import lionking.quest.MessageQuestDelay;
import lionking.common.LKLevelData;

import java.util.ArrayList;

public abstract class LKQuestBase {
    public static LKQuestBase[] allQuests = new LKQuestBase[16];

    public static LKQuestBase rafikiQuest = new LKQuestRafiki(0).setName("Rafiki's Quest");
    public static LKQuestBase outlandsQuest = new LKQuestOutlands(1).setName("An Outlandish Scheme");

    public int stagesDelayed;
    private String questName;
    public int currentStage;
    public int[] stagesCompleted;
    public int checked;
    public int questIndex;

    public static ArrayList orderedQuests;

    static {
        orderedQuests = new ArrayList();
        orderedQuests.add(rafikiQuest);
        orderedQuests.add(outlandsQuest);
    }

    public LKQuestBase(int i) {
        allQuests[i] = this;
        questIndex = i;
        stagesCompleted = new int[getNumStages() + 1];
    }

    public LKQuestBase setName(String name) {
        questName = name;
        return this;
    }

    public String getName() {
        return StatCollector.translateToLocal("quest." + questIndex + ".name");
    }

    public boolean isComplete() {
        return currentStage == getNumStages();
    }

    public abstract boolean canStart();
    public abstract String[] getRequirements();
    public abstract int getNumStages();
    public abstract ItemStack getIcon();
    public abstract String getObjectiveByStage(int i);

    public void progress(int stage) {
        if (stage != currentStage + 1) {
            return;
        }
        if (currentStage > 0) {
            for (int i = 0; i < currentStage; i++) {
                if (stagesCompleted[i] == 0) {
                    return;
                }
            }
        }
        stagesCompleted[currentStage] = 1;

        mod_LionKing.networkQuests.sendToAll(new MessageQuestStage((byte) questIndex, (byte) currentStage)); // Исправлено: network ? networkQuests
        LKLevelData.needsSave = true;
    }

    public int getQuestStage() {
        return currentStage;
    }

    public void setDelayed(boolean flag) {
        stagesDelayed = flag ? 1 : 0;
        mod_LionKing.networkQuests.sendToAll(new MessageQuestDelay((byte) questIndex, (byte) stagesDelayed)); // Исправлено: network ? networkQuests
        LKLevelData.needsSave = true;
    }

    public boolean isDelayed() {
        return stagesDelayed == 1;
    }

    public boolean isStageComplete(int i) {
        return stagesCompleted[i] == 1;
    }

    public boolean isChecked() {
        return checked == 1;
    }

    public void setChecked(boolean flag) {
        checked = flag ? 1 : 0;
        mod_LionKing.networkQuests.sendToAll(new MessageQuestCheckUpdate((byte) questIndex, (byte) checked)); // Исправлено: network ? networkQuests
        LKLevelData.needsSave = true;
    }

    public void resetProgress() {
        currentStage = 0;
        stagesDelayed = 0;
        for (int j = 0; j < stagesCompleted.length; j++) {
            stagesCompleted[j] = 0;
        }
        checked = 0;
    }

    public static boolean anyUncheckedQuests() {
        for (int i = 0; i < allQuests.length; i++) {
            LKQuestBase quest = allQuests[i];
            if (quest == null) {
                continue;
            }
            if (quest.canStart() && !quest.isChecked()) {
                return true;
            }
        }
        return false;
    }

    public static void updateAllQuests() {
        for (int i = 0; i < allQuests.length; i++) {
            LKQuestBase quest = allQuests[i];
            if (quest == null) {
                continue;
            }
            if (quest.stagesDelayed == 0 && quest.stagesCompleted[quest.currentStage] == 1 && quest.currentStage < quest.getNumStages()) {
                quest.currentStage++;
                mod_LionKing.networkQuests.sendToAll(new MessageQuestStage((byte) quest.questIndex, (byte) quest.currentStage)); // Исправлено: network ? networkQuests
                LKLevelData.needsSave = true;
            }
        }
    }

    public static void writeAllQuestsToNBT(NBTTagCompound nbt) {
        for (int i = 0; i < allQuests.length; i++) {
            LKQuestBase quest = allQuests[i];
            if (quest == null) {
                continue;
            }
            nbt.setInteger("Quest_" + i + "_Stage", quest.currentStage);
            nbt.setInteger("Quest_" + i + "_Delayed", quest.stagesDelayed);
            for (int j = 0; j < quest.stagesCompleted.length; j++) {
                nbt.setInteger("Quest_" + i + "_CompletedStage_" + j, quest.stagesCompleted[j]);
            }
            nbt.setInteger("Quest_" + i + "_Checked", quest.checked);
        }
    }

    public static void readAllQuestsFromNBT(NBTTagCompound nbt) {
        for (int i = 0; i < allQuests.length; i++) {
            LKQuestBase quest = allQuests[i];
            if (quest == null) {
                continue;
            }
            quest.currentStage = nbt.getInteger("Quest_" + i + "_Stage");
            quest.stagesDelayed = nbt.getInteger("Quest_" + i + "_Delayed");
            for (int j = 0; j < quest.stagesCompleted.length; j++) {
                quest.stagesCompleted[j] = nbt.getInteger("Quest_" + i + "_CompletedStage_" + j);
            }
            quest.checked = nbt.getInteger("Quest_" + i + "_Checked");
        }
    }
}
