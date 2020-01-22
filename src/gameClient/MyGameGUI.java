package gameClient;


import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.nodeData;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;


public class MyGameGUI extends JFrame implements ActionListener, MouseListener, Runnable {
    private static DGraph _g;
    private game_service game;
    private int scenario;
    private Graph_Algo ga;
    private int PathCreatorCounter = 0;
    private int NodeCreatorCounter = 0;
    private HashMap<Point3D, Fruits> fruits = new HashMap<>();
    private HashMap<Integer, Robot> robots = new HashMap<Integer, Robot>();
    double xMax = Double.NEGATIVE_INFINITY;
    double xMin = Double.POSITIVE_INFINITY;
    double yMax = Double.NEGATIVE_INFINITY;
    double yMin = Double.POSITIVE_INFINITY;
    public static KML_Logger kml = null;

    Thread t = new Thread(this);
    private HashMap<Integer, List<node_data>> routes;
    static int rid = -1;
    final static double EPS = 0.0002;
    private String mode;

    /**
     * creating the first view of client graphic user interface.
     */
    public MyGameGUI() {
        //Game_Server.login(316486786);
        Object[] levels = {"-1", "0", "1", "2", "3", "4", "5", "6",
                "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18",
                "19", "20", "21", "22", "23"};
        String level = (String) JOptionPane.showInputDialog(null, "Choose Level: [0,23] ", "level", JOptionPane.QUESTION_MESSAGE, null, levels, null);
        this.scenario = Integer.parseInt(level);
        Object[] modes = {"Auto", "Manual"};
        mode = (String) JOptionPane.showInputDialog(null, "Choose Mode ", "Mode", JOptionPane.QUESTION_MESSAGE, null, modes, null);
        StdDraw.setCanvasSize(1200, 1200);
        StdDraw.setXscale(xMin, xMax);
        StdDraw.setYscale(yMin, yMax);
        initGame(this.scenario);

    }

    public MyGameGUI(int num) {
        Skip(num);
    }

    public void Skip(int num) {
        this.scenario = num;
        this.mode = "Auto";
        //StdDraw.setCanvasSize(1200, 1200);
        StdDraw.setXscale(xMin, xMax);
        StdDraw.setYscale(yMin, yMax);
        initGame(num);
    }

    /**
     * initializing this game by a client choice scenario
     *
     * @param gameNumber is the scenario.
     */
    private void initGame(int gameNumber) {
        kml = new KML_Logger(scenario);
        this.game = Game_Server.getServer(gameNumber); // you have [0,23] games
        String sg = game.getGraph();
        this._g = new DGraph(sg);
        this.ga = new Graph_Algo(this._g);
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("robots");
            routes = new HashMap<>();
            System.out.println(info);
            // the list of fruits should be considered in your solution
            Iterator<String> f_iter = game.getFruits().iterator();
            int fs = 0;
            while (f_iter.hasNext()) {
                fs++;
                Fruits f = new Fruits(f_iter.next());
                this.fruits.put(f.getLocation(), f);
                if (f.getType() == 1)
                    kml.PlaceMark("apple", f.getLocation());
                else
                    kml.PlaceMark("banana", f.getLocation());

                f.edgeLocator(_g);

            }
            Iterator<Fruits> f = fruits.values().iterator();
            for (int a = 0; a < rs; a++) {
                if (a < fs) {
                    Fruits fruit = f.next();
                    game.addRobot(fruit.getEdge().getSrc());
                    nodeData temp = (nodeData) _g.getNode(fruit.getEdge().getSrc());
                    kml.PlaceMarkRobot("robot", "Robot" + a, temp.getLocation());

                    temp.setRobot(true);
                } else {
                    nodeData temp = (nodeData) _g.getNode(a);
                    if (!temp.getRobot())
                        game.addRobot(a);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        game.startGame();
        t.start();
        linearTranspose();
    }

    /**
     * transposing x and y scale for the gui from the graph world.
     */
    private void linearTranspose() {
        for (Iterator<node_data> verIter = _g.getV().iterator(); verIter.hasNext(); ) {
            int point = verIter.next().getKey();
            if (_g.getNode(point).getLocation().x() > xMax)
                xMax = _g.getNode(point).getLocation().x();
            if (_g.getNode(point).getLocation().y() > yMax)
                yMax = _g.getNode(point).getLocation().y();
            if (_g.getNode(point).getLocation().x() < xMin)
                xMin = _g.getNode(point).getLocation().x();
            if (_g.getNode(point).getLocation().y() < yMin)
                yMin = _g.getNode(point).getLocation().y();
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

    /**
     * drawing this graph nodes.
     */
    public void drawGraph() {
        if (!_g.getV().isEmpty()) {
            Iterator<node_data> nodes = _g.getV().iterator();
            while (nodes.hasNext()) {
                node_data n = nodes.next();
                double x = n.getLocation().x();
                double y = n.getLocation().y();
                if (NodeCreatorCounter < _g.nodeSize()) {
                    kml.PlaceMark("node", n.getLocation());
                    NodeCreatorCounter++;
                }
                StdDraw.setPenRadius(0.05);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.point(x, y);
                StdDraw.setPenColor(StdDraw.BLACK);
                String str = n.getKey() + "";
                StdDraw.text(x, y, str);
            }

            Iterator<node_data> allNodes = _g.getV().iterator();
            while (allNodes.hasNext()) {
                node_data n = allNodes.next();
                Iterator<edge_data> allEdgesOfNode = _g.getE(n.getKey()).iterator();
                while (allEdgesOfNode.hasNext()) {
                    edge_data edges = allEdgesOfNode.next();
                    double sx = _g.getNode(edges.getSrc()).getLocation().x();
                    double sy = _g.getNode(edges.getSrc()).getLocation().y();
                    double dx = _g.getNode(edges.getDest()).getLocation().x();
                    double dy = _g.getNode(edges.getDest()).getLocation().y();
                    if (PathCreatorCounter < _g.edgeSize()) {
                        kml.PlaceMarkPath(edges.getSrc() + "To" + edges.getDest(), _g.getNode(edges.getSrc()).getLocation(),
                                _g.getNode(edges.getDest()).getLocation());
                        PathCreatorCounter++;
                    }

                    StdDraw.setPenRadius(0.005);
                    StdDraw.setPenColor(StdDraw.BLUE);

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

    private void addRobots() {
        List<String> log = game.move();
        if (log != null) {
            for (int i = 0; i < log.size(); i++) {
                String robot_json = log.get(i);
                try {
                    JSONObject line = new JSONObject(robot_json);
                    JSONObject ttt = line.getJSONObject("Robot");
                    int rid = ttt.getInt("id");
                    robots.put(rid, new Robot(robot_json));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateRobots() {
        List<String> log = game.move();
        if (log != null) {
            for (int i = 0; i < log.size(); i++) {
                String robot_json = log.get(i);
                try {
                    JSONObject line = new JSONObject(robot_json);
                    System.out.println(robot_json);
                    JSONObject ttt = line.getJSONObject("Robot");
                    int rid = ttt.getInt("id");
                    robots.get(rid).update(robot_json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * drawing robots on the graph.
     */
    public void drawRobot() {
        List<String> log = game.getRobots();
        Iterator<String> rob = log.iterator();
        while (rob.hasNext()) {
            String Json = rob.next();
            Robot robot1 = new Robot(Json);
            robots.put(robot1.getId(), robot1);
            StdDraw.picture(robot1.getLocation().x(), robot1.getLocation().y(), "robot1.png", 0.001, 0.001);
            kml.PlaceMarkRobot("robot", "Robot" + robot1.getId(), robot1.getLocation());
        }
    }


    /**
     * drawing fruits on the graph.
     */
    public void drawFruit() {
        fruits.clear();
        Iterator<String> fruit = game.getFruits().iterator();
        while (fruit.hasNext()) {
            Fruits cf = new Fruits(fruit.next());
            this.fruits.put(cf.getLocation(), cf);
            if (cf.getType() == 1) {
                StdDraw.picture(cf.getLocation().x(), cf.getLocation().y(), "apple.png", 0.0008, 0.0008);
                kml.PlaceMark("apple", cf.getLocation());
            } else {
                StdDraw.picture(cf.getLocation().x(), cf.getLocation().y(), "banana.png", 0.0007, 0.0007);
                kml.PlaceMark("banana", cf.getLocation());

            }
        }
    }

    /**
     * move the robots using graph algorithms to get the most fruits
     */
    public void moveRobotsManual() {
        int destNode = -1;
        int destByMouse = -1;
        int counter = 0;
        List<String> log = game.move();
        if (log != null) {
            long t = game.timeToEnd();
            if (StdDraw.isMousePressed())//check if there is new pressed
            {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                destNode = nextNodeManual(x, y);
            }
            if (destNode != -1)//if the mouse pressed at least one time
            {
                double minDis = Double.POSITIVE_INFINITY;
                int index = 0;
                for (int i = 0; i < log.size(); i++) //find the closest robots to the press
                {
                    counter++;
                    String robot_json = log.get(i);
                    try {
                        JSONObject line = new JSONObject(robot_json);
                        JSONObject ttt = line.getJSONObject("Robot");
                        int rid = ttt.getInt("id");
                        int src = ttt.getInt("src");
                        int dest = ttt.getInt("dest");
                        if (counter == 1) {
                            if (ga.shortestPathDist(src, destNode) < minDis) {
                                minDis = ga.shortestPathDist(src, destNode);
                                index = i;
                            }
                            if (ga.shortestPath(src, destNode).size() == 1)
                                destByMouse = destNode;
                            else
                                destByMouse = ga.shortestPath(src, destNode).get(1).getKey();

                            game.chooseNextEdge(rid, destByMouse);
                        } else {
                            if (dest == -1) {
                                dest = nextNodeRoute(rid, src, ga);//nextNode(gg, src);
                                game.chooseNextEdge(rid, dest);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        System.out.println("Turn to node: " + destByMouse + "  time to end:" + (t / 1000));
                        System.out.println((new JSONObject(log.get(index))).getJSONObject("Robot"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * moving the robots on the gui, printing the scenario log.
     */
    private void moveRobots() {
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
                        dest = nextNodeRoute(rid, src, ga);//nextNode(gg, src);
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

    /**
     * this method is the algorithm of the movement of each robot on the graph.
     * using a graph algorithm class to find the shortest path for the robot to move
     * the shortest path algorithm is dijkstra,
     * hebrew explanation(https://he.wikipedia.org/wiki/%D7%90%D7%9C%D7%92%D7%95%D7%A8%D7%99%D7%AA%D7%9D_%D7%93%D7%99%D7%99%D7%A7%D7%A1%D7%98%D7%A8%D7%94).
     * english explanation(https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm).
     *
     * @param rid this robot id.
     * @param src this robot source node.
     * @param g   the graph
     * @return
     */
    private int nextNodeRoute(int rid, int src, Graph_Algo g) {
        int ans = -1;
        Fruits temp = null;
        double minDist = Double.MAX_VALUE;
        if (routes.get(rid) == null || routes.get(rid).isEmpty()) {
            int fs = fruits.size();
            Iterator<Fruits> fi = fruits.values().iterator();
            for (int i = 0; i < fs; i++) {
                Fruits f = fi.next();
                if (!f.getOccupied()) {
                    f.edgeLocator((DGraph) g._graph);
                    if (g.shortestPathDist(src, f.getEdge().getDest()) < minDist) {
                        temp = f;
                    }
                }
            }
            temp.setOccupied(true);
            routes.put(rid, g.shortestPath(src, temp.getEdge().getDest()));
            if (routes.get(rid).size() > 1 && routes.get(rid).get(0).getKey() == src) {
                routes.get(rid).remove(0);
                return routes.get(rid).remove(0).getKey();
            } else if (routes.get(rid).size() == 1) {
                if (routes.get(rid).get(0).getKey() == src)
                    if (temp.getType() == 1)
                        return (routes.get(rid).remove(0).getKey() - 1) % g._graph.nodeSize();
                    else return (routes.get(rid).remove(0).getKey() + 1) % g._graph.nodeSize();
            }
        } else {
            if (routes.get(rid).size() > 1) {
                if (routes.get(rid).get(0).getKey() == src) {
                    routes.get(rid).remove(0);
                }
            }
            return routes.get(rid).remove(0).getKey();
        }
        return ans;
    }


//    private List<node_data> closestFruit(int src, Graph_Algo g) {
//        double shortestPath = 99999;
//        List<node_data> temp = null;
//        int fs = fruits.size();
//        Iterator<Fruits> fi = fruits.values().iterator();
//        for (int i = 0; i < fs; i++) {
//            Fruits f = fi.next();
//            if (f.getType() == 1) {
//                if (f.getEdge().getSrc() > f.getEdge().getDest()) {
//                    if (g.shortestPathDist(src, f.getDest()) + g.shortestPathDist(f.getDest(), f.getSrc()) < shortestPath) {
//                        shortestPath = g.shortestPathDist(src, f.getDest()) + g.shortestPathDist(f.getDest(), f.getSrc());
//                        temp = g.shortestPath(src, f.getDest());
//                        temp.add(g._graph.getNode(f.getSrc()));
//                    }
//                } else {
//                    if (g.shortestPathDist(src, f.getSrc()) + g.shortestPathDist(f.getSrc(), f.getDest()) < shortestPath) {
//                        shortestPath = g.shortestPathDist(src, f.getSrc()) + g.shortestPathDist(f.getSrc(), f.getDest());
//                        temp = g.shortestPath(src, f.getSrc());
//                        temp.add(g._graph.getNode(f.getDest()));
//                    }
//                }
//            } else {
//                if (f.getEdge().getSrc() > f.getEdge().getDest()) {
//                    if (g.shortestPathDist(src, f.getSrc()) + g.shortestPathDist(f.getSrc(), f.getDest()) < shortestPath) {
//                        shortestPath = g.shortestPathDist(src, f.getSrc()) + g.shortestPathDist(f.getSrc(), f.getDest());
//                        temp = g.shortestPath(src, f.getSrc());
//                        temp.add(g._graph.getNode(f.getDest()));
//                    }
//                } else {
//                    if (g.shortestPathDist(src, f.getDest()) + g.shortestPathDist(f.getDest(), f.getSrc()) < shortestPath) {
//                        shortestPath = g.shortestPathDist(src, f.getDest()) + g.shortestPathDist(f.getDest(), f.getSrc());
//                        temp = g.shortestPath(src, f.getDest());
//                        temp.add(g._graph.getNode(f.getSrc()));
//                    }
//                }
//            }
//        }
//        return temp;
//    }
    /**
     * if (g.shortestPathDist(src, f.getDest()) < shortestPath) {
     *                         shortestPath = g.shortestPathDist(src, f.getDest());
     *                         dest = f.getDest();
     *                     }
     */

    /**
     * drawing the score and time left for each scenario game
     * on the client graphic user interface while the game is still running.
     */
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

    /**
     * refreshing the graphic user interface all game long
     * while the game is running the robots, score and time is always running or changing position
     * on the graph, in order to see a clean client view we need to refresh it at all time.
     */
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

    /**
     * defining the run method for this game thread
     */
    public void run() {
        int counter = 0;
        while (game.isRunning()) {
            while (counter % 4 == 0 && game.isRunning()) {
                StdDraw.enableDoubleBuffering();
                synchronized (this) {
                    refreshDraw();
                    drawGraph();
                    drawFruit();
                    drawRobot();
                    drawScore();
                    if (mode.equals("Auto")) moveRobots();
                    else {
                        moveRobotsManual();
                    }
                    StdDraw.show();
                    try {
                        t.sleep(90);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            counter++;
        }
        String results = game.toString();
        System.out.println("Game Over: " + results);
        String remark = kml.kmlEndAndSave();
        game.sendKML(remark);
    }


    /**
     * by clicking the node the player want to go to, moves the chosen robot to him
     *
     * @return
     */
    private static int nextNodeManual(double x, double y) {//The manual moves
        double minDis = Double.POSITIVE_INFINITY;
        int src = 0;
        for (Iterator<node_data> verIter = _g.getV().iterator(); verIter.hasNext(); ) {
            int nd = verIter.next().getKey();
            if (Math.abs(x - _g.getNode(nd).getLocation().x()) + Math.abs(y - _g.getNode(nd).getLocation().y()) < minDis) {
                minDis = Math.abs(x - _g.getNode(nd).getLocation().x()) + Math.abs(y - _g.getNode(nd).getLocation().y());
                src = nd;
            }
        }
        return src;
    }


    public static void main(String[] args) {
        MyGameGUI a0 = new MyGameGUI();
    }
}
