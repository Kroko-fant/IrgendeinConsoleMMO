package data;

public class PlayerData {

    private String name;
    private int level, xp, gold, health, physicalDamage, physicalDefence, magicDamage, magicDefence, speed, luck;

    public PlayerData(String name, int level, int xp, int gold, int health, int physicalDamage, int physicalDefence, int magicDamage, int magicDefence, int speed, int luck) {
        this.name = name;
        this.level = level;
        this.xp = xp;
        this.gold = gold;
        this.health = health;
        this.physicalDamage = physicalDamage;
        this.physicalDefence = physicalDefence;
        this.magicDamage = magicDamage;
        this.magicDefence = magicDefence;
        this.speed = speed;
        this.luck = luck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getPhysicalDamage() {
        return physicalDamage;
    }

    public void setPhysicalDamage(int physicalDamage) {
        this.physicalDamage = physicalDamage;
    }

    public int getPhysicalDefence() {
        return physicalDefence;
    }

    public void setPhysicalDefence(int physicalDefence) {
        this.physicalDefence = physicalDefence;
    }

    public int getMagicDamage() {
        return magicDamage;
    }

    public void setMagicDamage(int magicDamage) {
        this.magicDamage = magicDamage;
    }

    public int getMagicDefence() {
        return magicDefence;
    }

    public void setMagicDefence(int magicDefence) {
        this.magicDefence = magicDefence;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }
}
