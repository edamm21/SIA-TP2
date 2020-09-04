import java.util.*;

import Enums.CharacterType;
import Enums.CrossoverType;
import Enums.EquipmentType;
import Enums.ImplementationType;
import Enums.MutationType;
import Enums.SelectionType;
import Enums.StopType;

public class GeneticAlgorithm {

    private final double HEIGHT_MAX = 2.0;
    private final double HEIGHT_MIN = 1.3;
    private Map<String, Object> values;
    private Map<EquipmentType, List<Equipment>> equipment;
    private int generationCount;
    private long startTime;
    private List<Character> population;
    private Character currentBestPerformer;
    private int repeatedBestPerformer;
    private int repeatedMostCommonGenes;
    private Integer currentMostCommonGenes;
    private Equipment[] bestEquipmentFromDataset = new Equipment[5];

    public GeneticAlgorithm(Map<String, Object> chosenValues, Map<EquipmentType, List<Equipment>> equipment) {
        this.values = chosenValues;
        this.equipment = equipment;
        this.generationCount = 0;
        this.repeatedBestPerformer = 0;
        this.repeatedMostCommonGenes = 0;
        this.currentMostCommonGenes = null;
        this.currentBestPerformer = null;
    }

    private Integer getMostCommonGenes() {
        Map<Integer, Integer> genesByAmount = new HashMap<>();
        for(Character c : this.population) {
            if(genesByAmount.containsKey(c.hashCode())) {
                Integer current = genesByAmount.get(c.hashCode());
                genesByAmount.put(c.hashCode(), current + 1);
            } else {
               genesByAmount.put(c.hashCode(), 1);
            }
        }
        Map.Entry<Integer, Integer> max = null;
        for(Map.Entry<Integer, Integer> e : genesByAmount.entrySet()) {
            if(max == null || e.getValue() > max.getValue())
                max = e;
        }
        System.out.println("max: " + max.getValue());
        System.out.println("%: " + (double)max.getValue() / (double)this.population.size());
        if((double)max.getValue() / this.population.size() > 0.6)
            return max.getKey();
        else
            return -1;
    }

    private boolean searchActive() {
        Long factor = (Long)values.get("FACTOR");
        switch ((StopType)this.values.get("STOP")) {
            case GENERATIONS:
                return this.generationCount <= factor;
            case STRUCTURE:// una parte relevante de la poblacion no cambia una cantidad de generaciones
                // busco cuales son los genes mas repetidos en la poblacion
                return this.repeatedMostCommonGenes < factor;
            case SOLUTION: // solucion aceptable
                return this.currentBestPerformer.getPerformance() < factor;
            case CONTENT:
                return this.repeatedBestPerformer < factor;
            case TIME:
                return (System.currentTimeMillis() - this.startTime) <= factor;
            default:
                return false;
        }
    }

    @SuppressWarnings("unchecked")
    private void generatePopulation(Map<EquipmentType, List<Equipment>> equipment) {
        int count = 0;
        this.population = new ArrayList<>();
        while(count < (Long)this.values.get("POPULATION")) {
            Random random = new Random();
            int randomHelmet = random.nextInt(equipment.get(EquipmentType.HELMET).size() - 1);
            int randomArmor = random.nextInt(equipment.get(EquipmentType.ARMOR).size() - 1);
            int randomGloves = random.nextInt(equipment.get(EquipmentType.GLOVES).size() - 1);
            int randomBoots = random.nextInt(equipment.get(EquipmentType.BOOTS).size() - 1);
            int randomWeapon = random.nextInt(equipment.get(EquipmentType.WEAPON).size() - 1);
            
            double randomHeight = HEIGHT_MIN + (HEIGHT_MAX - HEIGHT_MIN) * random.nextDouble();
            Equipment helmet = equipment.get(EquipmentType.HELMET).get(randomHelmet);
            Equipment armor= equipment.get(EquipmentType.ARMOR).get(randomArmor);
            Equipment gloves = equipment.get(EquipmentType.GLOVES).get(randomGloves);
            Equipment boots = equipment.get(EquipmentType.BOOTS).get(randomBoots);
            Equipment weapon = equipment.get(EquipmentType.WEAPON).get(randomWeapon);

            bestEquipmentFromDataset[0] = Collections.max(equipment.get(EquipmentType.HELMET));
            bestEquipmentFromDataset[1] = Collections.max(equipment.get(EquipmentType.ARMOR));
            bestEquipmentFromDataset[2] = Collections.max(equipment.get(EquipmentType.GLOVES));
            bestEquipmentFromDataset[3] = Collections.max(equipment.get(EquipmentType.BOOTS));
            bestEquipmentFromDataset[4] = Collections.max(equipment.get(EquipmentType.WEAPON));

            switch ((CharacterType)this.values.get("CHARACTER")) {
                case SPY:
                    this.population.add(new Spy(randomHeight, helmet, armor, gloves, boots, weapon));
                    break;
                case ARCHER:
                    this.population.add(new Archer(randomHeight, helmet, armor, gloves, boots, weapon));
                    break;
                case WARRIOR:
                    this.population.add(new Warrior(randomHeight, helmet, armor, gloves, boots, weapon));
                    break;
                case DEFENDER:
                    this.population.add(new Defender(randomHeight, helmet, armor, gloves, boots, weapon));
                    break;
                default:
                    break;
            }
            count++;
        }
    }

    private List<Character> selectFromSubPopulation(List<Character> subpopulation, String method, int amount) {
    	if(subpopulation.size() == 0)
    		return new ArrayList<>();
        switch ((SelectionType)this.values.get(method)) {
            case ELITE:
                return PopulationFilter.eliteSelection(subpopulation, amount);
            case ROULETTE:
                return PopulationFilter.rouletteSelection(subpopulation, amount);
            case UNIVERSAL:
                return PopulationFilter.universalSelection(subpopulation, amount);
            case BOLTZMANN:
                return PopulationFilter.boltzmannSelection(subpopulation, amount, this.generationCount);
            case DET_TOURNAMENT:
                return PopulationFilter.deterministicTourneySelection(subpopulation, amount, (int)(0+(Long)values.get("TOURNAMENT_M")));
            case PROB_TOURNAMENT:
                return PopulationFilter.probabilisticTourneySelection(subpopulation, amount);
            case RANKING:
                return PopulationFilter.rankSelection(subpopulation, amount);
            default:
                return new ArrayList<>();
        }
    }

    private List<Character> selectFromPopulation(String method, int amount) {
    	return selectFromSubPopulation(population, method, amount);
    }

    public List<Character> generateCrossover(List<Character> selected, Mutator mutator) {
        int individualsToBreed = selected.size() - (selected.size() % 2);	// Choose an even amount
        Collections.shuffle(selected);
        
        List<Character> children = new ArrayList<>();
        System.out.println("Parents breeding:");
        for(int i=0; i < individualsToBreed; i++)
        {
            Character parent1 = selected.get(i);
            Character parent2 = selected.get(i+1);
            i++;
            //System.out.println("\t" +parent1.getPerformance() +"\t<3\t" +parent2.getPerformance());
            Random random = new Random();
            switch ((CrossoverType) this.values.get("CROSSOVER"))
            {
	            case SINGLE_POINT: {
	                int point = random.nextInt(Character.GENE_COUNT);
	                children.addAll(Breeder.breedSinglePoint(parent1, parent2, point, mutator));
	                break;
	            }
	            case TWO_POINT: {
	                int point1 = random.nextInt(Character.GENE_COUNT);
	                int point2 = random.nextInt(Character.GENE_COUNT);
	                if(point2 < point1)
	                {
	                	int aux = point2;
	                	point2 = point1;
	                	point1 = aux;
	                }
	                List<Character> aux = Breeder.breedTwoPoint(parent1, parent2, point1, point2, mutator);
	                children.addAll(aux);
	                break;
	            }
	            case ANNULAR: {
	                int locus = random.nextInt(Character.GENE_COUNT);
	                int length = random.nextInt((int)Math.ceil(Character.GENE_COUNT / 2));
	                children.addAll(Breeder.breedAnnularCross(parent1, parent2, locus, length, mutator));
	                break;
	            }
	            case UNIFORM: {
	                double probability = random.nextDouble();
	                children.addAll(Breeder.breedUniformCross(parent1, parent2, probability, mutator));
	                break;
	            }
	            default:
	                children.addAll(new ArrayList<>());
	                break;
            }
        }
        return children;
    }

    private List<Character> selectIndividuals(List<Character> characters, int limit) {
        double b = (double)this.values.get("INDIV_SELECTION_B");
        int amountWithMethod3 = (int)(b * limit);
        int amountWithMethod4 = limit - amountWithMethod3;
        List<Character> selected = new ArrayList<>(selectFromSubPopulation(characters, "SELECTION_METHOD_3", amountWithMethod3));
        List<Character> subpopulation = new ArrayList<>(characters);
        selected.addAll(selectFromSubPopulation(subpopulation, "SELECTION_METHOD_4", amountWithMethod4));
        return selected;
    }

    private void performReplacement(List<Character> children) {
        long N = (long)this.values.get("POPULATION");
        long K = children.size();
        switch ((ImplementationType)this.values.get("IMPLEMENTATION"))
        {
            case FILL_ALL: {
                List<Character> all = new ArrayList<>(this.population);
                all.addAll(children);
                this.population = selectIndividuals(all, (int)N);
                break;
            }
            case FILL_PARENT: {
                if (K > N) {
                    this.population = selectIndividuals(children, (int)N);
                    break;
                } else {
                    this.population = selectIndividuals(this.population, (int)(N - K));
                    this.population.addAll(children);
                    break;
                }
            }
            default:
                break;
        }
    }

    private void analyzeGeneration(int generation, List<Character> population, Plotter plotter)
    {
        population.sort((Character p1, Character p2) -> Double.compare(p2.getPerformance(), p1.getPerformance()));
        int size = population.size();
        Character bestPerformerThisGen = population.get(0);
        Character avgPerformerThisGen = population.get(Math.round(size/2));
        Character worstPerformerThisGen = population.get(size - 1);
        Integer mostCommonGenesThisGeneration = getMostCommonGenes();
        if(currentBestPerformer == null) {
        	currentBestPerformer = bestPerformerThisGen;
        }
        else if(currentBestPerformer.getPerformance() < bestPerformerThisGen.getPerformance())
        {
            repeatedBestPerformer = 0;
            currentBestPerformer = bestPerformerThisGen;
        }
        else
            repeatedBestPerformer++;
        System.out.println("mostcommon this gen: " + mostCommonGenesThisGeneration);
        System.out.println("count: " + repeatedMostCommonGenes);
        if(!mostCommonGenesThisGeneration.equals(-1)) {
            if (currentMostCommonGenes == null) {
                currentMostCommonGenes = mostCommonGenesThisGeneration;
            } else if (!currentMostCommonGenes.equals(mostCommonGenesThisGeneration)) {
                this.repeatedMostCommonGenes = 0;
                this.currentMostCommonGenes = mostCommonGenesThisGeneration;
            } else {
                this.repeatedMostCommonGenes++;
            }
        } else {
            this.currentMostCommonGenes = null;
        }
        System.out.println("Generation " + this.generationCount + "'s best: " + bestPerformerThisGen);
        System.out.println("Generation " + this.generationCount + "'s worst: " +population.get(population.size()-1));
        System.out.println("Current best is " +currentBestPerformer.getPerformance() +" seen " +repeatedBestPerformer +" times!\n");
        plotter.replot(bestPerformerThisGen, worstPerformerThisGen, avgPerformerThisGen, generation);
    }
    
    public void start(Map<Integer, Set<Character>> reproduced, Map<Integer, Set<Character>> forgotten)
    {
        generatePopulation(equipment);
        this.population.sort((Character p1, Character p2) -> Double.compare(p2.getPerformance(), p1.getPerformance()));
        int size = this.population.size();
        this.currentBestPerformer = this.population.get(0);
        this.currentMostCommonGenes = getMostCommonGenes();
        Plotter plotter = new Plotter(this.currentBestPerformer, this.population.get(size - 1), this.population.get(Math.round((size / 2))), 0);
        this.startTime = System.currentTimeMillis();
        Mutator mutator = new Mutator((MutationType)this.values.get("MUTATION"), (Double)this.values.get("MUTATION_PROBABILITY"),
						        		this.equipment.get(EquipmentType.HELMET), this.equipment.get(EquipmentType.ARMOR),
						                this.equipment.get(EquipmentType.GLOVES), this.equipment.get(EquipmentType.BOOTS),
						                this.equipment.get(EquipmentType.WEAPON));
        List<Character> selected;
        List<Character> children;
        System.out.println("Running algorithm...");
        Double a = (Double)this.values.get("PARENT_SELECTION_A");
        int selectedWithMethod1 = (int)(a * (Long)this.values.get("K"));
        int selectedWithMethod2 = (int)((Long)this.values.get("K") - selectedWithMethod1);
        
        while(searchActive())
        {
        	analyzeGeneration(generationCount, population, plotter);
        	
            // Choose our future parents!
            selected = new ArrayList<>();
            selected.addAll(selectFromPopulation("SELECTION_METHOD_1", selectedWithMethod1));
            List<Character> subpopulation = new ArrayList<>(this.population);
            selected.addAll(selectFromSubPopulation(subpopulation,"SELECTION_METHOD_2", selectedWithMethod2));

            // Register data for this generation!
            reproduced.put(generationCount, new HashSet<>());
            forgotten.put(generationCount, new HashSet<>());
            for(Character c : population)
            {
            	if(!selected.contains(c))
                    forgotten.get(generationCount).add(c);
            }
            
            // Get our next generation and register who had children
            children = generateCrossover(selected, mutator);
            if(selected.size() % 2 != 0)
            	forgotten.get(generationCount).add(selected.get(selected.size()-1));
            for(int i=0; i < (selected.size() - (selected.size()%2)); i++)
            	reproduced.get(generationCount).add(selected.get(i));

            // Prepare for the next generation!
            performReplacement(children);
            generationCount++;
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Final best: " +currentBestPerformer);
        plotter.makeRadarChart(currentBestPerformer, bestEquipmentFromDataset);
    }

}
