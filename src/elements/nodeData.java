package elements;

import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

import java.io.Serializable;
import java.util.HashMap;

/**
 * node for the Graph
 */
public class nodeData implements node_data, Serializable {
    private static int id = -1;
    private int key;
    private Point3D location;
    private double weight;
    private String info;
    private int tag;
    private HashMap<Integer, edge_data> neighbors;

    /**
     * Constructor getting location with x and y axis and creates node with new unique id as key
     * location by the parameter given
     * weight will be MAX.Value for algorithm purposes
     * tag will be set to -1 for not been visited
     * info not been set for now later will be used in algorithm
     * empty hashMap for future neighbors
     *
     * @param _location
     */
    public nodeData(Point3D _location) {
        id++;
        this.key = id;
        this.location = _location;
        this.weight = Integer.MAX_VALUE;
        this.tag = -1;
        this.neighbors = new HashMap<>();
    }

    /**
     * Returns nodes Key
     *
     * @return
     */
    @Override
    public int getKey() {
        return this.key;
    }

    /**
     * Returns nodes Location
     *
     * @return
     */
    @Override
    public Point3D getLocation() {
        return this.location;
    }

    /**
     * Returns nodes Weight
     *
     * @return
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * Returns nodes info
     *
     * @return
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * Returns nodes Tag
     *
     * @return
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * Sets nodes Location
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(Point3D p) {
        this.location = p;
    }

    /**
     * Sets nodes Weight
     *
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    /**
     * Sets nodes Info
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * Sets nodes Tag
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /**
     * Returns Nodes HashMap of edges and neighbors
     *
     * @return
     */
    public HashMap<Integer, edge_data> getNeighbors() {
        return neighbors;
    }

}
