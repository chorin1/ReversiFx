import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;

public class Assets {
	private static final Image spriteImage = new Image("img/agk_spritesheet_gamebits3.png");
	private static final PixelReader reader = spriteImage.getPixelReader();
	public  static final Image blank = new Image("img/blankPiece.png");
	private static final WritableImage whitePiece = new WritableImage(reader, 389, 4, 55, 56);
	private static final WritableImage blackPiece = new WritableImage(reader, 389, 68, 55, 56);
	private static final WritableImage whiteKing = new WritableImage(reader, 4, 131, 56, 59);
	private static final WritableImage blackKing = new WritableImage(reader, 132, 131, 56, 59);

	private static final ArrayList<Image> piecesArr = new ArrayList<Image>();
	static {
		piecesArr.add(blackPiece);
		piecesArr.add(whitePiece);
		piecesArr.add(whiteKing);
		piecesArr.add(blackKing);
	}
	public static ArrayList<Image> getPiecesList() {
		return piecesArr;
	}
	public static int getPiecesListSize() {
		return piecesArr.size();
	}
	public static int getNextIndex(int index) {
		if (index == getPiecesListSize()-1)
			return 0;
		return index+1;
	}
}