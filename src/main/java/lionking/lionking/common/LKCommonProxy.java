package lionking.common;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import lionking.item.LKItemDartQuiver;
import lionking.entity.LKEntityTimon;
import lionking.entity.LKEntitySimba;
import lionking.tileentity.LKTileEntityDrum;
import lionking.tileentity.LKTileEntityBugTrap;
import lionking.tileentity.LKTileEntityGrindingBowl;
import lionking.inventory.LKContainerGrindingBowl;
import lionking.inventory.LKContainerSimba;
import lionking.inventory.LKContainerTimon;
import lionking.inventory.LKInventoryQuiver;
import lionking.inventory.LKContainerQuiver;
import lionking.inventory.LKContainerItemInfo;
import lionking.inventory.LKContainerBugTrap;
import lionking.inventory.LKContainerDrum;

public class LKCommonProxy implements IGuiHandler {
    
    public static final int GUI_ID_BOWL = 0;
    public static final int GUI_ID_SIMBA = 1;
    public static final int GUI_ID_TIMON = 2;
    public static final int GUI_ID_QUIVER = 3;
    public static final int GUI_ID_QUESTS = 4;
    public static final int GUI_ID_ACHIEVEMENTS = 5;
    public static final int GUI_ID_TRAP = 6;
    public static final int GUI_ID_DRUM = 7;

    public void onPreload() {
    }

    public void onLoad() {
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case GUI_ID_BOWL:
                TileEntity bowl = world.getTileEntity(x, y, z); 
                if (bowl instanceof LKTileEntityGrindingBowl) {
                    return new LKContainerGrindingBowl(player, (LKTileEntityGrindingBowl) bowl);
                }
                break;
            case GUI_ID_SIMBA:
                Entity simba = getEntityFromID(x, world);
                if (simba instanceof LKEntitySimba) {
                    return new LKContainerSimba(player, (LKEntitySimba) simba);
                }
                break;
            case GUI_ID_TIMON:
                Entity timon = getEntityFromID(x, world);
                if (timon instanceof LKEntityTimon) {
                    return new LKContainerTimon(player, (LKEntityTimon) timon);
                }
                break;
            case GUI_ID_QUIVER:
                LKInventoryQuiver quiver = LKItemDartQuiver.getQuiverInventory(x, world);
                if (quiver != null) {
                    return new LKContainerQuiver(player, quiver);
                }
                break;
            case GUI_ID_QUESTS:
                return new LKContainerItemInfo(player);
            case GUI_ID_TRAP:
                TileEntity trap = world.getTileEntity(x, y, z); 
                if (trap instanceof LKTileEntityBugTrap) {
                    return new LKContainerBugTrap(player, (LKTileEntityBugTrap) trap);
                }
                break;
            case GUI_ID_DRUM:
                TileEntity drum = world.getTileEntity(x, y, z); 
                if (drum instanceof LKTileEntityDrum) {
                    return new LKContainerDrum(player, world, (LKTileEntityDrum) drum);
                }
                break;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null; 
    }

    public Entity getEntityFromID(int entityId, World world) {
        for (Object obj : world.loadedEntityList) {
            Entity entity = (Entity) obj;
            if (entity.getEntityId() == entityId) { 
                return entity;
            }
        }
        return null;
    }

    public int getGrindingBowlRenderID() { return 0; }
    public int getPillarRenderID() { return 0; }
    public int getStarAltarRenderID() { return 0; }
    public int getVaseRenderID() { return 0; }
    public int getBugTrapRenderID() { return 0; }
    public int getAridGrassRenderID() { return 0; }
    public int getKiwanoBlockRenderID() { return 0; }
    public int getKiwanoStemRenderID() { return 0; }
    public int getLeverRenderID() { return 0; }
    public int getLilyRenderID() { return 0; }
    public int getRugRenderID() { return 0; }

    public void setInPrideLandsPortal(EntityPlayer player) {
        LKTickHandlerServer.playersInPortals.putIfAbsent(player, 0);
    }

    public void setInOutlandsPortal(EntityPlayer player) {
        LKTickHandlerServer.playersInOutPortals.putIfAbsent(player, 0);
    }

    public void playPortalFXForUpendi(World world) {
    }

    public void spawnParticle(String type, double x, double y, double z, double vx, double vy, double vz) {
    }

    public EntityPlayer getSPPlayer() {
        return null;
    }
}
