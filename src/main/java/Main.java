import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
	

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

    private static void parseInputData(JSONObject object) {
        JSONObject data = (JSONObject) object.get("data");
        String name = (String)data.get("name");
        String lastname = (String)data.get("lastname");
        Long id = (Long)data.get("id");
        System.out.println("name: " + name + ", lastname: " + lastname + ", id: " + id);
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
    	parseInputData(json);
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
    	Character redSpy = new Spy(1.79, helmets.get(4), armors.get(5), gloves.get(1), boots.get(3), weapons.get(16));
    	
    	List<Character> population = new ArrayList<>();
    	population.add(bluSpy);
    	population.add(redSpy);
    	
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
    	List<Character> bestOnes = PopulationFilter.rankSelection(population, 2);
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
