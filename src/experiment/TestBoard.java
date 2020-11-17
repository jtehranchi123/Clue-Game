// Authors: Tommy Bullock and Jordan Tehranchi
package experiment;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class TestBoard {
	/**
	 * Array of TestBoardCells. Should not contain nulls.
	 */
	TestBoardCell[][] board;
	/**
	 * Stores target list from last call of calcTargets().
	 */
	Set<TestBoardCell> targets;

	private TreeMap<TestBoardCell, TreeSet<TestBoardCell>> adjacencies;

	/**
	 * Instantiates board and fills with generic TestBoardCells.
	 * 
	 * @param rows - Number of rows for the board.
	 * @param cols - Number of columns for the board.
	 */
	public TestBoard(int rows, int cols) {
		board = new TestBoardCell[rows][cols];
		// The following loop ensures each position in the array has a TestBoardCell
		// instead of a null. Passes location information to the TestBoardCells through
		// their constructor.
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				board[i][j] = new TestBoardCell(i, j);
			}
		}
		adjacencies = calcAdjLists();
	}

	/**
	 * Calculates legal targets for a move from startCell of length pathlength.
	 * 
	 * @param startCell  - TestBoardCell which you are starting from
	 * @param pathlength - Length of path to calculate targets from.
	 */
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		boolean[][] visited = new boolean[board.length][board[0].length];
		visited[startCell.getRow()][startCell.getCol()] = true;
		targets = calcTargets(startCell.getRow(), startCell.getCol(), pathLength, visited);
	}

	/**
	 * Private recursive method that generates the target list.
	 * 
	 * @param currentRow
	 * @param currentCol
	 * @param remainingPathlength
	 * @return - A TreeSet of acceptable spaces to move to.
	 */

	private TreeSet<TestBoardCell> calcTargets(int currentRow, int currentCol, int remainingPathlength,
			boolean[][] visited) {
		TreeSet<TestBoardCell> targetList = new TreeSet<TestBoardCell>();

		TestBoardCell thisCell = board[currentRow][currentCol];
		// mark current as visited
		visited[currentRow][currentCol] = true;
		// base case
		if (remainingPathlength == 0 || thisCell.isRoom()) {
			targetList.add(thisCell);
			return targetList;
		}
		// non-base case

		for (TestBoardCell eachCell : adjacencies.get(thisCell)) {
			if (!visited[eachCell.getRow()][eachCell.getCol()] && !eachCell.isOccupied()) {
				targetList.addAll(calcTargets(eachCell.getRow(), eachCell.getCol(), remainingPathlength - 1,
						cloneBoolArray(visited)));
			}
		}

		return targetList;
	}

	private boolean[][] cloneBoolArray(boolean[][] boolArray) {
		boolean[][] clone = new boolean[boolArray.length][boolArray[0].length];

		for (int i = 0; i < boolArray.length; i++) {
			for (int j = 0; j < boolArray[0].length; j++) {
				clone[i][j] = boolArray[i][j];
			}
		}

		return clone;
	}

	/**
	 * Private utility method that generates an adjacency list for all cells and
	 * passes each list to the respective cell using TestboardCell.setAdjList().
	 * 
	 * @return - The Map that contains all adjacencies.
	 */

	private TreeMap<TestBoardCell, TreeSet<TestBoardCell>> calcAdjLists() {
		TreeMap<TestBoardCell, TreeSet<TestBoardCell>> adjacencies = new TreeMap<TestBoardCell, TreeSet<TestBoardCell>>();
		for (TestBoardCell[] eachRow : board) {
			for (TestBoardCell eachCell : eachRow) {
				adjacencies.put(eachCell, new TreeSet<TestBoardCell>());
				// checks up, down, left, and right, in that order; adds if valid.
				addIfValid(adjacencies.get(eachCell), eachCell.getRow() - 1, eachCell.getCol());
				addIfValid(adjacencies.get(eachCell), eachCell.getRow() + 1, eachCell.getCol());
				addIfValid(adjacencies.get(eachCell), eachCell.getRow(), eachCell.getCol() - 1);
				addIfValid(adjacencies.get(eachCell), eachCell.getRow(), eachCell.getCol() + 1);
				eachCell.setAdjList(adjacencies.get(eachCell));
			}
		}

		return adjacencies;
	}

	/**
	 * Private utility method that takes an arrayList and coordinates, adding the
	 * cell at the designated coordinates if the cell is within bounds.
	 * 
	 * @param list - List to add cell to.
	 * @param row  - Row of cell.
	 * @param col  - Column of cell.
	 */
	public void addIfValid(TreeSet<TestBoardCell> list, int row, int col) {
		// check if the coordinates are within bounds.
		if (row >= 0 && row < board.length && col >= 0 && col < board[0].length) {
			list.add(board[row][col]);
		}
	}

	public Set<TestBoardCell> getTargets() {
		return targets;
	}

	public TestBoardCell getCell(int row, int col) {
		return board[row][col];
	}

}
