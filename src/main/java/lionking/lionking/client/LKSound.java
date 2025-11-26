package lionking.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.PlayBackgroundMusicEvent;
import lionking.mod_LionKing;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class LKSound {
    private final Random rand = new Random();
    private static final String[] MUSIC_TRACKS = {
        "lionking:The Circle of Life",
        "lionking:I Just Can't Wait to Be King",
        "lionking:Be Prepared",
        "lionking:Hakuna Matata",
        "lionking:Can You Feel the Love Tonight"
    };

    @SubscribeEvent
    public void playBackgroundMusic(PlayBackgroundMusicEvent event) {
        if (Minecraft.getMinecraft().theWorld != null && isLKWorld(Minecraft.getMinecraft().theWorld.provider.dimensionId)) {
            int randInt = rand.nextInt(100);
            if (randInt < mod_LionKing.lkMusicChance) { 
                int trackIndex = rand.nextInt(MUSIC_TRACKS.length);
                ResourceLocation music = new ResourceLocation(MUSIC_TRACKS[trackIndex]);
                event.result = new SoundPoolEntry(music, 1.0F, 1.0F, true); 
            }
        }
    }

    private boolean isLKWorld(int dimensionId) {
        return dimensionId == mod_LionKing.idPrideLands || 
               dimensionId == mod_LionKing.idOutlands || 
               dimensionId == mod_LionKing.idUpendi;
    }
}