package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MakeAnAccusation extends JDialog {

	private JComboBox<String> personSelection;
	private JComboBox<String> weaponSelection;
	private JComboBox<String> roomSelection;

	public MakeAnAccusation() {

		setTitle("Make an Accusation");
		setSize(250, 300);
		setLayout(new GridLayout(4, 1));

		// Room Panel to mainPanel
		JPanel roomPanel = new JPanel();
		roomPanel.setBorder(new TitledBorder(new EtchedBorder()));
		roomPanel.setLayout(new GridLayout(1, 2));
		JPanel roomAccusation = new JPanel();
		roomAccusation.setLayout(new GridLayout(1, 1));
		JLabel room = new JLabel("Room");
		roomAccusation.add(room);
		JPanel roomChoice = new JPanel();
		roomChoice.setLayout(new GridLayout(1, 1));
		roomSelection = new JComboBox<String>();
		// shuffle the deck so the order of stuff is randomized
		Collections.shuffle(Board.getInstance().getDeck());
		for (Card card : Board.getInstance().getDeck()) {
			if (card.getCardType() == CardType.ROOM) {
				roomSelection.addItem(card.getCardName());
			}
		}
		roomChoice.add(roomSelection);
		roomPanel.add(roomAccusation);
		roomPanel.add(roomChoice);
		add(roomPanel);

		// Person Panel to mainPanel
		JPanel playerPanel = new JPanel();
		playerPanel.setBorder(new TitledBorder(new EtchedBorder()));
		playerPanel.setLayout(new GridLayout(1, 2));
		JPanel personAccusation = new JPanel();
		personAccusation.setLayout(new GridLayout(1, 1));
		JLabel person = new JLabel("Person");
		personAccusation.add(person);

		JPanel personChoice = new JPanel();
		personChoice.setLayout(new GridLayout(1, 1));
		personSelection = new JComboBox<String>();

		for (Player player : Board.getInstance().getPlayers()) {
			personSelection.addItem(player.getName());
		}
		personChoice.add(personSelection);
		playerPanel.add(personAccusation);
		playerPanel.add(personChoice);
		add(playerPanel);

		// Weapon Panel to mainPanel
		JPanel weaponPanel = new JPanel();
		weaponPanel.setBorder(new TitledBorder(new EtchedBorder()));
		weaponPanel.setLayout(new GridLayout(1, 2));
		JPanel weaponAccusation = new JPanel();

		weaponAccusation.setLayout(new GridLayout(1, 1));
		JLabel weapon = new JLabel("Weapon");
		weaponAccusation.add(weapon);
		JPanel weaponChoice = new JPanel();
		weaponChoice.setLayout(new GridLayout(1, 1));
		weaponSelection = new JComboBox<String>();
		for (Card card : Board.getInstance().getDeck()) {
			if (card.getCardType() == CardType.WEAPON) {
				weaponSelection.addItem(card.getCardName());
			}
		}
		weaponChoice.add(weaponSelection);
		weaponPanel.add(weaponAccusation);
		weaponPanel.add(weaponChoice);
		add(weaponPanel);

		// Add Buttons to Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		// Add submit button to Panel for Accusations
		// Player winOrLose Dialog added based on accusation by HumanPlayer
		JPanel submitPanel = new JPanel();
		submitPanel.setLayout(new GridLayout(1, 1));
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Solution playerAccusation = new Solution(
						new Card((String) personSelection.getSelectedItem(), CardType.PERSON),
						new Card((String) roomSelection.getSelectedItem(), CardType.ROOM),
						new Card((String) weaponSelection.getSelectedItem(), CardType.WEAPON));
				boolean finalResult = Board.getInstance().checkAccusation(playerAccusation);
				String resultTitle = "Accusation Result";
				String winOrLossMessage;
				if (finalResult) {
					winOrLossMessage = "You have won! It was " + playerAccusation.getPerson() + " in the "
							+ playerAccusation.getRoom() + " with the " + playerAccusation.getWeapon();
					JOptionPane.showMessageDialog(Board.getInstance(), winOrLossMessage, resultTitle,
							JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				} else {
					winOrLossMessage = "You have lost!! The guess: " + playerAccusation.getPerson() + " in the "
							+ playerAccusation.getRoom() + " with the " + playerAccusation.getWeapon()
							+ " is incorrect.\n The real solution- " + Board.getInstance().getTheAnswer()
							+ "\nBetter luck next time!";
					JOptionPane.showMessageDialog(Board.getInstance(), winOrLossMessage, resultTitle,
							JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
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

	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
		MakeAnAccusation test = new MakeAnAccusation();
		test.setVisible(true);
		System.out.println(board.getTheAnswer());
	}

}
