package elements;

import Server.robot;
import oop_dataStructure.oop_edge_data;
import oop_elements.OOP_Edge;
import oop_utils.OOP_Point3D;

public class fruit{
    private OOP_Point3D _pos;
    private double _value;
    private oop_edge_data _edge;

    public fruit() {
    }

    public fruit(double v, OOP_Point3D p, oop_edge_data e) {
        this._value = v;
        this._pos = new OOP_Point3D(p);
        this._edge = e;
    }

    public int getType() {
        int ans = this._edge.getDest() - this._edge.getSrc();
        return ans;
    }

    public OOP_Point3D getLocation() {
        return new OOP_Point3D(this._pos);
    }

    public String toJSON1() {
        String ans = "{\"Fruit\":{\"value\":10,\"type\":1,\"pos\":\"35.187615443099276,32.103800431932775,0.0\"}}";
        return ans;
    }

    public String toString() {
        return this.toJSON();
    }

    public String toJSON() {
        int d = 1;
        if (this._edge.getSrc() > this._edge.getDest()) {
            d = -1;
        }

        String ans = "{\"Fruit\":{\"value\":" + this._value + "," + "\"type\":" + d + "," + "\"pos\":\"" + this._pos.toString() + "\"" + "}" + "}";
        return ans;
    }

    public double getValue() {
        return this._value;
    }

    public double graph(robot r, double dist) {
        double ans = 0.0D;
        if (this._edge != null && r != null) {
            int d = r.getNextNode();
            if (this._edge.getDest() == d) {
                OOP_Point3D rp = r.getLocation();
                if (dist > rp.distance2D(this._pos)) {
                    ans = this._value;
                }
            }
        }

        return ans;
    }

    public static void main(String[] a) {
        double v = 10.0D;
        oop_edge_data e = new OOP_Edge(5, 3, 2.0D);
        OOP_Point3D p = new OOP_Point3D(1.0D, 2.0D, 3.0D);
        Server.Fruit f = new Server.Fruit(v, p, e);
        String s = f.toJSON();
        System.out.println(s);
    }
}
