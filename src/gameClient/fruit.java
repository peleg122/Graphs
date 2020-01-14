package gameClient;

import utils.Point3D;

import javax.swing.*;

public class fruit {
    /**
     * This class represents Fruit- target on space every fruit have id, point3D,weight
     * /////////Attention!!: should be time, time(the time that he were eaten in).
     * @author Bar Genish
     * @author Elyashiv Deri
     * @author lioz elmalem
     */
    private int ID;
    private Point3D pos;
    private double type;
    private double value;
    private ImageIcon fruitimage;
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
        setPos(new Point3D(x, y,z));
        setValue(weight);
        setFruitimage(new ImageIcon("cherry.png"));
    }
    /**
     * Regular constractor
     * @param ID the number of the fruit
     * @param P the point of the fruit
     * @param weight the weight of the fruit
     */
    public fruit(int ID, Point3D P, double weight) {
        setID(ID);
        setPos(P);
        setValue(weight);
        setFruitimage(new ImageIcon("cherry.png"));
    }
    /**
     * Copy constractor.
     * @param ot create a deep copy of ot fruit
     */
    public fruit(fruit ot) {
        if(ot==null)throw new IllegalArgumentException("fruit cant be null");
        setID(ot.getID());
        setPos(new Point3D(ot.getPos()));
        setValue(ot.getValue());
        setFruitimage(ot.getFruitimage());
    }
    public ImageIcon getFruitimage() {//getters and setters
        return fruitimage;
    }
    public void setFruitimage(ImageIcon fruitimage) {
        this.fruitimage = fruitimage;
    }
    public int getID() {
        return ID;
    }
    public void setID(int iD) {
        ID = iD;
    }
    public Point3D getPos() {
        return pos;
    }
    public void setPos(Point3D orient) {
        pos = orient;
    }
    public double getType() {
        return type;
    }
    public void setType(double type) {
        this.type = type;
    }
    public void setValue(double value){
        this.value = value;
    }
    public double getValue(){
        return value;
    }
    /**
     * write the Fruit as string.
     * @return string of the Fruit.
     */
    public String toString() {
        return ID+","+pos.toString()+","+value;
    }
}