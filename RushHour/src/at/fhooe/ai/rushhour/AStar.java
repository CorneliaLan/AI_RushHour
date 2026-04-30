package at.fhooe.ai.rushhour;

import java.util.*;

/**
 * This is the template for a class that performs A* search on a given rush hour
 * puzzle with a given heuristic. The main search computation is carried out by
 * the constructor for this class, which must be filled in. The solution (a path
 * from the initial state to a goal state) is returned as an array of
 * <tt>State</tt>s called <tt>path</tt> (where the first element
 * <tt>path[0]</tt> is the initial state). If no solution is found, the
 * <tt>path</tt> field should be set to <tt>null</tt>. You may also wish to
 * return other information by adding additional fields to the class.
 */
public class AStar {

    /** The solution path is stored here */
    public State[] path;

    private Heuristic heuristic;

    private PriorityQueue<Node> OPEN;

    // CLOSED speichert nur final expandierte States
    private HashSet<State> CLOSED;

    // best known g-values
    private HashMap<State, Integer> gScore;

    /** * This is the constructor that performs A* search to compute a
     * solution for the given puzzle using the given heuristic.
     * */
    public AStar(Puzzle puzzle, Heuristic heuristic) {

        this.heuristic = heuristic;

        OPEN = new PriorityQueue<>(Comparator.comparingInt(n ->
                n.getDepth() + heuristic.getValue(n.getState())
        ));

        CLOSED = new HashSet<>();
        gScore = new HashMap<>();

        Node start = puzzle.getInitNode();

        OPEN.add(start);
        gScore.put(start.getState(), 0);

        while (!OPEN.isEmpty()) {

            Node current = OPEN.poll();
            State currentState = current.getState();
            int currentG = current.getDepth();

            // skip if we already expanded it optimally
            Integer knownBest = gScore.get(currentState);
            if (knownBest != null && currentG > knownBest) {
                continue;
            }

            // goal test
            if (currentState.isGoal()) {
                buildPath(current);
                return;
            }

            // mark as expanded
            CLOSED.add(currentState);

            Node[] successors = current.expand();

            for (Node next : successors) {

                State nextState = next.getState();
                int newG = next.getDepth();

                // if already expanded, skip (standard A* CLOSED rule)
                if (CLOSED.contains(nextState)) {
                    continue;
                }

                Integer bestKnown = gScore.get(nextState);

                // only accept better path
                if (bestKnown == null || newG < bestKnown) {

                    gScore.put(nextState, newG);
                    OPEN.add(next);
                }
            }
        }

        path = null;
    }

    /**
     * Reconstructs path from goal to start via parent pointers.
     */
    private void buildPath(Node goalNode) {

        List<State> result = new ArrayList<>();
        Node current = goalNode;

        while (current != null) {
            result.add(current.getState());
            current = current.getParent();
        }

        Collections.reverse(result);
        path = result.toArray(new State[0]);
    }
}