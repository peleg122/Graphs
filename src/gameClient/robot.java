package gameClient;

import dataStructure.Robot;
import utils.Point3D;
import utils.StdDraw;

import javax.swing.*;

public class robot extends JFrame implements Robot {

    private int _ID;
    private boolean _occupied = false;
    private double _money = 0;
    private Point3D _location;
    public final ImageIcon _robocop = new ImageIcon("robot.png");


    public robot(int id){
        this._ID = id;
    }
    @Override
    public void init() {

    }

    @Override
    public String toJSON() {
        return null;
    }

    @Override
    public void fromJSON(String JSON) {

    }

    @Override
    public void setMoney(double value) {

    }

    @Override
    public void setLocation(Point3D location) {

    }

    @Override
    public void setSpeed(long speed) {

    }

    @Override
    public void setOccupied(boolean bool) {

    }

    @Override
    public void setIMG(ImageIcon img) {

    }

    @Override
    public double getMoney() {
        return 0;
    }

    @Override
    public Point3D getPointLocation() {
        Point3D p = new Point3D(0,0,0);
        return  p;
    }

    @Override
    public long getSpeed() {
        return 0;
    }

    @Override
    public boolean getOccupied() {
        return false;
    }

    @Override
    public ImageIcon getIMG() {
        return null;
    }

    public static void main(String [] args){

        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(-100, 100);
        StdDraw.setYscale(-100, 100);
        StdDraw.clear();
        robot r = new robot(1);
        StdDraw.frame.setIconImage(r.getIconImage());
        StdDraw.picture(10,10,"robot.png");
        StdDraw.picture(20,20,"banana.png");
        StdDraw.picture(30,30,"apple.png");

    }

}
