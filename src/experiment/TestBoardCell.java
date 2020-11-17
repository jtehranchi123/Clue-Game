// Authors: Tommy Bullock and Jordan Tehranchi
package experiment;

import java.util.Set;
import java.util.TreeSet;

public class TestBoardCell implements Comparable<TestBoardCell> {

	private int row;
	private int col;
	private Set<TestBoardCell> adjList;
	private boolean isRoom;
	private boolean isOccupied;

	public TestBoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		adjList = new TreeSet<TestBoardCell>();
	}

	/**
	 * Sets of TestBoardCell values
	 * 
	 * @return
	 */
	public Set<TestBoardCell> getAdjList() {
		return adjList;
	}

	protected void setAdjList(Set<TestBoardCell> adjList) {
		this.adjList = adjList;
	}

	/**
	 * if cell is part of room set equal to true or false
	 * 
	 * @param isRoom
	 */
	public void setIsRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}

	public boolean isRoom() {
		return isRoom;
	}

	/**
	 * if cell is occupied by another player set equal to true or false
	 * 
	 * @param getOccupied
	 */
	public void setOccupied(boolean getOccupied) {
		this.isOccupied = getOccupied;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int compareTo(TestBoardCell tbc) {
		return 100 * (this.row - tbc.row) + (this.col - tbc.col);
	}

	public boolean equals(TestBoardCell tbc) {
		return this == tbc;
	}

	public String toString() {
		return "{" + row + ", " + col + "}";
	}

}
