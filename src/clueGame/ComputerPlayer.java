package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {

	private Solution previousSuggestion;
	List<Card> cardsNotSeen;
	List<Card> peopleNotSeen;
	List<Card> weaponsNotSeen;

	public ComputerPlayer(String playerName, int row, int col, Color color) {
		super(playerName, color, row, col);
		super.previousRooms = new ArrayList<Character>();
		hand = new ArrayList<Card>();
		seen = new ArrayList<Card>();

		cardsNotSeen = new ArrayList<Card>();
		peopleNotSeen = new ArrayList<Card>();
		weaponsNotSeen = new ArrayList<Card>();
	}

	public Solution createSuggestion(Board board) {
		Random rn = new Random();

		for (Card card : board.getStaticDeck()) {
			if (!seen.contains(card))
				cardsNotSeen.add(card);
		}

		for (Card card : cardsNotSeen) {
			if (card.getCardType() == CardType.PERSON)
				peopleNotSeen.add(card);
			else if (card.getCardType() == CardType.WEAPON)
				weaponsNotSeen.add(card);
		}

		Card randomPerson = peopleNotSeen.get(rn.nextInt(peopleNotSeen.size()));
		Card randomWeapon = weaponsNotSeen.get(rn.nextInt(weaponsNotSeen.size()));
		Card thisRoom = new Card(board.getRoomStringFromMap(board.getCell(row, column)), CardType.ROOM);

		// set Room
		previousSuggestion = new Solution(randomPerson, thisRoom, randomWeapon);
		return previousSuggestion;
	}

	private boolean roomIsPrevious(char room) {
		for (Character previousRoom : super.previousRooms) {
			if (room == previousRoom) {
				return true;
			}
		}
		return false;
	}

	public BoardCell selectTargets(Set<BoardCell> targets) {
		// Try to enter new Room
		for (BoardCell cell : targets) {
			if (!(cell == null) && (cell.isDoorway() || cell.isRoomCenter()) && !roomIsPrevious(cell.getInitial())) {
				// determine if door connects to previous room; if so, ignore
				if(!cell.isDoorway()) {
					return cell;
				} else {
					for(BoardCell adjCell : cell.getAdjList()) {
						if(adjCell.isRoom()) {
							if(!roomIsPrevious(adjCell.getInitial())) {
								return cell;
							}
						}
					}
				}
			}
		}

		// If ComputerPlayer cannot enter new room, then enter random target

		BoardCell[] targetArray = new BoardCell[targets.size()];
		targetArray = targets.toArray(targetArray);
		int validTargetsSize = targetArray.length;
		for (int i = 0; i < targetArray.length; i++) {
			if (roomIsPrevious(targetArray[i].getInitial())) {
				targetArray[i] = null;
				validTargetsSize--;
			}
		}
		BoardCell[] validTargets = new BoardCell[validTargetsSize];
		int index = 0;
		for (int i = 0; i < targetArray.length; i++) {
			if (targetArray[i] != null) {
				validTargets[index++] = targetArray[i];
			}
		}

		return validTargets[(int) (validTargetsSize * Math.random())];
	}

	public void updateNotSeen(Card notSeenCard) {
		cardsNotSeen.add(notSeenCard);
	}

	public List<Card> getCardsNotSeen() {

		return cardsNotSeen;

	}

	public List<Card> getWeaponsNotSeen() {

		return weaponsNotSeen;
	}

	public List<Card> getPeopleNotSeen() {

		return peopleNotSeen;
	}

	public void doMove(BoardCell cell) {
		this.row = cell.getRow();
		this.column = cell.getCol();
		if (cell.isRoom()) {
			super.previousRooms.add(cell.getInitial());
		}
	}

}
