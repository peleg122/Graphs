package gameClient;


import Server.Game_Server;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import elements.nodeData;

import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import javax.swing.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MyGameGUI {
    private int _numberOfRobots;
    private int _numberOfFruits;
    private HashMap<Integer, fruit> _allFruits;
    private HashMap<Integer, robot> _allRobots;
    public Game_Server _game_server;
    private DGraph _dGraph;
    private Graph_Algo _graph_algo;


    MyGameGUI() {
        _allFruits = new HashMap<>();
        _allRobots = new HashMap<>();
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();
        init();
        setGame();
        paint();
        paintFruits();
        paintRobots();
        StdDraw.show();
    }

    public void init() {
        Object[] levels = {"0", "1", "3", "4", "5", "6",
                "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18",
                "19", "20", "21", "22", "23"};
        int level = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Choose Level: [0,23] ", "level", JOptionPane.QUESTION_MESSAGE, null, levels, null));

        _game_server = (Game_Server) _game_server.getServer(level);
        _dGraph = new DGraph(_game_server.getGraph());
        _graph_algo = new Graph_Algo(_dGraph);
        addFruits();
        addRobots();
        _numberOfFruits = _allFruits.size();
        _numberOfRobots = _allRobots.size();
    }

    public void paint() {
        paintEdges();
        paintVertex();
    }

    private void paintFruits() {
        for (fruit f : _allFruits.values()) {
            if (f.get_type() == 1)
                StdDraw.picture(f.get_pos().x(), f.get_pos().y(), "utils/apple.png");
            else
                StdDraw.picture(f.get_pos().x(), f.get_pos().y(), "utils/banana.png");
        }
    }

    private void paintRobots() {
        for (robot r : _allRobots.values()) {
            StdDraw.picture(r.getPointLocation().x(), r.getPointLocation().y(), "utils/robot.png");
        }
    }

    public void setGame() {
        double xMax = Double.MIN_VALUE;
        double xMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;
        double yMin = Double.MAX_VALUE;
        double epsilon = 0.002;
        Iterator<node_data> it = _dGraph.getV().iterator();
        nodeData n;
        while (it.hasNext()) {
            n = (nodeData) it.next();
            if (n.getLocation().x() > xMax)
                xMax = n.getLocation().x();
            if (n.getLocation().y() > yMax)
                yMax = n.getLocation().y();
            if (n.getLocation().x() < xMin)
                xMin = n.getLocation().x();
            if (n.getLocation().y() < yMin)
                yMin = n.getLocation().y();

            StdDraw.setCanvasSize(800, 800);
            StdDraw.setXscale(xMin - epsilon, xMax + epsilon);
            StdDraw.setYscale(yMin - epsilon, yMax + epsilon);
        }
    }

    private void paintVertex() {
        try {
            Collection<node_data> vertex = _dGraph.getV();
            for (node_data a : vertex) {
                double x = a.getLocation().x();
                double y = a.getLocation().y();
                StdDraw.setPenRadius(0.04);
                StdDraw.setPenColor(StdDraw.BOOK_BLUE);
                StdDraw.point(x, y);
                StdDraw.setPenRadius(0.009);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.text(x, y, "" + a.getKey());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void paintEdges() {
        Collection<node_data> vertex = _dGraph.getV();
        for (node_data a : vertex) {
            Collection<edge_data> edge = _dGraph.getE(a.getKey());
            for (edge_data e : edge) {
                double s_x = a.getLocation().x();
                double s_y = a.getLocation().y();
                double d_x = _dGraph.getNode(e.getDest()).getLocation().x();
                double d_y = _dGraph.getNode(e.getDest()).getLocation().y();
                //draw the edge between two vertexes
                StdDraw.setPenRadius(0.004);
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(s_x, s_y, d_x, d_y);
            }
        }
    }

    private void addFruits() {
        Iterator<String> it = _game_server.getFruits().iterator();
        String f;
        int j = 0;
        while (it.hasNext()) {
            f = it.next();
            try {
                JSONObject fruity = new JSONObject(f.substring(9, f.length() - 1));
                double x;
                double y;
                double v;
                int t;
                v = fruity.getDouble("value");
                t = fruity.getInt("type");
                String pos = fruity.getString("pos");
                String[] positions = pos.split(",");
                x = Double.parseDouble(positions[0]);
                y = Double.parseDouble(positions[1]);
                Point3D p = new Point3D(x, y);
                fruit fruit = new fruit(p, v, t);
                fruit.edgdeLocator(_dGraph);
                _allFruits.put(j, fruit);
                j++;
            } catch (Exception var12) {
                var12.printStackTrace();
            }
        }
    }

    private void addRobots() {
        String info = _game_server.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("robots");
            int fs = ttt.getInt("fruits");
            for (int a = 0; a < rs; a++) {
                if (a < fs) {
                    _game_server.addRobot(_allFruits.get(a).get_edge().getSrc());
                    nodeData temp = (nodeData) _dGraph.getNode(_allFruits.get(a).get_edge().getSrc());
                    temp.setRobot(true);
                } else {
                    nodeData temp = (nodeData) _dGraph.getNode(a);
                    if (!temp.getRobot())
                        _game_server.addRobot(a);
                }
            }
            Iterator<String> it = _game_server.getRobots().iterator();
            String r;
            int j = 0;
            while (it.hasNext()) {
                r = it.next();
                JSONObject robot = new JSONObject(r.substring(9, r.length() - 1));
                int t;
                t = robot.getInt("src");
                robot robot1 = new robot(t);
                robot1.location(_dGraph);
                _allRobots.put(j, robot1);
                j++;
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
    }
    //-------------Test Movement-----------------------------------------------//
    private static void moveRobots(Game_Server game, DGraph gg) {
        List<String> log = game.move();
        if(log!=null) {
            long t = game.timeToEnd();
            for(int i=0;i<log.size();i++) {
                String robot_json = log.get(i);
                try {
                    JSONObject line = new JSONObject(robot_json);
                    JSONObject ttt = line.getJSONObject("Robot");
                    int rid = ttt.getInt("id");
                    int src = ttt.getInt("src");
                    int dest = ttt.getInt("dest");

                    if(dest==-1) {
                        dest = nextNode(gg, src);
                        game.chooseNextEdge(rid, dest);
                        System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
                        System.out.println(ttt);
                    }
                }
                catch (JSONException e) {e.printStackTrace();}
            }
        }
    }
    private static int nextNode(DGraph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;}
        ans = itr.next().getDest();
        return ans;
    }


    public static void main(String[] args) {
        MyGameGUI g = new MyGameGUI();
        g._game_server.startGame();
        while(g._game_server.isRunning()) {
            moveRobots(g._game_server,g._dGraph);
            //need to update the fruits and move robot functions
        }
        String results = g._game_server.toString();
        System.out.println("Game Over: "+results);
    }
}
