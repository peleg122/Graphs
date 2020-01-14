package gameClient;

import dataStructure.edge_data;
import utils.Point3D;

import javax.swing.*;

/**
 * This class represents Fruit- target on space every fruit have id, point3D,weight
 * /////////Attention!!: should be time, time(the time that he were eaten in).
 * @author Amir Hoshen
 * @author Peleg Zoborovsky
 */

public class fruit {

    private int _ID;
    private Point3D _pos;
    private double _type;
    private double _value;
    private boolean _occupied = false;
    private edge_data _edge;
    private ImageIcon _fruitimage;
    /**
     * Regular constractor.
     * @param ID the fruit number
     * @param x the x point of the fruit
     * @param y the y point of the fruit
     * @param z the z point of the fruit
     * @param weight the weight of the fruit
     */
    public fruit(int ID, double x, double y, double z, double weight) {
        setID(ID);
        set_pos(new Point3D(x, y,z));
        set_value(weight);
        set_fruitimage(new ImageIcon(String.valueOf(this._fruitimage)));
    }
    /**
     * Regular constractor
     * @param ID the number of the fruit
     * @param P the point of the fruit
     * @param weight the weight of the fruit
     */
    public fruit(int ID, Point3D P, double weight) {
        setID(ID);
        set_pos(P);
        set_value(weight);
        set_fruitimage(new ImageIcon("cherry.png"));
    }
    /**
     * Copy constractor.
     * @param ot create a deep copy of ot fruit
     */
    public fruit(fruit ot) {
        if(ot==null)throw new IllegalArgumentException("fruit cant be null");
        setID(ot.getID());
        set_pos(new Point3D(ot.get_pos()));
        set_value(ot.get_value());
        set_fruitimage(ot.get_fruitimage());
    }
    public ImageIcon get_fruitimage() {//getters and setters
        return _fruitimage;
    }

    /**
     * setting the fruit image
     * if this fruit type is 1 image icon will be banana
     * if this fruit type is -1 image icon will be apple.
     * @param _fruitimage the fruit choice by criterion(1 or -1)
     */
    public void set_fruitimage(ImageIcon _fruitimage) {
        if(this._type ==1){
            this._fruitimage = new ImageIcon("banana.png");//banana
        }else if(this._type ==-1){
            this._fruitimage = new ImageIcon("apple.png");//apple
        }else {
            throw new IllegalArgumentException("fruit type can be only 1 or -1");
        }
    }
    public int getID() {
        return  _ID;
    }
    public void setID(int iD) {
        _ID = iD;
    }
    public Point3D get_pos() {
        return _pos;
    }
    public void set_pos(Point3D orient) {
        _pos = orient;
    }
    public double get_type() {
        return _type;
    }
    public void set_type(double _type) {
        this._type = _type;
    }
    public void set_value(double _value){
        this._value = _value;
    }
    public double get_value(){
        return _value;
    }
    public void set_occupied(boolean occupied){
        this._occupied = occupied;
    }
    public boolean get_Occupied(){
        return _occupied;
    }

    /**
     * write the Fruit as string.
     * @return string of the Fruit.
     */
    public String toString() {
        return _ID+","+ _pos.toString()+","+ _value;
    }
}