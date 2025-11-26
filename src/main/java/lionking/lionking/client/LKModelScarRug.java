package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.entity.LKEntityScarRug; 

@SideOnly(Side.CLIENT)
public class LKModelScarRug extends ModelBase {
    private static final int TEXTURE_WIDTH = 64;
    private static final int TEXTURE_HEIGHT = 64;
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;

    private final ModelRenderer body;
    private final ModelRenderer mane;
    private final ModelRenderer head;
    private final ModelRenderer legFrontLeft;
    private final ModelRenderer legFrontRight;
    private final ModelRenderer legBackLeft;
    private final ModelRenderer legBackRight;
    private final ModelRenderer tail;

    public LKModelScarRug() {
        textureWidth = TEXTURE_WIDTH;
        textureHeight = TEXTURE_HEIGHT;

        body = new ModelRenderer(this, 20, 0);
        body.addBox(0.0F, 0.0F, 0.0F, 20, 25, 2);
        body.setRotationPoint(-10.0F, 24.0F, -10.0F);

        mane = new ModelRenderer(this, 0, 43);
        mane.addBox(0.0F, 0.0F, 0.0F, 14, 12, 9);
        mane.setRotationPoint(-7.0F, 12.0F, -18.0F);

        head = new ModelRenderer(this, 32, 27);
        head.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8);
        head.setRotationPoint(-4.0F, 15.0F, -20.0F);
        head.setTextureOffset(52, 45);
        head.addBox(2.0F, 4.0F, -2.0F, 4, 4, 2);

        legFrontLeft = new ModelRenderer(this, 0, 0);
        legFrontLeft.addBox(0.0F, 0.0F, -4.0F, 2, 12, 4);
        legFrontLeft.setRotationPoint(-8.0F, 22.1F, 14.0F);

        legFrontRight = new ModelRenderer(this, 0, 0);
        legFrontRight.addBox(-2.0F, 0.0F, -4.0F, 2, 12, 4);
        legFrontRight.setRotationPoint(8.0F, 22.1F, 14.0F);

        legBackLeft = new ModelRenderer(this, 0, 0);
        legBackLeft.addBox(0.0F, 0.0F, 0.0F, 2, 12, 4);
        legBackLeft.setRotationPoint(-8.0F, 22.1F, -10.0F);

        legBackRight = new ModelRenderer(this, 0, 0);
        legBackRight.addBox(-2.0F, 0.0F, 0.0F, 2, 12, 4);
        legBackRight.setRotationPoint(8.0F, 22.1F, -10.0F);

        tail = new ModelRenderer(this, 0, 24);
        tail.addBox(-1.5F, 0.0F, 0.0F, 3, 1, 12);
        tail.setRotationPoint(0.0F, 22.05F, 14.0F);

        setInitialRotationAngles();
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        body.render(scale);

        GL11.glPushMatrix();
        if (((LKEntityScarRug) entity).getType() == 1) {
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
        }
        mane.render(scale);
        head.render(scale);
        GL11.glPopMatrix();

        legFrontLeft.render(scale);
        legFrontRight.render(scale);
        legBackLeft.render(scale);
        legBackRight.render(scale);
        tail.render(scale);
    }

    private void setInitialRotationAngles() {
        body.rotateAngleX = 90.0F * DEG_TO_RAD;

        legFrontLeft.rotateAngleX = 22.0F * DEG_TO_RAD;
        legFrontLeft.rotateAngleZ = 90.0F * DEG_TO_RAD;

        legFrontRight.rotateAngleX = 22.0F * DEG_TO_RAD;
        legFrontRight.rotateAngleZ = -90.0F * DEG_TO_RAD;

        legBackLeft.rotateAngleX = -22.0F * DEG_TO_RAD;
        legBackLeft.rotateAngleZ = 90.0F * DEG_TO_RAD;

        legBackRight.rotateAngleX = -22.0F * DEG_TO_RAD;
        legBackRight.rotateAngleZ = -90.0F * DEG_TO_RAD;

        tail.rotateAngleX = -4.0F * DEG_TO_RAD;
    }
}