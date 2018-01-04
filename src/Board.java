public class Board {
	private final int m_boardSize;
	private Cell[] m_board;

	public Board(int boardSize) {
		m_boardSize = boardSize;
		m_board = new Cell [m_boardSize * m_boardSize];
		int center = m_boardSize / 2;
		for (int i = 0; i < m_boardSize; i++) {
			for (int j = 0; j < m_boardSize; j++) {
				if ((j == center - 1 && i == center - 1) || (j == center && i == center))
					setCellValue(i, j, Cell.PLAYER2);
                else if ((j == center - 1 && i == center) || (j == center && i == center - 1))
					setCellValue(i, j, Cell.PLAYER1);
                else
				setCellValue(i, j, Cell.EMPTY);
			}
		}
	}
	public void setCellValue(int x, int y, Cell cell) {
		m_board[x + y*m_boardSize] = cell;
	}

	public Cell getCellAt(int x, int y) {
		return m_board[x + y*m_boardSize];
	}

	public int getBoardSize() {
		return m_boardSize;
	}
}