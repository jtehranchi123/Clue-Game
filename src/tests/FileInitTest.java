package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

public class FileInitTest {

	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 30;
	public static final int NUM_COLUMNS = 14;
	public static final int NUM_DOORS = 12;

	private static Board board;

	@BeforeAll
	public static void beforeTests() {
		board = Board.getInstance();

		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");

		board.initialize();
	}

	@Test
	public void testTheRoomLabels() {
		assertEquals("Kitchen", board.getRoom('K').getName());
		assertEquals("Observatory", board.getRoom('O').getName());
		assertEquals("Game Room", board.getRoom('G').getName());
		assertEquals("Lounge", board.getRoom('L').getName());
		assertEquals("Walkway", board.getRoom('W').getName());
	}

	@Test
	public void testBoardDims() {
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	@Test
	public void FourDoorsTypes() {
		BoardCell cell = board.getCell(8, 3);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(6, 6);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(22, 3);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		cell = board.getCell(13, 9);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(12, 4);
		assertFalse(cell.isDoorway());
	}

	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(NUM_DOORS, numDoors);
	}

	@Test
	public void testSecretPassage() {
		// Secret Passage from Theater to Kitchen
		BoardCell cell = board.getCell(4, 9);
		Room room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Game Room");
		assertTrue(cell.getSecretPassage() == 'B');

		// Secret Passage from Theater to Kitchen
		cell = board.getCell(2, 0);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Theater");
		assertTrue(cell.getSecretPassage() == 'K');
	}

	@Test
	public void testUnused() {
		// Test Unused space
		BoardCell cell = board.getCell(11, 5);
		Room room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Unused");
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isLabel());

		// Test Unused space
		cell = board.getCell(16, 7);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Unused");
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isLabel());
	}

	@Test
	public void testWalkway() {
		// Test Walkway
		BoardCell cell = board.getCell(3, 3);
		Room room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Walkway");
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isLabel());

		cell = board.getCell(18, 6);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Walkway");
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isLabel());

	}

	@Test
	public void testRooms() {

		// random cell
		BoardCell cell = board.getCell(24, 12);
		Room room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Kitchen");
		assertFalse(cell.isLabel());
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isDoorway());

		// center cell to test
		cell = board.getCell(18, 1);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Bakery");
		assertTrue(cell.isRoomCenter());
		assertTrue(room.getCenterCell() == cell);

		// label cell to test
		cell = board.getCell(9, 1);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Swimming Pool");
		assertTrue(cell.isLabel());
		assertTrue(room.getLabelCell() == cell);
	}
}
