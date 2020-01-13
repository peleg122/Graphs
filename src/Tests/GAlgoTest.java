package Tests;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.graph;
import dataStructure.node_data;
import elements.nodeData;
import gui.Graph_GUI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import utils.Point3D;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

public class GAlgoTest {
    public static graph g;

    @BeforeAll
    public static void setUp() {
        g = new DGraph();

        node_data n1 = new nodeData(new Point3D(10, 10));
        node_data n2 = new nodeData(new Point3D(15, 10));
        node_data n3 = new nodeData(new Point3D(10, 20));

        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);

        g.connect(0, 1, 1);
        g.connect(1, 2, 2);
        g.connect(2, 0, 3);
    }

    @Test
    public void isConnected() {
        Graph_Algo ga = new Graph_Algo();
        ga.init(g);

        if (!ga.isConnected()) {
            fail("Should be Connected!");
        } else {
            System.out.println("isConnected is all Good!");
        }
    }

    @Test
    public void ShortestPath() {
        Graph_Algo ga = new Graph_Algo(g);
        String Expected = "Path: 0 >> 1 >> 2 | Distance: 3.0";
        String Actual = ga.shortestPathToString(ga.shortestPath(0, 2));
        if (!Expected.equals(Actual)) {
            fail("Shortest Path doesnt Work");
        } else {
            System.out.println("Shortest Path working");
        }
    }

    @Test
    public void ShortestPathDist() {
        Graph_Algo ga = new Graph_Algo(g);
        String Expected = "5.0";
        String Actual = "" + ga.shortestPathDist(1, 0);
        if (!Expected.equals(Actual)) {
            fail("Shortest Path Dist doesnt Work");
        } else {
            System.out.println("Distance: " + Actual);
            System.out.println("Shortest Path Dist working");
        }
    }

    @Test
    public void SaveAndLoad() {
        Graph_Algo ga = new Graph_Algo(g);
        ga.save("tester.txt");
        Graph_Algo gaSave = new Graph_Algo();
        gaSave.init("tester.txt");
        String Expected = ga.shortestPathToString(ga.shortestPath(0, 1));
        String Actual = gaSave.shortestPathToString(gaSave.shortestPath(0, 1));
        if (!Expected.equals(Actual)) {
            fail("Save And Load doesnt Work!");
        } else {
            System.out.println("Save and Load(init) working");
        }
    }
    @Test
    public void TSP(){
        Graph_Algo ga = new Graph_Algo(g);
        String Expected = "Path: 0 >> 2 >> 1 >> 1 >> 0 >> 2 | Distance: 4.0";
        ArrayList<Integer> arrayList = new ArrayList();
        arrayList.add(0);
        arrayList.add(1);
        arrayList.add(2);
        String Actual = ga.shortestPathToString(ga.TSP(arrayList));
        if (!Expected.equals(Actual)) {
            fail("TSP doesnt Work");
        } else {
            System.out.println("TSP working");
        }
    }

    @Test
    public void copy() {
        Graph_Algo ga = new Graph_Algo();
        nodeData n = new nodeData(new Point3D(20,30));
        ga._graph.addNode(n);
        Graph_Algo gCopy = new Graph_Algo();
        ga.init(g);
        gCopy.init(ga.copy());
        int Expected = ga._graph.nodeSize();
        int Actual = gCopy._graph.nodeSize();
        if (Expected  != Actual) {
            fail("copy Doesnt Working!");
        } else {
            System.out.println("copy Working!");
        }

    }


    public static void main(String[] args) {
        Graph_GUI gg = new Graph_GUI();
        graph graph = new DGraph();
        Graph_Algo g1 = new Graph_Algo();

        node_data n1 = new nodeData(new Point3D(-20, -20));
        node_data n2 = new nodeData(new Point3D(40, 40));
        node_data n3 = new nodeData(new Point3D(70, 10));
        node_data n4 = new nodeData(new Point3D(-70, 10));
        node_data n5 = new nodeData(new Point3D(-50, -50));
        node_data n6 = new nodeData(new Point3D(50, -50));
        node_data n7 = new nodeData(new Point3D(0, 0));

        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);
        graph.addNode(n5);
        graph.addNode(n6);
        graph.addNode(n7);

        graph.connect(0, 1, 8);
        graph.connect(0, 2, 9);
        graph.connect(1, 6, 4);
        graph.connect(6, 2, 5);
        graph.connect(2, 3, 1);
        graph.connect(3, 6, 4);
        graph.connect(3, 0, 5);
        graph.connect(6, 5, 6);
        graph.connect(1, 3, 8);
        graph.connect(5, 2, 15);
        graph.connect(5, 3, 14);
        graph.connect(6, 4, 4);
        graph.connect(4, 5, 9);
        graph.connect(4, 3, 5);
        graph.connect(5, 1, 26);
        graph.connect(1, 2, 25);
        graph.connect(6, 0, 5);

        g1.init(graph);
        gg.init(graph);
        gg.drawGraph();
    }
}
