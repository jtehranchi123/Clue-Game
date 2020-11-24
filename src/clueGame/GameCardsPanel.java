package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameCardsPanel extends JPanel {

	private JPanel personPanel, roomPanel, knownCardsPanel, weaponPanel, wepHand, persHand, roomHand, wepSeen, persSeen,
			roomSeen;
	private JLabel inHandLabel, seenLabel;
	private JTextField cardLabel;

	public GameCardsPanel() {

		setLayout(new GridLayout(1, 1));
		setPreferredSize(new Dimension(200, 650));

		inHandLabel = new JLabel("In Hand:");
		seenLabel = new JLabel("Seen:");

		// knownCardsPanel adds playerPanel, roomPanel, and weaponPanel
		knownCardsPanel = new JPanel();
		knownCardsPanel.setLayout(new GridLayout(3, 1));
		TitledBorder centerBorder = new TitledBorder(new EtchedBorder(), "Known Cards");
		centerBorder.setTitleJustification(TitledBorder.CENTER);
		knownCardsPanel.setBorder(centerBorder);

		// playerPanel will include In Hand and Seen
		personPanel = new JPanel();
		personPanel.setLayout(new GridLayout(0, 1));
		personPanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		knownCardsPanel.add(personPanel);

		// roomPanel will include In Hand and Seen
		roomPanel = new JPanel();
		roomPanel.setLayout(new GridLayout(0, 1));
		roomPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		knownCardsPanel.add(roomPanel);

		// weaponPanel will include In Hand and Seen
		weaponPanel = new JPanel();
		weaponPanel.setLayout(new GridLayout(0, 1));
		weaponPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		knownCardsPanel.add(weaponPanel);

		inHandLabel = new JLabel("In Hand:");
		personPanel.add(inHandLabel);
		persHand = new JPanel();
		persHand.setLayout(new GridLayout(0, 2));
		personPanel.add(persHand);
		inHandLabel = new JLabel("In Hand:");
		roomPanel.add(inHandLabel);
		roomHand = new JPanel();
		roomHand.setLayout(new GridLayout(0, 2));
		roomPanel.add(roomHand);
		inHandLabel = new JLabel("In Hand:");
		weaponPanel.add(inHandLabel);
		wepHand = new JPanel();
		wepHand.setLayout(new GridLayout(0, 2));
		weaponPanel.add(wepHand);

		seenLabel = new JLabel("Seen:");
		personPanel.add(seenLabel);
		persSeen = new JPanel();
		persSeen.setLayout(new GridLayout(0, 2));
		personPanel.add(persSeen);
		seenLabel = new JLabel("Seen:");
		roomPanel.add(seenLabel);
		roomSeen = new JPanel();
		roomSeen.setLayout(new GridLayout(0, 2));
		roomPanel.add(roomSeen);
		seenLabel = new JLabel("Seen:");
		weaponPanel.add(seenLabel);
		wepSeen = new JPanel();
		wepSeen.setLayout(new GridLayout(0, 2));
		weaponPanel.add(wepSeen);

		// Adds to entire frame
		add(knownCardsPanel);

	}

	public void updateCards(Player player) {

		List<Card> hand = player.getHand(), seen = player.getSeen();

		JPanel tempPanel;

		boolean hasFoundPersonCard = false, hasFoundRoomCard = false, hasFoundWeaponCard = false;

		personPanel.removeAll();
		personPanel.setLayout(new GridLayout(0, 1));
		personPanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		roomPanel.removeAll();
		roomPanel.setLayout(new GridLayout(0, 1));
		weaponPanel.removeAll();
		weaponPanel.setLayout(new GridLayout(0, 1));

		inHandLabel = new JLabel("In Hand:");
		personPanel.add(inHandLabel);
		persHand = new JPanel();
		persHand.setLayout(new GridLayout(0, 2));
		personPanel.add(persHand);
		inHandLabel = new JLabel("In Hand:");
		roomPanel.add(inHandLabel);
		roomHand = new JPanel();
		roomHand.setLayout(new GridLayout(0, 2));
		roomPanel.add(roomHand);
		inHandLabel = new JLabel("In Hand:");
		weaponPanel.add(inHandLabel);
		wepHand = new JPanel();
		wepHand.setLayout(new GridLayout(0, 2));
		weaponPanel.add(wepHand);

		seenLabel = new JLabel("Seen:");
		personPanel.add(seenLabel);
		persSeen = new JPanel();
		persSeen.setLayout(new GridLayout(0, 2));
		personPanel.add(persSeen);
		seenLabel = new JLabel("Seen:");
		roomPanel.add(seenLabel);
		roomSeen = new JPanel();
		roomSeen.setLayout(new GridLayout(0, 2));
		roomPanel.add(roomSeen);
		seenLabel = new JLabel("Seen:");
		weaponPanel.add(seenLabel);
		wepSeen = new JPanel();
		wepSeen.setLayout(new GridLayout(0, 2));
		weaponPanel.add(wepSeen);

		// Filters and creates panels for hand
		for (Card card : hand) {
			tempPanel = new JPanel();
			tempPanel.setLayout(new BorderLayout());
			cardLabel = new JTextField(tempPanel.getWidth());
			cardLabel.setText(card.getCardName());
			cardLabel.setEditable(false);
			if (card.getPlayerColor() != null) {
				cardLabel.setBackground(card.getPlayerColor());
			}
			tempPanel.add(cardLabel);
			if (card.getCardType() == CardType.PERSON) {
				hasFoundPersonCard = true;
				persHand.add(tempPanel);

			} else if (card.getCardType() == CardType.WEAPON) {
				hasFoundWeaponCard = true;
				wepHand.add(tempPanel);

			} else if (card.getCardType() == CardType.ROOM) {
				hasFoundRoomCard = true;
				roomHand.add(tempPanel);

			}
		}

		// Flags for hand for setting to "None"
		if (!hasFoundPersonCard) {
			tempPanel = new JPanel();
			tempPanel.setLayout(new BorderLayout());
			cardLabel = new JTextField("None");
			cardLabel.setEditable(false);
			tempPanel.add(cardLabel);
			persHand.add(tempPanel);
		}

		if (!hasFoundRoomCard) {
			tempPanel = new JPanel();
			tempPanel.setLayout(new BorderLayout());
			cardLabel = new JTextField("None");
			cardLabel.setEditable(false);
			tempPanel.add(cardLabel);
			roomHand.add(tempPanel);
		}

		if (!hasFoundWeaponCard) {
			tempPanel = new JPanel();
			tempPanel.setLayout(new BorderLayout());
			cardLabel = new JTextField("None");
			cardLabel.setEditable(false);
			tempPanel.add(cardLabel);
			wepHand.add(tempPanel);
		}
		// reset flags for the seen list
		hasFoundPersonCard = false;
		hasFoundRoomCard = false;
		hasFoundWeaponCard = false;

		// Filters and creates panels for seen
		for (Card card : seen) {
			tempPanel = new JPanel();
			tempPanel.setLayout(new BorderLayout());
			cardLabel = new JTextField(tempPanel.getWidth());
			cardLabel.setText(card.getCardName());
			cardLabel.setEditable(false);
			if (card.getPlayerColor() != null) {
				cardLabel.setBackground(card.getPlayerColor());
			}
			tempPanel.add(cardLabel);
			if (card.getCardType() == CardType.PERSON) {
				hasFoundPersonCard = true;
				persSeen.add(tempPanel);

			} else if (card.getCardType() == CardType.WEAPON) {
				hasFoundWeaponCard = true;
				wepSeen.add(tempPanel);

			} else if (card.getCardType() == CardType.ROOM) {
				hasFoundRoomCard = true;
				roomSeen.add(tempPanel);

			}
		}

		// Flags for seen for setting to "None"
		if (!hasFoundPersonCard) {
			tempPanel = new JPanel();
			tempPanel.setLayout(new BorderLayout());
			cardLabel = new JTextField("None");
			cardLabel.setEditable(false);
			tempPanel.add(cardLabel);
			persSeen.add(tempPanel);
		}

		if (!hasFoundRoomCard) {
			tempPanel = new JPanel();
			tempPanel.setLayout(new BorderLayout());
			cardLabel = new JTextField("None");
			cardLabel.setEditable(false);
			tempPanel.add(cardLabel);
			roomSeen.add(tempPanel);
		}

		if (!hasFoundWeaponCard) {
			tempPanel = new JPanel();
			tempPanel.setLayout(new BorderLayout());
			cardLabel = new JTextField("None");
			cardLabel.setEditable(false);
			tempPanel.add(cardLabel);
			wepSeen.add(tempPanel);
		}
		validate();
		repaint();
		validate();
		setVisible(true);
		validate();

	}

	public static void main(String[] args) {
		GameCardsPanel panel = new GameCardsPanel(); // create the panel
		JFrame frame = new JFrame(); // create the frame
		HumanPlayer human = new HumanPlayer("Generic Human Name", 1, 1, Color.RED);
		ComputerPlayer computer = new ComputerPlayer("YoMama", 10, 10, new Color(20, 200, 20));
		ComputerPlayer computer2 = new ComputerPlayer("YoMama2", 15, 15, Color.magenta);
		ArrayList<Card> hand = new ArrayList<Card>(), seen = new ArrayList<Card>();

		// human (red) draws these cards
		seen.add(new Card("Fred", CardType.PERSON)); // index 0 in seen
		seen.add(new Card("Daphne", CardType.PERSON));
		seen.add(new Card("Scooby Doo", CardType.PERSON));
		human.updateHand(seen.get(0));
		human.updateHand(seen.get(1));
		human.updateHand(seen.get(2));
		// computer 2 (magenta) draws this card, human sees it
		seen.add(new Card("Velma", CardType.PERSON)); // index 3 in seen
		computer2.updateHand(seen.get(3));
		human.updateSeen(seen.get(3));
		// computer1 (green) draws these cards, human sees them
		seen.add(new Card("Shaggy", CardType.PERSON)); // index 4 in seen
		seen.add(new Card("Scrappy Doo", CardType.PERSON));
		computer.updateHand(seen.get(4));
		computer.updateHand(seen.get(5));
		human.updateSeen(seen.get(4));
		human.updateSeen(seen.get(5));

		// human draws these cards
		hand.add(new Card("Theater", CardType.ROOM)); // index 0 in hand
		hand.add(new Card("Wrench", CardType.WEAPON)); // index 1 in hand
		human.updateHand(hand.get(0));
		human.updateHand(hand.get(1));

		human.seen = seen; // probably unnecessary, ensures the 'seen' cards are in human player's seen

		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(230, 800); // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close

		panel.updateCards(human);
		frame.setVisible(true); // make it visible
		panel.updateCards(human);
		panel.repaint();
	}
}
