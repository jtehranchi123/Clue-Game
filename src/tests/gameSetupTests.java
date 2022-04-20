package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class gameSetupTests {

	private static Board board = Board.getInstance();

	@BeforeEach
	public void setUp() {
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
	}

	@Test
	void testCardEquals() {
		Card c1, c2;
		// test 1: different names, different types; should not equal
		c1 = new Card("1", CardType.PERSON);
		c2 = new Card("2", CardType.WEAPON);
		assertTrue(!c1.equals(c2));
		// test 2: different names, same types; should not equal
		c1 = new Card("1", CardType.PERSON);
		c2 = new Card("2", CardType.PERSON);
		assertTrue(!c1.equals(c2));
		// test 3: same names, different types; should not equal
		c1 = new Card("1", CardType.PERSON);
		c2 = new Card("1", CardType.WEAPON);
		assertTrue(!c1.equals(c2));
		// test 4: same names, same types; should equal
		c1 = new Card("1", CardType.PERSON);
		c2 = new Card("1", CardType.PERSON);
		assertTrue(c1.equals(c2));
	}

	@Test
	void testUpdateHand() {
		Player p1 = new HumanPlayer("rex", 1, 1, new Color(155, 135, 30)),
				p2 = new ComputerPlayer("fido", 2, 2, new Color(30, 30, 30));
		Card c1 = new Card("knife", CardType.WEAPON), c2 = new Card("gun", CardType.WEAPON);
		// test 1: add to empty hand for both players: check size and containment
		assertTrue(p1.getHandSize() == 0);
		assertTrue(!p1.getHand().contains(c1));
		p1.updateHand(c1);
		assertTrue(p1.getHandSize() == 1);
		assertTrue(p1.getHand().contains(c1));
		// for player 2
		assertTrue(p2.getHandSize() == 0);
		assertTrue(!p2.getHand().contains(c2));
		p2.updateHand(c2);
		assertTrue(p2.getHandSize() == 1);
		assertTrue(p2.getHand().contains(c2));

		// test 2: add unique cards to the existing hands
		assertTrue(p1.getHandSize() == 1);
		assertTrue(!p1.getHand().contains(c2));
		p1.updateHand(c2);
		assertTrue(p1.getHandSize() == 2);
		assertTrue(p1.getHand().contains(c2));
		// for player 2
		assertTrue(p2.getHandSize() == 1);
		assertTrue(!p2.getHand().contains(c1));
		p2.updateHand(c1);
		assertTrue(p2.getHandSize() == 2);
		assertTrue(p2.getHand().contains(c1));

		// test 3: show that adding duplicates does not add
		assertTrue(p1.getHandSize() == 2);
		assertTrue(p1.getHand().contains(c2));
		p1.updateHand(c2);
		assertTrue(p1.getHandSize() == 2);
		// for player 2
		assertTrue(p2.getHandSize() == 2);
		assertTrue(p2.getHand().contains(c2));
		p2.updateHand(c2);
		assertTrue(p2.getHandSize() == 2);
	}

	@Test
	void testHumanAndComputer() {
		// this test ensures 5 computer players and 1 human player
		Player[] players = board.getPlayers();
		int humanCount = 0, computerCount = 0;

		for (Player player : players) {

			if (player instanceof HumanPlayer) {
				humanCount++;
			} else if (player instanceof ComputerPlayer) {
				computerCount++;
			}
		}

		assertTrue(humanCount == 1);
		assertTrue(computerCount == 5);
	}
}
