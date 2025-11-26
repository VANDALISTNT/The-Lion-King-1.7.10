package lionking.common;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.util.StatCollector;

import java.util.Random;


public enum LKCharacterSpeech {
    MORNING_REPORT("Zazu", "speech.lionking.zazu.morning_report"),
    HYENA_BONES("Rafiki", "speech.lionking.rafiki.hyena_bones"),
    MENTION_SCAR("Rafiki", "speech.lionking.rafiki.mention_scar"),
    TERMITES("Rafiki", "speech.lionking.rafiki.termites"),
    MANGOES("Rafiki", "speech.lionking.rafiki.mangoes"),
    STAR_ALTAR("Rafiki", "speech.lionking.rafiki.star_altar"),
    HINT("Rafiki", "speech.lionking.rafiki.hint"),
    FLOWERS("Rafiki", "speech.lionking.rafiki.flowers"),
    BUGS("Timon", "speech.lionking.timon.bugs"),
    MORE_BUGS("Timon", "speech.lionking.timon.more_bugs"),
    RUG_SCAR("Scar", "speech.lionking.scar.rug"),
    ZIRA_INGOTS("Zira", "speech.lionking.zira.ingots"),
    ZIRA_FEATHERS("Zira", "speech.lionking.zira.feathers"),
    ZIRA_CONQUEST("Zira", "speech.lionking.zira.conquest"),
    RUG_ZIRA("Zira", "speech.lionking.zira.rug"),
    LION("Lion", "speech.lionking.lion"),
    LIONESS("Lioness", "speech.lionking.lion"),
    LION_CUB("Lion Cub", "speech.lionking.lion_cub"),
    LIONESS_CUB("Lioness Cub", "speech.lionking.lion_cub"),
    ZEBRA("Zebra", "speech.lionking.zebra"),
    ZEBRA_FOAL("Zebra Foal", "speech.lionking.zebra_foal"),
    RHINO("Rhino", "speech.lionking.rhino"),
    RHINO_CALF("Rhino Calf", "speech.lionking.rhino_calf"),
    GEMSBOK("Gemsbok", "speech.lionking.gemsbok"),
    GEMSBOK_CALF("Gemsbok Calf", "speech.lionking.gemsbok_calf");

    private static final Random RANDOM = new Random(); 

    private final String characterName; 
    private final String speechKeyBase; 

    LKCharacterSpeech(String characterName, String speechKeyBase) {
        this.characterName = characterName;
        this.speechKeyBase = speechKeyBase;
    }

    
    public String getSpeech() {
        int speechCount = getSpeechCount();
        int index = RANDOM.nextInt(speechCount);
        String speech = StatCollector.translateToLocal(speechKeyBase + "." + index);
        return "\u00a7e<" + characterName + "> \u00a7f" + speech;
    }

    
    public static String giveSpeech(LKCharacterSpeech speech) {
        return speech.getSpeech();
    }

    
    private int getSpeechCount() {
        int count = 0;
        while (StatCollector.canTranslate(speechKeyBase + "." + count)) {
            count++;
        }
        return Math.max(count, 1); 
    }

    
    public static void registerLocalizations() {
        
        LanguageRegistry.instance().loadLocalization("/assets/lionking/lang/en_US.lang", "en_US", false);
		LanguageRegistry.instance().loadLocalization("/assets/lionking/lang/ru_RU.lang", "ru_RU", false);
    }
}
