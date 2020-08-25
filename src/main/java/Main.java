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
	
    public static void main(String[] args)
    {
    	JSONObject json = readJSON();
    	parseInputData(json);
    	List<Equipment> helmets = new ArrayList<>();
    	List<Equipment> armors = new ArrayList<>();
    	List<Equipment> gloves = new ArrayList<>();
    	List<Equipment> boots = new ArrayList<>();
    	List<Equipment> weapons = new ArrayList<>();
    	
    	loadEquipment(helmets, "fulldata/cascos.tsv", EquipmentType.HELMET);
    	System.out.println("\nLoaded " +helmets.size() +" helmets");
    	
    	loadEquipment(armors, "fulldata/pecheras.tsv", EquipmentType.ARMOR);
    	System.out.println("Loaded " +armors.size() +" armors");
    	
    	loadEquipment(gloves, "fulldata/guantes.tsv", EquipmentType.GLOVES);
    	System.out.println("Loaded " +gloves.size() +" gloves");
    	
    	loadEquipment(boots, "fulldata/botas.tsv", EquipmentType.BOOTS);
    	System.out.println("Loaded " +boots.size() +" boots");
    	
    	loadEquipment(weapons, "fulldata/armas.tsv", EquipmentType.WEAPON);
    	System.out.println("Loaded " +weapons.size() +" weapons");
    	
    	System.out.println("READY!");
    }

    @SuppressWarnings("unchecked")
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
    
    private static void loadEquipment(List<Equipment> list, String file, EquipmentType type)
    {
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
}
