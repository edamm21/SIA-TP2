import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Equipment {

    EquipmentType type;
    double strength;
    double agility;
    double resistance;
    double health;
    double skill;

    public Equipment(EquipmentType type, double str, double ag, double res, double hp, double skl) {
        this.type = type;
        this.strength = str;
        this.agility = ag;
        this.resistance = res;
        this.health = hp;
        this.skill = skl;
    }

    public EquipmentType getType() {
        return type;
    }

    public double getStrength() {
        return strength;
    }

    public double getAgility() {
        return agility;
    }

    public double getResistance() {
        return resistance;
    }

    public double getHealth() {
        return health;
    }

    public double getSkill() {
        return skill;
    }

    public Equipment mutate() {
        String path = null;
        switch (this.type) {
            case GLOVES:
                path = "fulldata/guantes.tsv";
                break;
            case BOOTS:
                path = "fulldata/botas.tsv";
                break;
            case HELMET:
                path = "fulldata/cascos.tsv";
                break;
            case WEAPON:
                path = "fulldata/armas.tsv";
                break;
            case ARMOR:
                path = "fulldata/pecheras.tsv";
                break;
            default:
                break;
        }
        long lines = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
        lines -= 1;
        int randomRow = (int)(Math.random() * (lines - 2));
        BufferedReader br = null;
        String line = "";
        Equipment newEquipment = null;
        try {
            br = new BufferedReader(new FileReader(path));
            line = br.readLine();
            while ((line = br.readLine()) != null && newEquipment == null)
            {
                String[]contents = line.split("\t");
                if(Integer.parseInt(contents[0]) == randomRow)
                    newEquipment = new Equipment(type, Double.parseDouble(contents[1]), Double.parseDouble(contents[2]), Double.parseDouble(contents[3]),
                            Double.parseDouble(contents[4]), Double.parseDouble(contents[5]));

            }
            br.close();
            return newEquipment;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return this.type.toString() + " AG:" + this.agility +
                " HP: " + this.health + " RES: " + this.resistance +
                " SKL: " + this.skill + " STR: " + this.strength;
    }
}
