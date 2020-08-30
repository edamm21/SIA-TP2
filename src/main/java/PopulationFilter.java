import java.util.ArrayList;
import java.util.List;

public class PopulationFilter {

	public static List<Character> eliteSelection(List<Character> population, int selected)
	{
		population.sort((Character p1, Character p2) -> Double.compare(p2.getPerformance(), p1.getPerformance())); // Highest performance to lowest
		List<Character> list = new ArrayList<>();
		for(int i=0; i < selected && i < population.size(); i++)
		{
			list.add(population.get(i));
		}
		return list;
	}
	
	
	public static List<Character> rouletteSelection(List<Character> population, int selected)
	{
		// Calculate relative performance (p) and acum relative performances (q)
		double totalPerformance = 0;
		double[] p = new double[population.size()];
		double[] q = new double[population.size()];
		for(Character c : population)
			totalPerformance += c.getPerformance();
		for(int i=0; i < population.size(); i++)
		{
			p[i] = population.get(i).getPerformance() / totalPerformance;
			q[i] = 0;
			for(int j=0; j <= i; j++)
				q[i] += p[i];
		}
		
		// Get K random values and for each, get the characters with lowest acum performance higher than that value.
		// This defines intervals the size of relative performances, giving the best characters a better chance of landing a random in its interval!
		// This might return the same character multiple times, but that's fine. It skews me towards better results and they might mutate anyway!
		double[] r = new double[selected];
		List<Character> list = new ArrayList<>();
		for(int j=0; j < selected; j++)
		{
			r[j] = Math.random();
			if(r[j] <= q[0])
				list.add(population.get(0));
			else for(int i=1; i < q.length; i++)
			{
				if(q[i-1] < r[j] && r[j] <= q[i])
				{
					list.add(population.get(i));
					i = q.length; // end i loop
				}
			}
		}
		return list;
	}
}