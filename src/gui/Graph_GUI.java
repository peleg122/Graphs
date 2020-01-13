package gui;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.nodeData;
import utils.StdDraw;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Graph_GUI extends JFrame implements ActionListener {

    graph g = new DGraph();
    Graph_Algo g_a = new Graph_Algo();

    public void init(graph g) {
        this.g = g;
        g_a.init(g);
    }

    private void load() {
        Graph_Algo ga = new Graph_Algo();
        JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnV = jf.showOpenDialog(null);
        if (returnV == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jf.getSelectedFile();
            ga.init(selectedFile.getAbsolutePath());
            this.g = ga.copy();
            repaint();
        }
    }

    public void save() {
        String sb = "TEST CONTENT";
        Graph_Algo ga = new Graph_Algo();
        ga.init(g);
        JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jf.setApproveButtonText("Save");
        int returnV = jf.showOpenDialog(null);
        if (returnV == JFileChooser.APPROVE_OPTION) {
            try {
                ga.save(jf.getSelectedFile()+".txt");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String act = e.getActionCommand();
        switch (act) {
            case "Save":
                save();
                break;
            case "Load":
                load();
                break;
            case "is Connected":
                isConnected();
                break;
            case "Shortest Path":
                shotestPath();
                break;
            case "TSP":
                TSPEnterNodes();
                break;
            default:
                break;
        }
    }

    private void isConnected() {

        if (g_a != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(3);
            StdDraw.text(-80, 80, "is connected: " + g_a.isConnected());
        }
    }


    public void drawGraph() {
        try {
            if (g.getV() != null) {
                StdDraw.setCanvasSize(800, 800);
                StdDraw.setXscale(-100, 100);
                StdDraw.setYscale(-100, 100);

                //menu bar
                JMenuBar menu = new JMenuBar();
                JMenu file = new JMenu("File options");
                menu.add(file);
                JMenuItem save = new JMenuItem("Save");
                JMenuItem load = new JMenuItem("Load");
                JMenuItem isCon = new JMenuItem("is Connected");
                JMenuItem tsp = new JMenuItem("TSP");
                JMenuItem sP = new JMenuItem("Shortest Path");
                file.add(save);
                file.add(load);
                file.add(isCon);
                file.add(tsp);
                file.add(sP);
                save.addActionListener(this);
                load.addActionListener(this);
                isCon.addActionListener(this);
                tsp.addActionListener(this);
                sP.addActionListener(this);

                StdDraw.frame.setJMenuBar(menu);

                drawEdges();
                drawVertex();
                drawVertexKey();

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void drawVertexKey() {
        try {
            Collection<node_data> vertex = g.getV();
            for (node_data a : vertex) {
                double x = a.getLocation().x() - 0.01;
                double y = a.getLocation().y() - 0.4;
                StdDraw.setPenRadius(0.009);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.text(x, y, "" + a.getKey());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void drawVertex() {
        try {
            Collection<node_data> vertex = g.getV();
            for (node_data a : vertex) {
                double x = a.getLocation().x();
                double y = a.getLocation().y();
                StdDraw.setPenRadius(0.04);
                StdDraw.setPenColor(StdDraw.BOOK_BLUE);
                StdDraw.point(x, y);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void drawEdges() {
        Collection<node_data> vertex = g.getV();
        for (node_data a : vertex) {
            nodeData tmp = (nodeData) a;
            Collection<edge_data> edge = g.getE(a.getKey());
            for (edge_data e : edge) {
                double s_x = a.getLocation().x();
                double s_y = a.getLocation().y();
                double d_x = g.getNode(e.getDest()).getLocation().x();
                double d_y = g.getNode(e.getDest()).getLocation().y();
                //draw the edge between two vertexes
                StdDraw.setPenRadius(0.004);
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(s_x, s_y, d_x, d_y);
                //draw the weight on the middle edge
                StdDraw.setPenColor(StdDraw.YELLOW);
                StdDraw.setPenRadius(0.019);
                StdDraw.filledCircle((0.2) * s_x + (0.8) * d_x, (0.2) * s_y + (0.8) * d_y, 0.6);
                StdDraw.setPenRadius(0.3);
                StdDraw.setPenColor(StdDraw.RED);
                String weight = Double.toString(e.getWeight());
                StdDraw.text((0.65) * s_x + (0.35) * d_x, (0.65) * s_y + (0.35) * d_y, weight);
            }
        }
    }

    private void shotestPath() {
        drawGraph();
        JFrame jinput = new JFrame();
        String fromS = JOptionPane.showInputDialog(jinput, "Enter From");
        String to = JOptionPane.showInputDialog(jinput, "Enter To");
        fromS = fromS.trim();
        to = to.trim();
        try {
            int fromN = Integer.parseInt(fromS);
            int toN = Integer.parseInt(to);
            Graph_Algo ga = new Graph_Algo();
            ga.init(g);
            List<node_data> ans = ga.shortestPath(fromN, toN);
            for (int j = 0; j < ans.size() - 1; j++) {
                StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                StdDraw.line(ans.get(j).getLocation().ix(), ans.get(j).getLocation().iy(), ans.get(j + 1).getLocation().ix(), ans.get(j + 1).getLocation().iy());
            }
            drawVertex();
            drawVertexKey();
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void TSPEnterNodes() {
        drawGraph();
        JFrame jinput = new JFrame();
        JOptionPane.showMessageDialog(jinput, "To get TSP enter all the nodes from start node to end node \n after you are done enter DONE");
        ArrayList<Integer> arrayTSP = new ArrayList<Integer>();
        String ans;
        do {
            ans = JOptionPane.showInputDialog(jinput, "Enter node or DONE when it is the last node");
            if (ans.equalsIgnoreCase("done")) {
                break;
            }
            try {
                arrayTSP.add(Integer.parseInt(ans));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!ans.equalsIgnoreCase("done"));
        Graph_Algo ga = new Graph_Algo();
        ga.init(g);
        List<node_data> ansTSP = ga.TSP(arrayTSP);
        for (int j = 0; j < ansTSP.size() - 1; j++) {
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            StdDraw.line(ansTSP.get(j).getLocation().ix(), ansTSP.get(j).getLocation().iy(), ansTSP.get(j + 1).getLocation().ix(), ansTSP.get(j + 1).getLocation().iy());
        }
        drawVertex();
        drawVertexKey();
        repaint();
    }


    public static void main(String[] args) throws Exception {
    }
}