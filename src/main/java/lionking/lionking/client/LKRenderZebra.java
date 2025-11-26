package lionking.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Класс рендера для сущности зебры в моде "The Lion King".
 */
@SideOnly(Side.CLIENT)
public class LKRenderZebra extends LKRenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:mob/zebra.png");

    /**
     * Конструктор рендера для зебры.
     */
    public LKRenderZebra() {
        super(new LKModelZebra(), 0.5F); // Модель зебры и размер тени
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