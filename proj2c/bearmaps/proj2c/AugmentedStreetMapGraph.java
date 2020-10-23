package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;
import edu.princeton.cs.algs4.TrieSET;

import java.util.*;


/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private PointSet kdTree;
    private Map<Point, Node> pointToNode;
    private TrieSET trie;
    private Map<String, HashSet<Node>> nameToNode;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);

        List<Point> points = new LinkedList<>();
        pointToNode = new HashMap<>();
        nameToNode = new HashMap<>();
        trie = new TrieSET();
        for (Node n : this.getNodes()) {
            if (neighbors(n.id()).size() > 0) {
                Point p = new Point(n.lon(), n.lat());
                points.add(p);
                pointToNode.put(p, n);
            }
            if (n.name() != null) {
                String cleanedName = cleanString(n.name());
                trie.add(cleanedName);
                if (!nameToNode.containsKey(cleanedName)) {
                    nameToNode.put(cleanedName, new HashSet<>());
                }
                nameToNode.get(cleanedName).add(n);
            }
        }

        kdTree = new KDTree(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point p = kdTree.nearest(lon, lat);
        return pointToNode.get(p).id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * Runtime should be O(k) where k is the number of words sharing the input prefix.
     * @param prefix Prefix string to be searched for. Could be any case, with or without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> locations = new LinkedList<>();
        List<String> keys = (List<String>) trie.keysWithPrefix(prefix);
        for (String key : keys) {
            for (Node n : nameToNode.get(key)) {
                locations.add(n.name());
            }
        }
        return locations;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> locations = new LinkedList<>();
        Set<Node> nodes = nameToNode.get(cleanString(locationName));
        for (Node n : nodes) {
            Map<String, Object> info = new HashMap<>();
            info.put("lat", n.lat());
            info.put("lon", n.lon());
            info.put("name", n.name());
            info.put("id", n.id());
            locations.add(info);
        }
        return locations;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
