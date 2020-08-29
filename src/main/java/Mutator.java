import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Mutator {
    private MutationType mutationType;
    private double probability;
    List<Equipment> helmets;
    List<Equipment> armors;
    List<Equipment> gloves;
    List<Equipment> boots;
    List<Equipment> weapons;
    private static final double GROWTH_DELTA = 0.05;

    public Mutator(MutationType mutationType, double probability, List<Equipment> helmets, List<Equipment> armors, List<Equipment> gloves, List<Equipment> boots, List<Equipment> weapons) {
        this.mutationType = mutationType;
        this.probability = probability;
        this.helmets = helmets;
        this.armors = armors;
        this.gloves = gloves;
        this.boots = boots;
        this.weapons = weapons;
    }
    
    private Object mutateIndividualGeneWithProbability(Object gene) {
        return Math.random() < this.probability ? mutateIndividualGene(gene) : gene;
    }
    
    private Object mutateIndividualGene(Object gene) {
        if(gene instanceof Equipment) {
            return mutateEquipment((Equipment)gene);
        } else {
        	double growth = Math.random()*GROWTH_DELTA;
        	if(Math.random() < 0.5)
        		growth = -growth;
        	gene = (double) gene + growth;
        }
        return gene;
    }

    public Object[] mutate(Object[] currentGenes) {
        switch (this.mutationType) {
            case GENE:
                return mutateSingleGene(currentGenes);
            case LIMITED_MULTIGENE:
                return mutateMultiGeneLimited(currentGenes);
            case UNIFORM_MULTIGENE:
                return mutateMultiGeneUniform(currentGenes);
            case COMPLETE:
                return mutateComplete(currentGenes);
            default:
                return currentGenes;
        }
    }

    private Object[] mutateSingleGene(Object[] currentGenes) {
        // first choose a random gene
        int gene = (int) (Math.random() * currentGenes.length);
        if(Math.random() <= this.probability)
            currentGenes[gene] = mutateIndividualGene(currentGenes[gene]);
        return currentGenes;
    }

    private Object[] mutateMultiGeneLimited(Object[] currentGenes) {
        // Choose how many genes can be mutated
        int geneCount = 1 + (int) (Math.random() * (currentGenes.length-1));
        if(geneCount == 1)
            return mutateSingleGene(currentGenes);
        
        // Pick which genes to change
        Set<Integer> indexes = new HashSet<>();
        for(int i = 0 ; i < geneCount ; i++)
        {
            int index = (int) (Math.random() * (currentGenes.length));
            if(indexes.contains(index))
            	i--;
            else
            	indexes.add(index);
        }

        for(Integer i : indexes)
        	currentGenes[i] = mutateIndividualGeneWithProbability(currentGenes[i]);
        return currentGenes;
    }

    private Object[] mutateMultiGeneUniform(Object[] currentGenes) {
        for(int i = 0 ; i < currentGenes.length ; i++) {
            currentGenes[i] = mutateIndividualGeneWithProbability(currentGenes[i]);
        }
        return currentGenes;
    }

    private Object[] mutateComplete(Object[] currentGenes) {
        if(Math.random() < this.probability)
        {
            return mutateAllGenes(currentGenes);
        }
        return currentGenes;
    }
    
    private Object[] mutateAllGenes(Object[] currentGenes) {
        for(int i=0; i < currentGenes.length; i++)
        	currentGenes[i] = mutateIndividualGene(currentGenes[i]);
        return currentGenes;
    }

    private Equipment mutateEquipment(Equipment e)
    {
        switch (e.getType()) {
	        case GLOVES:
	            return gloves.get((int)Math.random()*gloves.size());
	        case BOOTS:
	        	return boots.get((int)Math.random()*boots.size());
	        case HELMET:
	        	return helmets.get((int)Math.random()*helmets.size());
	        case WEAPON:
	        	return weapons.get((int)Math.random()*weapons.size());
	        case ARMOR:
	        	return armors.get((int)Math.random()*armors.size());
	        default:
	            return null;
        }
    }

}
