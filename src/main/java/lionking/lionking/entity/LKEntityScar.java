package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;
import java.util.List; 

import lionking.common.LKCharacter;
import lionking.common.LKAngerable;
import lionking.common.LKIngame;
import lionking.common.LKAchievementList;
import lionking.common.LKLevelData;
import lionking.entity.LKEntities;
import lionking.mod_LionKing;

public class LKEntityScar extends EntityCreature implements LKCharacter, LKAngerable, IMob {
    
    private static final float WIDTH = 1.3F;
    private static final float HEIGHT = 1.6F;
    private static final double MAX_HEALTH = 250.0D;
    private static final double MOVEMENT_SPEED = 0.25D;
    private static final double ATTACK_SPEED = 1.4D;
    private static final double WANDER_SPEED = 1.0D;
    private static final float WATCH_RANGE = 8.0F;
    private static final float ATTACK_DAMAGE = 6.0F;
    private static final float ATTACK_RANGE = 2.4F;
    private static final int ATTACK_COOLDOWN = 20;
    private static final double DETECTION_RANGE = 16.0D;
    private static final double SPEECH_RANGE = 8.0D;
    private static final float HYENA_SPAWN_HEALTH_THRESHOLD = 175.0F;
    private static final float EXPLOSION_HEALTH_THRESHOLD = 70.0F;

    public LKEntityScar(World world) {
        super(world);
        setSize(WIDTH, HEIGHT);
        getNavigator().setAvoidsWater(true);
        initializeAITasks();
    }

    private void initializeAITasks() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LKEntityAILionAttack(this, EntityPlayer.class, ATTACK_SPEED, false));
        tasks.addTask(2, new EntityAIWander(this, WANDER_SPEED));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, WATCH_RANGE));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, new LKEntityAIAngerableAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HEALTH);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MOVEMENT_SPEED);
    }

    public boolean isHostile() {
        return isScarHostile();
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        return 0;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(16, (byte) 0);
        dataWatcher.addObject(17, (byte) 0);
        dataWatcher.addObject(18, (byte) 0);
        dataWatcher.addObject(19, (byte) 0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("IsHostile", isScarHostile());
        nbt.setBoolean("HasFirstSpoken", getHasFirstSpoken());
        nbt.setBoolean("HasSpawnedHyenas", getHasSpawnedHyenas());
        nbt.setBoolean("HasMadeExplosions", getHasMadeExplosions());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        setScarHostile(nbt.getBoolean("IsHostile"));
        setHasFirstSpoken(nbt.getBoolean("HasFirstSpoken"));
        setHasSpawnedHyenas(nbt.getBoolean("HasSpawnedHyenas"));
        setHasMadeExplosions(nbt.getBoolean("HasMadeExplosions"));
    }

    public boolean isScarHostile() {
        return dataWatcher.getWatchableObjectByte(16) == 1;
    }

    public void setScarHostile(boolean hostile) {
        dataWatcher.updateObject(16, (byte) (hostile ? 1 : 0));
    }

    public boolean getHasFirstSpoken() {
        return dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public void setHasFirstSpoken(boolean spoken) {
        dataWatcher.updateObject(17, (byte) (spoken ? 1 : 0));
    }

    public boolean getHasSpawnedHyenas() {
        return dataWatcher.getWatchableObjectByte(18) == 1;
    }

    public void setHasSpawnedHyenas(boolean spawned) {
        dataWatcher.updateObject(18, (byte) (spawned ? 1 : 0));
    }

    public boolean getHasMadeExplosions() {
        return dataWatcher.getWatchableObjectByte(19) == 1;
    }

    public void setHasMadeExplosions(boolean exploded) {
        dataWatcher.updateObject(19, (byte) (exploded ? 1 : 0));
    }

    @Override
    protected Entity findPlayerToAttack() {
        if (!isScarHostile()) return null;
        EntityPlayer player = worldObj.getClosestVulnerablePlayerToEntity(this, DETECTION_RANGE);
        return (player != null && canEntityBeSeen(player)) ? player : null;
    }

    @Override
    protected void attackEntity(Entity entity, float distance) {
        if (attackTime <= 0 && distance < ATTACK_RANGE && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY) {
            attackTime = ATTACK_COOLDOWN;
            attackEntityAsMob(entity);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), ATTACK_DAMAGE);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack item = player.inventory.getCurrentItem();
            if (item == null || Item.getIdFromItem(item.getItem()) != Item.getIdFromItem(mod_LionKing.rafikiStick)) {
                amount = 0.0F;
            }
            if (!getHasFirstSpoken()) {
                triggerFirstSpeech(player);
                becomeAngryAt(player);
            }
        } else {
            amount = 0.0F;
        }
        return super.attackEntityFrom(source, amount);
    }

    private void triggerFirstSpeech(EntityPlayer player) {
        if (!worldObj.isRemote) {
            String scarName = StatCollector.translateToLocal("entity.lionking.scar.name");
            String message = StatCollector.translateToLocal("chat.scar.initial");
            LKIngame.sendMessageToAllPlayers("§e<" + scarName + "> §f" + message);
        }
        setHasFirstSpoken(true);
    }

    private void becomeAngryAt(Entity entity) {
        entityToAttack = entity;
        setScarHostile(true);
        worldObj.playSoundAtEntity(this, "lionking:lionangry", getSoundVolume() * 2.0F, pitchVariation(1.8F));
    }

    private float pitchVariation(float basePitch) {
        return ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * basePitch;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!getHasFirstSpoken()) {
            checkForFirstSpeech();
        } else {
            handleHostileBehavior();
        }
    }

    private void checkForFirstSpeech() {
        EntityPlayer player = worldObj.getClosestVulnerablePlayerToEntity(this, SPEECH_RANGE);
        if (player != null) {
            Vec3 playerLook = player.getLook(1.0F).normalize();
            Vec3 scarToPlayer = Vec3.createVectorHelper(posX - player.posX, boundingBox.minY + height / 2.0F - (player.posY + player.getEyeHeight()), posZ - player.posZ);
            double distance = scarToPlayer.lengthVector();
            scarToPlayer = scarToPlayer.normalize();
            double dotProduct = playerLook.dotProduct(scarToPlayer);
            if (dotProduct > 1.0D - 0.025D / distance && player.canEntityBeSeen(this)) {
                triggerFirstSpeech(player);
                becomeAngryAt(player);
            }
        }
    }

    private void handleHostileBehavior() {
        spawnAmbientParticles();
        EntityPlayer player = worldObj.getClosestPlayerToEntity(this, 128.0D);
        if (player == null) return;

        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);

        if (getHealth() <= HYENA_SPAWN_HEALTH_THRESHOLD && !getHasSpawnedHyenas()) {
            spawnInitialHyenas(x, y, z);
        }

        if (!worldObj.isRemote && getHasSpawnedHyenas() && rand.nextInt(200) == 0) {
            spawnAdditionalHyena(x, y, z);
        }

        if (getHealth() <= EXPLOSION_HEALTH_THRESHOLD && !getHasMadeExplosions()) {
            triggerExplosions(x, y, z);
        }
    }

    private void spawnAmbientParticles() {
        if (rand.nextInt(3) != 0) {
            double dx = rand.nextGaussian() * 0.02D;
            double dy = rand.nextGaussian() * 0.02D;
            double dz = rand.nextGaussian() * 0.02D;
            String particle = rand.nextInt(3) == 0 ? "smoke" : "flame";
            double px = posX + (rand.nextFloat() * width * 2.0F - width);
            double py = posY + rand.nextFloat() * height;
            double pz = posZ + (rand.nextFloat() * width * 2.0F - width);
            worldObj.spawnParticle(particle, px, py, pz, dx, dy, dz);
        }
    }

    private void spawnInitialHyenas(int x, int y, int z) {
        if (!worldObj.isRemote) {
            String scarName = StatCollector.translateToLocal("entity.lionking.scar.name");
            String message = StatCollector.translateToLocal("chat.scar.hyenas");
            LKIngame.sendMessageToAllPlayers("§e<" + scarName + "> §f" + message);
            for (int i = 0; i < 3; i++) {
                LKEntityHyena hyena = new LKEntityHyena(worldObj);
                hyena.setLocationAndAngles(x, y + 1, z, rotationYaw, rotationPitch);
                worldObj.spawnEntityInWorld(hyena);
            }
        }
        setHasSpawnedHyenas(true);
    }

    private void spawnAdditionalHyena(int x, int y, int z) {
        List<?> hyenas = worldObj.getEntitiesWithinAABB(LKEntityHyena.class, boundingBox.expand(16.0F, 16.0F, 16.0F)); 
        if (hyenas.size() < 6) {
            LKEntityHyena hyena = new LKEntityHyena(worldObj);
            hyena.setLocationAndAngles(x, y + 1, z, rotationYaw, rotationPitch);
            worldObj.spawnEntityInWorld(hyena);
        }
    }

    private void triggerExplosions(int x, int y, int z) {
        if (!worldObj.isRemote) {
            String scarName = StatCollector.translateToLocal("entity.lionking.scar.name");
            String message = StatCollector.translateToLocal("chat.scar.explosions");
            LKIngame.sendMessageToAllPlayers("§e<" + scarName + "> §f" + message);
            for (int dx = -8; dx < 9; dx++) {
                for (int dy = -8; dy < 9; dy++) {
                    for (int dz = -8; dz < 8; dz++) {
                        Block block = worldObj.getBlock(x + dx, y + dy, z + dz);
                        if (block != null && block.isOpaqueCube() && worldObj.isAirBlock(x + dx, y + dy + 1, z + dz) && rand.nextInt(5) == 0) {
                            worldObj.setBlock(x + dx, y + dy + 1, z + dz, Blocks.fire, 0, 3);
                        }
                        if (worldObj.isAirBlock(x + dx, y + dy, z + dz) && rand.nextInt(40) == 0) {
                            worldObj.createExplosion(this, x + dx, y + dy, z + dz, 0.0F, false);
                        }
                    }
                }
            }
        }
        setHasMadeExplosions(true);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (source.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source.getEntity();
            player.triggerAchievement(LKAchievementList.killScar);
            handleDeathEffects(player);
        }
    }

    private void handleDeathEffects(EntityPlayer player) {
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);

        extinguishFiresAndLava(x, y, z);
        killNearbyHyenas();
        createDeathPlatform(x, y, z);
        dropDeathLoot(x, y, z);

        LKLevelData.setDefeatedScar(true);
    }

    private void extinguishFiresAndLava(int x, int y, int z) {
        for (int dx = -5; dx < 6; dx++) {
            for (int dy = -5; dy < 6; dy++) {
                for (int dz = -5; dz < 6; dz++) {
                    Block block = worldObj.getBlock(x + dx, y + dy, z + dz);
                    if (block == Blocks.fire || block == Blocks.flowing_lava || block == Blocks.lava) {
                        worldObj.setBlockToAir(x + dx, y + dy, z + dz);
                    }
                }
            }
        }
    }

    private void killNearbyHyenas() {
        List<?> hyenas = worldObj.getEntitiesWithinAABB(LKEntityHyena.class, boundingBox.expand(18.0F, 18.0F, 18.0F)); 
        for (Object obj : hyenas) {
            if (!worldObj.isRemote) {
                ((LKEntityHyena) obj).setDead();
            }
        }
    }

    private void createDeathPlatform(int x, int y, int z) {
        for (int dx = -3; dx < 4; dx++) {
            for (int dz = -3; dz < 4; dz++) {
                worldObj.setBlock(x + dx, y - 1, z + dz, mod_LionKing.prideBrick, 0, 3);
            }
        }
    }

    private void dropDeathLoot(int x, int y, int z) {
        if (!worldObj.isRemote) {
            worldObj.createExplosion(this, x, y, z, 0.0F, false);
            int boneCount = rand.nextInt(7) + 7;
            int meatCount = rand.nextInt(7) + 7;
            for (int i = 0; i < boneCount; i++) dropItem(mod_LionKing.hyenaBone, 1);
            for (int i = 0; i < meatCount; i++) dropItem(mod_LionKing.lionCooked, 1);
            for (int i = 0; i < 20; i++) {
                worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, rand.nextInt(30) + 36));
            }
            dropItem(mod_LionKing.scarRug, 1);
        }
    }

    @Override
    protected String getLivingSound() {
        return "lionking:lion";
    }

    @Override
    protected String getHurtSound() {
        return "lionking:lionroar";
    }

    @Override
    protected String getDeathSound() {
        return "lionking:lionangry";
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }

    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.scar.name");
    }
}