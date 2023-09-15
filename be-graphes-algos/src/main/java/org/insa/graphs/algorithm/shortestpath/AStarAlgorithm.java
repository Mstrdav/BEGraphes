package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();

        // Notify observers just like BellmanFord does it ;)
        notifyOriginProcessed(data.getOrigin());

        // Step 1 : initialisation
        LabelStar[] labels = new LabelStar[nbNodes];
        for (int i = 0; i < nbNodes; i++) {
            labels[i] = new LabelStar(graph.get(i), data.getDestination());
            labels[i].setFather(null);
            labels[i].setAchievedCost(Double.POSITIVE_INFINITY);

            if (graph.get(i) == data.getOrigin()) {
                labels[i].setAchievedCost(0);
                labels[i].mark();
            }
        }

        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        heap.insert(labels[data.getOrigin().getId()]);
        boolean done = false;
        Label currentLabel = null;

        // Step 2 : find the shortest path
        while (!heap.isEmpty() && !done) {
            currentLabel = heap.deleteMin();
            currentLabel.mark();
            // Notify observers
            notifyNodeMarked(currentLabel.getCurrentNode());

            // We have reached the destination
            if (currentLabel.getCurrentNode() == data.getDestination()) {
                done = true;
                System.out.println("Destination reached !");
                System.out.println("Visited " + heap.size() + " nodes");
            }

            // We look at all the successors of the current node
            for (Arc arc : currentLabel.getCurrentNode().getSuccessors()) {
                // Small test to check allowed roads...
                if (!data.isAllowed(arc)) {
                    continue;
                }

                // We get the label of the successor
                LabelStar successorLabel = labels[arc.getDestination().getId()];

                // If the node is not marked, we update its cost and add it to the heap
                if (!successorLabel.isMarked()) {
                    double oldCost = successorLabel.getAchievedCost();
                    double newCost = currentLabel.getAchievedCost() + data.getCost(arc);

                    // First time we reach the node
                    if (Double.isInfinite(oldCost) && Double.isFinite(newCost)) {
                        notifyNodeReached(arc.getDestination());
                    }

                    // Better path found
                    if (newCost < oldCost) {
                        successorLabel.setAchievedCost(newCost);
                        successorLabel.setFather(arc);
                        heap.insert(successorLabel);
                    }
                }
            }
        }

        // Step 3 : create the solution
        ShortestPathSolution solution = null;

        // Destination has no predecessor, the solution is infeasible... (je reprends les coms)
        if (labels[data.getDestination().getId()].getFather() == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
            System.out.println("No solution found");
        } else {
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = labels[data.getDestination().getId()].getFather();
            while (arc != null) {
                arcs.add(arc);
                arc = labels[arc.getOrigin().getId()].getFather();
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
            System.out.println("Solution found");
        }

        return solution;
    }
}
