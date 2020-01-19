package gameClient;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import elements.nodeData;
import org.json.JSONObject;
import utils.Point3D;

import java.util.Collection;
import java.util.Iterator;

public class Fruits {
    private static int _ID;
    private DGraph g;
    private edge_data edge;
    private double value;
    private int type;
    private Point3D location;
    int dest;
    int src;
    private String info;
    private String pic;
    private boolean occupied;

    /**
     * Default constructor.
     */

    public Fruits(){
        this.value = 0;
        this.type = 0;
        this.edge = null;
        this.location = null;
        this.pic = "";
        this.info = "";
        this.dest = 0;
        this.src = 0;
        this.occupied =false;
    }

    /**
     * the method creates fruit with all his Properties from a JSON file:
     * value of fruit is the value(money) that each robot gets when collecting this fruit.
     * type -1 is banana and 1 is an apple.
     * position of the fruit.
     * @param jsonSTR
     */
    public Fruits(String jsonSTR)    {
        if(!jsonSTR.isEmpty()) {
            try {
                JSONObject fruit = new JSONObject(jsonSTR);
                fruit=fruit.getJSONObject("Fruit");
                double val = fruit.getDouble("value");
                this.value =val;
                String pos=fruit.getString("pos");
                this.location = new Point3D(pos);
                int t=fruit.getInt("type");
                this.type=t;
            }
            catch (Exception e)
            {

                e.printStackTrace();
            }
        }
    }

    /**
     * Regular constractor.
     *
     * @param x     the x point of the fruit
     * @param y     the y point of the fruit
     * @param z     the z point of the fruit
     * @param value of the fruit
     */
    public Fruits(double x, double y, double z, double value) {
        _ID++;
        setLocation(new Point3D(x, y, z));
        setValue(value);
    }

    /**
     * fruit constructor
     *
     * @param x     this fruit position on x axis
     * @param y     this fruit position on y axis
     * @param z     this fruit position on z axis
     * @param value this fruit value
     * @param type  (apple(-1) || banana(1))
     */
    public Fruits(double x, double y, double z, double value, int type) {
        _ID++;
        setLocation(new Point3D(x, y, z));
        setValue(value);
        setType(type);
    }

    /**
     * fruit constructor
     *
     * @param P     Point3D position on the graph edge.
     * @param value this fruit value.
     * @param type  this fruit type(apple(-1) || banana(1)).
     */
    public Fruits(Point3D P, double value, int type) {
        _ID++;
        setLocation(P);
        setValue(value);
        setType(type);
    }

    /**
     * Copy constractor.
     *
     * @param ot create a deep copy of ot fruit
     */
    public Fruits(Fruits ot) {
        if (ot == null) throw new IllegalArgumentException("fruit cant be null");
        _ID++;
        setLocation(new Point3D(ot.getLocation()));
        setValue(ot.getValue());
        setType(ot.getType());
    }

    /**
     * get this fruit value.
     * @return value as type double.
     */
    public double getValue() {
        return this.value;
    }

    /**
     * setting this fruit value.
     * @param value
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * method gets this fruit location.
     * @return this fruit location.
     */
    public Point3D getLocation() {
        return this.location;
    }

    /**
     * method will set this fruit location.
     * @param location
     */
    public void setLocation(Point3D location) {
        this.location = location;
    }

    /**
     * method will get this fruit location.
     * @return this fruit location.
     */
    public int getType() {
        return this.type;
    }

    /**
     * setting this fruit type.
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * method return the destination node the robot need to reach in order to
     * properly eat the fruit by definition fruit can only be eaten if the
     * robot will get to the edge in the path from lowest node number to a higher node number.
     * @return the fruit destination.
     */
    public int getDest() {
        return dest;
    }

    /**
     * setting this fruit destination.
     * @param dest
     */
    public void setDest(int dest) {
        this.dest = dest;
    }

    /**
     * gets this fruit source node number to reach in order to eat
     * this fruit
     * @return the source node
     */
    public int getSrc() {
        return src;
    }

    /**
     * setting this fruit source node number.
     * @param src
     */
    public void setSrc(int src) {
        this.src = src;
    }

    /**
     * gets the edge this fruit is sitting on.
     * @return graph edge type this fruit is on.
     */
    public edge_data getEdge() {
        return this.edge;
    }

    /**
     * gets the closest edge a fruit is on from the current robot.
     * @param graph
     * @return the closest fruit edge.
     */
    public edge_data edgeLocator(DGraph graph) {
        double distanceAtoB;
        double distanceAtoF;
        double distanceFtoB;
        double minDistance = Double.MAX_VALUE;
        double minDistanceChecking;
        edge_data temp = null;
        Collection<node_data> collection = graph.getV();
        Iterator<node_data> it = collection.iterator();
        while (it.hasNext()) {
            nodeData n = (nodeData) it.next();
            Iterator<edge_data> itE = n.getNeighbors().values().iterator();
            while (itE.hasNext()) {
                edge_data edgeData = itE.next();
                distanceAtoB = graph.getNode(edgeData.getSrc()).getLocation()
                        .distance2D(graph.getNode(edgeData.getDest()).getLocation());
                distanceAtoF = graph.getNode(edgeData.getSrc()).getLocation()
                        .distance2D(this.location);
                distanceFtoB = this.location.distance2D(graph.getNode(edgeData.getDest()).getLocation());
                minDistanceChecking = Math.abs(distanceAtoB - (distanceAtoF + distanceFtoB));
                if (minDistanceChecking <= minDistance) {
                    minDistance = minDistanceChecking;
                    temp = edgeData;
                }
            }
        }
        if (type == 1 && temp.getDest() - temp.getSrc() == -1 ||
                type == -1 && temp.getDest() - temp.getSrc() == 1) {
            temp = graph.getEdge(temp.getDest(), temp.getSrc());
        }
        this.edge = temp;
        return temp;
    }

    /**
     * let us know if this fruit is waiting for a robot.
     * @return true if a robot is on his way
     * false otherwise.
     */
    public boolean getOccupied() {
        return this.occupied;
    }

    /**
     * if any robot is going towards this fruit, we return a mark that this fruit is occupied.
     * @return true if a robot is on his way
     * otherwise false.
     */
    public void setOccupied(boolean set){
        this.occupied = set;
    }
}