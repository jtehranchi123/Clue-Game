package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MakeASuggestion extends JDialog {

	private JComboBox<String> personSelection;
	private JComboBox<String> weaponSelection;
	private String currentRoom;
	private Board board;

	public MakeASuggestion(String currentRoom, Board board) {

		this.currentRoom = currentRoom;
		this.board = board;

		setTitle("Make a Suggestion");
		setSize(250, 300);
		setLayout(new GridLayout(4, 1));

		// Room Panel to mainPanel
		JPanel roomPanel = new JPanel();
		roomPanel.setBorder(new TitledBorder(new EtchedBorder()));
		roomPanel.setLayout(new GridLayout(1, 2));
		JLabel roomLeftLabel = new JLabel("Current Room");
		JLabel roomRightLabel = new JLabel(currentRoom);
		roomPanel.add(roomLeftLabel);
		roomPanel.add(roomRightLabel);
		add(roomPanel);

		// Person Panel to mainPanel
		JPanel playerPanel = new JPanel();
		playerPanel.setBorder(new TitledBorder(new EtchedBorder()));
		playerPanel.setLayout(new GridLayout(1, 2));
		JPanel personSuggestion = new JPanel();
		personSuggestion.setLayout(new GridLayout(1, 1));
		JLabel person = new JLabel("Person");
		personSuggestion.add(person);

		JPanel personChoice = new JPanel();
		personChoice.setLayout(new GridLayout(1, 1));
		personSelection = new JComboBox<String>();

		for (Player player : board.getPlayers()) {
			personSelection.addItem(player.getName());
		}
		personChoice.add(personSelection);
		playerPanel.add(personSuggestion);
		playerPanel.add(personChoice);
		add(playerPanel);

		// Weapon Panel to mainPanel
		JPanel weaponPanel = new JPanel();
		weaponPanel.setBorder(new TitledBorder(new EtchedBorder()));
		weaponPanel.setLayout(new GridLayout(1, 2));
		JPanel weaponSuggestion = new JPanel();

		weaponSuggestion.setLayout(new GridLayout(1, 1));
		JLabel weapon = new JLabel("Weapon");
		weaponSuggestion.add(weapon);
		JPanel weaponChoice = new JPanel();
		weaponChoice.setLayout(new GridLayout(1, 1));
		weaponSelection = new JComboBox<String>();
		for (Card card : board.getDeck()) {
			if (card.getCardType() == CardType.WEAPON) {
				weaponSelection.addItem(card.getCardName());
			}
		}
		weaponChoice.add(weaponSelection);
		weaponPanel.add(weaponSuggestion);
		weaponPanel.add(weaponChoice);
		add(weaponPanel);

		// Add Buttons to Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		// Add submit button to Panel for Suggestions
		// Handling of actions for setting guess/guess results set based on Human Player
		// suggestion
		JPanel submitPanel = new JPanel();
		submitPanel.setLayout(new GridLayout(1, 1));
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Solution solution = new Solution(new Card((String) personSelection.getSelectedItem(), CardType.PERSON),
						new Card(currentRoom, CardType.ROOM),
						new Card((String) weaponSelection.getSelectedItem(), CardType.WEAPON));
				Player player = Board.getInstance().getCurrentPlayer();
				Card card = Board.getInstance().handleSuggestion(solution, player);
				if (card != null) {
					ClueGame.setSuggestionUI(solution.toString());
					Board.getInstance().getCurrentPlayer().updateSeen(card);
				} else {
					ClueGame.setSuggestionUI(solution.toString());
				}
				dispose();
			}
		});
		submitPanel.add(submitButton);

		// Add cancel button to Panel
		JPanel cancelPanel = new JPanel();
		cancelPanel.setLayout(new GridLayout(1, 1));
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelPanel.add(cancelButton);
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel);

	}

	// For Testing of Suggestion Dialog 
	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
		MakeASuggestion test = new MakeASuggestion("Pool", board);
		test.setVisible(true);
	}

}
