import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Enums.CharacterType;
import Enums.CrossoverType;
import Enums.EquipmentType;
import Enums.ImplementationType;
import Enums.MutationType;
import Enums.StopType;
import Exceptions.InvalidCharacterTypeException;
import Exceptions.InvalidCrossoverTypeException;
import Exceptions.InvalidFactorException;
import Exceptions.InvalidImplementationTypeException;
import Exceptions.InvalidMutationTypeException;
import Exceptions.InvalidStopTypeException;

public class Main {
	
    private static final int INPUT_VALUE_COUNT = 6;

	private static JSONObject readJSON()
	{
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader("input.json");
            Object objects = parser.parse(reader);
            JSONObject inputs = (JSONObject) objects;
            return inputs;
        } catch (FileNotFoundException e) {
            System.out.println("File missing!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File error!");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Error parsing file!");
            e.printStackTrace();
        }
        return null;
	}

    private static void parseInputData(JSONObject object) throws Exception{
        String character = (String) object.get("CHARACTER");
        String crossover = (String) object.get("CROSSOVER");
        String mutation = (String) object.get("MUTATION");
        String implementation = (String) object.get("IMPLEMENTATION");
        String stop = (String) object.get("STOP");
        Long factor = (Long) object.get("FACTOR");
        Object[] values = new Object[INPUT_VALUE_COUNT];
        CharacterType[] characters = CharacterType.values();
        CrossoverType[] crossovers = CrossoverType.values();
        MutationType[] mutations = MutationType.values();
        ImplementationType[] implementations = ImplementationType.values();
        StopType[] stops = StopType.values();
        System.out.println("Reading config file...");
        for(int i = 0 ; i < characters.length && values[0] == null ; i++) {
            if(characters[i].toString().equals(character))
                values[0] = characters[i];
        }
        if(values[0] == null)
            throw new InvalidCharacterTypeException();
        for(int i = 0 ; i < crossovers.length && values[1] == null ; i++) {
            if(crossovers[i].toString().equals(crossover))
                values[1] = crossovers[i];
        }
        if(values[1] == null)
            throw new InvalidCrossoverTypeException();
        for(int i = 0 ; i < mutations.length && values[2] == null ; i++) {
            if(mutations[i].toString().equals(mutation))
                values[2] = mutations[i];
        }
        if(values[2] == null)
            throw new InvalidMutationTypeException();
        for(int i = 0 ; i < implementations.length && values[3] == null ; i++) {
            if(implementations[i].toString().equals(implementation))
                values[3] = implementations[i];
        }
        if(values[3] == null)
            throw new InvalidImplementationTypeException();
        for(int i = 0 ; i < stops.length && values[4] == null ; i++) {
            if(stops[i].toString().equals(stop))
                values[4] = stops[i];
        }
        if(values[4] == null)
            throw new InvalidStopTypeException();
        if(factor == null)
            throw new InvalidFactorException();
        values[5] = factor;
        System.out.println("Chosen params:");
        for(Object param : values) {
            System.out.println(param.getClass() + ": " + param);
        }
    }
    
    private static void loadEquipment(List<Equipment> list, String file, EquipmentType type) {
        BufferedReader br = null;
        String line = "";
		try {
			br = new BufferedReader(new FileReader(file));
			line = br.readLine();
            while ((line = br.readLine()) != null)
            {
                String[] contents = line.split("\t");
                list.add(new Equipment(type, Double.parseDouble(contents[1]), Double.parseDouble(contents[2]), Double.parseDouble(contents[3]),
                				Double.parseDouble(contents[4]), Double.parseDouble(contents[5])));
            }
            br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args)
    {
    	// Read JSON input
    	JSONObject json = readJSON();
    	try {
            parseInputData(json);
        } catch (Exception e) {
    	    e.printStackTrace();
            System.out.println("Exiting...");
    	    return;
        }
    	List<Equipment> helmets = new ArrayList<>();
    	List<Equipment> armors = new ArrayList<>();
    	List<Equipment> gloves = new ArrayList<>();
    	List<Equipment> boots = new ArrayList<>();
    	List<Equipment> weapons = new ArrayList<>();
    	
    	// Import equipment database
    	System.out.println("\nImporting equipment, please wait...");
    	loadEquipment(helmets, "fulldata/cascos.tsv", EquipmentType.HELMET);
    	System.out.println("Loaded " +helmets.size() +" helmets");
    	loadEquipment(armors, "fulldata/pecheras.tsv", EquipmentType.ARMOR);
    	System.out.println("Loaded " +armors.size() +" armors");
    	loadEquipment(gloves, "fulldata/guantes.tsv", EquipmentType.GLOVES);
    	System.out.println("Loaded " +gloves.size() +" gloves");
    	loadEquipment(boots, "fulldata/botas.tsv", EquipmentType.BOOTS);
    	System.out.println("Loaded " +boots.size() +" boots");
    	loadEquipment(weapons, "fulldata/armas.tsv", EquipmentType.WEAPON);
    	System.out.println("Loaded " +weapons.size() +" weapons\n");
    	
    	// Test
    	Mutator m = new Mutator(MutationType.UNIFORM_MULTIGENE, 0.8, helmets, armors, gloves, boots, weapons);
    	Character bluSpy = new Spy(1.78, helmets.get(6), armors.get(9), gloves.get(2), boots.get(6), weapons.get(4));
    	Character redSpy = new Spy(1.6, helmets.get(4), armors.get(0), gloves.get(0), boots.get(0), weapons.get(16));
    	
    	List<Character> population = new ArrayList<>();
    	population.add(bluSpy);
    	population.add(redSpy);

    	String stopChosen = "TIME";
    	float time = 10*1000; //milliseconds
    	List<Character> babySpies = Breeder.breedSinglePoint(bluSpy, redSpy, 5, m);
    	
    	// Breeder Testing
    	/*
    	System.out.println("Parent 1: " +bluSpy.toString());
		System.out.println("Parent 2: " +redSpy.toString());
		int i=0;
		if(babySpies == null)
			System.out.println("No children could be obtained! Check crossover params!");
		else for(Character c : babySpies)
		{
			i++;
			System.out.println("Child " +i +":" +c.toString());
		}
		*/
		
    	// PopulationFilter Testing
		population.addAll(babySpies);
    	List<Character> bestOnes = PopulationFilter.boltzMannSelection(population, 2, 0);
    	for(Character c : population)
    	{
    		System.out.println("Possible: " +c.getPerformance());
    	}
    	System.out.println();
    	for(Character c : bestOnes)
    	{
    		System.out.println("Winner: " +c.getPerformance());
    	}
		
    }
}
