package elements;

import dataStructure.graph;
import oop_dataStructure.oop_edge_data;
import utils.Point3D;


public class fruit{
    public static final double EPS = 0.00001;
    private Point3D _location;
    int _type;
    int _value;
    boolean _isOccupied = false;
    graph _g;
    oop_edge_data _edge;

    //default constructor
    public fruit(){
        this._location = null;
        this._type = 0;
        this._value = -1;
        this._edge = null;
        this._g = null;
    }

    //constructors:
    public fruit(graph g){
        this._location = null;
        this._type = 0;
        this._value = -1;
        this._edge = null;
        this._g = g;
    }

    public fruit(Point3D point, int type, int value,oop_edge_data edge, graph graph){
        this._location = new Point3D(point);
        this._type = type;
        this._value = value;
        this._edge = edge;
        this._g = graph;
    }

    //get this fruit location.
    private Point3D get_location(){
        return _location;
    }

    //setting the fruit location
    private void set_location(Point3D p){
        this._location = new Point3D(p);
    }


}
