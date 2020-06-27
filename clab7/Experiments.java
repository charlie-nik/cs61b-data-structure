import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.*;

public class Experiments {
    public static void experiment1() {
        BST<Integer> bst = new BST<>();
        List<Integer> xValues = new ArrayList<>(); // number of items
        List<Double> yValues = new ArrayList<>(); // average depth
        List<Double> y2Values = new ArrayList<>(); // average depth of optimal BST

        for (int i = 0; i < 5000; i++) {
            ExperimentHelper.randomInsert(bst, 5000 * 2);
            xValues.add(i);
            yValues.add(bst.avgDepth());
            y2Values.add(ExperimentHelper.optimalAverageDepth(i));
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("number of items").yAxisTitle("average depth").build();
        chart.addSeries("Real average depth", xValues, yValues);
        chart.addSeries("Optimal average depth", xValues, y2Values);

        new SwingWrapper<>(chart).displayChart();
    }

    public static void experiment2() {
        BST<Integer> bst = new BST<>();

        // Starting position
        for (int i = 0; i < 5000; i++) {
            ExperimentHelper.randomInsert(bst, 5000 * 2);
        }

        List<Integer> xValues = new ArrayList<>(); // number of operations
        List<Double> yValues = new ArrayList<>(); // average depth
        double startDepth = bst.avgDepth();
        xValues.add(0);
        yValues.add(startDepth);
        for (int i = 1; i < 10000; i++) {
            bst.deleteTakingSuccessor(bst.getRandomKey());
            ExperimentHelper.randomInsert(bst, 5000 * 2);
            xValues.add(i);
            yValues.add(bst.avgDepth());
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("number of operations").yAxisTitle("average depth").build();
        chart.addSeries("Knott's average depth", xValues, yValues);

        new SwingWrapper<>(chart).displayChart();
    }

    public static void experiment3() {
        BST<Integer> bst = new BST<>();
        for (int i = 0; i < 5000; i++) {
            ExperimentHelper.randomInsert(bst, 5000 * 2);
        }

        List<Integer> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        double startDepth = bst.avgDepth();
        xValues.add(0);
        yValues.add(startDepth);

        for (int i = 0; i < 10000; i++) {
            bst.deleteTakingRandom(bst.getRandomKey());
            ExperimentHelper.randomInsert(bst, 5000 * 2);
            xValues.add(i);
            yValues.add(bst.avgDepth());
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("number of operations").yAxisTitle("average depth").build();
        chart.addSeries("Epplinger's average depth", xValues, yValues);

        new SwingWrapper<>(chart).displayChart();

    }

    public static void main(String[] args) {
        //experiment1();
        //experiment2();
        //experiment3();
    }
}
