package lionking.entity;

public class LKEntityEggInfo {
    public final int spawnedID;
    public final String entityName;
    public final int primaryColor;
    public final int secondaryColor;

    public LKEntityEggInfo(int id, String name, int primary, int secondary) {
        this.spawnedID = id;
        this.entityName = name;
        this.primaryColor = primary;
        this.secondaryColor = secondary;
    }
}