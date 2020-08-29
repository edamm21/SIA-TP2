public class Mutator {
    private Breeder breeder;
    private String mutationType;
    private double probability;

    public Mutator(String mutationType, Breeder breeder, double probability) {
        this.mutationType = mutationType;
        this.breeder = breeder;
        this.probability = probability;
    }

    // Single byte mutation (Gen)
    public Object[] mutate(Object[] currentGenes) {
        switch (this.mutationType) {
            case "GEN":
                return mutateGene(currentGenes);
            case "MULTIGEN-LIM":
                return mutateMultiGeneLimited(currentGenes);
            case "MULTIGEN-UNIF":
                return mutateMultiGeneUniform(currentGenes);
            case "COMPLETE":
                return mutateComplete(currentGenes);
            default:
                return currentGenes;
        }
    }

    private Object[] mutateGene(Object[] currentGenes) {
        // first choose a random gene
        int gene = (int) (Math.random() * currentGenes.length);
        if(Math.random() <= this.probability)
            currentGenes[gene] = mutateIndividualGene(currentGenes[gene]);
        return currentGenes;
    }

    private Object[] mutateMultiGeneLimited(Object[] currentGenes) {
        // first choose how many genes might be mutated
        int geneCount = (int) (Math.random() * (currentGenes.length - 1));
        if(geneCount == 0)
            return mutateGene(currentGenes);
        Object[] aux = currentGenes;
        for(int i = 0 ; i < geneCount ; i++) {
            aux = mutateGene(aux);
        }
        return aux;
    }

    private Object mutateIndividualGeneWithProbability(Object gene) {
        return Math.random() < this.probability ? mutateIndividualGene(gene) : gene;
    }

    private Object[] mutateMultiGeneUniform(Object[] currentGenes) {
        for(int i = 0 ; i < currentGenes.length ; i++) {
            currentGenes[i] = mutateIndividualGeneWithProbability(currentGenes[i]);
        }
        return currentGenes;
    }

    private Object mutateIndividualGene(Object gene) {
        if(gene instanceof Equipment) {
            return ((Equipment)gene).mutate();
        } else {
            // TODO decidir como hacer una varianza a la altura
        }
        return gene;
    }

    private void mutateAllGenes(Object[] currentGenes) {
        for(Object gene : currentGenes) {
            gene = mutateIndividualGene(gene);
        }
    }

    private Object[] mutateComplete(Object[] currentGenes) {
        if(Math.random() < this.probability)
            mutateAllGenes(currentGenes);
        return currentGenes;
    }

}
