package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;

public class ComputerAITest {

	private static Board board = Board.getInstance();

	@BeforeEach
	public void setUp() {
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
	}

	// Test selectTargets()
	@Test
	public void testSelectTargets() {

		ComputerPlayer cpu = new ComputerPlayer("Dalek", 9, 10, new Color(0, 0, 0));

		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(15, 12).setOccupied(true);
		board.getCell(9, 11).setOccupied(true);
		board.calcTargets(board.getCell(9, 10), 1);
		board.getCell(15, 12).setOccupied(false);
		BoardCell target = cpu.selectTargets(board.getTargets());
		// the set of targets is 9,9; 8,10; 15,12. The first two are walkways,
		// the last is the Observatory room.
		assertTrue((target.equals(board.getCell(15, 12))));

		// try again but when the ComputerPlayer has Observatory as a previous room.
		// This should force it to pick one of the walkways.
		cpu.addPreviousRoom('O');
		target = cpu.selectTargets(board.getTargets());
		assertTrue(target == null || (target.equals(board.getCell(9, 9))) || (target.equals(board.getCell(8, 10))));
	}

}
