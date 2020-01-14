package dataStructure;

import utils.Point3D;

import javax.swing.*;

public interface Fruit {
    //constructor should be:
    // fruits(double values,int type,Point3D location){
    //   setImage("robot.png");
    //   id++;
    //   key = id;
    //   init - Values , type , pos...
    // }
    String toJSON();

    void fromJSON(String JSON);

    void setValues(double values);

    void setType(int type);

    void setPoint3D(Point3D point);

    double getValues();

    int getType();

    Point3D getPoint3D();

    int getKey();

    void setImg(ImageIcon img);

    ImageIcon getImg();
}
