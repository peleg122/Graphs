package gameClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;

import Server.Game_Server;
import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;
import utils.StdDraw;

public class gameLogic {
    private game_service game;
    private DGraph g;
    private Graph_Algo ga;
    private HashMap<Point3D, fruit> fruits;
    private HashMap<Integer,robot> robots;
    private int destNode=-1;
    private int destByMouse;
    private robot move;

    public gameLogic(Game_Server game_server, HashMap<Point3D, fruit> allFruits, HashMap<Integer, robot> allRobots) {
        this.game = game_server;
        fruits=allFruits;
        robots=allRobots;
        g=new DGraph(game.getGraph());
        ga=new Graph_Algo();
        ga.init(g);
    }

    public void update(Game_Server game_server, HashMap<Point3D, fruit> allFruits, HashMap<Integer, robot> allRobots) {
        this.game=game_server;
        fruits=allFruits;
        robots=allRobots;
    }

    public void moveRobots() {
        List<Integer> destList = new ArrayList<Integer>();
        List<String> log = game.move();
        if(log!=null) {
            long t = game.timeToEnd();
            for(int i=0;i<log.size();i++) //for each robots find his next destination
            {
                destList.add(robots.get(i).getDest());
            }
            for(int i=0;i<log.size();i++) //for each robots find his next destination
            {
                try {
                    int rid=robots.get(i).getId();
                    int src=robots.get(i).getSrc();
                    int dest=robots.get(i).getDest();
                    if(dest==-1)
                    {
                        dest = bestNode(src,destList); //find the closet fruit
                        destList.add(dest);
                        game.chooseNextEdge(rid, dest);
                        System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
                        System.out.println((new JSONObject(log.get(i))).getJSONObject("Robot"));
                    }
                }
                catch (JSONException e) {e.printStackTrace();}
            }
        }
    }
    private  int bestNode(int src,List<Integer>destList)
    {
        double minpath=Double.POSITIVE_INFINITY;
        edge_data e=null;
        int destFinal=0;
        boolean isGetDest=false;
        for(int i=0;i<fruits.size();i++) //find the fruit that is closest to the src
        {
            double dist;List<node_data> node;int dest;
            e=fruits.get(i).getEdge();
            if(fruits.get(i).getType()==1) //if its apple
            {
                dist=ga.shortestPathDist(src, e.getSrc());
                node=ga.shortestPath(src, e.getSrc());
                if(node.size()==1)
                    dest= e.getDest();
                else
                    dest=node.get(1).getKey();
            }
            else//if its banana
            {
                dist=ga.shortestPathDist(src, e.getDest());
                node=ga.shortestPath(src, e.getDest());
                if(node.size()==1)
                    dest= e.getSrc();
                else
                    dest=node.get(1).getKey();
            }
            if(dist<minpath&&!destList.contains(dest))//check if its better result and if the dest is not in the list
            {
                isGetDest=true;
                minpath=dist;
                destFinal=dest;
            }
        }
        if(isGetDest)
            return destFinal;
        else //if there isnt a fruit available for this robots
        {
            e=fruits.get(0).getEdge();
            if(src==e.getSrc())
            {
                return e.getDest();
            }
            else
            if(src==e.getDest())
                return e.getSrc();
            List<node_data> node=ga.shortestPath(src, e.getSrc());
            return (node.get(1).getKey());
        }
    }

    public void moveRobotsbyMouse() {

    }

    /**
     * This class runs algorithms for moving robots in the graph
     * There are two algorithms : by mouse an by automatic thats moves the robot to the next close node
     * @author itay simhayev and lilach mor
     *
     */
    public class algoForGui
    {
        private game_service game;
        private DGraph g;
        private Graph_Algo ga;
        private List<fruit> fruits;
        private List<robot> robots;
        private int destNode=-1;
        private int destByMouse;
        private robot move;

        public algoForGui(game_service game1,List<fruit> f,List<robot> p)
        {
            this.game=game1;
            fruits=f;
            robots=p;
            g=new DGraph(game.getGraph());
            ga=new Graph_Algo();
            ga.init(g);
        }
        /**
         * Update the game, fruit and pacman
         * @param game1 - the update game
         * @param f - the update list of fruits
         * @param p - the update list of Pacman
         */
        public  void update(game_service game1,List<fruit> f,List<robot> p)
        {
            this.game=game1;
            fruits=f;
            robots=p;
        }

        /**
         * The function move the robots by algorithms
         * each robot to the closest fruit
         */

        public void moveRobots()
        {
            List<Integer> destList = new ArrayList<Integer>();
            List<String> log = game.move();
            if(log!=null) {
                long t = game.timeToEnd();
                for(int i=0;i<log.size();i++) //for each robots find his next destination
                {
                    destList.add(robots.get(i).getDest());
                }
                for(int i=0;i<log.size();i++) //for each robots find his next destination
                {
                    try {
                        int rid=robots.get(i).getId();
                        int src=robots.get(i).getSrc();
                        int dest=robots.get(i).getDest();
                        if(dest==-1)
                        {
                            dest = bestNode(src,destList); //find the closet fruit
                            destList.add(dest);
                            game.chooseNextEdge(rid, dest);
                            System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
                            System.out.println((new JSONObject(log.get(i))).getJSONObject("Robot"));
                        }
                    }
                    catch (JSONException e) {e.printStackTrace();}
                }
            }
        }
        /**
         * find the fruits that is the closest to the robot in node src
         * @param src
         * @return the id of the next node
         */
        private  int bestNode(int src,List<Integer>destList)
        {
            double minpath = Double.POSITIVE_INFINITY;
            edge_data e=null;
            int destFinal=0;
            boolean isGetDest=false;
            for(int i=0;i<fruits.size();i++) //find the fruit that is closest to the src
            {
                double dist;List<node_data> node;int dest;
                e=fruits.get(i).getEdge();
                if(fruits.get(i).getType()==1) //if its apple
                {
                    dist=ga.shortestPathDist(src, e.getSrc());
                    node=ga.shortestPath(src, e.getSrc());
                    if(node.size()==1)
                        dest= e.getDest();
                    else
                        dest=node.get(1).getKey();
                }
                else//if its banana
                {
                    dist=ga.shortestPathDist(src, e.getDest());
                    node=ga.shortestPath(src, e.getDest());
                    if(node.size()==1)
                        dest= e.getSrc();
                    else
                        dest=node.get(1).getKey();
                }
                if(dist<minpath&&!destList.contains(dest))//check if its better result and if the dest is not in the list
                {
                    isGetDest=true;
                    minpath=dist;
                    destFinal=dest;
                }
            }
            if(isGetDest)
                return destFinal;
            else //if there isnt a fruit available for this robots
            {
                e=fruits.get(0).getEdge();
                if(src==e.getSrc())
                {
                    return e.getDest();
                }
                else
                if(src==e.getDest())
                    return e.getSrc();
                List<node_data> node=ga.shortestPath(src, e.getSrc());
                return (node.get(1).getKey());
            }
        }
        ////////////by mouse////////

        /**
         * The function move the robots by mouse
         * moves the closest robots to the node that pressed until the robots arrives to the node
         */
        public  void moveRobotsbyMouse()
        {
            List<String> log = game.move();
            if(log!=null)
            {
                long t = game.timeToEnd();
                if(StdDraw.isMousePressed())//check if there is new pressed
                {
                    double x=StdDraw.mouseX();double y=StdDraw.mouseY();
                    destNode=findNode(x,y);
                }
                if(destNode!=-1)//if the mouse pressed at least one time
                {
                    double minDis=Double.POSITIVE_INFINITY;int index=0;
                    for(int i=0;i<log.size();i++) //find the closest robots to the press
                    {
                        int src=robots.get(i).getSrc();
                        if(ga.shortestPathDist(src, destNode)<minDis)
                        {
                            minDis=ga.shortestPathDist(src, destNode);
                            move=robots.get(i);
                            index=i;
                        }
                    }
                    if(ga.shortestPath(move.getSrc(),destNode).size()==1)
                        destByMouse=destNode;
                    else
                        destByMouse=ga.shortestPath(move.getSrc(),destNode).get(1).getKey();
                    game.chooseNextEdge(move.getId(), destByMouse);
                    try {
                        System.out.println("Turn to node: "+destByMouse+"  time to end:"+(t/1000));
                        System.out.println((new JSONObject(log.get(index))).getJSONObject("Robot"));
                    }
                    catch (JSONException e) {e.printStackTrace();}
                }
            }
        }
        /**
         * The function find the node that is closest to the press
         * @param x- the coordinate of X that Pressed
         * @param y- the coordinate of Y that Pressed
         * @return the id of the node
         */
        private int findNode(double x,double y)
        {
            double minDis=Double.POSITIVE_INFINITY;
            int src=0;
            for(Iterator<node_data> verIter=g.getV().iterator();verIter.hasNext();)
            {
                int nd=verIter.next().getKey();
                if(Math.abs(x-g.getNode(nd).getLocation().x())+Math.abs(y-g.getNode(nd).getLocation().y())<minDis)
                {
                    minDis=Math.abs(x-g.getNode(nd).getLocation().x())+Math.abs(y-g.getNode(nd).getLocation().y());
                    src=nd;
                }
            }
            return src;
        }
    }
}
