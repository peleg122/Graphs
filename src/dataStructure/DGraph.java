package dataStructure;

import elements.nodeData;
import elements.nodeEdge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DGraph implements graph, Serializable {
    /***
     * Directional Graph Implementation:
     * _allNodesFast - to get all the nodes in the graph in O(1)
     * _allNodes - holder of the nodes but with access time and search time of O(1)
     * _allEdges - holds all edges used for getting specific edge using src and dest keys
     * edgeSize - number of edges in the graph ( not really used)
     * mc - Mode Count ( Not used)
     */
    private ArrayList<node_data> _allNodeFast;
    private HashMap<Integer, node_data> _allNodes;
    private HashMap<Integer, HashMap<Integer, edge_data>> _allEdges;
    private int edgeSize;
    private int mc;

    /**
     * default Constructor : creates Empty Graph
     */
    public DGraph() {
        this._allNodeFast = new ArrayList<>();
        this._allNodes = new HashMap<>();
        this._allEdges = new HashMap<>();
        this.edgeSize = 0;
        this.mc = 0;
    }

    /**
     * getting node_data via node key (id)
     *
     * @param key - the node_id
     * @return
     */
    @Override
    public node_data getNode(int key) {
        if (!_allNodes.containsKey(key)) {
            System.out.println("getNode(int key): node doesn't exist!");
            return null;
        } else {
            return _allNodes.get(key);
        }
    }

    /**
     * getting specific edge using source and destination keys
     *
     * @param src  - starting node
     * @param dest - ending node
     * @return - edge_data
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if (!_allNodes.containsKey(src) || !_allNodes.containsKey(dest)) {
            System.out.println("getEdge(src,dest): one of the nodes missing!");
            return null;
        } else {
            if (_allEdges.get(src).containsKey(dest))//only if src contains dest in its hashMap keys!
                return _allEdges.get(src).get(dest);
            else
                return null;
        }
    }

    /**
     * adds node to _allNodes & _allNodesFast and adding one to the Mode Count
     *
     * @param n - node to be added
     */
    @Override
    public void addNode(node_data n) {
        nodeData _temp = (nodeData) n;
        _allNodes.put(_temp.getKey(), _temp);
        _allEdges.put(_temp.getKey(), _temp.getNeighbors());
        _allNodeFast.add(_temp);
        mc++;
    }

    /**
     * Connecting Nodes using Edges
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (_allNodes.containsKey(src) && _allNodes.containsKey(dest)) {
            nodeEdge _temp = new nodeEdge(src, dest, w);
            nodeData _tempNode = (nodeData) _allNodes.get(src);
            _tempNode.getNeighbors().put(dest, _temp);
            _allEdges.put(src, _tempNode.getNeighbors());
            edgeSize++;
            mc++;
        } else {
            System.out.println("connect(src,dest,w): src or dest doesn't exist! (no action preformed)");
        }
    }

    /**
     * returns Collection of Nodes in O(1)
     *
     * @return - Collection of Nodes in O(1)
     */
    @Override
    public Collection<node_data> getV() {
        return _allNodeFast;
    }

    /**
     * returns Collection of edges the the node_id is the src in them
     *
     * @param node_id - source node in the edge
     * @return - Collection of Edges with sources neighbors
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (!_allNodes.containsKey(node_id)) {
            System.out.println("getE(node_id): node doesn't exists!");
            return null;
        } else {
            nodeData _temp = (nodeData) _allNodes.get(node_id);
            return _temp.getNeighbors().values();
        }
    }

    /**
     * removes Node and all its Edges from both _allNodes and _allNodesFast O(n)
     *
     * @param key - id of the node to be deleted
     * @return - return the node that was deleted
     */
    @Override
    public node_data removeNode(int key) {
        if (!_allNodes.containsKey(key)) {
            System.out.println("removeNode(key): didn't remove, node doesn't exist!");
            return null;
        } else {
            _allNodeFast.remove(_allNodes.get(key));
            edgeSize -= _allEdges.get(key).size();
            _allEdges.remove(key);
            for (HashMap n : _allEdges.values()) {
                if (n.containsKey(key)) {
                    n.remove(key);
                    edgeSize--;
                }
            }
            return _allNodes.remove(key);
        }
    }

    /**
     * deletes Edge by the edge going to the neighbors list inside the node and Deleting it and from _allEdges
     *
     * @param src
     * @param dest
     * @return - deleted edge is returned
     */
    @Override
    public edge_data removeEdge(int src, int dest) {// need to remove from allNodesFast in node!
        if (_allNodes.containsKey(src) && _allNodes.containsKey(dest)) {//check if src even has this dest
            if (_allEdges.get(src).containsKey(dest)) {
                return _allEdges.get(src).remove(dest);
            } else {
                System.out.println("removeEdge(src,dest): edge doesn't exist!");
                return null;
            }
        } else {
            System.out.println("removeEdge(src,dest): src or dest doesn't exist!");
            return null;
        }
    }

    /**
     * returns number of nodes in the Graph
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return _allNodeFast.size();
    }

    /**
     * returns number of edges in the graph
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return edgeSize;
    }

    /**
     * returns Mode Count to check for Changes in the graph
     *
     * @return
     */
    @Override
    public int getMC() {
        return this.mc;
    }

}
