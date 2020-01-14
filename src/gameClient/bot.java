package gameClient;

import dataStructure.Robot;
import utils.Point3D;

import javax.swing.*;

public class bot implements Robot {

    private int _ID;
    private boolean _occupied = false;
    private double _money = 0;
    private Point3D _location;
    public final ImageIcon _robocop = new ImageIcon("robot.png");



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
    public Point3D getLocation() {
        return null;
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

    //to JSON return string
    //Json to string


}
