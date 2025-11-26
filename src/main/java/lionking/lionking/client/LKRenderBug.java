package lionking.client;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Класс рендера для сущности "жук" в моде "The Lion King".
 */
@SideOnly(Side.CLIENT)
public class LKRenderBug extends LKRenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:mob/bug.png");

    /**
     * Конструктор рендера для жука.
     */
    public LKRenderBug() {
        super(new LKModelTermite(), 0.3F); // Модель и размер тени
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