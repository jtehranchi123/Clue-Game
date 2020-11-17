package clueGame;

import java.awt.Color;
import java.util.ArrayList;

public class HumanPlayer extends Player {

	public HumanPlayer(String playerName, int row, int col, Color color) {
		super(playerName, color, row, col);
		hand = new ArrayList<Card>();
		seen = new ArrayList<Card>();
	}

}
