package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

class BoardAdjTargetTest {

	private static Board board;

	@BeforeAll
	public static void beforeStart() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		// Initialize will load config files
		board.initialize();
	}

	// Cells are YELLOW on the planning spreadsheet
	@Test
	public void testAdjacenciesForRooms() {
		// Testing Theatre
		Set<BoardCell> testList = board.getAdjList(1, 1);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(1, 3)));
		assertTrue(testList.contains(board.getCell(26, 11)));

		// Testing Casino
		testList = board.getAdjList(25, 7);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(22, 5)));
		assertTrue(testList.contains(board.getCell(24, 9)));

		// Testing Kitcen
		testList = board.getAdjList(26, 11);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(21, 11)));
		assertTrue(testList.contains(board.getCell(1, 1)));
	}

	// Cells are YELLOW on the planning spreadsheet
	@Test
	public void testAdjacencyForDoor() {
		Set<BoardCell> testList = board.getAdjList(15, 2);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(14, 2)));
		assertTrue(testList.contains(board.getCell(15, 1)));
		assertTrue(testList.contains(board.getCell(15, 3)));
		assertTrue(testList.contains(board.getCell(18, 1)));

		testList = board.getAdjList(22, 3);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(22, 2)));
		assertTrue(testList.contains(board.getCell(21, 3)));
		assertTrue(testList.contains(board.getCell(22, 4)));
		assertTrue(testList.contains(board.getCell(28, 1)));

		testList = board.getAdjList(22, 5);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(22, 4)));
		assertTrue(testList.contains(board.getCell(21, 5)));
		assertTrue(testList.contains(board.getCell(25, 7)));

	}

	// These tests are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways() {
		Set<BoardCell> testList = board.getAdjList(29, 4);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(28, 4)));

		testList = board.getAdjList(22, 1);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(22, 0)));
		assertTrue(testList.contains(board.getCell(22, 2)));
		assertTrue(testList.contains(board.getCell(21, 1)));

		testList = board.getAdjList(22, 4);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(22, 3)));
		assertTrue(testList.contains(board.getCell(22, 5)));
		assertTrue(testList.contains(board.getCell(21, 4)));
		assertTrue(testList.contains(board.getCell(23, 4)));

		testList = board.getAdjList(11, 8);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(10, 8)));
		assertTrue(testList.contains(board.getCell(12, 8)));
		assertTrue(testList.contains(board.getCell(11, 9)));

	}

	// Tests out of room center, 1, 2 and 3
	// These are GRAY on the planning spreadsheet
	@Test
	public void testTargetsInObservatory() {

		// roll of 1
		board.calcTargets(board.getCell(15, 12), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(13, 9)));
		assertTrue(targets.contains(board.getCell(9, 10)));

		// roll of 2
		board.calcTargets(board.getCell(15, 12), 2);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(14, 9)));
		assertTrue(targets.contains(board.getCell(13, 8)));
		assertTrue(targets.contains(board.getCell(8, 10)));

		// roll of 3
		board.calcTargets(board.getCell(15, 12), 3);
		targets = board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(15, 9)));
		assertTrue(targets.contains(board.getCell(14, 8)));
		assertTrue(targets.contains(board.getCell(12, 8)));
		assertTrue(targets.contains(board.getCell(11, 9)));
		assertTrue(targets.contains(board.getCell(10, 9)));
		assertTrue(targets.contains(board.getCell(9, 8)));
		assertTrue(targets.contains(board.getCell(8, 9)));
		assertTrue(targets.contains(board.getCell(8, 11)));
		assertTrue(targets.contains(board.getCell(9, 12)));
		assertTrue(targets.contains(board.getCell(7, 10)));
	}

	@Test
	public void testTargetsInTheKitchen() {
		// test a roll of 1
		board.calcTargets(board.getCell(26, 11), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(21, 11)));

		// test a roll of 2
		board.calcTargets(board.getCell(26, 11), 2);
		targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(20, 11)));
		assertTrue(targets.contains(board.getCell(21, 10)));
		assertTrue(targets.contains(board.getCell(21, 12)));
		assertTrue(targets.contains(board.getCell(1, 1)));

		// test a roll of 3
		board.calcTargets(board.getCell(26, 11), 3);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(21, 9)));
		assertTrue(targets.contains(board.getCell(21, 13)));
		assertTrue(targets.contains(board.getCell(19, 11)));
		assertTrue(targets.contains(board.getCell(20, 10)));
		assertTrue(targets.contains(board.getCell(20, 12)));
		assertTrue(targets.contains(board.getCell(1, 1)));
	}

	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1
		board.calcTargets(board.getCell(9, 10), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(9, 9)));
		assertTrue(targets.contains(board.getCell(8, 10)));
		assertTrue(targets.contains(board.getCell(15, 12)));

		// test a roll of 2
		board.calcTargets(board.getCell(9, 10), 2);
		targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(7, 10)));
		assertTrue(targets.contains(board.getCell(8, 11)));
		assertTrue(targets.contains(board.getCell(10, 9)));
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(3, 2), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));

		// test a roll of 2
		board.calcTargets(board.getCell(3, 2), 2);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 0)));
	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(12, 3), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(11, 3)));
		assertTrue(targets.contains(board.getCell(13, 3)));

		// test a roll of 2
		board.calcTargets(board.getCell(12, 3), 2);
		targets = board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(10, 3)));
		assertTrue(targets.contains(board.getCell(11, 4)));
		assertTrue(targets.contains(board.getCell(13, 4)));
		assertTrue(targets.contains(board.getCell(14, 3)));
		assertTrue(targets.contains(board.getCell(13, 2)));

	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		board.getCell(17, 8).setOccupied(true);
		board.calcTargets(board.getCell(15, 8), 3);
		board.getCell(17, 8).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(12, 8)));
		assertTrue(targets.contains(board.getCell(13, 9)));
		assertTrue(targets.contains(board.getCell(16, 8)));
		assertFalse(targets.contains(board.getCell(17, 8)));
		assertFalse(targets.contains(board.getCell(18, 8)));

		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(15, 12).setOccupied(true);
		board.getCell(9, 11).setOccupied(true);
		board.calcTargets(board.getCell(9, 10), 1);
		board.getCell(15, 12).setOccupied(false);
		board.getCell(9, 11).setOccupied(false);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(9, 9)));
		assertTrue(targets.contains(board.getCell(8, 10)));
		assertTrue(targets.contains(board.getCell(15, 12)));

		// check leaving a room with a blocked doorway
		board.getCell(13, 9).setOccupied(true);
		board.calcTargets(board.getCell(15, 12), 3);
		board.getCell(13, 9).setOccupied(false);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(8, 11)));
		assertTrue(targets.contains(board.getCell(8, 9)));
		assertTrue(targets.contains(board.getCell(7, 10)));

	}

}
