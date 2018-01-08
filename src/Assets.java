import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class Assets {
	private static Image spirteImage = new Image("img/agk_spritesheet_gamebits3.png");
	static PixelReader reader = spirteImage.getPixelReader();
	public static WritableImage whitePiece = new WritableImage(reader, 385, 0, 65, 65);
	public static WritableImage blackPiece = new WritableImage(reader, 385, 63, 65, 65);
	public static Image blank = new Image("img/blankPiece.png");
}