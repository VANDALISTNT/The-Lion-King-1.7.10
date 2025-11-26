package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.StatCollector;

import lionking.common.LKCharacterSpeech;

public class LKEntityLioness extends LKEntityLionBase {
    public LKEntityLioness(World world) {
        super(world); 
        setSize(1.2F, 1.3F); 
    }

    @Override
    public LKCharacterSpeech getCharacterSpeech() {
        return LKCharacterSpeech.LIONESS; 
    }

    @Override
    public LKCharacterSpeech getChildCharacterSpeech() {
        return LKCharacterSpeech.LIONESS_CUB; 
    }

    @Override
    public String getAnimalName() {
        return "Lioness"; 
    }
     
    @Override
    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lioness.name");
    }
}