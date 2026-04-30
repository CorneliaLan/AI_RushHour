package at.fhooe.ai.rushhour;
/**
 * This is a template for the class corresponding to the blocking heuristic.
 * This heuristic returns zero for goal states, and otherwise returns one plus
 * the number of cars blocking the path of the goal car to the exit. This class
 * is an implementation of the <tt>Heuristic</tt> interface, and must be
 * implemented by filling in the constructor and the <tt>getValue</tt> method.
 */
public class BlockingHeuristic implements Heuristic {

  private Puzzle puzzle;

  /**
   * This is the required constructor, which must be of the given form.
   */
  public BlockingHeuristic(Puzzle puzzle) {
    // TODO
    this.puzzle = puzzle;
  }

  /**
   * This method returns the value of the heuristic function at the given state.
   */
  public int getValue(State state) {
    // TODO
    if (state.isGoal()) {
      return 0;
    }

    int gridSize = puzzle.getGridSize();

    // red car info
    int redRow = puzzle.getFixedPosition(0);
    int redPos = state.getVariablePosition(0);
    int redSize = puzzle.getCarSize(0);

    // count blocking cars
    int blockingCars = 0;

    // check all cells right of the red car
    for (int x = redPos + redSize; x < gridSize; x++) {
        int [][] grid = state.getGrid();
        int v = grid[x][redRow];

        if (v != -1 && v != 0) {
          blockingCars++;
        }
    }

    return 1 + blockingCars;
  }

  @Override
  public String getName() {
    return "BlockingHeuristic";
  }

}
