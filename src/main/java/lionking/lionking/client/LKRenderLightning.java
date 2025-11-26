package lionking.client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityLightning;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class LKRenderLightning extends Render
{
    private static final int SEGMENTS = 8;
    private static final float SEGMENT_HEIGHT = 16.0F;
    private static final float BASE_BRIGHTNESS = 0.5F;
    private static final float[] COLOR = {0.9F, 0.9F, 1.0F, 0.3F}; 

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks)
    {
        renderLightning((LKEntityLightning)entity, x, y, z);
    }

    private void renderLightning(LKEntityLightning lightning, double x, double y, double z)
    {
        Tessellator tessellator = Tessellator.instance;
        setupGLState();

        double[] offsetsX = new double[SEGMENTS];
        double[] offsetsZ = new double[SEGMENTS];
        Random random = new Random(lightning.boltVertex);
        generateMainPath(offsetsX, offsetsZ, random);

        renderLightningLayers(tessellator, x, y, z, offsetsX, offsetsZ, lightning.boltVertex);

        cleanupGLState();
    }

    private void setupGLState()
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
    }

    private void cleanupGLState()
    {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void generateMainPath(double[] offsetsX, double[] offsetsZ, Random random)
    {
        double currentOffsetX = 0.0D;
        double currentOffsetZ = 0.0D;
        for (int i = SEGMENTS - 1; i >= 0; i--)
        {
            offsetsX[i] = currentOffsetX;
            offsetsZ[i] = currentOffsetZ;
            currentOffsetX += random.nextInt(11) - 5;
            currentOffsetZ += random.nextInt(11) - 5;
        }
    }

    private void renderLightningLayers(Tessellator tessellator, double x, double y, double z, 
                                     double[] offsetsX, double[] offsetsZ, long seed)
    {
        Random layerRandom = new Random(seed);
        
        for (int layer = 0; layer < 4; layer++)
        {
            layerRandom.setSeed(seed); 
            for (int pass = 0; pass < 3; pass++)
            {
                int startSegment = pass > 0 ? 7 - pass : 7;
                int endSegment = pass > 0 ? startSegment - 2 : 0;
                
                double segmentOffsetX = offsetsX[startSegment];
                double segmentOffsetZ = offsetsZ[startSegment];

                renderLayerPass(tessellator, x, y, z, layer, pass, startSegment, endSegment, 
                              segmentOffsetX, segmentOffsetZ, layerRandom);
            }
        }
    }

    private void renderLayerPass(Tessellator tessellator, double x, double y, double z, int layer, int pass,
                                int startSegment, int endSegment, double segmentOffsetX, double segmentOffsetZ,
                                Random random)
    {
        for (int segment = startSegment; segment >= endSegment; segment--)
        {
            double prevOffsetX = segmentOffsetX;
            double prevOffsetZ = segmentOffsetZ;

            segmentOffsetX += (pass == 0 ? random.nextInt(11) - 5 : random.nextInt(31) - 15);
            segmentOffsetZ += (pass == 0 ? random.nextInt(11) - 5 : random.nextInt(31) - 15);

            tessellator.startDrawing(GL11.GL_QUAD_STRIP);
            tessellator.setColorRGBA_F(COLOR[0] * BASE_BRIGHTNESS, COLOR[1] * BASE_BRIGHTNESS, 
                                     COLOR[2] * BASE_BRIGHTNESS, COLOR[3]);

            double width = 0.1 + layer * 0.2;
            if (pass == 0) width *= segment * 0.1 + 1.0;
            double prevWidth = 0.1 + layer * 0.2;
            if (pass == 0) prevWidth *= (segment - 1) * 0.1 + 1.0;

            renderSegmentVertices(tessellator, x, y, z, segment, width, prevWidth, 
                                segmentOffsetX, segmentOffsetZ, prevOffsetX, prevOffsetZ);
            
            tessellator.draw();
        }
    }

    private void renderSegmentVertices(Tessellator tessellator, double x, double y, double z, int segment,
                                     double width, double prevWidth, double segmentOffsetX, double segmentOffsetZ,
                                     double prevOffsetX, double prevOffsetZ)
    {
        for (int vertex = 0; vertex < 5; vertex++)
        {
            double vertexX = (x + 0.5) - width;
            double vertexZ = (z + 0.5) - width;
            if (vertex == 1 || vertex == 2) vertexX += width * 2.0;
            if (vertex == 2 || vertex == 3) vertexZ += width * 2.0;

            double prevVertexX = (x + 0.5) - prevWidth;
            double prevVertexZ = (z + 0.5) - prevWidth;
            if (vertex == 1 || vertex == 2) prevVertexX += prevWidth * 2.0;
            if (vertex == 2 || vertex == 3) prevVertexZ += prevWidth * 2.0;

            tessellator.addVertex(prevVertexX + segmentOffsetX, y + (segment * SEGMENT_HEIGHT), 
                                prevVertexZ + segmentOffsetZ);
            tessellator.addVertex(vertexX + prevOffsetX, y + ((segment + 1) * SEGMENT_HEIGHT), 
                                vertexZ + prevOffsetZ);
        }
    }
}
