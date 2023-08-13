package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

public class Launch {

    /**
     * Create a new Drawing inside a JFrame an return it.
     * 
     * @return The created drawing.
     * 
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static void main(String[] args) throws Exception {

        // Visit these directory to see the list of available files on Commetud.
        final String mapName = "/home/mstrdav/insa/BEGraphes/maps/insa.mapgr";
        final String pathName = "/home/mstrdav/insa/BEGraphes/paths/path_fr31insa_rangueil_r2.path";

        // Create a graph reader.
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        // Read the graph.
        final Graph graph = reader.read();
        System.out.println(graph);

        // Create the drawing:
        final Drawing drawing = createDrawing();

        // Draw the graph on the drawing.
        drawing.drawGraph(graph);

        // Create a PathReader.
        final PathReader pathReader = new BinaryPathReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(pathName)))
        );

        // Read the path.
        final Path path = pathReader.readPath(graph);

        // Draw the path.
        drawing.drawPath(path);

        /*
         * Testing Dijkstra, using the provided path. Simply change the map and/or the path for other examples.
         * If Dijkstra works, both paths should be the same.
         * If it does not, everything explodes!!!
         */

        Node origin = graph.get(path.getOrigin().getId());
        Node destination = graph.get(path.getDestination().getId());

        System.out.println("Path from " + path.getOrigin().getId() + " to " + path.getDestination().getId() + ":");
        System.out.println("  - Length: " + path.getLength());
        System.out.println("  - Time: " + path.getMinimumTravelTime());
        System.out.println("  - Nb Arcs: " + path.getArcs().size());

        // run Dijkstra
        System.out.println("Running Dijkstra...");
        ShortestPathData data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solution = dijkstra.run();
        Path pathDijkstra = solution.getPath();

        System.out.println("Path from " + pathDijkstra.getOrigin().getId() + " to " + pathDijkstra.getDestination().getId() + ":");
        System.out.println("  - Length: " + pathDijkstra.getLength());
        System.out.println("  - Time: " + pathDijkstra.getMinimumTravelTime());
        System.out.println("  - Nb Arcs: " + pathDijkstra.getArcs().size());

        // run A*
        System.out.println("Running A*...");
        data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(0));
        AStarAlgorithm aStar = new AStarAlgorithm(data);
        solution = aStar.run();
        Path pathAStar = solution.getPath();

        System.out.println("Path from " + pathAStar.getOrigin().getId() + " to " + pathAStar.getDestination().getId() + ":");
        System.out.println("  - Length: " + pathAStar.getLength());
        System.out.println("  - Time: " + pathAStar.getMinimumTravelTime());
        System.out.println("  - Nb Arcs: " + pathAStar.getArcs().size());

        // draw the path
        drawing.drawPath(pathDijkstra);
        drawing.drawPath(pathAStar);
    }
}
