package algorithms;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.nodeData;

import java.io.*;
import java.util.*;

/**
 * This class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2
 * <p>
 * takes a DGraph and "Wraps" it with the algorithms of Graph_Algo
 *
 * @author Peleg Z & Amir H
 */
public class Graph_Algo implements graph_algorithms, Serializable {
    public graph _graph;

    public Graph_Algo() {
        this._graph = new DGraph();
    }

    public Graph_Algo(graph g) {
        this._graph = g;
    }

    /**
     * Comparator to determine what node to take next in Dijkstra algorithm
     * the node with the least weight will be the first!
     */
    private Comparator<node_data> comparator = new Comparator<node_data>() {
        @Override
        public int compare(node_data o1, node_data o2) {
            if (o1.getWeight() < o2.getWeight())
                return -1;
            else if (o1.getWeight() > o2.getWeight())
                return 1;
            else
                return 0;
        }
    };

    /**
     * initializing the grapg in Grapg_Algo to be the graph in the parameter
     *
     * @param g - the graph to be wrapped in the Graph_Algo
     */
    @Override
    public void init(graph g) {
        this._graph = g;
    }

    /**
     * take a String of the Dir and file name with its extension and creates a Graph object and init it with init(Graph)
     *
     * @param file_name - Dir + filename + ".txt" extension
     */
    @Override
    public void init(String file_name) {
        deserialize(file_name);

    }

    /**
     * take a String of the Dir and file name with its extension and saves it to the place specified in the string
     *
     * @param file_name - Dir + filename + ".txt" extension
     */
    @Override
    public void save(String file_name) {
        serialize(file_name);
    }

    /**
     * creates txt file with the Graph Object to later be able to load from
     *
     * @param file_name - dir + file name + .txt
     */
    private void serialize(String file_name) {
        try {
            FileOutputStream file = new FileOutputStream(file_name);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(this._graph);

            out.close();
            file.close();

            System.out.println("Object has benn serialized");
        } catch (IOException e) {
            System.err.println("IOException is caught,Object didnt save.");
        }

    }

    /**
     * Creates Graph object from the saved txt file and then uses init to initialize the Graph_Algo
     *
     * @param file_name - dir + file name + .txt
     */
    private void deserialize(String file_name) {
        try {
            FileInputStream file = new FileInputStream(file_name);
            ObjectInputStream in = new ObjectInputStream(file);

            this._graph = (graph) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized");
        } catch (IOException e) {
            System.err.println("IOException is caught,object didnt uploaded");
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException is caught,object didnt uploaded");
        }
    }

    /**
     * SC - Check if the _graph is Strong Connected by checking each node is reachable from every node
     * will take every edge of the first arbitrary node and will try reach every other node it is connected to via
     * its neighbors using DFS approach it will check if every node was reached will return false if one of the nodes
     * wasn't visited (tag == -1 is not Visited) then it will flip the edges direction and will do DFS again and if
     * the node we first selected cant reach one of the other nodes will return false if it could reach every node
     * after flipping the edges it will return true (Strong Connectivity)
     *
     * @return true if every node is reachable from every node false if otherwise O(|V|+|E|)
     */
    @Override
    public boolean isConnected() {
        Graph_Algo _temp = new Graph_Algo();
        _temp.init(this.copy());
        _temp.resetVisits();
        Iterator<node_data> iterator = _temp._graph.getV().iterator();
        if (iterator.hasNext()) {
            nodeData node = (nodeData) _temp._graph.getV().iterator().next();
            DFS(node.getKey(), _temp);
            for (node_data n : _temp._graph.getV()) {
                if (n.getTag() == -1)
                    return false;
            }
            Transpose(_temp);
            _temp.resetVisits();
            DFS(node.getKey(), _temp);
            for (node_data n : _temp._graph.getV()) {
                if (n.getTag() == -1)
                    return false;
            }
        }else{
            return true;//graph is empty
        }
        return true;
    }

    /**
     * Searches via DFS for the neighbors of the node that was given and will tag them as Visited ( tag == 1)
     * works recursively
     *
     * @param key - source of the visited node
     * @param g   - graph to search in
     */
    private void DFS(int key, Graph_Algo g) {
        g._graph.getNode(key).setTag(1);
        Iterator<edge_data> i = g._graph.getE(key).iterator();
        int n;
        while (i.hasNext()) {
            n = i.next().getDest();
            if (g._graph.getNode(n).getTag() == -1)
                DFS(n, g);
        }
    }

    /**
     * Flips the edges direction for the given graph temporary graph will be given via isConnected() method
     *
     * @param g - temp graph from isConnected()
     */
    private void Transpose(Graph_Algo g) {
        HashMap<node_data, Collection> _temp = new HashMap<>();
        for (node_data n : g._graph.getV()) {
            _temp.put(n, new ArrayList());
            _temp.get(n).addAll(g._graph.getE(n.getKey()));
            g._graph.getE(n.getKey()).clear();//doesnt reduce the num of edges with clear (temp graph edge size Double!)
        }
        for (node_data n : _temp.keySet()) {
            Iterator<edge_data> i = _temp.get(n).iterator();
            while (i.hasNext()) {
                edge_data e = i.next();
                g._graph.connect(e.getDest(), e.getSrc(), e.getWeight());
            }
        }
        //notice : by clearing the Edge Collection Edge size will be Changed but it will be on the temp Graph
        //so the main graph isn't changed so no panic if num of edges doubles it will only appear in the temp Graph!
    }

    /**
     * Searches for the shortest route from source to destination using dijkstra algorithm and return the distance
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return - return the distance of the path between source to destination node
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        dijkstra(src);
        return _graph.getNode(dest).getWeight();
    }

    /**
     * Searches for the shortest route from source to destination using dijkstra algorithm and returns a list of nodes
     * it was going through from the starting to the destination node
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return - list of nodes  from the start to the End
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {//from Start to the end
        if (src == dest) {
            ArrayList<node_data> _self = new ArrayList<>();
            _self.add(_graph.getNode(src));
            return _self;
        }
        ArrayList<node_data> pathToSrc = new ArrayList<>();
        dijkstra(_graph.getNode(src).getKey());
        node_data _temp = _graph.getNode(dest);
        int n;
        while (_temp.getInfo() != null && _temp.getKey() != src) {
            pathToSrc.add(_temp);
            n = Integer.parseInt(_temp.getInfo());
            _temp = _graph.getNode(n);
        }
        pathToSrc.add(_temp);
        Stack<node_data> s = new Stack<>();
        for (node_data i : pathToSrc) {
            s.add(i);
        }
        pathToSrc.clear();
        while (!s.empty()) {//reverse order of the path to begin from the start >> end
            pathToSrc.add(s.pop());
        }
        return pathToSrc;
    }

    /**
     * printing function for visual representation of the route the shortest path is making plus the distance
     *
     * @param l - list of nodes from the starting point to the end of the shortestPath(src,dest) function
     */
    public String shortestPathToString(List<node_data> l) {//prints path from start to end
        String ans = "Path: ";
        Queue<node_data> s = new LinkedList<>();
        for (node_data n : l) {
            s.add(n);
        }
        while (!s.isEmpty()) {
            if (s.size() > 1)
                ans += s.poll().getKey() + " >> ";
            else
                ans += s.peek().getKey() + " | Distance: " + s.poll().getWeight();
            ;
        }
        System.out.println(ans);
        return ans;
    }

    /**
     * Creates A path of the nodes to go through the targets list where each target is tagged at least once
     * with the shortest path (relatively)
     *
     * @param targets
     * @return
     */
    @Override
    public List<node_data> TSP(List<Integer> targets) {
        if (!isConnected())
            return null;
        if (targets.size() == 0 || targets == null)
            return null;
        if (targets.size() == 1)
            return shortestPath(targets.get(0), targets.get(0));

        List<node_data> paths = new LinkedList<>();
        for (int i = 0; i < targets.size() - 1; i++) {
            //adding all the shortest paths through every member of the targets list : relatively shortest (by order)
            paths.addAll(shortestPath(targets.get(i), targets.get(i + 1)));
        }
        return paths;
    }

    /**
     * creates new DGraph and with iterators copies its content to the new DGraph
     *
     * @return - new freshly made Graph!
     */
    @Override
    public graph copy() {
        graph _temp = new DGraph();
        for (node_data n : _graph.getV()) {
            _temp.addNode(n);
        }
        for (node_data n : _graph.getV()) {
            for (edge_data e : _graph.getE(n.getKey())) {
                _temp.connect(e.getSrc(), e.getDest(), e.getWeight());
            }
        }
        return _temp;
    }

    /**
     * Private use function in Dijkstra : reset every node to be -1 ( not Visited)
     */
    private void resetVisits() {
        for (node_data n : _graph.getV()) {
            n.setTag(-1);
        }
    }

    /**
     * Private use function in Dijkstra : reset the weight of each node to be infinity ( MAX.VALUE)
     */
    private void resetWeights() {
        for (node_data node : _graph.getV()) {
            node.setWeight(Integer.MAX_VALUE);
        }
    }

    /**
     * Private use function in shortestPath: starting from the source node searching neighbors and computing
     * the shortest path from the source to its neighbors and each neighbor to their neighbor
     *
     * @param src - source node to start with
     */
    private void dijkstra(int src) {
        resetVisits();
        resetWeights();
        PriorityQueue<node_data> node_dataPriorityQueue = new PriorityQueue<>(comparator);
        node_dataPriorityQueue.add(_graph.getNode(src));
        node_dataPriorityQueue.peek().setWeight(0);
        while (!node_dataPriorityQueue.isEmpty()) {
            node_data _tempSrc = node_dataPriorityQueue.poll();
            if (_tempSrc.getTag() == -1) {
                _tempSrc.setTag(1);
                Collection<edge_data> edges = _graph.getE(_tempSrc.getKey());
                for (edge_data edge : edges) {
                    nodeData _tempDest = (nodeData) _graph.getNode(edge.getDest());
                    if (_tempDest.getWeight() > _tempSrc.getWeight() + edge.getWeight()) {
                        _tempDest.setWeight(_tempSrc.getWeight() + edge.getWeight());
                        if (_tempDest.getKey() != src)
                            _tempDest.setInfo(_tempSrc.getKey() + "");
                        if (_tempDest.getTag() == -1) {
                            node_dataPriorityQueue.add(_tempDest);
                        }
                    }
                }
            }
        }
    }
}
