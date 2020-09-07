import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Plotter {
    private static final int BUBBLE_SIZE = 10;
    private final int PLAYER_EQUIPMENT = 5;
    private List<Double> bestPerformersPerGeneration = new ArrayList<>();
    private List<Double> worstPerformersPerGeneration = new ArrayList<>();
    private List<Double> avgPerformersPerGeneration = new ArrayList<>();
    private List<Double> generations = new ArrayList<>();
    private XYChart mainChart;
    private SwingWrapper<XYChart> wrapper;
    private RadarChart radarChart;
    private SwingWrapper<RadarChart> radarWrapper;


    public Plotter(Character bestPerformer, Character worstPerformer, Character avgPerformer, int generation) {
        this.generations.add((double)generation);
        this.bestPerformersPerGeneration.add(bestPerformer.getPerformance());
        this.worstPerformersPerGeneration.add(worstPerformer.getPerformance());
        this.avgPerformersPerGeneration.add(avgPerformer.getPerformance());
        this.mainChart = new XYChartBuilder().width(1500).height(900).title("Evolución por generaciones").xAxisTitle("Generación").yAxisTitle("Aptitud").build();
        this.mainChart.addSeries( "Mejor", generations, bestPerformersPerGeneration);
        this.mainChart.addSeries("Promedio", generations, avgPerformersPerGeneration);
        this.mainChart.addSeries("Peor", generations, worstPerformersPerGeneration);
        this.wrapper = new SwingWrapper<>(mainChart);
        this.wrapper.displayChart();
    }

    public void replot(Character bestPerformer, Character worstPerformer, Character avgPerformer, int generation) {
        this.bestPerformersPerGeneration.add(bestPerformer.getPerformance());
        this.worstPerformersPerGeneration.add(worstPerformer.getPerformance());
        this.avgPerformersPerGeneration.add(avgPerformer.getPerformance());
        this.generations.add((double)generation);
        this.mainChart.updateXYSeries("Mejor", generations, bestPerformersPerGeneration, null);
        this.mainChart.updateXYSeries("Promedio", generations, avgPerformersPerGeneration, null);
        this.mainChart.updateXYSeries("Peor", generations, worstPerformersPerGeneration, null);
        this.wrapper.repaintChart();
    }

    private void populateAttributes(String[] names, Equipment[] equipment, Character best) {
        equipment[0] = best.getHelmet();
        equipment[1] = best.getArmor();
        equipment[2] = best.getGloves();
        equipment[3] = best.getBoots();
        equipment[4] = best.getWeapon();
        names[0] = "Casco";
        names[1] = "Pechera";
        names[2] = "Guantes";
        names[3] = "Botas";
        names[4] = "Arma";
    }

    public void makeRadarChart(Character best, Equipment[] bestEquipment) {
        this.radarChart = new RadarChartBuilder().width(600).height(600).title("Mejor " + best.getType()).build();
        this.radarChart.getStyler().setToolTipsEnabled(true);
        this.radarChart.getStyler().setHasAnnotations(true);
        this.radarChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
        String[] eqNames = new String[PLAYER_EQUIPMENT];
        Equipment[] eqValues = new Equipment[PLAYER_EQUIPMENT];
        double[] normalizedValues = new double[PLAYER_EQUIPMENT];
        populateAttributes(eqNames, eqValues, best);
        for(int i = 0 ; i < PLAYER_EQUIPMENT ; i++) {
            normalizedValues[i] = eqValues[i].getSum() / bestEquipment[i].getSum();
        }
        this.radarChart.setVariableLabels(eqNames);
        this.radarChart.addSeries("Equipamiento", normalizedValues);
        this.radarWrapper = new SwingWrapper<RadarChart>(this.radarChart);
        this.radarWrapper.displayChart();
    }

    private List<Double> getPerformances(Set<Character> characters) {
        List<Double> performances = new ArrayList<>();
        for (Character character : characters) {
            performances.add(character.getPerformance());
        }
        return performances;
    }

    public void makeScatterChart(Map<Integer, Set<Character>> reproduced, Map<Integer, Set<Character>> forgotten) {
        BubbleChart chart = new BubbleChartBuilder().height(500).width(1000).title("Selección por generación").xAxisTitle("Generación").yAxisTitle("Selección").build();
        chart.getStyler().setSeriesColors(new Color[]{Color.WHITE});
        chart.getStyler().setSeriesLines(new BasicStroke[]{new BasicStroke()});
        chart.getStyler().setLegendVisible(false);
        for(Integer generation : forgotten.keySet()) {
            List<Integer> xAxisData = Collections.nCopies(forgotten.get(generation).size(), generation);
            List<Integer> bubbleData = Collections.nCopies(forgotten.get(generation).size(), BUBBLE_SIZE);
            List<Double> performances = getPerformances(forgotten.get(generation));
            BubbleSeries series;
            if (performances.size() != 0) {
                series = chart.addSeries("Forgotten" + generation, xAxisData, performances, bubbleData);
                series.setFillColor(Color.RED);
                series.setLineWidth(0);
            }
        }
        for(Integer generation : reproduced.keySet()) {
            List<Integer> xAxisData = Collections.nCopies(reproduced.get(generation).size(), generation);
            List<Integer> bubbleData = Collections.nCopies(reproduced.get(generation).size(), BUBBLE_SIZE);
            List<Double> performances = getPerformances(reproduced.get(generation));
            BubbleSeries series = chart.addSeries("Reproduced " + generation, xAxisData, performances, bubbleData);
            series.setFillColor(Color.BLUE);
            series.setLineWidth(0);
        }
        new SwingWrapper<>(chart).displayChart();
    }
}
