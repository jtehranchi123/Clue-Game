package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		setSize(250,300);
		setLayout(new GridLayout(4,1));

		// Room Panel to mainPanel
		JPanel roomPanel = new JPanel();
		roomPanel.setBorder(new TitledBorder(new EtchedBorder()));
		roomPanel.setLayout(new GridLayout(1,2));
		JPanel roomSuggestion = new JPanel();
		roomSuggestion.setLayout(new GridLayout(1,1));
		JLabel room = new JLabel("Room");
		roomSuggestion.add(room);
		JPanel roomChoice = new JPanel();
		roomChoice.setLayout(new GridLayout(1,1));
		roomSelection = new JComboBox<String>();
		for( Card card : Board.getInstance().getDeck()) {
			if(card.getCardType() == CardType.ROOM) {
				roomSelection.addItem(card.getCardName());
			}
		}
		roomChoice.add(roomSelection);
		roomPanel.add(roomSuggestion);
		roomPanel.add(roomChoice);
		add(roomPanel);
		
		
		

		// Person Panel to mainPanel
		JPanel playerPanel = new JPanel();
		playerPanel.setBorder(new TitledBorder(new EtchedBorder()));
		playerPanel.setLayout(new GridLayout(1,2));
		JPanel personSuggestion = new JPanel();
		personSuggestion.setLayout(new GridLayout(1,1));
		JLabel person = new JLabel("Person");
		personSuggestion.add(person);

		JPanel personChoice = new JPanel();
		personChoice.setLayout(new GridLayout(1,1));
		personSelection = new JComboBox<String>();

		for( Player player: Board.getInstance().getPlayers()) {
			personSelection.addItem(player.getName());
		}
		personChoice.add(personSelection);
		playerPanel.add(personSuggestion);
		playerPanel.add(personChoice);
		add(playerPanel);



		// Weapon Panel to mainPanel
		JPanel weaponPanel = new JPanel();
		weaponPanel.setBorder(new TitledBorder(new EtchedBorder()));
		weaponPanel.setLayout(new GridLayout(1,2));
		JPanel weaponSuggestion = new JPanel();

		weaponSuggestion.setLayout(new GridLayout(1,1));
		JLabel weapon = new JLabel("Weapon");
		weaponSuggestion.add(weapon);
		JPanel weaponChoice = new JPanel();
		weaponChoice.setLayout(new GridLayout(1,1));
		weaponSelection = new JComboBox<String>();
		for( Card c : Board.getInstance().getDeck()) {
			if(c.getCardType() == CardType.WEAPON) {
				weaponSelection.addItem(c.getCardName());
			}
		}
		weaponChoice.add(weaponSelection);
		weaponPanel.add(weaponSuggestion);
		weaponPanel.add(weaponChoice);
		add(weaponPanel);


		// Add Buttons to Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));

		// Add submit button to Panel
		JPanel submitPanel = new JPanel();
		submitPanel.setLayout(new GridLayout(1,1));
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Solution accusation = new Solution((Card)personSelection.getSelectedItem(),(Card)roomSelection.getSelectedItem(),(Card)weaponSelection.getSelectedItem());
				boolean finalResult = Board.getInstance().checkAccusation(accusation); 
				String resultTitle = "Accusation Result";
				String message;
				if(finalResult) {
					message = "You have won! It was " + accusation.getPerson() + " in the " + accusation.getRoom() + " with the " + accusation.getWeapon();
				}
				else {
					message = "You have guessed incorrectly. The guess: " + accusation.getPerson() + " in the " + accusation.getRoom() + " with the " + accusation.getWeapon() + " is incorrect.";
				}
				JOptionPane.showMessageDialog(Board.getInstance(), message, resultTitle, JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
				dispose();
			}
		});
		submitPanel.add(submitButton);


		// Add cancel button to Panel
		JPanel cancelPanel = new JPanel();
		cancelPanel.setLayout(new GridLayout(1,1));
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
	}


}
