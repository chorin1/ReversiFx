package com.ReversiFx.model;

public class Board {
	private final int m_boardSize;
	private boardCell[] m_board;

	public Board(int boardSize) {
		m_boardSize = boardSize;
		m_board = new boardCell[m_boardSize * m_boardSize];
		int center = m_boardSize / 2;
		for (int i = 0; i < m_boardSize; i++) {
			for (int j = 0; j < m_boardSize; j++) {
				if ((j == center - 1 && i == center - 1) || (j == center && i == center))
					setCellValue(i, j, boardCell.PLAYER2);
                else if ((j == center - 1 && i == center) || (j == center && i == center - 1))
					setCellValue(i, j, boardCell.PLAYER1);
                else
				setCellValue(i, j, boardCell.EMPTY);
			}
		}
	}
	public void setCellValue(int x, int y, boardCell cell) {
		m_board[x + y*m_boardSize] = cell;
	}

	public boardCell getCellAt(int x, int y) {
		return m_board[x + y*m_boardSize];
	}

	public int getBoardSize() {
		return m_boardSize;
	}

	@Override public String toString() {
		String boardString = "";
		for (int i = 0; i<m_boardSize; i++) {
			boardString += "[";
			for (int j=0; j<m_boardSize; j++) {
				switch (getCellAt(i,j)) {
					case EMPTY:
						boardString += " ";
						break;
					case PLAYER1:
						boardString += "X";
						break;
					case PLAYER2:
						boardString += "O";
						break;
				}
			}
			boardString += "]\n";
		}
		return boardString;
	}
}