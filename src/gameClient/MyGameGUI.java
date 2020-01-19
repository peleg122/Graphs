package gameClient;


import elements.nodeData;
import utils.Point3D;
import utils.StdDraw;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;


public class MyGameGUI extends JFrame implements ActionListener, MouseListener, Runnable {
    private DGraph g;
    private game_service game;
    private int senario;
    private Graph_Algo ga;
    private HashMap<Point3D, Fruits> fruits = new HashMap<>();
    private HashMap<Integer, Robot> robots = new HashMap<Integer, Robot>();
    double xMax = Double.NEGATIVE_INFINITY;
    double xMin = Double.POSITIVE_INFINITY;
    double yMax = Double.NEGATIVE_INFINITY;
    double yMin = Double.POSITIVE_INFINITY;

    public MyGameGUI() {
        Object[] levels = {"0", "1", "2", "3", "4", "5", "6",
                "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18",
                "19", "20", "21", "22", "23"};
        String level = (String) JOptionPane.showInputDialog(null, "Choose Level: [0,23] ", "level", JOptionPane.QUESTION_MESSAGE, null, levels, null);
        this.senario = Integer.parseInt(level);
        StdDraw.setCanvasSize(1200, 1200);
        StdDraw.setXscale(xMin, xMax);
        StdDraw.setYscale(yMin, yMax);
        initGame(this.senario);

    }

    private void initGame(int gameNumber) {
        Thread t = new Thread(this);
        this.game = Game_Server.getServer(gameNumber); // you have [0,23] games
        String sg = game.getGraph();
        this.g = new DGraph(sg);
        this.ga = new Graph_Algo(this.g);
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("robots");
            System.out.println(info);
            // the list of fruits should be considered in your solution
            Iterator<String> f_iter = game.getFruits().iterator();
            int fs = 0;
            while (f_iter.hasNext()) {
                fs++;
                Fruits f = new Fruits(f_iter.next().toString());
                this.fruits.put(f.getLocation(), f);
                f.edgdeLocator(g);
            }
            Iterator<Fruits> f = fruits.values().iterator();
            for (int a = 0; a < rs; a++) {
                if (a < fs) {
                    Fruits fruit = f.next();
                    game.addRobot(fruit.getEdge().getSrc());//need pos
                    nodeData temp = (nodeData) g.getNode(fruit.getEdge().getSrc());
                    temp.setRobot(true);
                } else {
                    nodeData temp = (nodeData) g.getNode(a);
                    if (!temp.getRobot())
                        game.addRobot(a);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        game.startGame();
        t.start();
        linearTranspos();
    }

    private void linearTranspos() {
        for (Iterator<node_data> verIter = g.getV().iterator(); verIter.hasNext(); ) {
            int point = verIter.next().getKey();
            if (g.getNode(point).getLocation().x() > xMax)
                xMax = g.getNode(point).getLocation().x();
            if (g.getNode(point).getLocation().y() > yMax)
                yMax = g.getNode(point).getLocation().y();
            if (g.getNode(point).getLocation().x() < xMin)
                xMin = g.getNode(point).getLocation().x();
            if (g.getNode(point).getLocation().y() < yMin)
                yMin = g.getNode(point).getLocation().y();
        }
        double epsilon = 0.0025;
        StdDraw.setCanvasSize(1200, 800);
        xMin = xMin - epsilon;
        xMax = xMax + epsilon;
        yMin = yMin - epsilon / 2;
        yMax = epsilon / 2 + yMax;
        StdDraw.setXscale(xMin, xMax);
        StdDraw.setYscale(yMin, yMax);
    }

    /*
     * Function that draws the node.
     */
    public void drawGraph() {
        if (!g.getV().isEmpty()) {
            Iterator<node_data> nodes = g.getV().iterator();
            while (nodes.hasNext()) {
                node_data n = nodes.next();
                double x = n.getLocation().x();
                double y = n.getLocation().y();
                StdDraw.setPenRadius(0.05);
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.point(x, y);
                StdDraw.setPenColor(StdDraw.BLACK);
                String str = n.getKey() + "";
                StdDraw.text(x, y, str);
            }

            Iterator<node_data> allNodes = g.getV().iterator();
            while (allNodes.hasNext()) {
                node_data n = allNodes.next();
                Iterator<edge_data> allEdgesOfNode = g.getE(n.getKey()).iterator();
                while (allEdgesOfNode.hasNext()) {
                    edge_data edges = allEdgesOfNode.next();
                    double sx = g.getNode(edges.getSrc()).getLocation().x();
                    double sy = g.getNode(edges.getSrc()).getLocation().y();
                    double dx = g.getNode(edges.getDest()).getLocation().x();
                    double dy = g.getNode(edges.getDest()).getLocation().y();

                    StdDraw.setPenRadius(0.005);
                    StdDraw.setPenColor(StdDraw.MAGENTA);

                    StdDraw.line(sx, sy, dx, dy);

                    StdDraw.setPenRadius(0.02);
                    StdDraw.setPenColor(StdDraw.CYAN);

                    double arrowX = (dx * 8 + sx) / 9;
                    double arrowY = (dy * 8 + sy) / 9;
                    StdDraw.point(arrowX, arrowY);

                    String te = String.format("%.4g%n", edges.getWeight());

                    StdDraw.setPenRadius(0.15);
                    StdDraw.setPenColor(Color.BLACK);

                    double newX = (dx * 4 + sx) / 5;
                    double newY = (dy * 4 + sy) / 5;

                    StdDraw.text(newX, newY, te);
                }
            }

        }
    }

    public void drawRobot() {
        List<String> log = game.getRobots();
        Iterator<String> rob = log.iterator();
        while (rob.hasNext()) {
            String Json = rob.next();
            Robot robot1 = new Robot(Json);
            robots.put(robot1.getId(), robot1);
            StdDraw.picture(robot1.getLocation().x(), robot1.getLocation().y(), "robot1.png", 0.001, 0.001);
        }
    }

    public void drawFruit() {
        fruits.clear();
        Iterator<String> fruit = game.getFruits().iterator();
        while (fruit.hasNext()) {
            Fruits cf = new Fruits(fruit.next());
            this.fruits.put(cf.getLocation(), cf);
            if (cf.getType() == 1) {
                StdDraw.picture(cf.getLocation().x(), cf.getLocation().y(), "banana.png", 0.0008, 0.0008);
            } else {
                StdDraw.picture(cf.getLocation().x(), cf.getLocation().y(), "apple.png", 0.0007, 0.0007);
            }
        }
    }

    private int nextNode(DGraph g, int src) {
        int ans = -1;

        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int) (Math.random() * s);
        int i = 0;
        while (i < r) {
            itr.next();
            i++;
        }
        ans = itr.next().getDest();
        return ans;

    }

    private void moveRobots(game_service game2, DGraph gg) {
        List<String> log = game.move();
        if (log != null) {
            long t = game.timeToEnd();
            for (int i = 0; i < log.size(); i++) {
                String robot_json = log.get(i);
                try {
                    JSONObject line = new JSONObject(robot_json);
                    JSONObject ttt = line.getJSONObject("Robot");
                    int rid = ttt.getInt("id");
                    int src = ttt.getInt("src");
                    int dest = ttt.getInt("dest");

                    if (dest == -1) {
                        dest = src + 1 % g.nodeSize();//nextNode(gg, src);
                        game.chooseNextEdge(rid, dest);
                        System.out.println("Turn to node: " + dest + "  time to end:" + (t / 1000));
                        System.out.println(ttt);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void drawScore() {
        double temp = xMax - xMin;
        double tmp = yMax - yMin;
        int score = 0;
        String time = game.timeToEnd() / 1000 + "." + game.timeToEnd() % 1000;
        String results = game.toString();
        try {
            JSONObject scoreobj = new JSONObject(results);
            JSONObject ttt = scoreobj.getJSONObject("GameServer");
            score = ttt.getInt("grade");
            StdDraw.text(xMin + temp / 1.2, yMin + tmp / 1.3, "Time: " + time);
            StdDraw.text(xMin + temp / 1.2, yMin + tmp / 1.2, "Score: " + score);
        } catch (Exception e) {
            throw new RuntimeException("Failed");
        }
    }

    public void refreshDraw() {
        StdDraw.clear();
        drawGraph();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void run() {
        while (game.isRunning()) {
            StdDraw.enableDoubleBuffering();
            synchronized (this) {
                refreshDraw();
                drawGraph();
                drawFruit();
                drawRobot();
                drawScore();
                moveRobots(this.game, this.g);
                StdDraw.show();
            }
        }
        String results = game.toString();
        System.out.println("Game Over: " + results);
    }

    public static void main(String[] args) {
        MyGameGUI junior = new MyGameGUI();
    }
}