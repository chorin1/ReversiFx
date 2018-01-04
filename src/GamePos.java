public class GamePos {
	public int m_x;
	public int m_y;
	GamePos(int x, int y) {
		m_x=x;
		m_y=y;
	}
	GamePos(GamePos other) {
		m_x=other.m_x;
		m_y=other.m_y;
	}

	public boolean equals (GamePos other){
		return m_x==other.m_x && m_y==other.m_y;
	}
}
