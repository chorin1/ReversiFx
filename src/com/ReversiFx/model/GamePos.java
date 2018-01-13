package com.ReversiFx.model;
import java.util.Objects;
public class GamePos {
	public int m_x;
	public int m_y;
	public GamePos(int x, int y) {
		m_x=x;
		m_y=y;
	}
	public GamePos(GamePos other) {
		m_x=other.m_x;
		m_y=other.m_y;
	}
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof GamePos)) {
			return false;
		}
		GamePos pos = (GamePos) o;
		return pos.m_x == m_x && pos.m_y==m_y;
	}
	public boolean equals (GamePos other){
		return m_x==other.m_x && m_y==other.m_y;
	}
	public boolean equals (int x, int y) {
		return x==m_x && y==m_y;
	}

	@Override public int hashCode() {
		return Objects.hash(m_x, m_y);
	}
	@Override public String toString() {
		return "(" + m_x + "," + m_y + ")";
	}
}
