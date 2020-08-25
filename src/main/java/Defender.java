public class Defender extends Character
{
	public Defender(double height, Equipment helmet, Equipment armor, Equipment gloves, Equipment boots, Equipment weapon){
		super(height, helmet, armor, gloves, boots, weapon);
	}

	public double getPerformance(){
		return 0.8*getAttack() + 0.3*getDefense();
	}
}
