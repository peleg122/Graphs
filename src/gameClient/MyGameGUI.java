package gameClient;


import Server.Game_Server;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import org.json.JSONObject;
import utils.StdDraw;

import javax.swing.*;
import java.util.HashMap;

public class MyGameGUI {
    private int _numberOfRobots;
    private int _numberOfFruits;
    private HashMap<Integer, fruit> _allFruits;
    private HashMap<Integer, robot> _allRobots;
    private Game_Server _game_server;
    private String _graph;
    private DGraph _dGraph;
    private Graph_Algo _graph_algo;

    MyGameGUI() {
        _allFruits = new HashMap<>();
        _allRobots = new HashMap<>();
        StdDraw.enableDoubleBuffering();
        init();


    }

    public void init() {
        Object[] levels = {"0", "1", "3", "4", "5", "6",
                "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18",
                "19", "20", "21", "22", "23"};
        int level = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Choose Level: [0,23] ", "level", JOptionPane.QUESTION_MESSAGE, null, levels, null));

        _game_server = (Game_Server) _game_server.getServer(level);
        _dGraph = new DGraph();
        _dGraph.init(_game_server.getGraph());
        _graph_algo = new Graph_Algo(_dGraph);

    }

    public static void main(String[] args) {
        MyGameGUI g = new MyGameGUI();
    }



    /*public String toJSON() {
        String ans = null;
        JSONObject res = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("graph", this.graph);
            data.put("fruits", this.numberOfFruits);
            data.put("grade", this.score);
            data.put("moves", this._number_of_moves);
            data.put("robots", this._robots_number);
            res.put("GameServer", data);
            ans = res.toString();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return ans;
    }*/
}
