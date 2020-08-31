import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class Breeder {
	// HEIGHT - HELMET - ARMOR - GLOVES - BOOTS - WEAPON
	private static Object[] getGenes(Character p)
	{
		Object[] genes = {p.getHeight(), p.getHelmet(), p.getArmor(), p.getGloves(), p.getBoots(), p.getWeapon()};
		return genes;
	}
	
	private static List<Character> makeChildren(Class<? extends Character> c, Object[] c1Genes, Object[] c2Genes)
	{
		Constructor<? extends Character> constructor;
		Character child1 = null;
		Character child2 = null;
		
		try{
			constructor = c.getConstructor(double.class, Equipment.class, Equipment.class, Equipment.class, Equipment.class, Equipment.class);
			child1 = constructor.newInstance(c1Genes[0], c1Genes[1], c1Genes[2], c1Genes[3], c1Genes[4], c1Genes[5]);
			child2 = constructor.newInstance(c2Genes[0], c2Genes[1], c2Genes[2], c2Genes[3], c2Genes[4], c2Genes[5]);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		if(child1 != null && child2 != null)
		{
			List<Character> ret = new ArrayList<>();
			ret.add(child1);
			ret.add(child2);
			return ret;
		}
		return new ArrayList<>();
	}

	public static List<Character> breedSinglePoint(Character parent1, Character parent2, int point, Mutator m)
	{
		Object[] p1Genes = getGenes(parent1);
		Object[] p2Genes = getGenes(parent2);
		Object[] c1Genes = getGenes(parent1);
		Object[] c2Genes = getGenes(parent2);
		if(point < 0 || point >= p1Genes.length)
			return new ArrayList<>();
		
		for(int i=0; i < point; i++)
		{
			c1Genes[i] = p2Genes[i];
			c2Genes[i] = p1Genes[i];
		}
		if(m != null)
		{
	        c1Genes = m.mutate(c1Genes);
	        c2Genes = m.mutate(c2Genes);
		}
        return makeChildren(parent2.getClass(), c1Genes, c2Genes);
	}
	
	public static List<Character> breedTwoPoint(Character parent1, Character parent2, int from, int to, Mutator m)
	{
		Object[] p1Genes = getGenes(parent1);
		Object[] p2Genes = getGenes(parent2);
		Object[] c1Genes = getGenes(parent1);
		Object[] c2Genes = getGenes(parent2);
		if(from < 0 || to < 0 || from >= p1Genes.length || from > to)
			return new ArrayList<>();
		
		for(int i=from; i < to; i++)
		{
			c1Genes[i] = p2Genes[i];
			c2Genes[i] = p1Genes[i];
		}
		if(m != null)
		{
	        c1Genes = m.mutate(c1Genes);
	        c2Genes = m.mutate(c2Genes);
		}
		return makeChildren(parent2.getClass(), c1Genes, c2Genes);
	}
	
	public static List<Character> breedAnnularCross(Character parent1, Character parent2, int from, int length, Mutator m)
	{
		Object[] p1Genes = getGenes(parent1);
		Object[] p2Genes = getGenes(parent2);
		Object[] c1Genes = getGenes(parent1);
		Object[] c2Genes = getGenes(parent2);
		if(from < 0 || from >= p1Genes.length)
			return new ArrayList<>();
		
		int index = from;
		for(int i=0; i < length; i++)
		{
			if(index >= p1Genes.length)
				index = 0;
			c1Genes[index] = p2Genes[index];
			c2Genes[index] = p1Genes[index];
			index++;
		}
		if(m != null)
		{
	        c1Genes = m.mutate(c1Genes);
	        c2Genes = m.mutate(c2Genes);
		}
		return makeChildren(parent2.getClass(), c1Genes, c2Genes);
	}
	
	public static List<Character> breedUniformCross(Character parent1, Character parent2, double chance, Mutator m)
	{
		if(chance > 1)
			return new ArrayList<>();
		Object[] p1Genes = getGenes(parent1);
		Object[] p2Genes = getGenes(parent2);
		Object[] c1Genes = getGenes(parent1);
		Object[] c2Genes = getGenes(parent2);
		
		for(int i=0; i < p1Genes.length; i++)
		{
			double random = Math.random();
			if(random < chance)
			{
				c1Genes[i] = p2Genes[i];
				c2Genes[i] = p1Genes[i];
			}
		}
		if(m != null)
		{
	        c1Genes = m.mutate(c1Genes);
	        c2Genes = m.mutate(c2Genes);
		}
		return makeChildren(parent2.getClass(), c1Genes, c2Genes);
	}
}
