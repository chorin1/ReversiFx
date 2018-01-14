package model;
import java.util.HashSet;

/**
 * Model for game Reversi, contains the game board and logic.
 */
public class Model {
	private Board board;
	// available moves for each player
	private HashSet<GamePos> possibleMovesP1 = new HashSet<>();
	private HashSet<GamePos> possibleMovesP2 = new HashSet<>();
	private int scoreP1 = 2;
	private int scoreP2 = 2;

	// allowed directions to check for a valid move
	private final static int[] ROW_OFFSET_ARR = {-1, -1, -1,  0,  0,  1,  1,  1};
	private final static int[] COL_OFFSET_ARR = {-1,  0,  1, -1,  1, -1,  0,  1};

	/**
	 * Start a new game model
	 * @param boardSize size of the new board to make (boardSize*boardSize)
	 */
	public Model(int boardSize) {
		board = new Board(boardSize);
		updatePossibleMoves(Player.PLAYER1);
		updatePossibleMoves(Player.PLAYER2);
	}

	/**
	 * getter for board size
	 * @return current board size
	 */
	public int getBoardSize() {
		return board.getBoardSize();
	}

	/**
	 * Checks if a move is possible (available in the possible moves container)
	 * @param player player to make the move
	 * @param pos GamePos of the wanted move
	 * @return true if move is available
	 */
	private boolean isPossibleMove(Player player, GamePos pos) {
		HashSet<GamePos> possibleMoves = (player==Player.PLAYER1)? possibleMovesP1 : possibleMovesP2;
		return possibleMoves.contains(pos);
	}

	/**
	 * Checks if a move is possible (available in the possible moves container)
	 * @param player player to make the move
	 * @param x coordinate X of the wanted move
	 * @param y coordinate Y of the wanted move
	 * @return true if move is available
	 */
	public boolean isPossibleMove(Player player, int x, int y) {
		return isPossibleMove(player,new GamePos(x,y));
	}

	/**
	 * update possible moves container (by finding all possible moves on the board)
	 * @param player player to update possible moves for
	 */
	private void updatePossibleMoves(Player player) {
		HashSet<GamePos> possibleMoves = (player==Player.PLAYER1)? possibleMovesP1 : possibleMovesP2;
				possibleMoves.clear();
		for (int row = 1; row <= getBoardSize(); row++)
			for (int clmn = 1; clmn <= getBoardSize(); clmn++)
				if (calcIsPossibleMove(player, row, clmn))
					possibleMoves.add(new GamePos(row,clmn));
	}

	/**
	 * flip a piece
	 * @param x coordinate x of piece to flip
	 * @param y coordinate y of piece to flip
	 */
	private void flip(int x, int y) {
		BoardCell currPiece = getCellAt(x,y);
		switch (currPiece) {
			case EMPTY:
				return;
			case PLAYER1:
				setCellAt(x,y, BoardCell.PLAYER2);
				scoreP1--;
				scoreP2++;
				break;
			case PLAYER2:
				setCellAt(x,y, BoardCell.PLAYER1);
				scoreP2--;
				scoreP1++;
				break;
		}
	}

	/**
	 * getter for the piece in a cell (in board coordinates)
	 * @param pos coordinate of the cell
	 * @return what the cell contains
	 */
	public BoardCell getCellAt(GamePos pos) {
		if (isOutOfBounds(pos))
			return null;
		return board.getCellAt(pos.m_x-1, pos.m_y-1); //conversion from board to array
	}

	/**
	 * getter for the piece in a cell (in board coordinates)
	 * @param x coordinate x of the cell (1..n)
	 * @param y coordinate y of the cell (1..n)
	 * @return what the cell contains
	 */
	public BoardCell getCellAt(int x, int y) {
		if (isOutOfBounds(x,y))
			return null;
		return board.getCellAt(x-1, y-1); //conversion from board to array
	}

	// setters for cell value (private method), outside user should use "place"
	private void setCellAt(GamePos pos, BoardCell piece) {
		if (isOutOfBounds(pos))
			System.out.println("Trying to set cell on out of bounds Pos");
		board.setCellValue(pos.m_x - 1, pos.m_y - 1, piece);
	}
	private void setCellAt(int x, int y, BoardCell piece) {
		board.setCellValue(x - 1, y - 1, piece);
	}

	// check if a position is out of board bounds
	private boolean isOutOfBounds(GamePos pos) {
		int boardSize = getBoardSize();
		return pos.m_x < 1 || pos.m_y < 1 || pos.m_x > boardSize || pos.m_y > boardSize;
	}
	private boolean isOutOfBounds(int x, int y) {
		return x<1 || y<1 || x>getBoardSize() || y>getBoardSize();
	}

	/*
	calculates (by Reversi rules) if a move is possible.
	used by the model to update possible moves
	 */
	private boolean calcIsPossibleMove(Player player, int x, int y) {
		if (getCellAt(x, y)!= BoardCell.EMPTY)
			return false;
		BoardCell thisPiece = (player==Player.PLAYER1)? BoardCell.PLAYER1 : BoardCell.PLAYER2;
		BoardCell oppPiece = (player==Player.PLAYER1)? BoardCell.PLAYER2 : BoardCell.PLAYER1;
		// for each offset
		for (int i=0; i<8; i++) {
			int currX = x + ROW_OFFSET_ARR[i];
			int currY = y + COL_OFFSET_ARR[i];
			boolean hasOppPieceBetween = false;
			while (!isOutOfBounds(currX, currY)) {
				if (getCellAt(currX,currY) == oppPiece)
					hasOppPieceBetween = true;
				else if (getCellAt(currX,currY) == thisPiece && hasOppPieceBetween)
					return true;
				else
					break;
				currX += ROW_OFFSET_ARR[i];
				currY += COL_OFFSET_ARR[i];
			}
		}
		return false;
	}

	/**
	 * make a move by the player
	 * @param player player that wants to make the move
	 * @param x coordinate X (row) of the placed piece (1..n)
	 * @param y coordinate Y (column) of the placed piece (1..n)
	 * @return true if the move was made (if its possible by game rules)
	 */
	public boolean place(Player player, int x, int y) {
		if (getCellAt(x, y)!= BoardCell.EMPTY || !isPossibleMove(player,x,y)) {
			System.out.println("illegal move - (" +x + "," + y +")");
			return false;
		}
		BoardCell thisPlayerPiece, oppPiece;
		if (player==Player.PLAYER1) {
			thisPlayerPiece = BoardCell.PLAYER1;
			oppPiece = BoardCell.PLAYER2;
			scoreP1++;
		} else {
			thisPlayerPiece = BoardCell.PLAYER2;
			oppPiece = BoardCell.PLAYER1;
			scoreP2++;
		}
		setCellAt(x,y,thisPlayerPiece);
		// for each offset
		for (int i=0; i<8; i++) {
			int currX = x + ROW_OFFSET_ARR[i];
			int currY = y + COL_OFFSET_ARR[i];
			boolean hasOppPieceBetween = false;
			while (!isOutOfBounds(currX, currY)) {
				BoardCell currPiece = getCellAt(currX,currY);
				if (currPiece == oppPiece)
					hasOppPieceBetween = true;
				else if (currPiece == thisPlayerPiece && hasOppPieceBetween) {
					// found valid move, go back in the same offset until reaching this player's piece again
					currX -= ROW_OFFSET_ARR[i];
					currY -= COL_OFFSET_ARR[i];
					do {
						flip (currX, currY);
						currX -= ROW_OFFSET_ARR[i];
						currY -= COL_OFFSET_ARR[i];
					} while (getCellAt(currX,currY)!=thisPlayerPiece);
					break;
				}
				else // empty or this player's piece without an opp piece between
					break;
				currX += ROW_OFFSET_ARR[i];
				currY += COL_OFFSET_ARR[i];
			}
		}
		// update possible moves for both players
		updatePossibleMoves(Player.PLAYER1);
		updatePossibleMoves(Player.PLAYER2);
		return true;
	}

	/**
	 * @return player 1 score
	 */
	public int getScoreP1() {
		return  scoreP1;
	}

	/**
	 * @return player 2 score
	 */
	public int getScoreP2() {
		return  scoreP2;
	}

	/**
	 * check's if a player has no available moves
	 * @param player player to check
	 * @return true if that player has no possible moves
	 */
	public boolean cantMove(Player player) {
		HashSet<GamePos> possibleMoves = (player==Player.PLAYER1)? possibleMovesP1 : possibleMovesP2;
		return possibleMoves.isEmpty();
	}

	/**
	 * @return string of the whole model data
	 */
	@Override public String toString() {
		String showData;
		showData = "Score p1 = " + scoreP1 + " Score p2 = " + scoreP2 +"\n";
		showData += "Possible moves p1 = "+possibleMovesP1+"\n";
		showData += "Possible moves p2 = "+possibleMovesP2+"\n";
		showData += "Board:\n" + board;
		return showData;
	}
}
