package Tests;

import dataStructure.DGraph;
import dataStructure.edge_data;
import elements.nodeData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import static org.junit.jupiter.api.Assertions.fail;

class DGraphTest {
    public static DGraph graph;

    @BeforeAll
    public static void SetUp() {
        graph = new DGraph();
    }

    @Test
    void addNode() {
        int Expected = graph.nodeSize();
        nodeData n1 = new nodeData(new Point3D(20, 30));
        nodeData n2 = new nodeData(new Point3D(20, 30));
        nodeData n3 = new nodeData(new Point3D(20, 30));
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        int Actual = graph.nodeSize();
        if (Expected + 3 != Actual) {
            fail("addNode Doesnt work!");
        } else {
            System.out.println("addNode Working!");
        }
    }

    @Test
    void getNode() {
        nodeData n = new nodeData(new Point3D(20, 30));
        graph.addNode(n);
        nodeData nGot = (nodeData) graph.getNode(n.getKey());
        if (n.getKey() != nGot.getKey()) {
            fail("getNode Doesnt Work!");
        } else {
            System.out.println("getNode Working!");
        }
    }

    @Test
    void connect() {
        int Expected = graph.edgeSize();
        int Actual;
        nodeData n = new nodeData(new Point3D(20, 30));
        nodeData n1 = new nodeData(new Point3D(20, 30));
        graph.addNode(n);
        graph.addNode(n1);
        graph.connect(n.getKey(), n1.getKey(), 5);
        Actual = graph.edgeSize();
        if (Expected + 1 != Actual) {
            fail("Connect doesnt working!");
        } else {
            System.out.println("Connect Working!");
        }
    }

    @Test
    void getEdge() {
        nodeData n = new nodeData(new Point3D(20, 30));
        nodeData n1 = new nodeData(new Point3D(20, 30));
        graph.addNode(n);
        graph.addNode(n1);
        graph.connect(n.getKey(), n1.getKey(), 5);
        edge_data Expected = n.getNeighbors().get(n1.getKey());
        edge_data Actual = graph.getEdge(n.getKey(), n1.getKey());
        if (Expected.getDest() != Actual.getDest() && Expected.getSrc() != Actual.getSrc()) {
            fail("getEdge Doesnt Working!");
        } else {
            System.out.println("getEdge Working!");
        }
    }

    @Test
    void getV() {
        nodeData n = new nodeData(new Point3D(20, 30));
        graph.addNode(n);
        int Expected = graph.nodeSize();
        int Actual = graph.getV().size();
        if (Expected != Actual) {
            fail("getV Doesnt Working!");
        } else {
            System.out.println("getV Working!");
        }
    }

    @Test
    void getE() {
        nodeData n = new nodeData(new Point3D(20, 30));
        nodeData n1 = new nodeData(new Point3D(20, 30));
        graph.addNode(n);
        graph.addNode(n1);
        graph.connect(n.getKey(), n1.getKey(), 5);
        int Expected = n.getNeighbors().size();
        int Actual = graph.getE(n.getKey()).size();
        if (Expected != Actual) {
            fail("getE Doesnt Working!");
        } else {
            System.out.println("getE Working!");
        }
    }

    @org.junit.jupiter.api.Test
    void removeNode() {
        nodeData n = new nodeData(new Point3D(20, 30));
        nodeData n1 = new nodeData(new Point3D(20, 30));
        graph.addNode(n);
        graph.addNode(n1);
        graph.connect(n.getKey(), n1.getKey(), 5);
        int Expected = graph.nodeSize();
        int ExpectedEgdes = graph.nodeSize();
        graph.removeNode(n.getKey());
        int Actual = graph.nodeSize();
        int ActualEdges = graph.nodeSize();
        if (Expected - 1 != Actual && ExpectedEgdes - 1 != ActualEdges) {
            fail("removeNode Doesnt Working!");
        } else {
            System.out.println("removeNode Working!");
        }
    }

    @org.junit.jupiter.api.Test
    void removeEdge() {
        nodeData n = new nodeData(new Point3D(20, 30));
        nodeData n1 = new nodeData(new Point3D(20, 30));
        graph.addNode(n);
        graph.addNode(n1);
        graph.connect(n.getKey(), n1.getKey(), 5);
        int Expected = graph.edgeSize();
        graph.removeEdge(n.getKey(), n1.getKey());
        int Actual = graph.edgeSize();
        int ExpectedNeighbors = n.getNeighbors().size();
        int ActualNeighbors = graph.getE(n.getKey()).size();
        if (Expected != Actual && ExpectedNeighbors != ActualNeighbors) {
            fail("removeEdge Doesnt Working!");
        } else {
            System.out.println("removeEdge Working!");
        }

    }

    @org.junit.jupiter.api.Test
    void nodeSize() {
        int Expected = graph.nodeSize();
        nodeData n = new nodeData(new Point3D(20, 30));
        graph.addNode(n);
        int Actual = graph.nodeSize();
        if (Expected + 1 != Actual) {
            fail("nodeSize Doesnt Working!");
        } else {
            System.out.println("nodeSize Working!");
        }
    }

    @org.junit.jupiter.api.Test
    void edgeSize() {
        int Expected = graph.edgeSize();
        nodeData n = new nodeData(new Point3D(20, 30));
        nodeData n1 = new nodeData(new Point3D(20, 30));
        graph.addNode(n);
        graph.addNode(n1);
        graph.connect(n.getKey(), n1.getKey(), 5);
        int Actual = graph.edgeSize();
        if (Expected + 1 != Actual) {
            fail("edgeSize Doesnt Working!");
        } else {
            System.out.println("edgeSize Working!");
        }
    }

    @org.junit.jupiter.api.Test
    void getMC() {
        int Expected = graph.getMC();
        nodeData n = new nodeData(new Point3D(20, 30));
        graph.addNode(n);
        int Actual = graph.getMC();
        if (Expected + 1 != Actual) {
            fail("getMC Doesnt Working!");
        } else {
            System.out.println("getMC Working!");
        }
    }
}