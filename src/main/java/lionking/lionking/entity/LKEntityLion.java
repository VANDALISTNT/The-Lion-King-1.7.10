package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.StatCollector;

import lionking.entity.LKEntityQuestAnimal;
import lionking.common.LKCharacterSpeech;

public class LKEntityLion extends LKEntityLionBase {
    public LKEntityLion(World world) {
        super(world);
        setSize(1.3F, 1.6F);
    }

    @Override
    public LKCharacterSpeech getCharacterSpeech() {
        return LKCharacterSpeech.LION;
    }

    @Override
    public LKCharacterSpeech getChildCharacterSpeech() {
        return LKCharacterSpeech.LION_CUB;
    }

    @Override
    public String getAnimalName() {
        return StatCollector.translateToLocal("entity.lion.name");
    }

    @Override
    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.lion.name");
    }
}