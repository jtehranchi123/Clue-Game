// Authors: Tommy Bullock and Jordan Tehranchi
package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel {
	/**
	 * Singleton instance of Board.
	 */
	// Temporarily Initialized to 50x50 board for tests to not error out of bounds.
	// Will be modified in upcoming assignments to accept board size from initialize
	// method
	private static Board theInstance = new Board();
	/**
	 * Array of TestBoardCells. Should not contain nulls.
	 */
	BoardCell[][] grid;
	/**
	 * Stores target list from last call of calcTargets().
	 */
	Set<BoardCell> targets;

	private Map<BoardCell, TreeSet<BoardCell>> adjacencies; // Code Review Change

	private Map<Character, Room> roomMap; // Map for storing rooms from loadSetupConfig() method

	private int numRows = 0;
	private int numCols = 0;
	private String setupConfigFile;
	private String layoutConfigFile;
	private Solution theAnswer;
	private boolean turnIsOver = false;
	private List<Solution> potentialAccusations;
	protected GameControlPanel thisControlPanel;

	protected ActionListener nextPlayerLogic = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			calcTargets(getCell(getCurrentPlayer().getRow(), getCurrentPlayer().getCol()), getDiceRoll());
			Player currentPlayer = getCurrentPlayer();
			// check player type; if human, check if turn is complete
			if (currentPlayer instanceof HumanPlayer) {
				if (turnIsOver) { // process human's suggestions and move
					turnIsOver = false;

					JOptionPane.showMessageDialog(thisControlPanel, "Press next to show each CPU's turns.",
							"Turn complete!", JOptionPane.INFORMATION_MESSAGE);
				} else { // make an angry message to the user

					JOptionPane.showMessageDialog(thisControlPanel, "Please complete your turn.", "Turn not over!",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			} else if (!deactivatedComputers[playerIndex]) { // ComputerPlayer logic follows
				ComputerPlayer cpu = (ComputerPlayer) currentPlayer;
				Solution computerSuggestion = cpu.createSuggestion(theInstance);
				cpu.selectTargets(getTargets());
				BoardCell target = cpu.selectTargets(getTargets());
				cpu.doMove(target);
				if (!computerSuggestion.getRoom().getCardName().equals("Walkway")) {
					players[0].updateSeen(handleSuggestion(computerSuggestion, cpu));
				}
				if (!potentialAccusations.isEmpty()) {
					int randIndex = (int) (Math.random() * potentialAccusations.size());
					Solution accusation = potentialAccusations.get(randIndex);
					potentialAccusations.remove(randIndex);
					deactivatedComputers[playerIndex] = checkAccusation(accusation);
				}
			}
			rollDice();
			nextPlayer();
			calcTargets(getCell(getCurrentPlayer().getRow(), getCurrentPlayer().getCol()), getDiceRoll());
			thisControlPanel.update();
			ClueGame.updateCardsPanel();
			repaint();
			System.out.println(players[0].getName() + " : " + players[0].getSeen());
		}
	};

	private class targetClicksManager implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			int row = (int) (numRows * Math.floor(e.getY() - Board.getInstance().getY())
					/ (float) (Board.getInstance().getHeight())),
					col = numCols * (e.getX() - Board.getInstance().getX()) / Board.getInstance().getWidth();
			BoardCell clickedCell = grid[row][col];
			if (targets.contains(clickedCell)) {
				getCurrentPlayer().doMove(clickedCell.getRow(), clickedCell.getCol());
				turnIsOver = true;
				repaint();
				if (roomMap.containsKey(clickedCell.getInitial()) && clickedCell.getInitial() != 'W') {
					MakeASuggestion suggestionDialog = new MakeASuggestion(
							roomMap.get(getCell(getCurrentPlayer().getRow(), getCurrentPlayer().getCol()).getInitial())
									.getName(),
							Board.getInstance());
					suggestionDialog.setVisible(true);
				}
			} else {
				JOptionPane.showMessageDialog(thisControlPanel,
						"Please select a valid target. \nYou will see your player move to\n that space. Click Next to \n confirm the move.",
						"Invalid target!", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

	}

	public String getRoomStringFromMap(BoardCell cell) {
		return roomMap.get(cell.getInitial()).getName();
	}

	public Solution getTheAnswer() {
		return theAnswer;
	}

	private Player[] players;
	private boolean[] deactivatedComputers;
	public static final int NUMPLAYERS = 6;
	private int playerIndex, diceRoll;
	List<Card> deck;
	List<Card> staticDeck;

	public void nextPlayer() {
		playerIndex = (playerIndex + 1) % NUMPLAYERS;
	}

	public Player getCurrentPlayer() {
		return players[playerIndex];
	}

	public void rollDice() {
		diceRoll = (int) (1 + 6 * Math.random());
	}

	public int getDiceRoll() {
		return diceRoll;
	}

	public Player[] getPlayers() {
		return players;
	}

	public List<Card> getDeck() {
		return deck;
	}

	public boolean isTurnOver() {
		return turnIsOver;
	}

	/**
	 * Instantiates board and fills with generic TestBoardCells. Must call
	 * initialize() still for full population and functionality.
	 */
	private Board() {
		super();
		this.addMouseListener(new targetClicksManager());
		potentialAccusations = new ArrayList<Solution>();
	}

	// Singleton Pattern
	public static Board getInstance() {
		return theInstance;
	}

	public boolean checkAccusation(Solution accusation) {
		return accusation.equals(theAnswer);
	}

	public Card handleSuggestion(Solution suggestion, Player accuser) {
		Card[] disputeCards = new Card[NUMPLAYERS];
		for (int i = 0; i < NUMPLAYERS; i++) {
			disputeCards[i] = (players[i].equals(accuser)) ? null : (players[i].disproveSuggestion(suggestion));
			if (disputeCards[i] != null) {
				{
					if (disputeCards[i].getOwnedBy() == null) {
						disputeCards[i].setOwnedBy(players[i]);
					}
					// gets room based on the name of suggestion's room card
					BoardCell roomCenter = roomMap.get(suggestion.getRoom().getCardName().charAt(0)).getCenterCell();
					// move player to the room in the suggestion
					if (roomCenter != null) {
						disputeCards[i].getOwnedBy().doMove(roomCenter.getRow(), roomCenter.getCol());
					}
				}
				String guessResult = "";
				guessResult = (accuser instanceof HumanPlayer)
						? disputeCards[i].getOwnedBy().getName() + " disproved the suggestion with their card, "
								+ disputeCards[i].getCardName() // player suggestion shows card when disproven
						: disputeCards[i].getOwnedBy().getName() + " disproved the suggestion."; // CPU does not
				thisControlPanel.setGuessResult(guessResult);
				return disputeCards[i];
			}
		}
		potentialAccusations.add(suggestion);
		thisControlPanel.setGuessResult("No new clue");
		return null;
	}

	public void initialize() {
		// instantiate players, deck
		players = new Player[NUMPLAYERS];
		deactivatedComputers = new boolean[NUMPLAYERS];
		deck = new ArrayList<Card>();
		staticDeck = new ArrayList<Card>();
		// reset the board size before filling out any info
		numRows = 0;
		numCols = 0;
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (FileNotFoundException | BadConfigFormatException e1) {
			e1.printStackTrace();
		}
		deal();
	}

	public void loadLayoutConfig() throws BadConfigFormatException, FileNotFoundException {

		Set<BoardCell> allCells = new TreeSet<BoardCell>();
		Scanner fileInput;
		fileInput = new Scanner(new File(layoutConfigFile));
		while (fileInput.hasNextLine()) {
			String eachLine = fileInput.nextLine();
			String[] cells = eachLine.split(",");
			if (numCols == 0) {
				numCols = cells.length;
			}
			if (numCols != cells.length) {
				throw new BadConfigFormatException("Missing Elements and Bad Columns within ClueLayout File");
			}
			if (cells.length != 0) {
				numRows++;
			}
			for (int i = 0; i < cells.length; i++) {
				BoardCell thisCell;
				if (cells[i].length() > 1) {
					thisCell = new BoardCell(numRows - 1, i, cells[i].charAt(0), cells[i].charAt(1), null);
				} else {
					thisCell = new BoardCell(numRows - 1, i, cells[i].charAt(0), ' ', null);
				}
				if (!roomMap.containsKey(thisCell.getInitial())) {
					throw new BadConfigFormatException("Bad Room detected in ClueLayout File");
				}
				allCells.add(thisCell);
			}
		}
		grid = new BoardCell[numRows][numCols];
		for (BoardCell eachCell : allCells) {
			grid[eachCell.getRow()][eachCell.getCol()] = eachCell;
		}

		for (Entry<Character, Room> eachEntry : roomMap.entrySet()) {
			char roomID = eachEntry.getKey();
			Room eachRoom = eachEntry.getValue();
			for (BoardCell[] eachRow : grid) {
				for (BoardCell eachCell : eachRow) {
					if (eachCell.getInitial() == roomID) {
						if (eachCell.getSecretPassage() == '*') {
							eachRoom.setCenterCell(eachCell);
						}
						if (eachCell.getSecretPassage() == '#') {
							eachRoom.setLabelCell(eachCell);
						}
					}
				}
			}
		}
		adjacencies = new TreeMap<BoardCell, TreeSet<BoardCell>>();
		calcAdjLists();
		for (BoardCell eachCell : allCells) {
			eachCell.setAdjList(adjacencies.get(eachCell));
		}
	}

	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {

		roomMap = new TreeMap<Character, Room>();
		File setupText = new File(setupConfigFile);
		int scanLine = 0; // track line for errors
		try {
			Scanner scan = new Scanner(setupText);

			while (scan.hasNextLine()) {
				String fullLineString = scan.nextLine();
				String[] line = fullLineString.split(", ");
				switch (line[0]) {
				case "Room": {
					roomMap.put(line[2].charAt(0), new Room(line[1]));
				}
					break;
				case "Space": {
					roomMap.put(line[2].charAt(0), new Room(line[1]));
				}
					break;
				case "Player": {
					Player newPlayer = null;
					// parse data from the line
					int row = Integer.parseInt(line[4]), col = Integer.parseInt(line[5]);
					String playerName = line[1];
					Color color;
					// color logic: parse colors and convert to RGB
					switch (line[2]) {
					case "yellow": {
						color = new Color(255, 255, 0);
					}
						break;
					case "red": {
						color = new Color(255, 0, 0);
					}
						break;
					case "blue": {
						color = new Color(0, 0, 255);
					}
						break;
					case "white": {
						color = new Color(255, 255, 255);
					}
						break;
					case "green": {
						color = new Color(0, 255, 0);
					}
						break;
					case "magenta": {
						color = new Color(255, 0, 255);
					}
						break;
					default:
						throw new BadConfigFormatException("Unrecognized color name in setup file at line " + scanLine);
					}
					for (int i = 0; i < NUMPLAYERS; i++) {
						// find first null to place the Player in
						// differentiate between human, computer and instantiate Player
						if (players[i] == null) {
							switch (line[3]) {
							case "Human": {
								players[i] = new HumanPlayer(playerName, row, col, color);
								players[i].index = i;
							}
								break;
							case "Computer": {
								players[i] = new ComputerPlayer(playerName, row, col, color);
								players[i].index = i;
							}
								break;
							default:
								throw new BadConfigFormatException("Failed to Load in Players");
							}
							break;
						}
					}
				}
					break;
				case "Card": {
					String cardName = line[2]; // name is given as String already
					CardType cardType; // parse from second String in line
					// convert the String into a CardType
					switch (line[1]) {
					case "W": {
						cardType = CardType.WEAPON;
					}
						break;
					case "P": {
						cardType = CardType.PERSON;
					}
						break;
					case "R": {
						cardType = CardType.ROOM;
					}
						break;
					default:
						throw new BadConfigFormatException("Unrecognized card type");
					}
					deck.add(new Card(cardName, cardType));

				}
					break;
				default:
					if (line[0].length() != 0 && line[0].charAt(0) == '/') {
						continue; // ignore comments
					} else if (fullLineString.length() != 0) {
						// if the line is neither a comment nor empty, it's a bad config!
						throw new BadConfigFormatException(
								"Invalid Type in ClueSetup File. Should be Room, Card, Player, or Space");
					}
				}
				scanLine++;
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			throw new BadConfigFormatException("Incorrect number of parameters from input file at line " + scanLine);
		}
	}

	public void deal() {
		// only deal if you have a deck and exactly 6 players
		boolean hasPlayers = true;
		for (Player each : players) {
			if (each == null) {
				hasPlayers = false;
			}
		}
		if (!(deck.size() == 0) && hasPlayers) {
			// create solution first
			Card person = null, room = null, weapon = null, randCard = null;
			// find a random person, room, weapon
			while (person == null | room == null | weapon == null) {
				// pick a random card
				int randIndex = (int) (deck.size() * Math.random());
				randCard = deck.get(randIndex);
				// for each type, fill with proper card if it hasn't been found yet
				// once a card gets assigned to the solution, remove from deck
				if (randCard.getCardType() == CardType.PERSON && person == null) {
					person = randCard;
					staticDeck.add(randCard);
					deck.remove(randIndex);
					continue;
				}
				if (randCard.getCardType() == CardType.ROOM && room == null) {
					room = randCard;
					staticDeck.add(randCard);
					deck.remove(randIndex);
					continue;
				}
				if (randCard.getCardType() == CardType.WEAPON && weapon == null) {
					weapon = randCard;
					staticDeck.add(randCard);
					deck.remove(randIndex);
					continue;
				}
			}
			theAnswer = new Solution(person, room, weapon);

			// deal all remaining cards round robin style:
			// loop through all players, give one random card each until the deck is empty
			while (!deck.isEmpty()) {
				for (Player eachPlayer : players) {
					if (!deck.isEmpty()) {
						int randIndex = (int) (deck.size() * Math.random());
						randCard = deck.get(randIndex);
						staticDeck.add(randCard);
						eachPlayer.updateHand(randCard);
						deck.remove(randIndex);
					} else {
						break;
					}
				}
			}
		}
		deck = staticDeck; // refill the deck so it can be used later
	}

	public void setConfigFiles(String clueLayoutFile, String clueSetupFile) {
		this.layoutConfigFile = clueLayoutFile;
		this.setupConfigFile = clueSetupFile;

	}

	public Room getRoom(char c) {

		return roomMap.get(c);
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numCols;
	}

	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	/**
	 * Calculates legal targets for a move from startCell of length pathlength.
	 * 
	 * @param startCell  - TestBoardCell which you are starting from
	 * @param pathlength - Length of path to calculate targets from.
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
		boolean[][] visited = new boolean[grid.length][grid[0].length];
		visited[startCell.getRow()][startCell.getCol()] = true;
		targets = calcTargets(startCell.getRow(), startCell.getCol(), pathLength, visited);
	}

	/**
	 * Private recursive method that generates the target list.
	 * 
	 * @param currentRow
	 * @param currentCol
	 * @param remainingPathlength
	 * @return - A TreeSet of acceptable spaces to move to.
	 */

	private TreeSet<BoardCell> calcTargets(int currentRow, int currentCol, int remainingPathlength,
			boolean[][] visited) {
		TreeSet<BoardCell> targetList = new TreeSet<BoardCell>();

		BoardCell thisCell = grid[currentRow][currentCol];
		// base case; visited check prevents edge case of starting in a room from ending
		// the recursive method instantly
		if (remainingPathlength == 0 || (thisCell.isRoom() && !visited[currentRow][currentCol])) {
			targetList.add(thisCell);
			return targetList;
		}

		// mark current as visited
		visited[currentRow][currentCol] = true;
		// non-base case

		for (BoardCell eachCell : adjacencies.get(thisCell)) {
			if (!visited[eachCell.getRow()][eachCell.getCol()] && !eachCell.isOccupied()) {
				targetList.addAll(calcTargets(eachCell.getRow(), eachCell.getCol(), remainingPathlength - 1,
						cloneBoolArray(visited)));

			} else if (!visited[eachCell.getRow()][eachCell.getCol()] && eachCell.isRoomCenter()) {
				targetList.addAll(calcTargets(eachCell.getRow(), eachCell.getCol(), 0, cloneBoolArray(visited)));

			}
		}

		return targetList;
	}

	private boolean[][] cloneBoolArray(boolean[][] boolArray) {
		boolean[][] clone = new boolean[boolArray.length][boolArray[0].length];

		for (int i = 0; i < boolArray.length; i++) {
			for (int j = 0; j < boolArray[0].length; j++) {
				clone[i][j] = boolArray[i][j];
			}
		}

		return clone;
	}

	/**
	 * Private utility method that generates an adjacency list for all cells and
	 * passes each list to the respective cell using TestboardCell.setAdjList().
	 * 
	 * @return - The Map that contains all adjacencies.
	 */

	private void calcAdjLists() {
		// ensure all room centers are initialized before doorValid is called
		for (Room eachRoom : roomMap.values()) {
			if (eachRoom.getCenterCell() != null) {
				adjacencies.put(eachRoom.getCenterCell(), new TreeSet<BoardCell>());
			}
		}
		for (BoardCell[] eachRow : grid) {
			for (BoardCell eachCell : eachRow) {
				if (!adjacencies.containsKey(eachCell)) {
					adjacencies.put(eachCell, new TreeSet<BoardCell>());
				}
				// for walkways:
				if (eachCell.getInitial() == 'W') {
					// checks up, down, left, and right, in that order; adds if valid.
					TreeSet<BoardCell> eachCellAdj = adjacencies.get(eachCell);
					addIfValid(eachCellAdj, eachCell.getRow() - 1, eachCell.getCol(), eachCell);
					addIfValid(eachCellAdj, eachCell.getRow() + 1, eachCell.getCol(), eachCell);
					addIfValid(eachCellAdj, eachCell.getRow(), eachCell.getCol() - 1, eachCell);
					addIfValid(eachCellAdj, eachCell.getRow(), eachCell.getCol() + 1, eachCell);

					// Assignment:
					// door logic within doorValid method. switch handles directionality
					// For Doorways:
					if (eachCell.isDoorway()) {
						switch (eachCell.getDoorDirection()) {
						case UP:
							doorValid(adjacencies.get(eachCell), eachCell.getRow() - 1, eachCell.getCol(), eachCell);
							break;
						case DOWN:
							doorValid(adjacencies.get(eachCell), eachCell.getRow() + 1, eachCell.getCol(), eachCell);
							break;
						case LEFT:
							doorValid(adjacencies.get(eachCell), eachCell.getRow(), eachCell.getCol() - 1, eachCell);
							break;
						case RIGHT:
							doorValid(adjacencies.get(eachCell), eachCell.getRow(), eachCell.getCol() + 1, eachCell);
							break;
						case NONE:
							break;
						default:
							break;
						}
					}
				}
				eachCell.setAdjList(adjacencies.get(eachCell));
			}
		}

		// secret passage logic
		// and
		// non-center room cells logic & self-connection logic
		for (BoardCell[] eachRow : grid) {
			for (BoardCell eachCell : eachRow) {
				for (Entry<Character, Room> eachEntry : roomMap.entrySet()) {
					// if this is a secret passage to an existing room in roomMap
					// avoid linking things to themselves
					if (eachCell.getInitial() != eachEntry.getKey()
							&& eachCell.getSecretPassage() == eachEntry.getKey()) {
						// add both room centers to each other's adj list
						BoardCell thisCenter = roomMap.get(eachCell.getInitial()).getCenterCell(),
								thatCenter = eachEntry.getValue().getCenterCell();
						adjacencies.get(thisCenter).add(thatCenter);
						adjacencies.get(thatCenter).add(thisCenter);
					}
					// if this is part of a Room, this cell gets all roomCenter adjacencies
					if (eachCell.getInitial() != 'X' && eachCell.getInitial() != 'W'
							&& eachCell.getInitial() == eachEntry.getKey()) {
						adjacencies.put(eachCell, adjacencies.get(eachEntry.getValue().getCenterCell()));
					}
					// remove this cell from its own adj list
					if (adjacencies.get(eachCell).contains(eachCell)) {
						adjacencies.get(eachCell).remove(eachCell);
					}
				}
				// remove this cell from its own adj list
				if (adjacencies.get(eachCell).contains(eachCell)) {
					adjacencies.get(eachCell).remove(eachCell);
				}
			}
		}
	}

	public void doorValid(TreeSet<BoardCell> list, int row, int col, BoardCell origCell) {
		if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
			BoardCell destinationCell = grid[row][col];
			// if this door points into a room, add the room center to adj
			if (destinationCell.isRoom()) {
				BoardCell centerCell = roomMap.get(destinationCell.getInitial()).getCenterCell();
				list.add(centerCell);
				// add this door to the center's adjacency
				adjacencies.get(centerCell).add(origCell);
			}
		}
	}

	/**
	 * Private utility method that takes an arrayList and coordinates, adding the
	 * cell at the designated coordinates if the cell is within bounds.
	 * 
	 * @param list - List to add cell to.
	 * @param row  - Row of cell.
	 * @param col  - Column of cell.
	 */
	public void addIfValid(TreeSet<BoardCell> list, int row, int col, BoardCell origCell) {
		// check if the coordinates are within bounds.
		if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
			BoardCell destinationCell = grid[row][col];
			// for walkways, only connect to walkways
			if (!(grid[row][col].getInitial() == 'W') || !(destinationCell.getInitial() == 'W')) {
				return; // invalid
			}
			// it is adjacent
			list.add(destinationCell);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int width = getWidth(); // Width of cell using Graphics getWidth()
		int height = getHeight(); // Height of cell using Graphics getHeight()
		int x = 0; // current Cell x value
		int y = 0; // current Cell y value
		Color playerColor = getCurrentPlayer().getColor();

		// Loops and creates cells and room names adjusted to changing screen size
		for (int i = 0; i < numRows; i++) {
			x = 0;
			for (int j = 0; j < numCols; j++) {
				boolean isTarget = (getCurrentPlayer() instanceof HumanPlayer) && getTargets() != null
						&& getTargets().contains(grid[i][j]);
				// Call to BoardCell draw function
				g.setColor(playerColor);
				grid[i][j].draw(g, (int) width / numCols, (int) height / numRows, x, y, isTarget);
				x = (int) (x + (width / numCols));

				// Check for if Room Label is found, to implement Room Name String
				if (grid[i][j].isLabel()) {
					String roomName = new String();
					for (Map.Entry<Character, Room> entry : roomMap.entrySet()) {
						if (entry.getKey() == grid[i][j].getInitial())
							roomName = entry.getValue().getName();
					}
					roomName = roomName.toUpperCase();
					g.setColor(Color.black);
					Font font = new Font("Arial", Font.BOLD, 10);
					g.setFont(font);
					g.drawString(roomName, x - ((int) (width / numCols)), y - ((int) (height / numRows)));
				}

			}
			y = (int) (y + (height / numRows));
		}

		// Draws all players using Player Draw function
		for (int i = 0; i < players.length; i++) {
			players[i].draw(g, (int) width / numCols, (int) height / numRows);
		}
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	public Set<BoardCell> getAdjList(int i, int j) {
		return adjacencies.get(grid[i][j]);
	}

	public void setTheAnswerTest() {
		Card thePerson = new Card("Miss Peacock", CardType.PERSON);
		Card theRoom = new Card("Theater", CardType.ROOM);
		Card theWeapon = new Card("Rope", CardType.WEAPON);
		Solution rightAnswer = new Solution(thePerson, theRoom, theWeapon);
		theAnswer = rightAnswer;
	}

	public List<Card> getStaticDeck() {
		return staticDeck;
	}
}
