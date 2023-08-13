package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {

    // Node associated with this label.
    private final Node currentNode;

    // Boolean, true if the node has been marked.
    private boolean marked;

    // Cost from origin to this node.
    private double achievedCost;

    // Predecessor of this node on the path from the origin.
    private Arc father;

    /**
     * Construct a new label.
     * 
     * @param node Node associated with this label.
     */
    public Label(Node node) {
        this.currentNode = node;
        this.marked = false;
        this.achievedCost = Double.POSITIVE_INFINITY;
        this.father = null;
    }

    /**
     * @return Node associated with this label.
     */
    public Node getCurrentNode() {
        return currentNode;
    }

    /**
     * @return True if the node has been marked, false otherwise.
     */
    public boolean isMarked() {
        return marked;
    }

    /**
     * Mark the node.
     */
    public void mark() {
        this.marked = true;
    }

    /**
     * @return Cost from origin to this node.
     */
    public double getAchievedCost() {
        return achievedCost;
    }

    /**
     * Set the cost from origin to this node.
     * 
     * @param cost New cost.
     */
    public void setAchievedCost(double cost) {
        this.achievedCost = cost;
    }

    /**
     * @return Predecessor of this node on the path from the origin.
     */
    public Arc getFather() {
        return father;
    }

    /**
     * Set the predecessor of this node on the path from the origin.
     * 
     * @param father New predecessor.
     */
    public void setFather(Arc father) {
        this.father = father;
    }

    /**
     * @return Total cost of the path from the origin to this node.
     */
    public double getCost() {
        return this.achievedCost;
    }

    @Override
    public int compareTo(Label o) {
        return Double.compare(this.getCost(), o.getCost());
    }
}
