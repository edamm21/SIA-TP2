public class Archer extends Character
{
	public Archer(double height, Equipment helmet, Equipment armor, Equipment gloves, Equipment boots, Equipment weapon){
		super(height, helmet, armor, gloves, boots, weapon);
	}
	
	public double getPerformance(){
		return 0.9*getAttack() + 0.1*getDefense();
	}
}
