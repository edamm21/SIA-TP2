public class Equipment {

    EquipmentType type;
    float strength;
    float agility;
    float resistance;
    float health;
    float skill; // Pericia, me pareció la traducción más acorde

    public Equipment(EquipmentType type, float strength, float agility, float resistance, float health, float skill) {
        this.type = type;
        this.strength = strength;
        this.agility = agility;
        this.resistance = resistance;
        this.health = health;
        this.skill = skill;
    }

    public EquipmentType getType() {
        return type;
    }

    public float getStrength() {
        return strength;
    }

    public float getAgility() {
        return agility;
    }

    public float getResistance() {
        return resistance;
    }

    public float getHealth() {
        return health;
    }

    public float getSkill() {
        return skill;
    }
}
