public class Equipment {

    EquipmentType type;
    double strength;
    double agility;
    double resistance;
    double health;
    double skill;

    public Equipment(EquipmentType type, double str, double ag, double res, double hp, double skl) {
        this.type = type;
        this.strength = str;
        this.agility = ag;
        this.resistance = res;
        this.health = hp;
        this.skill = skl;
    }

    public EquipmentType getType() {
        return type;
    }

    public double getStrength() {
        return strength;
    }

    public double getAgility() {
        return agility;
    }

    public double getResistance() {
        return resistance;
    }

    public double getHealth() {
        return health;
    }

    public double getSkill() {
        return skill;
    }
}
