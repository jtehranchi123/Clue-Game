package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {

	private JTextField currentPlayer = new JTextField(22);
	private JTextField roll = new JTextField(3);
	private JTextField theGuess = new JTextField(50);
	private JTextField theGuessResult = new JTextField(50);
	private Board board;

	public GameControlPanel(Board board) {
		this.board = board;
		setLayout(new GridLayout(2, 0));

		// Top Panel which includes whosTurnPanel, dieRollPanel, nextPlayerPanel, and
		// makeAccusationPanel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 4));

		// whosTurnPanel created and added to GameControlPanel
		JPanel whosTurnPanel = new JPanel();
		whosTurnPanel.setLayout(new GridLayout(2, 1));
		JLabel currPlayerLabel = new JLabel("Whose turn?");
		whosTurnPanel.add(currPlayerLabel);
		whosTurnPanel.add(currentPlayer);
		whosTurnPanel.setBorder(new TitledBorder(new EtchedBorder()));
		currentPlayer.setEditable(false);
		topPanel.add(whosTurnPanel);

		// dieRollPanel created and added to GameControlPanel
		JPanel dieRollPanel = new JPanel();
		dieRollPanel.setLayout(new GridLayout(1, 2));
		JLabel dieLabel = new JLabel("Roll:");
		dieRollPanel.add(dieLabel);
		roll.setEditable(false);
		dieRollPanel.add(roll);
		dieRollPanel.setBorder(new TitledBorder(new EtchedBorder(), "Die"));
		topPanel.add(dieRollPanel);

		// Button for next Player created and added to GameControlPanel
		JButton nextPlayer = new JButton("Go to Next!");
		nextPlayer.setBackground(Color.cyan);
		nextPlayer.setOpaque(true);
		JPanel nextPlayerPanel = new JPanel(new BorderLayout());
		nextPlayerPanel.add(nextPlayer);
		topPanel.add(nextPlayerPanel);
		// nextPlayer action listener: increments player index
		GameControlPanel thisControlPanel = this;

		// Following is turn management logic.
		nextPlayer.addActionListener(board.nextPlayerLogic);

		// Button for making an Accusation created and added to GameControlPanel
		JButton makeAccusation = new JButton("Make Accusation");
		makeAccusation.setBackground(Color.ORANGE);
		makeAccusation.setOpaque(true);
		JPanel makeAccusationPanel = new JPanel(new BorderLayout());
		makeAccusationPanel.add(makeAccusation);
		topPanel.add(makeAccusationPanel);

		// bottomPanel which includes guessPanel, and guessResultPanel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(0, 2));

		// guessPanel created and added to GameControlPanel
		JPanel guessPanel = new JPanel();
		guessPanel.setLayout(new GridLayout(1, 2));
		guessPanel.add(theGuess);
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		bottomPanel.add(guessPanel);

		// guessResultPanel created and added to GameControlPanel
		JPanel guessResultPanel = new JPanel();
		guessResultPanel.setLayout(new GridLayout(1, 2));
		guessResultPanel.add(theGuessResult);
		guessResultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		bottomPanel.add(guessResultPanel);

		// Adds the top 1x4 and bottom 0x2 panel together to create GameControlPanel
		add(topPanel);
		add(bottomPanel);
	}

	// Sets currentPlayer name and also takes die roll in and sets to roll
	private void setTurn(Player currentPlayer, int roll) {
		this.currentPlayer.setText(currentPlayer.getName());
		String rollStr = Integer.toString(roll);
		this.roll.setText(rollStr);

	}

	// Sets Guess and updates JTextField for theGuess
	private void setGuess(String theGuess) {
		this.theGuess.setText(theGuess);
	}

	// Sets GuessResults and updates JTextField for theGuessResult
	private void setGuessResult(String theGuessResult) {
		this.theGuessResult.setText(theGuessResult);

	}

	public void update() {
		setTurn(board.getCurrentPlayer(), board.getDiceRoll());
		repaint(); 
	}

	public static void main(String[] args) {
		Board board = Board.getInstance();
		GameControlPanel panel = new GameControlPanel(board); // create the panel

		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
		JFrame frame = new JFrame(); // create the frame
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(750, 180); // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible

		// test filling in the data
		panel.setTurn(new ComputerPlayer("Bob the Builder", 10, 4, new Color(255, 165, 0)), 6);
		panel.setGuess("I got no guess!");
		panel.setGuessResult("Wait, you have nothing?");
	}

}
