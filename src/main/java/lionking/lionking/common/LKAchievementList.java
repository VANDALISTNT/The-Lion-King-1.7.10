package lionking.common;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.AchievementPage;

import java.lang.reflect.Field;
import java.util.LinkedList;

import lionking.mod_LionKing;

public class LKAchievementList extends AchievementPage {
    public static int minDisplayColumn;
    public static int minDisplayRow;
    public static int maxDisplayColumn;
    public static int maxDisplayRow;

    
    public static LKAchievement enterPrideLands;
    public static LKAchievement killHyena;
    public static LKAchievement getMango;
    public static LKAchievement morningReport;
    public static LKAchievement craftGrindBowl;
    public static LKAchievement getStick;
    public static LKAchievement shootDart;
    public static LKAchievement killScar;
    public static LKAchievement enterOutlands;
    public static LKAchievement heads;
    public static LKAchievement simba;
    public static LKAchievement hakunaMatata;
    public static LKAchievement rhinoHorn;
    public static LKAchievement termite;
    public static LKAchievement fartBomb;
    public static LKAchievement crystal;
    public static LKAchievement rugs;
    public static LKAchievement fireTool;
    public static LKAchievement chocolateMufasa;
    public static LKAchievement enchantDiggah;
    public static LKAchievement bugTrap;
    public static LKAchievement ticketLionSuit;
    public static LKAchievement killZira;
    public static LKAchievement peacock;
    public static LKAchievement teleportSimba;
    public static LKAchievement upendi;
    public static LKAchievement animalQuest;
    public static LKAchievement drumEnchant;
    public static LKAchievement powerDrum;
    public static LKAchievement getBanana;
    public static LKAchievement rideGiraffe;
    public static LKAchievement behead;
    public static LKAchievement wings;


    public static void preInitAchievements() {
        enterPrideLands = new LKAchievement(0, "achievement.lionking.enter_pride_lands", 0, -5, mod_LionKing.ticket, null)
                .setIndependent().registerAchievement();
        killHyena = new LKAchievement(1, "achievement.lionking.kill_hyena", -2, 2, mod_LionKing.hyenaBone, enterPrideLands)
                .registerAchievement();
        getMango = new LKAchievement(2, "achievement.lionking.get_mango", -3, -4, mod_LionKing.mango, enterPrideLands)
                .registerAchievement();
        morningReport = new LKAchievement(3, "achievement.lionking.morning_report", 3, -4, mod_LionKing.featherBlue, enterPrideLands)
                .registerAchievement();
        craftGrindBowl = new LKAchievement(4, "achievement.lionking.craft_grind_bowl", -4, 4, mod_LionKing.itemGrindingBowl, killHyena)
                .registerAchievement();
        getStick = new LKAchievement(5, "achievement.lionking.get_stick", 0, 5, mod_LionKing.rafikiStick, killHyena)
                .registerAchievement();
        shootDart = new LKAchievement(6, "achievement.lionking.shoot_dart", -6, 5, mod_LionKing.dartShooter, craftGrindBowl)
                .registerAchievement();
        killScar = new LKAchievement(7, "achievement.lionking.kill_scar", -1, 9, mod_LionKing.scarRug, getStick)
                .setSpecial().registerAchievement();
        enterOutlands = new LKAchievement(8, "achievement.lionking.enter_outlands", 1, 11, mod_LionKing.outlandsPortalFrame, killScar)
                .registerAchievement();
        heads = new LKAchievement(9, "achievement.lionking.heads", -5, 2, mod_LionKing.hyenaHeadItem, killHyena)
                .setSpecial().registerAchievement();
        simba = new LKAchievement(10, "achievement.lionking.simba", -3, 13, mod_LionKing.lionDust, enterOutlands)
                .setSpecial().registerAchievement();
        hakunaMatata = new LKAchievement(11, "achievement.lionking.hakuna_matata", 5, 3, mod_LionKing.bug, enterPrideLands)
                .registerAchievement();
        rhinoHorn = new LKAchievement(12, "achievement.lionking.rhino_horn", -3, 6, mod_LionKing.hornGround, craftGrindBowl)
                .registerAchievement();
        termite = new LKAchievement(13, "achievement.lionking.termite", 3, 12, mod_LionKing.dartBlack, enterOutlands)
                .registerAchievement();
        fartBomb = new LKAchievement(14, "achievement.lionking.fart_bomb", 3, 4, mod_LionKing.pumbaaBomb, hakunaMatata)
                .registerAchievement();
        crystal = new LKAchievement(15, "achievement.lionking.crystal", 7, 5, mod_LionKing.crystal, hakunaMatata)
                .registerAchievement();
        rugs = new LKAchievement(16, "achievement.lionking.rugs", -5, 7, mod_LionKing.rug, craftGrindBowl)
                .setSpecial().registerAchievement();
        fireTool = new LKAchievement(17, "achievement.lionking.fire_tool", 2, 14, mod_LionKing.pickaxeKivulite, enterOutlands)
                .registerAchievement();
        chocolateMufasa = new LKAchievement(18, "achievement.lionking.chocolate_mufasa", -3, 0, mod_LionKing.chocolateMufasa, enterPrideLands)
                .registerAchievement();
        enchantDiggah = new LKAchievement(19, "achievement.lionking.enchant_diggah", 8, 2, mod_LionKing.tunnahDiggah, hakunaMatata)
                .setSpecial().registerAchievement();
        bugTrap = new LKAchievement(20, "achievement.lionking.bug_trap", 4, 6, mod_LionKing.bugTrap, hakunaMatata)
                .registerAchievement();
        ticketLionSuit = new LKAchievement(21, "achievement.lionking.ticket_lion_suit", 4, -6, mod_LionKing.ticketLionSuit, null)
                .setIndependent().setSpecial().registerAchievement();
        killZira = new LKAchievement(22, "achievement.lionking.kill_zira", -2, 16, mod_LionKing.outlandsFeather, simba)
                .setSpecial().registerAchievement();
        peacock = new LKAchievement(23, "achievement.lionking.peacock", 3, 0, mod_LionKing.peacockGem, enterPrideLands)
                .registerAchievement();
        teleportSimba = new LKAchievement(24, "achievement.lionking.teleport_simba", -5, 14, mod_LionKing.charm, simba)
                .registerAchievement();
        upendi = new LKAchievement(25, "achievement.lionking.upendi", 2, 8, mod_LionKing.passionFruit, getStick)
                .setSpecial().registerAchievement();
        animalQuest = new LKAchievement(26, "achievement.lionking.animal_quest", 6, 7, mod_LionKing.amulet, hakunaMatata)
                .registerAchievement();
        drumEnchant = new LKAchievement(27, "achievement.lionking.drum_enchant", 6, 1, mod_LionKing.drum, peacock)
                .registerAchievement();
        powerDrum = new LKAchievement(28, "achievement.lionking.power_drum", 6, -1, mod_LionKing.staff, drumEnchant)
                .setSpecial().registerAchievement();
        getBanana = new LKAchievement(29, "achievement.lionking.get_banana", -3, -2, mod_LionKing.banana, enterPrideLands)
                .registerAchievement();
        rideGiraffe = new LKAchievement(30, "achievement.lionking.ride_giraffe", 3, -2, mod_LionKing.giraffeSaddle, enterPrideLands)
                .registerAchievement();
        behead = new LKAchievement(31, "achievement.lionking.behead", 0, 14, new ItemStack(mod_LionKing.hyenaHeadItem, 1, 3), enterOutlands)
                .registerAchievement();
        wings = new LKAchievement(32, "achievement.lionking.wings", 2, 2, mod_LionKing.wings, peacock)
                .registerAchievement();

        
        addTitle(enterPrideLands, "achievement.lionking.enter_pride_lands.title");
        addTitle(killHyena, "achievement.lionking.kill_hyena.title");
        addTitle(getMango, "achievement.lionking.get_mango.title");
        addTitle(morningReport, "achievement.lionking.morning_report.title");
        addTitle(craftGrindBowl, "achievement.lionking.craft_grind_bowl.title");
        addTitle(getStick, "achievement.lionking.get_stick.title");
        addTitle(shootDart, "achievement.lionking.shoot_dart.title");
        addTitle(killScar, "achievement.lionking.kill_scar.title");
        addTitle(enterOutlands, "achievement.lionking.enter_outlands.title");
        addTitle(heads, "achievement.lionking.heads.title");
        addTitle(simba, "achievement.lionking.simba.title");
        addTitle(hakunaMatata, "achievement.lionking.hakuna_matata.title");
        addTitle(rhinoHorn, "achievement.lionking.rhino_horn.title");
        addTitle(termite, "achievement.lionking.termite.title");
        addTitle(fartBomb, "achievement.lionking.fart_bomb.title");
        addTitle(crystal, "achievement.lionking.crystal.title");
        addTitle(rugs, "achievement.lionking.rugs.title");
        addTitle(fireTool, "achievement.lionking.fire_tool.title");
        addTitle(chocolateMufasa, "achievement.lionking.chocolate_mufasa.title");
        addTitle(enchantDiggah, "achievement.lionking.enchant_diggah.title");
        addTitle(bugTrap, "achievement.lionking.bug_trap.title");
        addTitle(ticketLionSuit, "achievement.lionking.ticket_lion_suit.title");
        addTitle(killZira, "achievement.lionking.kill_zira.title");
        addTitle(peacock, "achievement.lionking.peacock.title");
        addTitle(teleportSimba, "achievement.lionking.teleport_simba.title");
        addTitle(upendi, "achievement.lionking.upendi.title");
        addTitle(animalQuest, "achievement.lionking.animal_quest.title");
        addTitle(drumEnchant, "achievement.lionking.drum_enchant.title");
        addTitle(powerDrum, "achievement.lionking.power_drum.title");
        addTitle(getBanana, "achievement.lionking.get_banana.title");
        addTitle(rideGiraffe, "achievement.lionking.ride_giraffe.title");
        addTitle(behead, "achievement.lionking.behead.title");
        addTitle(wings, "achievement.lionking.wings.title");
    }


    public LKAchievementList() {
        super("achievement.lionking.page_name", getLKAchievements());
    }


    private static void addTitle(LKAchievement achievement, String titleKey) {
        achievement.lkAchievementTitle = titleKey;
    }


    private static LKAchievement[] getLKAchievements() {
        LKAchievement[] fallback = new LKAchievement[]{enterPrideLands};
        try {
            LinkedList<LKAchievement> achievements = new LinkedList<>();
            Field[] fields = LKAchievementList.class.getFields();
            for (Field field : fields) {
                Object value = field.get(null);
                if (value instanceof LKAchievement) {
                    achievements.add((LKAchievement) value);
                }
            }
            return achievements.toArray(new LKAchievement[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return fallback;
        }
    }
}
