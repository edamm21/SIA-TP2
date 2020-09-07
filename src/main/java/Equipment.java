import Enums.EquipmentType;

public class Equipment implements Comparable{

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

    public double getSum() {
        return this.getStrength() + this.getAgility() + this.getHealth() + this.getResistance() + this.getSkill();
    }

    @Override
    public String toString() {
        return this.type.toString() + " AG:" + this.agility +
                " HP: " + this.health + " RES: " + this.resistance +
                " SKL: " + this.skill + " STR: " + this.strength;
    }
    
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Equipment)
		{
			Equipment b = (Equipment) o;
			return type.equals(b.getType()) && strength == b.getStrength() && agility == b.getAgility() && resistance == b.getResistance() && health == b.getHealth() && skill == b.getSkill();
		}
		return false;
	}

    @Override
    public int compareTo(Object o) {
        Equipment other = (Equipment) o;
        double sum1 = this.getStrength() + this.getAgility() + this.getHealth() + this.getResistance() + this.getSkill();
        double sum2 = other.getStrength() + other.getAgility() + other.getHealth() + other.getResistance() + other.getSkill();
        return Double.compare(sum1, sum2);
    }
    
	@Override
	public int hashCode() {
		int hash = 7;
	    hash = 31 * hash + Double.hashCode(strength);
	    hash = 31 * hash + Double.hashCode(agility);
	    hash = 31 * hash + Double.hashCode(resistance);
	    hash = 31 * hash + Double.hashCode(health);
	    hash = 31 * hash + Double.hashCode(skill);
	    hash = 31 * hash + type.hashCode();
	    return hash;
	}
}
