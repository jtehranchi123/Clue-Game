// Authors: Tommy Bullock and Jordan Tehranchi
package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;
import java.util.TreeSet;

public class BoardCell implements Comparable<BoardCell> {

	private int row, col, width, height, x, y;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private Set<BoardCell> adjList;
	private boolean isOccupied;

	public BoardCell(int row, int col, char roomInitial, char secondChar, Set<BoardCell> adjList) {
		this.row = row; // row of board cell
		this.col = col; // column of board cell
		this.initial = roomInitial; // roomInitial to evaluate if Walkway, Unused, or Room
		secretPassage = secondChar; // second character attached to given roomInitial; might have none
		this.adjList = adjList; // adjacency list for given board cell

		// Second Character of roomInitial used in switch statement to evaluate if Door,
		// Passage, Label, Center, or None
		switch (secondChar) {
		case '^':
			doorDirection = DoorDirection.UP;
			break;
		case 'v':
			doorDirection = DoorDirection.DOWN;
			break;
		case '>':
			doorDirection = DoorDirection.RIGHT;
			break;
		case '<':
			doorDirection = DoorDirection.LEFT;
			break;
		case '#':
			roomLabel = true;
			doorDirection = DoorDirection.NONE;
			break;
		case '*':
			roomCenter = true;
			doorDirection = DoorDirection.NONE;
			break;
		default:
			doorDirection = DoorDirection.NONE;
			break;
		}

	}

	public char getInitial() {
		return initial;
	}

	/**
	 * Sets of TestBoardCell values
	 * 
	 * @return
	 */
	public Set<BoardCell> getAdjList() {
		return adjList;
	}

	protected void setAdjList(Set<BoardCell> adjList) {
		this.adjList = adjList;
	}

	public boolean isUnused() {

		return initial == 'X';
	}

	public boolean isRoom() {
		return initial != 'W' && initial != 'X';
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

	@Override
	public int compareTo(BoardCell tbc) {
		return 100 * (this.row - tbc.row) + (this.col - tbc.col);
	}

	public boolean equals(BoardCell tbc) {
		return (this.row == tbc.row && this.col == tbc.col && this.initial == tbc.initial
				&& this.doorDirection == tbc.doorDirection && this.roomLabel == tbc.roomLabel
				&& this.roomCenter == tbc.roomCenter && this.secretPassage == tbc.secretPassage
				&& this.isOccupied == tbc.isOccupied && (this.compareTo(tbc) == 0));
	}

	public String toString() {
		return "{" + row + ", " + col + "}";
	}

	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}

	public boolean isLabel() {
		return roomLabel;
	}

	public boolean isRoomCenter() {
		return roomCenter;
	}

	public char getSecretPassage() {
		return secretPassage;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	// Draws players on Board
	public void draw(Graphics g, int width, int height, int x, int y, boolean isTarget) {

		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;

		Color givenColor = g.getColor();

		if (isRoom()) {
			g.setColor(Color.gray);
			g.fillRect(x, y, width, height);
			g.drawRect(x, y, width, height);
		} else if (isUnused()) {
			g.setColor(Color.black);
			g.fillRect(x, y, width, height);
		} else {
			g.setColor(Color.orange);
			g.fillRect(x, y, width, height);
			g.setColor(Color.black);
			g.drawRect(x, y, width, height);
		}

		if (isTarget) {
			g.setColor(givenColor);
			g.fillOval(x + width / 4, y + height / 4, width / 2, height / 2);
			g.setColor(Color.RED);
			g.drawOval(x + width / 4, y + height / 4, width / 2, height / 2);
		}

		// Draw each doorway in the correct direction using an additional offset
		if (isDoorway()) {
			int doorHeight = 50;
			int doorWidth = 50;
			int verticalOffset = 0;
			int horizontalOffset = 0;

			switch (doorDirection) {
			case RIGHT:
				doorWidth = 4;
				horizontalOffset = width - doorWidth;
				break;
			case LEFT:
				doorWidth = 4;
				break;
			case UP:
				doorHeight = 4;
				break;
			case DOWN:
				doorHeight = 4;
				verticalOffset = height - doorHeight;
				break;
			default:
				break;
			}

			g.setColor(new Color(89, 54, 17));
			g.fillRect(x + horizontalOffset, y + verticalOffset, doorWidth, doorHeight);
		}
	}

}
