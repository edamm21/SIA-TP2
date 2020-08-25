public class Spy extends Character
{
	
	public Spy(double height, Equipment helmet, Equipment armor, Equipment gloves, Equipment boots, Equipment weapon){
		super(height, helmet, armor, gloves, boots, weapon);
	}

	public double getPerformance(){
		return 0.6*getAttack() + 0.6*getDefense();
	}
}
