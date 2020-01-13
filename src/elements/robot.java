package elements;

import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import oop_dataStructure.oop_node_data;
import oop_utils.OOP_Point3D;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class robot {
    public static final double EPS;
    public static final double StartMoney = 0.0D;
    public static final double EC = 1.0D;
    public static final double DEFAULT_SPEED = 1.0D;
    public static final double DS = 50.0D;
    public static final double TS = 100.0D;
    private static int _count;
    private static int _seed;
    private int _id;
    private long _key;
    private OOP_Point3D _pos;
    private double _speed;
    private oop_edge_data _curr_edge;
    private oop_node_data _curr_node;
    private oop_graph _gg;
    private long _start_move;
    private double _money;
    private double _DoubleSpeedW;
    private double _TurboleSpeedW;

    static {
        EPS = OOP_DGraph.EPS;
        _count = 0;
        _seed = 3331;
    }

    public robot(oop_graph g, int start_node) {
        this(g, start_node, 50.0D, 100.0D);
    }

    public robot(oop_graph g, int start_node, double ds, double ts) {
        this._gg = g;
        this.setMoney(0.0D);
        this._curr_node = this._gg.getNode(start_node);
        this._pos = this._curr_node.getLocation();
        this._id = _count++;
        this._key = this.getKey(this._id);
        this.setSpeed(1.0D);
        this.setDoubleSpeedWeight(ds);
        this.setTurboSpeedWeight(ts);
    }

    public int getSrcNode() {
        return this._curr_node.getKey();
    }

    public String toJSON() {
        int d = this.getNextNode();
        String ans = "{\"Robot\":{\"id\":" + this._id + "," + "\"value\":" + this._money + "," + "\"src\":" + this._curr_node.getKey() + "," + "\"dest\":" + d + "," + "\"speed\":" + this.getSpeed() + "," + "\"pos\":\"" + this._pos.toString() + "\"" + "}" + "}";
        return ans;
    }

    void setMoney(double v) {
        this._money = v;
    }

    public void addMoney(double d) {
        this._money += d;
    }

    private long getKey(int id) {
        long k0 = (new Random((long)this._id)).nextLong();
        long k1 = (new Random((long)_seed)).nextLong();
        long key = k0 ^ k1;
        return key;
    }

    public boolean setNextNode(int dest) {
        boolean ans = false;
        int src = this._curr_node.getKey();
        boolean reset_time = !this.isMoving();
        this._curr_edge = this._gg.getEdge(src, dest);
        if (this._curr_edge != null) {
            ans = true;
            if (reset_time) {
                this._start_move = (new Date()).getTime();
            }
        }

        return ans;
    }

    public boolean isMoving() {
        return this._curr_edge != null;
    }

    public boolean move() {
        boolean ans = false;
        if (this._curr_edge != null) {
            this.updateSpeed();
            long now = (new Date()).getTime();
            double dt = (double)(now - this._start_move) / 1000.0D;
            double v = this.getSpeed();
            double pr = v * dt / this._curr_edge.getWeight();
            int dest = this._curr_edge.getDest();
            oop_node_data dd = this._gg.getNode(dest);
            OOP_Point3D ddd = dd.getLocation();
            if (pr >= 1.0D) {
                this._pos = ddd;
                this._curr_node = dd;
                this._curr_edge = null;
                ans = true;
            } else {
                OOP_Point3D src = this._curr_node.getLocation();
                double dx = ddd.x() - src.x();
                double dy = ddd.y() - src.y();
                double dz = ddd.z() - src.z();
                double x = src.x() + dx * pr;
                double y = src.y() + dy * pr;
                double z = src.z() + dz * pr;
                OOP_Point3D cr = new OOP_Point3D(x, y, z);
                if (ddd.distance2D(cr) < ddd.distance2D(this._pos)) {
                    this._pos = cr;
                    ans = true;
                }
            }
        }

        return ans;
    }

    private void updateSpeed() {
        double cs = this.getSpeed();
        double w = this.getMoney();
        if (cs == 1.0D && w >= this.doubleSpeedWeight()) {
            this.setSpeed(2.0D);
        }

        if (cs == 2.0D && w >= this.turboSpeedWeight()) {
            this.setSpeed(5.0D);
        }

    }

    public void randomWalk() {
        if (!this.isMoving()) {
            Collection<oop_edge_data> ee = this._gg.getE(this._curr_node.getKey());
            int t = ee.size();
            int ii = (int)(Math.random() * (double)t);
            Iterator<oop_edge_data> itr = ee.iterator();

            for(int i = 0; i < ii; ++i) {
                itr.next();
            }

            this.setNextNode(((oop_edge_data)itr.next()).getDest());
        } else {
            this.move();
        }

    }

    public String toString() {
        return this.toJSON();
    }

    public String toString1() {
        String ans = this.getID() + "," + this._pos + ", " + this.isMoving() + "," + this.getMoney();
        return ans;
    }

    public int getID() {
        return this._id;
    }

    public long getKey() {
        return this._key;
    }

    public OOP_Point3D getLocation() {
        return this._pos;
    }

    public double getMoney() {
        return this._money;
    }

    public double getBatLevel() {
        return 0.0D;
    }

    public int getNextNode() {
        int ans;
        if (this._curr_edge == null) {
            ans = -1;
        } else {
            ans = this._curr_edge.getDest();
        }

        return ans;
    }

    public double getSpeed() {
        return this._speed;
    }

    public void setSpeed(double v) {
        this._speed = v;
    }

    public double doubleSpeedWeight() {
        return this._DoubleSpeedW;
    }

    public double turboSpeedWeight() {
        return this._TurboleSpeedW;
    }

    public void setDoubleSpeedWeight(double w) {
        this._DoubleSpeedW = w;
    }

    public void setTurboSpeedWeight(double w) {
        this._TurboleSpeedW = w;
    }
}
