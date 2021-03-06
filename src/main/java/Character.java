public abstract class Character
{
	private double height;
	private Equipment helmet;
	private Equipment armor;
	private Equipment gloves;
	private Equipment boots;
	private Equipment weapon;
	public static final int GENE_COUNT = 6;

	public Character(double height, Equipment helmet, Equipment armor, Equipment gloves, Equipment boots, Equipment weapon)
	{
		this.height = height;
		this.helmet = helmet;
		this.armor = armor;
		this.gloves = gloves;
		this.boots = boots;
		this.weapon = weapon;
	}

	public abstract String getType();

	@Override
	public int hashCode() {
		int hash = 7;
	    hash = 31 * hash + Double.hashCode(getHeight());
	    hash = 31 * hash + getHelmet().hashCode();
	    hash = 31 * hash + getArmor().hashCode();
	    hash = 31 * hash + getGloves().hashCode();
	    hash = 31 * hash + getBoots().hashCode();
	    hash = 31 * hash + getWeapon().hashCode();
	    return hash;
	}

	public Equipment[] getAllEquipment() {
		return new Equipment[]{getHelmet(), getArmor(), getGloves(), getBoots(), getWeapon()};
	}

	public double getStrength()
	{
		double sum = 0;
		sum += helmet.getStrength();
		sum += armor.getStrength();
		sum += gloves.getStrength();
		sum += boots.getStrength();
		sum += weapon.getStrength();
		return 100 * Math.tanh(0.01 * sum);
	}
	
	public double getAgility()
	{
		double sum = 0;
		sum += helmet.getAgility();
		sum += armor.getAgility();
		sum += gloves.getAgility();
		sum += boots.getAgility();
		sum += weapon.getAgility();
		return Math.tanh(0.01 * sum);
	}
	
	public double getSkill()
	{
		double sum = 0;
		sum += helmet.getSkill();
		sum += armor.getSkill();
		sum += gloves.getSkill();
		sum += boots.getSkill();
		sum += weapon.getSkill();
		return 0.6 * Math.tanh(0.01 * sum);
	}
	
	public double getResistance()
	{
		double sum = 0;
		sum += helmet.getResistance();
		sum += armor.getResistance();
		sum += gloves.getResistance();
		sum += boots.getResistance();
		sum += weapon.getResistance();
		return Math.tanh(0.01 * sum);
	}
	
	public double getHealth()
	{
		double sum = 0;
		sum += helmet.getHealth();
		sum += armor.getHealth();
		sum += gloves.getHealth();
		sum += boots.getHealth();
		sum += weapon.getHealth();
		return 100 * Math.tanh(0.01 * sum);
	}
	
	private double getATM()
	{
		return 0.7 - Math.pow(3*height - 5, 4) + Math.pow(3*height - 5, 2) + height/4;
	}
	
	private double getDEM()
	{
		return 1.9 - Math.pow(2.5*height - 4.16, 4) + Math.pow(2.5*height - 4.16, 2) + 3*height/10;
	}
	
	public double getAttack()
	{
		return (getAgility() + getSkill()) * getStrength() * getATM();
	}
	
	public double getDefense()
	{
		return (getResistance() + getSkill()) * getHealth() * getDEM();
	}
	
	public abstract double getPerformance();

	// GETTERS AND SETTERS
	public Equipment getHelmet() {
		return helmet;
	}

	public void setHelmet(Equipment helmet) {
		this.helmet = helmet;
	}

	public Equipment getArmor() {
		return armor;
	}

	public void setArmor(Equipment armor) {
		this.armor = armor;
	}

	public Equipment getGloves() {
		return gloves;
	}

	public void setGloves(Equipment gloves) {
		this.gloves = gloves;
	}

	public Equipment getBoots() {
		return boots;
	}

	public void setBoots(Equipment boots) {
		this.boots = boots;
	}

	public Equipment getWeapon() {
		return weapon;
	}

	public void setWeapon(Equipment weapon) {
		this.weapon = weapon;
	}
	
	public double getHeight(){
		return height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	@Override
	public String toString()
	{
		return "Performance\t" + getPerformance();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Character)
		{
			Character b = (Character) o;
			return this.height == b.getHeight() && this.helmet.equals(b.getHelmet()) && this.armor.equals(b.getArmor())
					&& this.gloves.equals(b.getGloves()) && this.boots.equals(b.getBoots()) && this.weapon.equals(b.getWeapon());
		}
		return false;
	}
	
	public String getInformation()
	{
		String s = "HEIGHT\t" +height +"m\n";
		s += helmet +"\n";
		s += armor +"\n";
		s += gloves +"\n";
		s += boots +"\n";
		s += weapon +"\n";
		s += "\nTOTAL PERFORMANCE: " +getPerformance();
		return s;
	}
}
