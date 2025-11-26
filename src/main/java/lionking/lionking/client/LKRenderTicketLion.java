package lionking.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.common.LKIngame;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * Класс рендера для сущности "Ticket Lion" в моде "The Lion King".
 */
@SideOnly(Side.CLIENT)
public class LKRenderTicketLion extends LKRenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation("lionking:mob/ticketlion.png");
    private static final ResourceLocation TEXTURE_CHRISTMAS = new ResourceLocation("lionking:mob/ticketlion_christmas.png");

    /**
     * Конструктор рендера для "Ticket Lion".
     */
    public LKRenderTicketLion() {
        super(new LKModelLion(), 0.5F, StatCollector.translateToLocal("entity.lionking.ticketlion.name"));
    }

    /**
     * Возвращает текстуру для указанной сущности в зависимости от времени года.
     * @param entity сущность, для которой определяется текстура
     * @return путь к текстуре (обычная или рождественская)
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return LKIngame.isChristmas() ? TEXTURE_CHRISTMAS : TEXTURE;
    }
}