package lionking.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * Класс рендера для сущности Тимона в моде "The Lion King".
 */
@SideOnly(Side.CLIENT)
public class LKRenderTimon extends LKRenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:mob/timon.png");

    /**
     * Конструктор рендера для Тимона.
     */
    public LKRenderTimon() {
        super(new LKModelTimon(), 0.5F, StatCollector.translateToLocal("entity.lionking.timon.name"));
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