package elements;

import dataStructure.edge_data;

import java.io.Serializable;

public class nodeEdge implements edge_data, Serializable {
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    public nodeEdge(int src, int dest, double weight) {
        if (weight > 0) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
            this.info = "";
            this.tag = -1;// no use in our algorithms
        } else {
            throw new RuntimeException("Wrong Weight");
        }
    }

    /**
     * returns Source key
     *
     * @return - source key
     */
    @Override
    public int getSrc() {
        return this.src;
    }

    /**
     * returns Destination key
     *
     * @return - Destination key
     */
    @Override
    public int getDest() {
        return this.dest;
    }

    /**
     * returns Weight of the edge
     *
     * @return - edge weight key
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * returns Info Stored inside the edge
     *
     * @return - Info of the edge (metadata)
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * gets a tag to determine if the edges has been visited before but isn't in use
     *
     * @return - returns tag number (-1 or 1)
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * Sets a String of info into info parameter
     *
     * @param s - info String
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /***
     * sets tag to determine if the edges has been visited before but isn't in use
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        if (t == -1 || t == 1)
            this.tag = t;
        else {
            System.out.println("Tag didnt change due to Wrong Values");
        }
    }
}
