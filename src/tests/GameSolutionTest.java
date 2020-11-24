package tests;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;

public class GameSolutionTest {

	private static Board board = Board.getInstance();
	private Card thePerson;
	private Card theRoom;
	private Card theWeapon;

	@BeforeEach
	public void setUp() {
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
		board.setTheAnswerTest();

		thePerson = new Card("Miss Peacock", CardType.PERSON);
		theRoom = new Card("Theater", CardType.ROOM);
		theWeapon = new Card("Rope", CardType.WEAPON);

	}

	// Tests for checkAccusation()
	@Test
	public void checkAccusationTests() {
		Card wrongPerson = new Card("Mr. Green", CardType.PERSON);
		Card wrongWeapon = new Card("Wrench", CardType.WEAPON);
		Card wrongRoom = new Card("Bakery", CardType.ROOM);

		// Test for solution that is correct
		Solution rightAnswer = new Solution(thePerson, theRoom, theWeapon);
		assertTrue(board.getTheAnswer().equals(rightAnswer));

		// Test for solution with wrong person
		Solution wrongAnswerPerson = new Solution(wrongPerson, theRoom, theWeapon);
		assertTrue(!board.getTheAnswer().equals(wrongAnswerPerson));

		// Test for solution with wrong weapon
		Solution wrongAnswerWeapon = new Solution(thePerson, theRoom, wrongWeapon);
		assertTrue(!board.getTheAnswer().equals(wrongAnswerWeapon));

		// Test for solution with wrong room
		Solution wrongAnswerRoom = new Solution(thePerson, wrongRoom, theWeapon);
		assertTrue(!board.getTheAnswer().equals(wrongAnswerRoom));

	}

	// Tests for disproveSuggestion()
	@Test
	public void disproveSuggestionTests() {
		Solution suggestion = new Solution(thePerson, theRoom, theWeapon); // Initial Suggestion, used for testing below

		Player player1 = new ComputerPlayer("Bob", 10, 5, new Color(0, 255, 255)); // Random player
		List<Card> hand = new ArrayList<Card>(); // Known hand for different test cases

		// One Matching card Test Case: ###
		Card matchingCard = new Card("Miss Peacock", CardType.PERSON);
		hand.add(matchingCard);
		hand.add(new Card("Bakery", CardType.ROOM));
		hand.add(new Card("Mr. Green", CardType.PERSON));
		player1.setHand(hand);
		assertTrue(player1.disproveSuggestion(suggestion).equals(matchingCard));

		hand.clear();

		// >1 matching card Test Case: ###
		Card matchingCard1 = new Card("Miss Peacock", CardType.PERSON);
		Card matchingCard2 = new Card("Theater", CardType.ROOM);
		hand.add(matchingCard1);
		hand.add(matchingCard2);
		hand.add(new Card("Mr. Green", CardType.PERSON));
		player1.setHand(hand);

		boolean rand1 = false, rand2 = false;
		int count = 0;

		while (rand1 != true & rand2 != true | count < 100) {
			count++;
			rand1 |= player1.disproveSuggestion(suggestion).equals(matchingCard1);
			rand2 |= player1.disproveSuggestion(suggestion).equals(matchingCard2);
		}
		assertTrue(rand1);
		assertTrue(rand2);

		hand.clear();

		// No matching cards Test Case: ###
		hand.add(new Card("Wrench", CardType.WEAPON));
		hand.add(new Card("Bakery", CardType.ROOM));
		hand.add(new Card("Mr. Green", CardType.PERSON));
		player1.setHand(hand);
		assertTrue(player1.disproveSuggestion(suggestion) == null);

	}

}
