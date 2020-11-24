package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {

	private static Board board;
	private static GameControlPanel controlPanel;
	private static GameCardsPanel cardsPanel;

	private static final int WINDOW_LEN = 950;
	private static final int WINDOW_HEIGHT = 950;

	public ClueGame() {

		setSize(WINDOW_LEN, WINDOW_HEIGHT); // Sets default window size
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Sets Close operation
		setTitle("Clue"); // Sets Title

		// setup board
		board = Board.getInstance();
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();

		// setup GUI
		add(board, BorderLayout.CENTER);
		controlPanel = new GameControlPanel(board);
		add(controlPanel, BorderLayout.SOUTH); // GameControlPanel added to ClueGame

		cardsPanel = new GameCardsPanel();
		for (Player player : board.getPlayers()) {
			if (player instanceof HumanPlayer) {
				cardsPanel.updateCards(player);
			}
		}
		add(cardsPanel, BorderLayout.EAST); // GameCardsPanel added to ClueGame

		setVisible(true); // setVisible set to true
		board.repaint(); // board repainted to ensure resizing works successfully
	}

	public static void updateCardsPanel() {
		for (Player player : board.getPlayers()) {
			if (player instanceof HumanPlayer) {
				cardsPanel.updateCards(player);
				cardsPanel.repaint();
				cardsPanel.setVisible(true);
			}
		}
	}

	public static void setSuggestionUI(String guess) {
		controlPanel.setGuess(guess);
		controlPanel.repaint();
	}

	public static void main(String[] args) {

		// Initialize new ClueGame
		ClueGame game = new ClueGame();
		board.rollDice();
		board.calcTargets(board.getCell(board.getCurrentPlayer().getRow(), board.getCurrentPlayer().getCol()),
				board.getDiceRoll());
		board.thisControlPanel = controlPanel;
		controlPanel.update();
		String intro = "Welcome to Clue";
		String userMessage = "You are " + board.getCurrentPlayer().getName()
				+ ".\nCan you find the solution\nbefore the computer players?";
		JOptionPane.showMessageDialog(game, userMessage, intro, JOptionPane.INFORMATION_MESSAGE);

	}

}
