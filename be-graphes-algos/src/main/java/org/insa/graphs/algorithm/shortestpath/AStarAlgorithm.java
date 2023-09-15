package org.insa.graphs.algorithm.shortestpath;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected LabelStar[] initLabels(ShortestPathData data) {
        final int nbNodes = data.getGraph().size();
        LabelStar[] labels = new LabelStar[nbNodes];
        for (int i = 0; i < nbNodes; i++) {
            labels[i] = new LabelStar(data.getGraph().get(i), data.getDestination());
            labels[i].setFather(null);
            labels[i].setAchievedCost(Double.POSITIVE_INFINITY);

            if (data.getGraph().get(i) == data.getOrigin()) {
                labels[i].setAchievedCost(0);
                labels[i].mark();
            }
        }
        return labels;
    }
}
