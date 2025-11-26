package lionking.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Класс рендера для сущности терміта в моде "The Lion King".
 */
@SideOnly(Side.CLIENT)
public class LKRenderTermite extends LKRenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:mob/termite.png");

    /**
     * Конструктор рендера для терміта.
     */
    public LKRenderTermite() {
        super(new LKModelTermite(), 0.3F); // Модель терміта і розмір тіні
    }

    /**
     * Повертає текстуру для вказаної сущності.
     * @param entity сущність, для якої визначається текстура
     * @return шлях до текстури
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }
}