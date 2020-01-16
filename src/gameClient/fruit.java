package gameClient;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import elements.nodeData;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Point3D;

import javax.swing.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class represents Fruit- target on space every fruit have id, point3D,weight
 * /////////Attention!!: should be time, time(the time that he were eaten in).
 *
 * @author Amir Hoshen
 * @author Peleg Zoborovsky
 */

public class fruit {

    private static int _ID = 0;
    private Point3D _pos;
    private double _type;
    private double _value;
    private boolean _occupied = false;
    private edge_data _edge;
    private ImageIcon _fruitimage;

    /**
     * Regular constractor.
     *
     * @param x     the x point of the fruit
     * @param y     the y point of the fruit
     * @param z     the z point of the fruit
     * @param value of the fruit
     */
    public fruit(double x, double y, double z, double value) {
        _ID++;
        set_pos(new Point3D(x, y, z));
        set_value(value);
        set_fruitimage(new ImageIcon(String.valueOf(get_fruitimage())));
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
    public fruit(double x, double y, double z, double value, double type) {
        _ID++;
        set_pos(new Point3D(x, y, z));
        set_value(value);
        set_type(type);
        set_fruitimage(new ImageIcon(String.valueOf(get_fruitimage())));
    }

    /**
     * fruit constructor
     *
     * @param P     Point3D position on the graph edge.
     * @param value this fruit value.
     * @param type  this fruit type(apple(-1) || banana(1)).
     */
    public fruit(Point3D P, double value, double type) {
        _ID++;
        set_pos(P);
        set_value(value);
        set_type(type);
        set_fruitimage(new ImageIcon(String.valueOf(get_fruitimage())));
    }

    /**
     * Copy constractor.
     *
     * @param ot create a deep copy of ot fruit
     */
    public fruit(fruit ot) {
        if (ot == null) throw new IllegalArgumentException("fruit cant be null");
        _ID++;
        set_pos(new Point3D(ot.get_pos()));
        set_value(ot.get_value());
        set_type(ot.get_type());
        set_fruitimage(ot.get_fruitimage());
    }

    public ImageIcon get_fruitimage() {//getters and setters
        return _fruitimage;
    }

    /**
     * setting the fruit image
     * if this fruit type is 1 image icon will be banana
     * if this fruit type is -1 image icon will be apple.
     *
     * @param fruitimage the fruit choice by criterion(1 ||-1)
     */
    public void set_fruitimage(ImageIcon fruitimage) {
        if (this._type == 1) {
            this._fruitimage = new ImageIcon("utils/banana.png");//banana
        } else if (this._type == -1) {
            this._fruitimage = new ImageIcon("utils/apple.png");//apple
        } else {
            throw new IllegalArgumentException("fruit type can be only 1 || -1");
        }
    }

    /**
     * this fruit ID
     *
     * @return ID
     */
    public int getID() {
        return _ID;
    }

    /**
     * this fruit position
     *
     * @return Point3D _pos (position of the fruit)
     */
    public Point3D get_pos() {
        return _pos;
    }

    /**
     * setting the fruit position (Point3D)
     *
     * @param position
     */
    public void set_pos(Point3D position) {
        _pos = position;
    }

    /**
     * the fruit type represented by two options
     * -1 is an apple
     * 1 is banana
     *
     * @return the fruit type(int== -1||1)
     */
    public double get_type() {
        return _type;
    }

    /**
     * setting this fruit type
     * 1 or -1, otherwise exceptions will thrown
     *
     * @param type (should be 1 || -1).
     */
    public void set_type(double type) {
        if (type == 1 || type == -1) this._type = type;
        else
            throw new IllegalArgumentException("the fruit type can be only 1 || -1");
    }

    /**
     * each fruit can have a different value
     * when the robot collect the fruits the robot criteria money(value) will be added.
     *
     * @param _value this fruit value.
     */
    public void set_value(double _value) {
        this._value = _value;
    }

    /**
     * getting this fruit value
     *
     * @return this value.
     */
    public double get_value() {
        return _value;
    }

    /**
     * this method will be used in MyGameGUI..
     * if a robot from the list is allready heading towards this fruit
     * we will say that this fruit is occupied such that other robots wont waste
     * any time heading towards targeted fruits.
     *
     * @param occupied - true- robot is on his way towards the fruit
     *                 -false- this fruit is a target.
     */
    public void set_occupied(boolean occupied) {
        this._occupied = occupied;
    }

    /**
     * @return true if a robot is on his way towards this fruit
     * false otherwise.
     */
    public boolean get_Occupied() {
        return _occupied;
    }

    /**
     * write the Fruit as string.
     *
     * @return string of the Fruit.
     */
    public String toString() {
        return _pos.toString() + "," + _value + " ," + _type;
    }

    public edge_data get_edge() {
        return _edge;
    }

    public edge_data edgdeLocator(DGraph graph) {
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
                        .distance2D(this._pos);
                distanceFtoB = this._pos.distance2D(graph.getNode(edgeData.getDest()).getLocation());
                minDistanceChecking = Math.abs(distanceAtoB - (distanceAtoF + distanceFtoB));
                if (minDistanceChecking <= minDistance) {
                    minDistance = minDistanceChecking;
                    temp = edgeData;
                }
            }
        }
        if (_type == 1 && temp.getDest() - temp.getSrc() == -1 ||
                _type == -1 && temp.getDest() - temp.getSrc() == 1) {
            temp = graph.getEdge(temp.getDest(), temp.getSrc());
        }
        this._edge = temp;
        return temp;
    }

}