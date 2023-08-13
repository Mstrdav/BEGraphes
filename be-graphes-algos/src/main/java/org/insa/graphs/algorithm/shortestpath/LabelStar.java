package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class LabelStar extends Label {

    // Estimated cost from this node to the destination.
    private double estimatedCost;

    /**
     * Construct a new label.
     * 
     * @param node Node associated with this label.
     */
    public LabelStar(Node node, Node destination) {
        super(node);
        this.estimatedCost = node.getPoint().distanceTo(destination.getPoint());
    }

    /**
     * @return Total cost including the estimated cost.
     */
    @Override
    public double getTotalCost() {
        return super.getAchievedCost() + this.estimatedCost;
    }
}
