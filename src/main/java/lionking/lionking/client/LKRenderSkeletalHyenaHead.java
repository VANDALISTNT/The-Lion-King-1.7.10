package lionking.client;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Класс рендера для скелетной головы гиены в моде "The Lion King".
 */
@SideOnly(Side.CLIENT)
public class LKRenderSkeletalHyenaHead extends LKRenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:mob/hyena_skeleton.png");

    /**
     * Конструктор рендера для скелетной головы гиены.
     */
    public LKRenderSkeletalHyenaHead() {
        super(new LKModelHyenaHead(true), 0.5F); // Модель и размер тени по умолчанию
    }

    /**
     * Возвращает текстуру для указанной сущности.
     * @param entity сущность, для которой определяется текстура
     * @return путь к текстуре
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }
}