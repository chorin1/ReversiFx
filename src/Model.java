import java.util.ArrayList;

public class Model {
	private Board board;
	ArrayList possibleMovesP1 = new ArrayList<Pos>();
	ArrayList possibleMovesP2 = new ArrayList<Pos>();

	

	Model(int boardSize) {

	}

	public boolean place (Player player, Pos pos) {

	}

	public boolean isPossibleMove(Player player, Pos pos) {

	}

	public int calcScoreOf(Player player) {

	}


	private boolean goTo(Direction direction, Cell currPlayerPiece, Pos pos, boolean doFlip, boolean found) {

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
