package model;

/**
 * board container of size NxN containing boardCell, stored in an efficient way
 * @see BoardCell
 */
public class Board {
	private final int m_boardSize;
	private BoardCell[] m_board;

	/**
	 * constructor of Board
	 * @param boardSize total rows / coulmn to create
	 */
	public Board(int boardSize) {
		m_boardSize = boardSize;
		m_board = new BoardCell[m_boardSize * m_boardSize];
		initForReversi();
	}

	/**
	 * initialize the board according to Reversi rules
	 */
	private void initForReversi() {
		int center = m_boardSize / 2;
		for (int i = 0; i < m_boardSize; i++) {
			for (int j = 0; j < m_boardSize; j++) {
				if ((j == center - 1 && i == center - 1) || (j == center && i == center))
					setCellValue(i, j, BoardCell.PLAYER2);
				else if ((j == center - 1 && i == center) || (j == center && i == center - 1))
					setCellValue(i, j, BoardCell.PLAYER1);
				else
					setCellValue(i, j, BoardCell.EMPTY);
			}
		}
	}

	/**
	 * setter for cell value
	 * @param x row of cell (0..n)
	 * @param y column of cell (0..n)
	 * @param cell value to set
	 */
	public void setCellValue(int x, int y, BoardCell cell) {
		m_board[x + y*m_boardSize] = cell;
	}

	/**
	 * getter of cell value
	 * @param x row of cell (0..n)
	 * @param y column of cell (0..n)
	 * @return cell value
	 */
	public BoardCell getCellAt(int x, int y) {
		return m_board[x + y*m_boardSize];
	}

	/**
	 * @return the size of the board
	 */
	public int getBoardSize() {
		return m_boardSize;
	}

	/**
	 * @return current board state
	 */
	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i<m_boardSize; i++) {
			sb.append("[");
			for (int j=0; j<m_boardSize; j++) {
				switch (getCellAt(i,j)) {
					case EMPTY:
						sb.append(" ");
						break;
					case PLAYER1:
						sb.append("X");
						break;
					case PLAYER2:
						sb.append("O");
						break;
				}
			}
			sb.append("]\n");
		}
		return sb.toString();
	}
}