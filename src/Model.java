import java.util.HashSet;

public class Model {
	private Board board;
	private HashSet<GamePos> possibleMovesP1 = new HashSet<GamePos>();
	private HashSet<GamePos> possibleMovesP2 = new HashSet<GamePos>();
	private int scoreP1 = 2;
	private int scoreP2 = 2;
	private final static int[] ROW_OFFSET_ARR = {-1, -1, -1,  0,  0,  1,  1,  1};
	private final static int[] COL_OFFSET_ARR = {-1,  0,  1, -1,  1, -1,  0,  1};

	Model(int boardSize) {
		board = new Board(boardSize);
		updatePossibleMoves(Player.PLAYER1);
		updatePossibleMoves(Player.PLAYER2);
	}

	public int getBoardSize() {
		return board.getBoardSize();
	}

	public boolean isPossibleMove(Player player, GamePos pos) {
		HashSet<GamePos> possibleMoves = (player==Player.PLAYER1)? possibleMovesP1 : possibleMovesP2;
		return possibleMoves.contains(pos);
	}
	public boolean isPossibleMove(Player player, int x, int y) {
		return isPossibleMove(player,new GamePos(x,y));
	}

	private void updatePossibleMoves(Player player) {
		HashSet<GamePos> possibleMoves;
		Cell currPiece;
		switch (player) {
			case PLAYER1:
				possibleMoves = possibleMovesP1;
				currPiece = Cell.PLAYER1;
				break;
			case PLAYER2:
			default:
				possibleMoves = possibleMovesP2;
				currPiece = Cell.PLAYER2;
				break;
		}

		possibleMoves.clear();
		for (int row = 1; row <= getBoardSize(); row++) {
			for (int clmn = 1; clmn <= getBoardSize(); clmn++) {
				if (calcIsPossibleMove(player, row, clmn))
					possibleMoves.add(new GamePos(row,clmn));
			}
		}
	}

	private void flip(int x, int y) {
		Cell currPiece = getCellAt(x,y);
		switch (currPiece) {
			case EMPTY:
				return;
			case PLAYER1:
				setCellAt(x,y,Cell.PLAYER2);
				scoreP1--;
				scoreP2++;
				break;
			case PLAYER2:
				setCellAt(x,y,Cell.PLAYER1);
				scoreP2--;
				scoreP1++;
				break;
		}
	}

	public Cell getCellAt(GamePos pos) {
		if (isOutOfBounds(pos))
			return null;
		return board.getCellAt(pos.m_x-1, pos.m_y-1); //conversion from board to array
	}
	public Cell getCellAt(int x, int y) {
		if (isOutOfBounds(x,y))
			return null;
		return board.getCellAt(x-1, y-1); //conversion from board to array
	}

	private void setCellAt(GamePos pos, Cell piece) {
		if (isOutOfBounds(pos))
			System.out.println("Trying to set cell on out of bounds Pos");
		board.setCellValue(pos.m_x - 1, pos.m_y - 1, piece);
	}
	private void setCellAt(int x, int y, Cell piece) {
		board.setCellValue(x - 1, y - 1, piece);
	}

	private boolean isOutOfBounds(GamePos pos) {
		int boardSize = getBoardSize();
		return pos.m_x < 1 || pos.m_y < 1 || pos.m_x > boardSize || pos.m_y > boardSize;
	}
	private boolean isOutOfBounds(int x, int y) {
		return x<1 || y<1 || x>getBoardSize() || y>getBoardSize();
	}

	private boolean calcIsPossibleMove(Player player, int x, int y) {
		if (getCellAt(x, y)!=Cell.EMPTY)
			return false;
		Cell thisPiece = (player==Player.PLAYER1)? Cell.PLAYER1 : Cell.PLAYER2;
		Cell oppPiece = (player==Player.PLAYER1)? Cell.PLAYER2 : Cell.PLAYER1;
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

	public boolean place(Player player, int x, int y) {
		if (getCellAt(x, y)!=Cell.EMPTY || !isPossibleMove(player,x,y)) {
			System.out.println("Trying to place a piece in an illegal move");
			return false;
		}
		Cell thisPlayerPiece, oppPiece;
		if (player==Player.PLAYER1) {
			thisPlayerPiece = Cell.PLAYER1;
			oppPiece = Cell.PLAYER2;
			scoreP1++;
		} else {
			thisPlayerPiece = Cell.PLAYER2;
			oppPiece = Cell.PLAYER1;
			scoreP2++;
		}
		setCellAt(x,y,thisPlayerPiece);
		// for each offset
		for (int i=0; i<8; i++) {
			int currX = x + ROW_OFFSET_ARR[i];
			int currY = y + COL_OFFSET_ARR[i];
			boolean hasOppPieceBetween = false;
			while (!isOutOfBounds(currX, currY)) {
				Cell currPiece = getCellAt(currX,currY);
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
}
