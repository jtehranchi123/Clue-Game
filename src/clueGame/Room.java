package clueGame;

public class Room {

	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;

	public Room(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}

	protected void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	protected void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

}
