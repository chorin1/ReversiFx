import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class Assets {
	private static final Image spriteImage = new Image("img/agk_spritesheet_gamebits3.png");
	private static final PixelReader reader = spriteImage.getPixelReader();
	public static final WritableImage whitePiece = new WritableImage(reader, 385, 0, 65, 65);
	public static final WritableImage blackPiece = new WritableImage(reader, 385, 63, 65, 65);
	public final static Image blank = new Image("img/blankPiece.png");
}