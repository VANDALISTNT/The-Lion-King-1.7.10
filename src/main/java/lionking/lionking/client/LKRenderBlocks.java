package lionking.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import lionking.mod_LionKing;
import lionking.block.LKBlockVase;
import lionking.block.LKBlockKiwanoStem;

@SideOnly(Side.CLIENT)
public class LKRenderBlocks implements ISimpleBlockRenderingHandler {
    private final boolean renderIn3DInventory;

    public LKRenderBlocks(boolean renderIn3D) {
        this.renderIn3DInventory = renderIn3D;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int renderId, RenderBlocks renderer) {
        if (renderId == mod_LionKing.proxy.getGrindingBowlRenderID())
            return renderGrindingBowl(renderer, block, x, y, z);
        if (renderId == mod_LionKing.proxy.getPillarRenderID())
            return renderPillar(renderer, world, block, x, y, z);
        if (renderId == mod_LionKing.proxy.getStarAltarRenderID())
            return renderStarAltar(renderer, block, x, y, z);
        if (renderId == mod_LionKing.proxy.getVaseRenderID())
            return renderVase(renderer, world, block, x, y, z);
        if (renderId == mod_LionKing.proxy.getBugTrapRenderID())
            return renderBugTrap(renderer, world, block, x, y, z);
        if (renderId == mod_LionKing.proxy.getAridGrassRenderID())
            return renderAridGrass(renderer, world, block, x, y, z);
        if (renderId == mod_LionKing.proxy.getKiwanoBlockRenderID())
            return renderKiwano(renderer, block, x, y, z);
        if (renderId == mod_LionKing.proxy.getKiwanoStemRenderID())
            return renderKiwanoStem(renderer, world, block, x, y, z);
        if (renderId == mod_LionKing.proxy.getLeverRenderID())
            return renderLever(renderer, block, x, y, z);
        if (renderId == mod_LionKing.proxy.getLilyRenderID())
            return renderLily(renderer, world, block, x, y, z);
        return false;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int renderId, RenderBlocks renderer) {
        if (renderId == mod_LionKing.proxy.getPillarRenderID())
            renderInvPillar(renderer, block, metadata);
        else if (renderId == mod_LionKing.proxy.getStarAltarRenderID())
            renderInvStarAltar(renderer, block);
        else if (renderId == mod_LionKing.proxy.getBugTrapRenderID())
            renderInvBugTrap(renderer, block);
        else if (renderId == mod_LionKing.proxy.getKiwanoBlockRenderID())
            renderInvKiwano(renderer, block);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return renderIn3DInventory;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

    private boolean renderGrindingBowl(RenderBlocks renderer, Block block, int x, int y, int z) {
        renderer.setRenderBounds(0.0F, 0.25F, 0.0F, 1.0F, 0.625F, 0.1875F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.0F, 0.25F, 0.8125F, 1.0F, 0.625F, 1.0F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.0F, 0.25F, 0.1875F, 0.1875F, 0.625F, 0.8125F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.8125F, 0.25F, 0.1875F, 1.0F, 0.625F, 0.8125F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        return true;
    }

    private boolean renderPillar(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        int adjustedMetadata = (metadata > 3 && metadata < 8) ? metadata - 4 : metadata;
        float offset = 0.125F * adjustedMetadata;
        renderer.setRenderBounds(offset, 0.0F, offset, 1.0F - offset, 1.0F, 1.0F - offset);
        renderer.renderStandardBlock(block, x, y, z);
        return true;
    }

    private void renderInvPillar(RenderBlocks renderer, Block block, int metadata) {
        int adjustedMetadata = (metadata > 3 && metadata < 8) ? metadata - 4 : metadata;
        float offset = 0.125F * adjustedMetadata;
        renderStandardInvBlock(renderer, block, offset, 0.0F, offset, 1.0F - offset, 1.0F, 1.0F - offset, metadata);
    }

    private boolean renderStarAltar(RenderBlocks renderer, Block block, int x, int y, int z) {
        renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.125F, 0.625F, 0.125F, 0.875F, 0.75F, 0.875F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        return true;
    }

    private void renderInvStarAltar(RenderBlocks renderer, Block block) {
        renderStandardInvBlock(renderer, block, 0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        renderStandardInvBlock(renderer, block, 0.125F, 0.625F, 0.125F, 0.875F, 0.75F, 0.875F);
        renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
    }

    private boolean renderVase(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z) {
        float offset = 0.1875F;
        renderer.setRenderBounds(offset, 0.0F, offset, 1.0F - offset, 0.75F, 1.0F - offset);
        renderer.renderStandardBlock(block, x, y, z);
        
        int metadata = world.getBlockMetadata(x, y, z);
        renderPlantOverlay(renderer, world, block, x, y + 0.71875D, z, metadata, LKBlockVase.getPlantTextureFromMetadata(metadata));
        return true;
    }

    private boolean renderBugTrap(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z) {
        renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.0F, 0.9375F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderer.renderStandardBlock(block, x, y, z);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                float minX = i == 0 ? 0.0F : 0.875F;
                float minZ = j == 0 ? 0.0F : 0.875F;
                float maxX = i == 0 ? 0.125F : 1.0F;
                float maxZ = j == 0 ? 0.125F : 1.0F;
                renderer.setRenderBounds(minX, 0.0625F, minZ, maxX, 0.9375F, maxZ);
                renderer.renderStandardBlock(block, x, y, z);
            }
        }

        renderer.overrideBlockTexture = mod_LionKing.planks.getIcon(2, 0);
        renderer.setRenderBounds(0.46875F, 0.4375F, 0.46875F, 0.53125F, 0.9375F, 0.53125F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.clearOverrideBlockTexture();

        float progress = 0.3125F * (world.getBlockMetadata(x, y, z) / 8.0F);
        renderer.overrideBlockTexture = mod_LionKing.blockSilver.getIcon(2, 0);
        renderBugTrapBars(renderer, block, x, y, z, progress);
        
        renderer.clearOverrideBlockTexture();
        renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    private void renderBugTrapBars(RenderBlocks renderer, Block block, int x, int y, int z, float progress) {
        renderer.setRenderBounds(0.125F, 0.0625F, 0.06F, 0.1875F + progress, 0.9375F, 0.07F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.8125F - progress, 0.0625F, 0.06F, 0.875F, 0.9375F, 0.07F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.125F, 0.0625F, 0.94F, 0.1875F + progress, 0.9375F, 0.93F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.8125F - progress, 0.0625F, 0.94F, 0.875F, 0.9375F, 0.93F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.06F, 0.0625F, 0.125F, 0.07F, 0.9375F, 0.1875F + progress);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.06F, 0.0625F, 0.8125F - progress, 0.07F, 0.9375F, 0.875F);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.94F, 0.0625F, 0.125F, 0.93F, 0.9375F, 0.1875F + progress);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.94F, 0.0625F, 0.8125F - progress, 0.93F, 0.9375F, 0.875F);
        renderer.renderStandardBlock(block, x, y, z);
    }

    private void renderInvBugTrap(RenderBlocks renderer, Block block) {
        renderStandardInvBlock(renderer, block, 0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        renderStandardInvBlock(renderer, block, 0.0F, 0.9375F, 0.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                float minX = i == 0 ? 0.0F : 0.875F;
                float minZ = j == 0 ? 0.0F : 0.875F;
                float maxX = i == 0 ? 0.125F : 1.0F;
                float maxZ = j == 0 ? 0.125F : 1.0F;
                renderStandardInvBlock(renderer, block, minX, 0.0625F, minZ, maxX, 0.9375F, maxZ);
            }
        }

        renderer.overrideBlockTexture = mod_LionKing.planks.getIcon(2, 0);
        renderStandardInvBlock(renderer, block, 0.46875F, 0.4375F, 0.46875F, 0.53125F, 0.9375F, 0.53125F);
        renderer.clearOverrideBlockTexture();

        renderer.overrideBlockTexture = mod_LionKing.blockSilver.getIcon(2, 0);
        renderStandardInvBlock(renderer, block, 0.125F, 0.0625F, 0.06F, 0.1875F, 0.9375F, 0.07F);
        renderStandardInvBlock(renderer, block, 0.8125F, 0.0625F, 0.06F, 0.875F, 0.9375F, 0.07F);
        renderStandardInvBlock(renderer, block, 0.125F, 0.0625F, 0.94F, 0.1875F, 0.9375F, 0.93F);
        renderStandardInvBlock(renderer, block, 0.8125F, 0.0625F, 0.94F, 0.875F, 0.9375F, 0.93F);
        renderStandardInvBlock(renderer, block, 0.06F, 0.0625F, 0.125F, 0.07F, 0.9375F, 0.1875F);
        renderStandardInvBlock(renderer, block, 0.06F, 0.0625F, 0.8125F, 0.07F, 0.9375F, 0.875F);
        renderStandardInvBlock(renderer, block, 0.94F, 0.0625F, 0.125F, 0.93F, 0.9375F, 0.1875F);
        renderStandardInvBlock(renderer, block, 0.94F, 0.0625F, 0.8125F, 0.93F, 0.9375F, 0.875F);
        renderer.clearOverrideBlockTexture();

        renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    private boolean renderAridGrass(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        applyColorMultiplier(tessellator, block.colorMultiplier(world, x, y, z));

        double posX = x + getRandomOffset(x, z, y, 16, 0.5D);
        double posY = y + getRandomOffset(x, z, y, 20, 0.2D) - 0.8D;
        double posZ = z + getRandomOffset(x, z, y, 24, 0.5D);

        renderer.drawCrossedSquares(block.getIcon(0, world.getBlockMetadata(x, y, z)), posX, posY, posZ, 1.0F);
        return true;
    }

    private boolean renderKiwano(RenderBlocks renderer, Block block, int x, int y, int z) {
        renderer.setRenderBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.8125F, 0.875F);
        renderer.renderStandardBlock(block, x, y, z);

        renderer.overrideBlockTexture = block.getIcon(0, -1);
        renderKiwanoSpikes(renderer, block, x, y, z);
        renderer.clearOverrideBlockTexture();
        return true;
    }

    private void renderKiwanoSpikes(RenderBlocks renderer, Block block, int x, int y, int z) {
        final int[][] X_OFFSETS = {
            {9, 4, 12, 7, 4, 11}, {6, 10, 3, 8, 12, 4}, {4, 12, 6, 10, 4, 11},
            {5, 3, 7, 12, 10, 3}, {4, 11, 6, 9, 5, 10}
        };
        final int[][] Y_OFFSETS = {
            {11, 9, 6, 5, 2, 1}, {10, 8, 6, 4, 2, 1}, {11, 10, 7, 6, 2, 1},
            {10, 9, 7, 6, 2, 1}, {11, 10, 8, 6, 4, 3}
        };

        for (int i = 0; i < 6; i++) {
            renderSpike(renderer, block, x, y, z, X_OFFSETS[0][i], Y_OFFSETS[0][i], 0.875F, 0.9375F, false);
            renderSpike(renderer, block, x, y, z, X_OFFSETS[1][i], Y_OFFSETS[1][i], 0.0625F, 0.125F, false);
            renderSpike(renderer, block, x, y, z, X_OFFSETS[2][i], Y_OFFSETS[2][i], 0.875F, 0.9375F, true);
            renderSpike(renderer, block, x, y, z, X_OFFSETS[3][i], Y_OFFSETS[3][i], 0.0625F, 0.125F, true);
            renderSpike(renderer, block, x, y, z, X_OFFSETS[4][i], Y_OFFSETS[4][i], 0.8125F, 0.875F, false);
        }
    }

    private void renderSpike(RenderBlocks renderer, Block block, int x, int y, int z, int xOffset, int yOffset, float minZ, float maxZ, boolean swapXZ) {
        float px = xOffset / 16.0F;
        float py = yOffset / 16.0F;

        if (swapXZ) {
            renderer.setRenderBounds(minZ, py, px, maxZ, py + 0.0625F, px + 0.0625F);
        } else {
            renderer.setRenderBounds(px, py, minZ, px + 0.0625F, py + 0.0625F, maxZ);
        }

        if (renderer.blockAccess != null) {
            renderer.renderStandardBlock(block, x, y, z);
            } else {
            Tessellator tess = Tessellator.instance;
            IIcon icon = renderer.overrideBlockTexture != null ? renderer.overrideBlockTexture : block.getBlockTextureFromSide(1);

            double u1 = icon.getMinU();
            double v1 = icon.getMinV();
            double u2 = icon.getMaxU();
            double v2 = icon.getMaxV();

            tess.startDrawingQuads();
            tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);

            if (swapXZ) {
                tess.addVertexWithUV(minZ, py + 0.0625F, px, u1, v1);
                tess.addVertexWithUV(maxZ, py + 0.0625F, px, u2, v1);
                tess.addVertexWithUV(maxZ, py + 0.0625F, px + 0.0625F, u2, v2);
                tess.addVertexWithUV(minZ, py + 0.0625F, px + 0.0625F, u1, v2);
            } else {
                tess.addVertexWithUV(px, py + 0.0625F, minZ, u1, v1);
                tess.addVertexWithUV(px + 0.0625F, py + 0.0625F, minZ, u2, v1);
                tess.addVertexWithUV(px + 0.0625F, py + 0.0625F, maxZ, u2, v2);
                tess.addVertexWithUV(px, py + 0.0625F, maxZ, u1, v2);
            }

            tess.draw();
       }
    }

    private void renderInvKiwano(RenderBlocks renderer, Block block) {
        renderer.setRenderBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.8125F, 0.875F);
        renderStandardInvBlock(renderer, block, 0.125F, 0.0F, 0.125F, 0.875F, 0.8125F, 0.875F);

        renderer.overrideBlockTexture = block.getIcon(0, -1);
        renderKiwanoSpikes(renderer, block, 0, 0, 0);
        renderer.clearOverrideBlockTexture();
    }

    private boolean renderKiwanoStem(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z) {
        LKBlockKiwanoStem stem = (LKBlockKiwanoStem) block;
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(stem.getMixedBrightnessForBlock(world, x, y, z));
        applyColorMultiplier(tessellator, stem.colorMultiplier(world, x, y, z));
        stem.setBlockBoundsBasedOnState(world, x, y, z);

        int state = stem.getState(world, x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        BlockStem pumpkinStem = (BlockStem) Blocks.pumpkin_stem;

        if (state < 0) {
            renderer.renderBlockStemSmall(pumpkinStem, metadata, renderer.renderMaxY, x, y, z);
        } else {
            renderer.renderBlockStemSmall(pumpkinStem, metadata, 0.5D, x, y, z);
            renderer.renderBlockStemBig(pumpkinStem, metadata, state, renderer.renderMaxY, x, y, z);
        }
        return true;
    }

    private boolean renderLever(RenderBlocks renderer, Block block, int x, int y, int z) {
        int metadata = renderer.blockAccess.getBlockMetadata(x, y, z);
        int direction = metadata & 7;
        boolean isActive = (metadata & 8) > 0;

        IIcon baseTexture = renderer.hasOverrideBlockTexture() ? renderer.overrideBlockTexture : mod_LionKing.pridestone.getIcon(2, 0);
        renderer.overrideBlockTexture = baseTexture;
        renderLeverBase(renderer, block, x, y, z, direction);
        renderer.clearOverrideBlockTexture();

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        renderLeverHandle(renderer, block, x, y, z, direction, isActive);
        return true;
    }

    private void renderLeverBase(RenderBlocks renderer, Block block, int x, int y, int z, int direction) {
        float width = 0.1875F;
        float depth = 0.25F;
        float height = 0.1875F;

        switch (direction) {
            case 5: renderer.setRenderBounds(0.5F - width, 0.0F, 0.5F - depth, 0.5F + width, height, 0.5F + depth); break;
            case 6: renderer.setRenderBounds(0.5F - depth, 0.0F, 0.5F - width, 0.5F + depth, height, 0.5F + width); break;
            case 4: renderer.setRenderBounds(0.5F - width, 0.5F - depth, 1.0F - height, 0.5F + width, 0.5F + depth, 1.0F); break;
            case 3: renderer.setRenderBounds(0.5F - width, 0.5F - depth, 0.0F, 0.5F + width, 0.5F + depth, height); break;
            case 2: renderer.setRenderBounds(1.0F - height, 0.5F - depth, 0.5F - width, 1.0F, 0.5F + depth, 0.5F + width); break;
            case 1: renderer.setRenderBounds(0.0F, 0.5F - depth, 0.5F - width, height, 0.5F + depth, 0.5F + width); break;
            case 0: renderer.setRenderBounds(0.5F - depth, 1.0F - height, 0.5F - width, 0.5F + depth, 1.0F, 0.5F + width); break;
            case 7: renderer.setRenderBounds(0.5F - width, 1.0F - height, 0.5F - depth, 0.5F + width, 1.0F, 0.5F + depth); break;
        }
        renderer.renderStandardBlock(block, x, y, z);
    }

    private void renderLeverHandle(RenderBlocks renderer, Block block, int x, int y, int z, int direction, boolean isActive) {
        IIcon texture = renderer.hasOverrideBlockTexture() ? renderer.overrideBlockTexture : block.getIcon(0, 0);
        double uMin = texture.getMinU();
        double uMax = texture.getMaxU();
        double vMin = texture.getMinV();
        double vMax = texture.getMaxV();

        Vec3[] vertices = createLeverHandleVertices(renderer, x, y, z, direction, isActive);
        renderLeverHandleFaces(Tessellator.instance, vertices, texture, uMin, uMax, vMin, vMax);
    }

    private Vec3[] createLeverHandleVertices(RenderBlocks renderer, int x, int y, int z, int direction, boolean isActive) {
        Vec3[] vertices = new Vec3[8];
        float width = 0.0625F;
        float depth = 0.0625F;
        float height = 0.625F;

        for (int i = 0; i < 8; i++) {
            double xOffset = (i & 1) == 0 ? -width : width;
            double zOffset = (i & 2) == 0 ? -depth : depth;
            double yOffset = (i & 4) == 0 ? 0.0D : height;
            vertices[i] = Vec3.createVectorHelper(xOffset, yOffset, zOffset);
        }

        float tiltAngle = (float) Math.PI * 2.0F / 9.0F;
        for (Vec3 vertex : vertices) {
            vertex.zCoord += isActive ? -0.0625D : 0.0625D;
            vertex.rotateAroundX(isActive ? tiltAngle : -tiltAngle);
            
            if (direction == 0 || direction == 7) vertex.rotateAroundZ((float) Math.PI);
            if (direction == 6 || direction == 0) vertex.rotateAroundY((float) Math.PI / 2.0F);

            if (direction >= 1 && direction <= 4) {
                vertex.yCoord -= 0.375D;
                vertex.rotateAroundX((float) Math.PI / 2.0F);
                if (direction == 2) vertex.rotateAroundY((float) Math.PI / 2.0F);
                if (direction == 1) vertex.rotateAroundY(-(float) Math.PI / 2.0F);
                if (direction == 3) vertex.rotateAroundY((float) Math.PI);
                vertex.addVector(x + 0.5D, y + 0.5D, z + 0.5D);
            } else if (direction == 0 || direction == 7) {
                vertex.addVector(x + 0.5D, y + 0.875F, z + 0.5D);
            } else {
                vertex.addVector(x + 0.5D, y + 0.125F, z + 0.5D);
            }
        }
        return vertices;
    }

    private void renderLeverHandleFaces(Tessellator tessellator, Vec3[] vertices, IIcon texture, double uMin, double uMax, double vMin, double vMax) {
        int[][] faceIndices = {{0, 1, 2, 3}, {7, 6, 5, 4}, {1, 0, 4, 5}, {2, 1, 5, 6}, {3, 2, 6, 7}, {0, 3, 7, 4}};
        double[][] uvCoords = {
            {uMin, vMax, uMax, vMin}, {uMin, vMax, uMax, vMin}, {uMin, vMax, uMax, vMin},
            {uMin, vMax, uMax, vMin}, {uMin, vMax, uMax, vMin}, {uMin, vMax, uMax, vMin}
        };

        for (int i = 0; i < 6; i++) {
            if (i == 0 || i == 2) {
                uvCoords[i][0] = texture.getInterpolatedU(7.0D);
                uvCoords[i][1] = i == 0 ? texture.getInterpolatedV(6.0D) : texture.getMaxV();
                uvCoords[i][2] = texture.getInterpolatedU(9.0D);
                uvCoords[i][3] = i == 0 ? texture.getInterpolatedV(8.0D) : texture.getMinV();
            }
            tessellator.addVertexWithUV(vertices[faceIndices[i][0]].xCoord, vertices[faceIndices[i][0]].yCoord, vertices[faceIndices[i][0]].zCoord, uvCoords[i][0], uvCoords[i][3]);
            tessellator.addVertexWithUV(vertices[faceIndices[i][1]].xCoord, vertices[faceIndices[i][1]].yCoord, vertices[faceIndices[i][1]].zCoord, uvCoords[i][2], uvCoords[i][3]);
            tessellator.addVertexWithUV(vertices[faceIndices[i][2]].xCoord, vertices[faceIndices[i][2]].yCoord, vertices[faceIndices[i][2]].zCoord, uvCoords[i][2], uvCoords[i][1]);
            tessellator.addVertexWithUV(vertices[faceIndices[i][3]].xCoord, vertices[faceIndices[i][3]].yCoord, vertices[faceIndices[i][3]].zCoord, uvCoords[i][0], uvCoords[i][1]);
        }
    }

    private boolean renderLily(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z) {
        renderer.renderBlockLilyPad(Blocks.waterlily, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        renderer.drawCrossedSquares(mod_LionKing.lily.getIcon(0, world.getBlockMetadata(x, y, z)), x, y + 0.005D, z, 0.75F);
        return true;
    }

    private void renderStandardInvBlock(RenderBlocks renderer, Block block, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        renderStandardInvBlock(renderer, block, minX, minY, minZ, maxX, maxY, maxZ, 0);
    }

    private void renderStandardInvBlock(RenderBlocks renderer, Block block, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, int metadata) {
        Tessellator tessellator = Tessellator.instance;
        renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    private void applyColorMultiplier(Tessellator tessellator, int color) {
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable) {
            float gray = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float redGray = (r * 30.0F + g * 70.0F) / 100.0F;
            float blueGray = (r * 30.0F + b * 70.0F) / 100.0F;
            r = gray;
            g = redGray;
            b = blueGray;
        }
        tessellator.setColorOpaque_F(r, g, b);
    }

    private double getRandomOffset(int x, int z, int y, int bitShift, double scale) {
        long seed = (x * 3129871L) ^ (z * 116129781L) ^ y;
        seed = seed * seed * 42317861L + seed * 11L;
        return ((float) (seed >> bitShift & 15L) / 15.0F - 0.5D) * scale;
    }

    private void renderPlantOverlay(RenderBlocks renderer, IBlockAccess world, Block block, double x, double y, double z, int metadata, IIcon plantTexture) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)x, (int)y, (int)z));
        applyColorMultiplier(tessellator, 0xFFFFFF);
        
        renderer.overrideBlockTexture = plantTexture;
        renderer.drawCrossedSquares(plantTexture, x, y, z, 1.0F);
        renderer.clearOverrideBlockTexture();
    }
}
