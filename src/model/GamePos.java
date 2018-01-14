package model;
import java.util.Objects;

/**
 * GamePos object is used to compare moves on a grid, and allow to store moves efficiently
 */
public class GamePos {
	public int m_x;
	public int m_y;

	/**
	 * contruct a a new position
	 * @param x row of the position
	 * @param y coulmn of the position
	 */
	public GamePos(int x, int y) {
		m_x=x;
		m_y=y;
	}

	/**
	 * Copy constructor
	 * @param other another GamePos
	 */
	public GamePos(GamePos other) {
		m_x=other.m_x;
		m_y=other.m_y;
	}

	/**
	 * compare 2 GamePos to check if they are equal
	 * @param o another object
	 * @return true if both GamePos point to the same coordinate
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof GamePos)) {
			return false;
		}
		GamePos pos = (GamePos) o;
		return pos.m_x == m_x && pos.m_y==m_y;
	}
	/**
	 * compare 2 GamePos to check if they are equal
	 * @param other another GamePos object
	 * @return true if both GamePos point to the same coordinate
	 */
	public boolean equals (GamePos other){
		return m_x==other.m_x && m_y==other.m_y;
	}

	/**
	 * compare this GamePos to a coordinate to see if it matches
	 * @param x coordinate x
	 * @param y coordinate y
	 * @return true if paramaters match the GamePos coordinate
	 */
	public boolean equals (int x, int y) {
		return x==m_x && y==m_y;
	}

	/**
	 * give this GamePos object a unique hashCode to store inside a hash type container
	 * @return unique hash code for GamePos instance
	 */
	@Override public int hashCode() {
		return Objects.hash(m_x, m_y);
	}

	/**
	 * @return string representing the GamePos
	 */
	@Override public String toString() {
		return "(" + m_x + "," + m_y + ")";
	}
}
