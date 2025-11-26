package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;

public class LKEntityPoisonedSpear extends LKEntitySpear {
    public LKEntityPoisonedSpear(World world) {
        super(world); // Конструктор без параметров
    }

    public LKEntityPoisonedSpear(World world, double d, double d1, double d2, int damage) {
        super(world, d, d1, d2, damage); // Конструктор с координатами и уроном
    }

    public LKEntityPoisonedSpear(World world, EntityLivingBase entityliving, float f, int damage) {
        super(world, entityliving, f, damage); // Конструктор с бросающим, скоростью и уроном
    }

    // Возвращает true, обозначая копье как отравленное
    public boolean isPoisoned() {
        return true;
    }
}