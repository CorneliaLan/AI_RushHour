package at.fhooe.ai.rushhour;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a template for the class corresponding to your original
 * advanced heuristic. This class is an implementation of the
 * <tt>Heuristic</tt> interface. After thinking of an original
 * heuristic, you should implement it here, filling in the constructor
 * and the <tt>getValue</tt> method.
 */
public class AdvancedHeuristic implements Heuristic {

    private Puzzle puzzle;

    /**
     * This is the required constructor, which must be of the given form.
     */
    public AdvancedHeuristic(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    /**
     * This method returns the value of the heuristic function at the
     * given state.
     */
    @Override
    public int getValue(State state) {

        if (state.isGoal()) return 0;

        int gridSize = puzzle.getGridSize();
        int[][] grid = state.getGrid();

        int redRow = puzzle.getFixedPosition(0);
        int redPos = state.getVariablePosition(0);
        int redSize = puzzle.getCarSize(0);

        // 1. distance to exit (LOWER BOUND)
        int distance = gridSize - (redPos + redSize);

        // 2. collect blocking cars (no recursion!)
        Set<Integer> blockers = new HashSet<>();

        for (int x = redPos + redSize; x < gridSize; x++) {
            int v = grid[x][redRow];

            if (v != -1 && v != 0) {
                blockers.add(v);
            }
        }

        // 3. secondary layer: ONLY direct dependency (no recursion)
        Set<Integer> secondary = new HashSet<>();

        for (int car : blockers) {

            int fixed = puzzle.getFixedPosition(car);
            int pos = state.getVariablePosition(car);
            int size = puzzle.getCarSize(car);
            boolean vertical = puzzle.getCarOrient(car);

            if (!vertical) continue; // nur relevant wenn Bewegung blockiert Richtung Exit

            // check only immediate blocking cells (no recursive expansion)
            if (pos > 0) {
                int v = grid[fixed][pos - 1];
                if (v != -1 && v != car) secondary.add(v);
            }

            if (pos + size < gridSize) {
                int v = grid[fixed][pos + size];
                if (v != -1 && v != car) secondary.add(v);
            }
        }

        // 4. final heuristic (SAFE combination)
        return distance + blockers.size() + secondary.size() / 2;
    }

    @Override
    public String getName() {
        return "AdvancedHeuristic";
    }
}