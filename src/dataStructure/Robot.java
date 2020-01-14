package dataStructure;

import utils.Point3D;

import javax.swing.*;

public interface Robot {
    void init();

    String toJSON();

    void fromJSON(String JSON);

    void setMoney(double value);

    void setLocation(Point3D location);

    void setSpeed(long speed);

    void setOccupied(boolean bool);

    void setIMG(ImageIcon img);

    double getMoney();

    Point3D getLocation();

    long getSpeed();

    boolean getOccupied();

    ImageIcon getIMG();
}
