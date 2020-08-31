import Enums.*;
import dataModels.DPoint;

import java.nio.DoubleBuffer;
import java.util.*;

public class GeneticAlgorithm {

    private final double HEIGHT_MAX = 2.0;
    private final double HEIGHT_MIN = 1.3;
    private Map<String, Object> values;
    private Map<String, List<Equipment>> equipment;
    private int generationCount;
    private long startTime;
    private List<Character> population;
    private Character currentBestPerformer;
    private int repeatedBestPerformer;

    public GeneticAlgorithm(Map<String, Object> chosenValues, Map<String, List<Equipment>> equipment) {
        this.values = chosenValues;
        this.equipment = equipment;
        this.generationCount = 0;
        this.repeatedBestPerformer = 0;
        this.currentBestPerformer = null;
    }

    private boolean runnable() {
        Long factor = (Long)values.get("FACTOR");
        switch ((StopType)this.values.get("STOP")) {
            case GENERATIONS:
                return this.generationCount <= factor;
            case STRUCTURE:
                // TODO
            case SOLUTION:
                // TODO
            case CONTENT:
                return this.repeatedBestPerformer < factor;
            case TIME:
                return (System.currentTimeMillis() - this.startTime) <= factor;
            default:
                return false;
        }
    }

    private void generatePopulation(Map<String, List<Equipment>> equipment) {
        int count = 0;
        this.population = new ArrayList<>();
        while(count < (Long)this.values.get("POPULATION")) {
            Random random = new Random();
            double randomHeight = HEIGHT_MIN + (HEIGHT_MAX - HEIGHT_MIN) * random.nextDouble();
            int randomHelmet = random.nextInt(equipment.get("helmets").size() - 1);
            int randomArmor = random.nextInt(equipment.get("armors").size() - 1);
            int randomGloves = random.nextInt(equipment.get("gloves").size() - 1);
            int randomBoots = random.nextInt(equipment.get("boots").size() - 1);
            int randomWeapon = random.nextInt(equipment.get("weapons").size() - 1);
            switch ((CharacterType)this.values.get("CHARACTER")) {
                case SPY:
                    this.population.add(new Spy(randomHeight,
                                            equipment.get("helmets").get(randomHelmet),
                                            equipment.get("armors").get(randomArmor), equipment.get("gloves").get(randomGloves),
                                            equipment.get("boots").get(randomBoots), equipment.get("weapons").get(randomWeapon)));
                    break;
                case ARCHER:
                    this.population.add(new Archer(randomHeight,
                                            equipment.get("helmets").get(randomHelmet),
                                            equipment.get("armors").get(randomArmor), equipment.get("gloves").get(randomGloves),
                                            equipment.get("boots").get(randomBoots), equipment.get("weapons").get(randomWeapon)));
                    break;
                case WARRIOR:
                    this.population.add(new Warrior(randomHeight,
                                            equipment.get("helmets").get(randomHelmet),
                                            equipment.get("armors").get(randomArmor), equipment.get("gloves").get(randomGloves),
                                            equipment.get("boots").get(randomBoots), equipment.get("weapons").get(randomWeapon)));
                    break;
                case DEFENDER:
                    this.population.add(new Defender(randomHeight,
                                            equipment.get("helmets").get(randomHelmet),
                                            equipment.get("armors").get(randomArmor), equipment.get("gloves").get(randomGloves),
                                            equipment.get("boots").get(randomBoots), equipment.get("weapons").get(randomWeapon)));
                    break;
                default:
                    break;
            }
            count++;
        }
    }

    private List<Character> selectFromSubPopulation(List<Character> subpopulation, String method, int amount) {
        switch ((SelectionType)this.values.get(method)) {
            case ELITE:
                return PopulationFilter.eliteSelection(subpopulation, amount);
            case ROULETTE:
                return PopulationFilter.rouletteSelection(subpopulation, amount);
            case UNIVERSAL:
                return PopulationFilter.universalSelection(subpopulation, amount);
            case BOLTZMANN:
                return PopulationFilter.boltzMannSelection(subpopulation, amount, this.generationCount);
            case DET_TOURNAMENT:
                return PopulationFilter.deterministicTourneySelection(subpopulation, amount, (int)this.values.get("TOURNAMENT_M"));
            case PROB_TOURNAMENT:
                return PopulationFilter.probabilisticTourneySelection(subpopulation, amount);
            case RANKING:
                return PopulationFilter.rankSelection(subpopulation, amount);
            default:
                return new ArrayList<>();
        }
    }

    private List<Character> selectFromPopulation(String method, int amount) {
        switch ((SelectionType)this.values.get(method)) {
            case ELITE:
                return PopulationFilter.eliteSelection(this.population, amount);
            case ROULETTE:
                return PopulationFilter.rouletteSelection(this.population, amount);
            case UNIVERSAL:
                return PopulationFilter.universalSelection(this.population, amount);
            case BOLTZMANN:
                return PopulationFilter.boltzMannSelection(this.population, amount, this.generationCount);
            case DET_TOURNAMENT:
                return PopulationFilter.deterministicTourneySelection(this.population, amount, (int)this.values.get("TOURNAMENT_M"));
            case PROB_TOURNAMENT:
                return PopulationFilter.probabilisticTourneySelection(this.population, amount);
            case RANKING:
                return PopulationFilter.rankSelection(this.population, amount);
            default:
                return new ArrayList<>();
        }
    }

    public List<Character> generateCrossover(List<Character> selected) {
        Mutator mutator = new Mutator((MutationType)this.values.get("MUTATION"), (double)this.values.get("MUTATION_PROBABILITY"),
                                        this.equipment.get("helmets"), this.equipment.get("armors"),
                                        this.equipment.get("gloves"), this.equipment.get("boots"),
                                        this.equipment.get("weapons"));
        int individualsToBreed = (int)((Double)this.values.get("CROSSOVER_PERCENTAGE") * selected.size());
        if(individualsToBreed % 2 != 0 && individualsToBreed != selected.size())
            individualsToBreed += 1; // para hacerlo par y poder realmente cruzar distintos
        else
            individualsToBreed -= 1;
        List<Integer> indexesToPerformCrossover = new ArrayList<>();
        while(indexesToPerformCrossover.size() < individualsToBreed) {
            Random random = new Random();
            int index = random.nextInt(individualsToBreed);
            if(!indexesToPerformCrossover.contains(index))
                indexesToPerformCrossover.add(index);
        }
        int numberOfCrossoversToPerform = individualsToBreed / 2;
        int numberOfCrossoversDone = 0;
        List<Character> children = new ArrayList<>();
        while(numberOfCrossoversDone < numberOfCrossoversToPerform) {
            Character parent1 = selected.get(2 * numberOfCrossoversDone);
            Character parent2 = selected.get(2 * numberOfCrossoversDone + 1);
            Random random = new Random();
            switch ((CrossoverType) this.values.get("CROSSOVER")) {
                case SINGLE_POINT: {
                    int point = random.nextInt(Character.GENE_COUNT + 1);
                    children.addAll(Breeder.breedSinglePoint(parent1, parent2, point, mutator));
                    break;
                }
                case TWO_POINT: {
                    int point1 = random.nextInt(Character.GENE_COUNT + 1);
                    int point2 = random.nextInt((Character.GENE_COUNT + 1 - point1) + point1); // desde point1 hasta el ultimo gen
                    children.addAll(Breeder.breedTwoPoint(parent1, parent2, point1, point2, mutator));
                    break;
                }
                case ANNULAR: {
                    int locus = random.nextInt(Character.GENE_COUNT + 1);
                    int length = random.nextInt((int)Math.ceil((Character.GENE_COUNT + 1) / 2));
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
            numberOfCrossoversDone++;
        }
        return children;
    }

    private List<Character> selectIndividuals(List<Character> characters, int limit) {
        double b = (double)this.values.get("INDIV_SELECTION_B");
        int amountWithMethod3 = (int)(b * limit);
        int amountWithMethod4 = limit - amountWithMethod3;
        List<Character> selected = new ArrayList<>(selectFromSubPopulation(characters, "SELECTION_METHOD_3", amountWithMethod3));
        List<Character> subpopulation = new ArrayList<>(characters);
        subpopulation.removeAll(selected);
        selected.addAll(selectFromSubPopulation(subpopulation, "SELECTION_METHOD_4", amountWithMethod4));
        return selected;
    }

    private void performReplacement(List<Character> children) {
        long N = (long)this.values.get("POPULATION");
        long K = children.size();
        long total = N + K;
        switch ((ImplementationType)this.values.get("IMPLEMENTATION")) {
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

    public List<Point> start() {
        generatePopulation(equipment);
        this.startTime = System.currentTimeMillis();
        Mutator mutator = new Mutator((MutationType)this.values.get("MUTATION"), (Double)this.values.get("MUTATION_PROBABILITY"), this.equipment.get("helmets"),
                                        this.equipment.get("armors"), this.equipment.get("gloves"),
                                        this.equipment.get("boots"), this.equipment.get("weapons"));
        List<Character> selected = new ArrayList<>();
        List<Character> children;
        System.out.println("Running algorithm...");
        Double a = (Double)this.values.get("PARENT_SELECTION_A");
        Double b = (Double)this.values.get("INDIV_SELECTION_B");
        Double crossoverProbability = (Double)this.values.get("CROSSOVER_PERCENTAGE");
        int selectedWithMethod1 = (int)(crossoverProbability * (Long)this.values.get("K"));
        int selectedWithMethod2 = (int)((Long)this.values.get("K") - selectedWithMethod1);
        Double mutationProbability = (Double)this.values.get("MUTATION_PROBABILITY");
        List<Point> points = new ArrayList<>();
        while(runnable()) {
            selected = new ArrayList<>();
            selected.addAll(selectFromPopulation("SELECTION_METHOD_1", selectedWithMethod1));
            List<Character> subpopulation = new ArrayList<>(this.population);
            subpopulation.removeAll(selected);
            selected.addAll(selectFromSubPopulation(subpopulation,"SELECTION_METHOD_2", selectedWithMethod2));
            children = generateCrossover(selected);
            performReplacement(children);
            this.population.sort((Character p1, Character p2) -> Double.compare(p2.getPerformance(), p1.getPerformance()));
            Character bestPerformer = this.population.get(0);
            if(this.currentBestPerformer == bestPerformer)
                this.repeatedBestPerformer++;
            else {
                this.repeatedBestPerformer = 0;
                this.currentBestPerformer = bestPerformer;
            }
            System.out.println("Generation " + this.generationCount + "'s best: " + bestPerformer);
            Point point = new Point(generationCount, bestPerformer.getPerformance());
            points.add(point);
            this.generationCount++;
        }
        this.population.sort((Character p1, Character p2) -> Double.compare(p2.getPerformance(), p1.getPerformance()));
        Character bestPerformer = this.population.get(0);
        System.out.println("Final best: " + bestPerformer);
        return points;
    }

}
