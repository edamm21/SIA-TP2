import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import Enums.CharacterType;
import Enums.CrossoverType;
import Enums.EquipmentType;
import Enums.ImplementationType;
import Enums.MutationType;
import Enums.SelectionType;
import Enums.StopType;
import Exceptions.InvalidArgument;

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

    private static void parseInputData(JSONObject object, Map<String, Object> values) throws Exception{
        Set<?> keys = object.keySet();
        Iterator<?> it = keys.iterator();
        while(it.hasNext()) {
            String key = (String) it.next();
            values.put(key, object.get(key));
        }
        CharacterType[] characters = CharacterType.values();
        CrossoverType[] crossovers = CrossoverType.values();
        MutationType[] mutations = MutationType.values();
        SelectionType[] selections = SelectionType.values();
        ImplementationType[] implementations = ImplementationType.values();
        StopType[] stops = StopType.values();
        boolean ok = false;
        System.out.println("Reading config file...");
        for(int i = 0 ; i < characters.length && !ok; i++) {
            if(characters[i].toString().equals((String)values.get("CHARACTER"))) {
                values.remove("CHARACTER");
                values.put("CHARACTER",characters[i]);
                ok = true;
            }
        }
        if(!ok)
            throw new InvalidArgument("Character");
        ok = false;
        for(int i = 0 ; i < crossovers.length && !ok ; i++) {
            if(crossovers[i].toString().equals((String)values.get("CROSSOVER"))) {
                values.remove("CROSSOVER");
                values.put("CROSSOVER", crossovers[i]);
                ok = true;
            }
        }
        if(!ok)
            throw new InvalidArgument("Crossover");
        ok = false;
        for(int i = 0 ; i < mutations.length && !ok ; i++) {
            if(mutations[i].toString().equals((String)values.get("MUTATION"))) {
                values.remove("MUTATION");
                values.put("MUTATION", mutations[i]);
                ok = true;
            }
        }
        if(!ok)
            throw new InvalidArgument("Mutation");
        ok = false;
        int methodsFound = 0;
        String m1 = (String)values.get("SELECTION_METHOD_1");
        String m2 = (String)values.get("SELECTION_METHOD_2");
        String m3 = (String)values.get("SELECTION_METHOD_3");
        String m4 = (String)values.get("SELECTION_METHOD_4");
        for(int i = 0 ; i < selections.length && methodsFound < 4 ; i++) {
            if(selections[i].toString().equals(m1)) {
                values.remove("SELECTION_METHOD_1");
                values.put("SELECTION_METHOD_1", selections[i]);
                methodsFound++;
            }
            if(selections[i].toString().equals(m2)) {
                values.remove("SELECTION_METHOD_2");
                values.put("SELECTION_METHOD_2", selections[i]);
                methodsFound++;
            }
            if(selections[i].toString().equals(m3)) {
                values.remove("SELECTION_METHOD_3");
                values.put("SELECTION_METHOD_3", selections[i]);
                methodsFound++;
            }
            if(selections[i].toString().equals(m4)) {
                values.remove("SELECTION_METHOD_4");
                values.put("SELECTION_METHOD_4", selections[i]);
                methodsFound++;
            }
        }
        if(methodsFound != 4)
            throw new InvalidArgument("Selection methods");
        ok = false;
        for(int i = 0 ; i < implementations.length && !ok ; i++) {
            if(implementations[i].toString().equals((String)values.get("IMPLEMENTATION"))) {
                values.remove("IMPLEMENTATION");
                values.put("IMPLEMENTATION", implementations[i]);
                ok = true;
            }
        }
        if(!ok)
            throw new InvalidArgument("Implementation");
        ok = false;
        for(int i = 0 ; i < stops.length && !ok ; i++) {
            if(stops[i].toString().equals((String)values.get("STOP"))) {
                values.remove("STOP");
                values.put("STOP", stops[i]);
                ok = true;
            }
        }
        if(!ok)
            throw new InvalidArgument("Stop");
        if((Double)values.get("PARENT_SELECTION_A") < 0.0 || (Double)values.get("PARENT_SELECTION_A") > 1.0)
            throw new InvalidArgument("Parent selection");
        if((Double)values.get("INDIV_SELECTION_B") < 0.0 || (Double)values.get("INDIV_SELECTION_B") > 1.0)
            throw new InvalidArgument("Individual selection");
        if((Double)values.get("MUTATION_PROBABILITY") < 0.0 || (Double)values.get("MUTATION_PROBABILITY") > 1.0)
            throw new InvalidArgument("Mutation probability");
        if((values.get("TOURNAMENT_M") != null && values.get("K") != null) && ((Long)values.get("TOURNAMENT_M") > (Long)values.get("K") ||
                                                                                (Long)values.get("TOURNAMENT_M") < 0 || (Long)values.get("K") < 0))
            throw new InvalidArgument("Tournament M");
        if((Long)values.get("K") < 0)
            throw new InvalidArgument("K");
        if((Long)values.get("POPULATION") < 0)
            throw new InvalidArgument("Population");
        if((Long)values.get("FACTOR") < 0)
            throw new InvalidArgument("Factor");

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
        Map<String, Object> values = new HashMap<>();
        try {
            parseInputData(json, values);
        } catch (Exception e) {
    	    e.printStackTrace();
            System.out.println("Exiting...");
    	    return;
        }
        Map<EquipmentType, List<Equipment>> equipment = new HashMap<>();
        equipment.put(EquipmentType.HELMET, new ArrayList<>());
        equipment.put(EquipmentType.ARMOR, new ArrayList<>());
        equipment.put(EquipmentType.GLOVES, new ArrayList<>());
        equipment.put(EquipmentType.BOOTS, new ArrayList<>());
        equipment.put(EquipmentType.WEAPON, new ArrayList<>());
    	
    	// Import equipment database
    	System.out.print("\nImporting equipment, please wait...");
        System.out.println((String)values.get("HELMETS_DATASET_PATH"));
    	loadEquipment(equipment.get(EquipmentType.HELMET), (String)values.get("HELMETS_DATASET_PATH"), EquipmentType.HELMET);
    	System.out.print(".");
    	loadEquipment(equipment.get(EquipmentType.ARMOR), (String)values.get("ARMOR_DATASET_PATH"), EquipmentType.ARMOR);
    	System.out.print(".");
    	loadEquipment(equipment.get(EquipmentType.GLOVES), (String)values.get("GLOVES_DATASET_PATH"), EquipmentType.GLOVES);
    	System.out.print(".");
    	loadEquipment(equipment.get(EquipmentType.BOOTS), (String)values.get("BOOTS_DATASET_PATH"), EquipmentType.BOOTS);
    	System.out.print(".");
    	loadEquipment(equipment.get(EquipmentType.WEAPON), (String)values.get("WEAPON_DATASET_PATH"), EquipmentType.WEAPON);
    	System.out.println(".");

    	// Run the algorithm
    	GeneticAlgorithm ga = new GeneticAlgorithm(values, equipment);
    	Map<Integer, Set<Character>> reproduced = new HashMap<>();
    	Map<Integer, Set<Character>> forgotten = new HashMap<>();
        ga.start(reproduced, forgotten);
    }


}
