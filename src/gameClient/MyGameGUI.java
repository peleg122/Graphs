package gameClient;


import Server.Game_Server;
import Server.game_service;
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
    private game_service _game_service;
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

        String s = JOptionPane.showInputDialog(null, "Enter Level (0-23): ");
        int level=-1;
        if(s.equals(null) || s.equals("")){
            level=0;
        }

        while (level < 0 || level > 23 && !s.isEmpty()) {
            level = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Level (0-23): "));
        }
        _game_service =  _game_server.getServer(level);
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
