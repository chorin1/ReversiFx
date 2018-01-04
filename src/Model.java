import java.util.HashSet;

public class Model {
	private Board board;
	private HashSet<GamePos> possibleMovesP1 = new HashSet<GamePos>();
	private HashSet<GamePos> possibleMovesP2 = new HashSet<GamePos>();

	Model(int boardSize) {
		board = new Board(boardSize);
		updatePossibleMoves(Player.PLAYER1);
		updatePossibleMoves(Player.PLAYER2);
	}

	public int getBoardSize() {
		return board.getBoardSize();
	}
	public boolean place (Player player, GamePos pos) {
		if (!isPossibleMove(player, pos))
			return false;
		Cell currPiece = (player == Player.PLAYER1)? Cell.PLAYER1 : Cell.PLAYER2;

		//move is possible, set current cell to player piece
		setCellAt(pos, currPiece);
		int row = pos.m_x;
		int clmn = pos.m_y;
		boolean south = false;
		boolean north = false;
		boolean east = false;
		boolean west = false;
		boolean nw = false;
		boolean ne = false;
		boolean sw = false;
		boolean se = false;

		//goTo (direction, currplayerpiece, nextposition, doflipIfValidMove, false
		south = goTo(Direction.SOUTH, currPiece, new GamePos(row+1, clmn), true, false);
		north = goTo(Direction.NORTH, currPiece, new GamePos(row-1, clmn), true, false);
		east = goTo(Direction.EAST, currPiece, new GamePos(row, clmn+1), true, false);
		west = goTo(Direction.WEST, currPiece, new GamePos(row, clmn-1), true, false);
		nw = goTo(Direction.NW, currPiece, new GamePos(row-1, clmn-1), true, false);
		ne = goTo(Direction.NE, currPiece, new GamePos(row-1, clmn+1), true, false);
		sw = goTo(Direction.SW, currPiece, new GamePos(row+1, clmn-1), true, false);
		se = goTo(Direction.SE, currPiece, new GamePos(row+1, clmn+1), true, false);

		updatePossibleMoves(player);
		Player otherPlayer = (player == Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;
		updatePossibleMoves(otherPlayer);
		return (north || south || east || west || nw || ne || sw || se);

	}

	public boolean isPossibleMove(Player player, GamePos pos) {
		HashSet<GamePos> possibleMoves = (player==Player.PLAYER1)? possibleMovesP1 : possibleMovesP2;
		return possibleMoves.contains(pos);
	}

	public int calcScoreOf(Player player) {
		int score = 0;
		Cell currPiece = (player==Player.PLAYER1)? Cell.PLAYER1 : Cell.PLAYER2;
		for (int row = 1; row <= getBoardSize(); row++) {
			for (int clmn = 1; clmn <= getBoardSize(); clmn++) {
				GamePos currPos = new GamePos(row, clmn);
				if (getCellAt(currPos) == currPiece)
					score++;
			}
		}
		return score;
	}


	private boolean goTo(Direction direction, Cell currPlayerPiece, GamePos pos, boolean doFlip, boolean found) {
		Cell currCell = getCellAt(pos);
		if (currCell==Cell.EMPTY)
			return false;
		if (currCell==currPlayerPiece) {
			if (!found)
				return false;
			if (found)
				return true;
		}
		GamePos nextPos = new GamePos(1,1);
		switch (direction) {
			case NORTH:
				nextPos.m_x = pos.m_x-1;
				nextPos.m_y = pos.m_y;
				break;
			case SOUTH:
				nextPos.m_x = pos.m_x+1;
				nextPos.m_y = pos.m_y;
				break;
			case EAST:
				nextPos.m_x = pos.m_x;
				nextPos.m_y = pos.m_y+1;
				break;
			case WEST:
				nextPos.m_x = pos.m_x;
				nextPos.m_y = pos.m_y-1;
				break;
			case NW:
				nextPos.m_x = pos.m_x-1;
				nextPos.m_y = pos.m_y-1;
				break;
			case NE:
				nextPos.m_x = pos.m_x-1;
				nextPos.m_y = pos.m_y+1;
				break;
			case SW:
				nextPos.m_x = pos.m_x+1;
				nextPos.m_y = pos.m_y-1;
				break;
			case SE:
				nextPos.m_x = pos.m_x+1;
				nextPos.m_y = pos.m_y+1;
				break;
			default:
				nextPos.m_x = 0;
				nextPos.m_y = 0;
				break;
		}
		if (goTo(direction, currPlayerPiece, nextPos, doFlip, true)) { //current cell contains opponent
			if (doFlip)
				flip(pos);
			return true;
		}
		return false;
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
				GamePos currPos = new GamePos(row,clmn);
				boolean south = false;
				boolean north = false;
				boolean east = false;
				boolean west = false;
				boolean nw = false;
				boolean ne = false;
				boolean sw = false;
				boolean se = false;

				if (getCellAt(currPos) == Cell.EMPTY) { //ok, current cell is empty, check adjacent cells
					south = goTo(Direction.SOUTH, currPiece, new GamePos(row+1, clmn), false, false);
					north = goTo(Direction.NORTH, currPiece, new GamePos(row-1, clmn), false, false);
					east = goTo(Direction.EAST, currPiece, new GamePos(row, clmn+1), false, false);
					west = goTo(Direction.WEST, currPiece, new GamePos(row, clmn-1), false, false);
					nw = goTo(Direction.NW, currPiece, new GamePos(row-1, clmn-1), false, false);
					ne = goTo(Direction.NE, currPiece, new GamePos(row-1, clmn+1), false, false);
					sw = goTo(Direction.SW, currPiece, new GamePos(row+1, clmn-1), false, false);
					se = goTo(Direction.SE, currPiece, new GamePos(row+1, clmn+1), false, false);
				}
				if (north || south || east || west || nw || ne || sw || se)
					possibleMoves.add(currPos);
			}
		}

	}

	private void flip(GamePos pos) {
		Cell currPiece = getCellAt(pos);
		if (currPiece==Cell.EMPTY)
			return;
		setCellAt(pos, (currPiece==Cell.PLAYER1)? Cell.PLAYER2 : Cell.PLAYER1);
	}

	public Cell getCellAt(GamePos pos) {
		if (isOutOfBounds(pos))
			return null;
		return board.getCellAt(pos.m_x-1, pos.m_y-1); //conversion from board to array
	}

	private void setCellAt(GamePos pos, Cell piece) {
		if (isOutOfBounds(pos))
			System.out.println("Trying to set cell on out of bounds Pos");
		board.setCellValue(pos.m_x - 1, pos.m_y - 1, piece);
	}

	boolean isOutOfBounds(GamePos pos) {
		int boardSize = getBoardSize();
		return pos.m_x < 1 || pos.m_y < 1 || pos.m_x > boardSize || pos.m_y > boardSize;
	}
	private enum Direction {
		NORTH,
		SOUTH,
		WEST,
		EAST,
		NW,
		NE,
		SE,
		SW
	}
}
