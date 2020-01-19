package gameClient;


import org.json.JSONObject;

import dataStructure.DGraph;
import utils.Point3D;

public class robot {

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
     * Default constructor.
     */

    public robot(){
        this.value = 0;
        this.id = 0;
        this.location = null;
        this.info = "";
        this.pic ="";
        this.g = null;
    }

    public robot(int value, int tag, int src, int dest, int speed, Point3D location, DGraph g, String info, String pic) {
        this.g = g;
        this.value = value;
        this.id = tag;
        this.src = src;
        this.dest = dest;
        this.speed = speed;
        this.location = location;
        this.info = info;
        this.pic = pic;
    }

    public robot(int key) {
        this.value = 0;
        this.id = key;
        this.location = null;
        this.info = "";
        this.pic = "";
    }

    public robot(String jsonSTR)
    {
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
    public void setGraph(DGraph g) {
        this.g = g;
    }
    public DGraph getGraph() {
        return g;
    }
    public void setLocation(Point3D location) {
        this.location = location;
    }
    public Point3D getLocation() {
        return location;
    }
    public double getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getPic() {
        return this.pic;
    }
    public void Robot(String file_name) {
        this.pic = file_name;
    }
}