import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PopulationFilter {

	private static final int INITIAL_TEMPERATURE = 100;
	private static final double K = 1.380649 * Math.pow(10, -4);	// Changed it to -4 instead of -23 so temp decreaces faster
	
	public static List<Character> eliteSelection(List<Character> population, int selected)
	{
		if(selected <= 0)
			return Collections.emptyList();
		List<Character> candidates = new ArrayList<>(population);
		while(candidates.size() < selected)
			candidates.addAll(population);
		candidates.sort((Character p1, Character p2) -> Double.compare(p2.getPerformance(), p1.getPerformance())); // Highest performance to lowest
		List<Character> list = new ArrayList<>();
		for(int i=0; i < selected; i++)
		{
			list.add(candidates.get(i));
		}
		return list;
	}
	
	
	public static List<Character> rouletteSelection(List<Character> population, int selected)
	{
		return performanceRoulette(population, selected, false);
	}

	public static List<Character> universalSelection(List<Character> population, int selected)
	{
		return performanceRoulette(population, selected, true);
	}

	private static List<Character> performanceRoulette(List<Character> population, int selected, boolean universal)
	{
		if(selected <= 0)
			return Collections.emptyList();
		
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
				q[i] += p[j];
		}
		
		// For Roulette:
		// 		Get K random values and for each, get the characters with lowest acum performance higher than that value.
		// 		This defines intervals the size of relative performances, giving the best characters a better chance of landing a random in its interval!
		// 		This might return the same character multiple times, but that's fine. It skews me towards better results and they might mutate anyway!
		// For Universal:
		//		Just like roulette, but random is selected differently, using a seed.
		double[] r = new double[selected];
		List<Character> list = new ArrayList<>();
		double seed = Math.random();

		for(int j=0; j < selected; j++)
		{
			if(universal)
				r[j] = (seed + j)/selected;	// Universal
			else
				r[j] = Math.random();		// Roulette
			
			if(r[j] <= q[0])
			{
				list.add(population.get(0));
			}
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
	
	public static List<Character> rankSelection(List<Character> population, int selected)
	{
		if(selected <= 0)
			return Collections.emptyList();
		population.sort((Character p1, Character p2) -> Double.compare(p2.getPerformance(), p1.getPerformance())); // Highest performance to lowest
		double f[] = new double[population.size()];
		for(int i=0; i < population.size(); i++)
		{
			// Since they're ordered from highest to lowest, i equals the rank
			f[i] = 1.0 - i/selected;
		}
		
		// From now on, it's like roulette but using f instead of performance
		double totalPerformance = 0;
		double[] p = new double[population.size()];
		double[] q = new double[population.size()];
		for(int i=0; i < population.size(); i++)
			totalPerformance += f[i];
		for(int i=0; i < population.size(); i++)
		{
			p[i] = f[i] / totalPerformance;
			q[i] = 0;
			for(int j=0; j <= i; j++)
				q[i] += p[i];
		}
		
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

	public static List<Character> deterministicTourneySelection(List<Character> population, int selected, int contestants)
	{
		if(selected <= 0)
			return Collections.emptyList();
		List<Character> list = new ArrayList<>();
		Set<Character> tournament = new HashSet<>();
		Character currentWinner = null;
		Character challenger = null;
		int index = 0;
		for(int i=0; i < selected; i++)
		{
			tournament.clear();
			currentWinner = null;
			for(int j=0; j < contestants; j++)
			{
				index = (int) (Math.random()*population.size());
				challenger = population.get(index);
				if(tournament.contains(challenger))
					j--;	// Try again
				else
				{
					tournament.add(challenger);
					if(currentWinner == null || challenger.getPerformance() > currentWinner.getPerformance())
						currentWinner = challenger;
				}
			}
			list.add(currentWinner);
		}
		return list;
	}
	
	public static List<Character> probabilisticTourneySelection(List<Character> population, int selected)
	{
		if(selected <= 0)
			return Collections.emptyList();
		List<Character> list = new ArrayList<>();
		Character challengerA = null;
		Character challengerB = null;
		boolean Awins;
		double threshold = Math.random()*0.5 + 0.5;
		double r;
		int indexA = -1;
		int indexB = -1;
		for(int i=0; i < selected; i++)
		{
			indexA = (int) (Math.random()*population.size());
			while (indexA == indexB || indexB < 0)
				indexB = (int) (Math.random()*population.size());
			challengerA = population.get(indexA);
			challengerB = population.get(indexB);
			r = Math.random();
			
			Awins = challengerA.getPerformance() > challengerB.getPerformance();
			if(r < threshold)
			{
				if(Awins)	list.add(challengerA);
				else		list.add(challengerB);
			}
			else
			{
				if(Awins)	list.add(challengerB);
				else		list.add(challengerA);
			}
		}
		return list;
	}
	
	private static double expVal(Character i, List<Character> g, double temperature)
	{
		double totalPerformance = 0;
		for(Character c : g)
			totalPerformance += c.getPerformance();
		
		double a = Math.exp(i.getPerformance() / temperature);
		double b = totalPerformance / g.size();
		return a/b;
	}
	
	private static double T(int t, double To)
	{
		return To * Math.exp(-K*t);
	}
	
	public static List<Character> boltzmannSelection(List<Character> population, int selected, int generation)
	{
		if(selected <= 0)
			return Collections.emptyList();
		double expVal[] = new double[population.size()];
		for(int i=0; i < population.size(); i++)
			expVal[i] = expVal(population.get(i), population, T(generation, INITIAL_TEMPERATURE));
		
		// From now on, it's like roulette but using expVal instead of performance
		double totalPerformance = 0;
		double[] p = new double[population.size()];
		double[] q = new double[population.size()];
		for(int i=0; i < population.size(); i++)
			totalPerformance += expVal[i];
		for(int i=0; i < population.size(); i++)
		{
			p[i] = expVal[i] / totalPerformance;
			q[i] = 0;
			for(int j=0; j <= i; j++)
				q[i] += p[j];
		}
		
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
