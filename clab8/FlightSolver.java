import java.util.*;

/**
 * Solver for the Flight problem (#9) from CS 61B Spring 2018 Midterm 2.
 * Assumes valid input, i.e. all Flight start times are >= end times.
 * If a flight starts at the same time as a flight's end time, they are
 * considered to be in the air at the same time.
 */
public class FlightSolver {

    ArrayList<Flight> flights;

    public FlightSolver(ArrayList<Flight> flights) {
        this.flights = flights;
    }

    /* My naive solution using BST.
     * All provided tests pass, and runtime is O(N log N) as required.
     */
    public int MySolve() {
        Comparator<Integer> reverseOrder = (Integer x, Integer y) -> Integer.compare(y, x);
        PriorityQueue<Integer> queue = new PriorityQueue<>(reverseOrder);
        Map<Integer, Integer> tree = new TreeMap<>();
        for (Flight f : flights) {
            for (int i = f.startTime; i <= f.endTime; i++) {
                int count;
                if (tree.get(i) != null) {
                    count = f.passengers + tree.get(i);
                } else {
                    count = f.passengers;
                }
                tree.put(i, count);
                queue.offer(count);
            }
        }
        return queue.remove();
    }

    /* The official solution using priority queue. Much more elegant! */
    public int solve() {
        // two priority queues, one recording startTime, the other recording endTime
        Comparator<Flight> compareStart = Comparator.comparingInt(Flight::startTime);
        PriorityQueue<Flight> queueStart = new PriorityQueue<>(compareStart);
        Comparator<Flight> compareEnd = Comparator.comparingInt(Flight::endTime);
        PriorityQueue<Flight> queueEnd = new PriorityQueue<>(compareEnd);

        // sort through flight arrays and add each flight to the two queues
        for (Flight f : flights) {
            queueStart.offer(f);
            queueEnd.offer(f);
        }

        // two variables to record number of passengers
        int tally = 0;
        int maxVal = 0;

        while (!queueStart.isEmpty() && !queueEnd.isEmpty()) {
            int start = queueStart.peek().startTime; // just peek
            int end = queueEnd.peek().endTime;

            if (start <= end) { // start-end overlap still counts
                Flight f = queueStart.remove();
                tally += f.passengers;
            } else {
                Flight f = queueEnd.remove();
                tally -= f.passengers;
            }

            maxVal = Math.max(tally, maxVal);
        }
        return maxVal;
    }
}
