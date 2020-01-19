package gameClient;


import dataStructure.node_data;
import org.json.JSONObject;

import dataStructure.DGraph;
import utils.Point3D;

public class Robot {
    node_data node;
    private DGraph g;
    private double value;
    private int id;
    private int src;
    private int dest;
    private double speed;
    private Point3D location;
    private String info;
    private String pic;

    /**
     * the method creates robot with all his Properties from a JSON file:
     * value- of value is the value(money) that each robot have after he collects the fruits.
     * src- source node the robot start from.
     * id- this robot id.
     * dest- destination node to the fruit.
     * speed- this robot speed travel.
     * pos- this robot position.
     * @param jsonSTR - a string that represents the robot
     */
    public Robot(String jsonSTR) {
        try {
            JSONObject robot = new JSONObject(jsonSTR);
            robot=robot.getJSONObject("Robot");
            value = robot.getDouble("value");
            this.src=robot.getInt("src");
            this.id=robot.getInt("id");
            this.dest=robot.getInt("dest");
            this.speed=robot.getDouble("speed");
            String pos=robot.getString("pos");
            this.location=new Point3D(pos);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * setting this robot on a graph.
     * @param g
     */
    public void setGraph(DGraph g) {
        this.g = g;
    }

    /**
     * get this robot graph.
     * @return
     */
    public DGraph getGraph() {
        return g;
    }

    /**
     * get this robot location
     * @return Point3D location of the robot.
     */
    public Point3D getLocation() {
        return location;
    }

    /**
     * getting this robot id.
     * @return his id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * setting this robot id.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *this robot source node
     * @return this source
     */
    public int getSrc() {
        return src;
    }

    /**
     * setting this robot source.
     * @param src
     */
    public void setSrc(int src) {
        this.src = src;
    }

    /**
     * getting this robot destination.
     * @returnthe robot destination.
     */
    public int getDest() {
        return dest;
    }

    /**
     * setting this robot destination.
     * @param dest
     */
    public void setDest(int dest) {
        this.dest = dest;
    }

    public node_data getNode() {
        return node;
    }
    public int getNodeKey() {
        return node.getKey();
    }

    public void setNode(node_data node) {
        this.node = node;
    }
}