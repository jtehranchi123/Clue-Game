package clueGame;

import java.awt.Color;

public class Card implements Comparable<Card> {

	@Override
	public String toString() {
		return cardName;
	}

	private String cardName;

	private CardType cardType;
	private Color playerColor;
	private Player ownedBy; 

	public Player getOwnedBy() {
		return ownedBy;
	}

	public void setOwnedBy(Player ownedBy) {
		this.ownedBy = ownedBy;
	}

	public Color getPlayerColor() {
		return playerColor;
	}

	public void setPlayerColor(Color playerColor) {
		this.playerColor = playerColor;
	}

	public Card(String cardName, CardType cardType) {
		this.cardName = cardName;
		this.cardType = cardType;
	}

	public boolean equals(Card target) {

		return this.cardName.equals(target.cardName) && this.cardType.equals(target.cardType);
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public void setType(CardType cardType) {
		this.cardType = cardType;
	}

	public String getCardName() {
		return cardName;
	}

	public CardType getCardType() {
		return cardType;
	}

	@Override
	public int compareTo(Card o) {
		return cardName.compareTo(o.cardName) + cardType.ordinal() - o.cardType.ordinal();
	}

}
