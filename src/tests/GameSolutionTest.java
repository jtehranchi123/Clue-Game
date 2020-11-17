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

	// Tests for handleSuggestions()
	@Test
	public void handleSuggestionTests() {
		CardType weap = CardType.WEAPON, pers = CardType.PERSON, room = CardType.ROOM;
		// each player gets one line of 3 cards descending.
		// so the first line of cards here goes to player 0, and so on
		Card[] deck = { new Card("P0-weap", weap), new Card("P0-Pers", pers), new Card("P0-Room", room),
				new Card("P1-weap", weap), new Card("P1-Pers", pers), new Card("P1-Room", room),
				new Card("P2-weap", weap), new Card("P2-Pers", pers), new Card("P2-Room", room),
				new Card("P3-weap", weap), new Card("P3-Pers", pers), new Card("P3-Room", room),
				new Card("P4-weap", weap), new Card("P4-Pers", pers), new Card("P4-Room", room),
				new Card("P5-weap", weap), new Card("P5-Pers", pers), new Card("P5-Room", room), };
		Player[] players = board.getPlayers();
		for (Player eachPlayer : players) { // reset hands so the deal is based only on the cards above
			eachPlayer.setHand(new ArrayList<Card>());
		}
		for (int i = 0; i < deck.length; i++) {
			players[i / 3].updateHand(deck[i]);
		}

		Solution testSuggestion;
		Card returnedCard;
		// Suggestion no one can disprove returns null
		testSuggestion = new Solution(thePerson, theRoom, theWeapon); // Anyone accuses.
		returnedCard = board.handleSuggestion(testSuggestion, players[3]);
		assertTrue(returnedCard == null);

		// Suggestion only accusing player can disprove returns null
		testSuggestion = new Solution(deck[3], deck[4], deck[5]); // P1 accuses here.
		returnedCard = board.handleSuggestion(testSuggestion, players[1]);
		assertTrue(returnedCard == null);

		// Suggestion only human can disprove returns answer
		testSuggestion = new Solution(deck[0], theRoom, theWeapon); // Not P0 accuses here.
		returnedCard = board.handleSuggestion(testSuggestion, players[4]);
		assertTrue(returnedCard != null && returnedCard.equals(deck[0]));

		// Suggestion that two players can disprove, correct player returns answer
		testSuggestion = new Solution(deck[0], deck[4], theWeapon); // this uses p0's weap, p1's room
		returnedCard = board.handleSuggestion(testSuggestion, players[4]);
		assertTrue(returnedCard != null && returnedCard.equals(deck[0]));
	}
}
