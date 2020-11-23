package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public abstract class Player {

	private String name;
	private Color color;
	protected int row;
	protected int column;

	protected List<Card> hand;
	protected List<Card> seen;
	protected List<Character> previousRooms;

	public List<Card> getHand() {
		return hand;
	}

	public void setHand(List<Card> hand) {

		this.hand = hand;
	}

	public Player(String playerName, Color color, int row, int column) {
		this.name = playerName;
		this.color = color;
		this.row = row;
		this.column = column;
	}

	public void updateHand(Card card) {
		if (!hand.contains(card)) {
			hand.add(card);
			card.setPlayerColor(color);
			card.setOwnedBy(this);
		}
	}

	public int getHandSize() {
		return hand.size();
	}

	public void addPreviousRoom(char room) {
		this.previousRooms.add(room);
	}

	public void updateSeen(Card seenCard) {
		seen.add(seenCard);
	}

	public Color getColor() {
		return color;
	}

	public List<Card> getSeen() {

		return seen;
	}

	public int getRow() {

		return row;
	}

	public int getCol() {

		return column;
	}

	public String getName() {
		return this.name;
	}

	public Card disproveSuggestion(Solution suggestion) {
		List<Card> containedCards = new ArrayList<Card>();
		Random rand = new Random();

		for (Card eachCard : hand) {
			// Adds person card if contained
			if (eachCard.equals(suggestion.getPerson())) {
				containedCards.add(suggestion.getPerson());
			}
			// Adds weapon card if contained
			if (eachCard.equals(suggestion.getWeapon())) {
				containedCards.add(suggestion.getWeapon());
			}
			// Adds room card if contained
			if (eachCard.equals(suggestion.getRoom())) {
				containedCards.add(suggestion.getRoom());
			}
		}

		// Picks random cards out of the cards suggested
		// If only 1 card, then that card is picked
		if (containedCards.size() > 0) {
			return containedCards.get(rand.nextInt(containedCards.size()));
		} else {
			return null;
		}
	}

	// Draws Players on Board
	public void draw(Graphics g, int width, int height) {
		g.setColor(color);
		g.fillOval(width * column, height * row, width, height);
	}

	public void doMove(int newRow, int newCol) {
		row = newRow;
		column = newCol;
	}
}
