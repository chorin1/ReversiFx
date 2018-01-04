public class Pos {
	private int m_x;
	private int m_y;
	Pos(int x, int y) {
		m_x=x;
		m_y=y;
	}
	Pos (Pos other) {
		m_x=other.m_x;
		m_y=other.m_y;
	}

	public boolean equals (Pos other){
		return m_x==other.m_x && m_y==other.m_y;
	}
}
