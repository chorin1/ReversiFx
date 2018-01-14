package assets;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A singleton class that contains all game assets.
 */
public class Assets {
	private static Assets instance = null;
	// stores game pieces
	private final ArrayList<Image> piecesArr = new ArrayList<>();
	// stores main app images (icons, graphical content etc..)
	private final HashMap<String, Image> appImages = new HashMap<>();

	// a blank image to occupy cell
	public WritableImage blank = null;
	private final int photosMaxSize = 120;

	//private constructor of singleton, runs only once
	private Assets() {
		// load core elements (basic pieces)
		try {
			final Image spriteImage = new Image("agk_spritesheet_gamebits3.png");
			final PixelReader reader = spriteImage.getPixelReader();
			final WritableImage whitePiece = new WritableImage(reader, 389, 4, 56, 56);
			final WritableImage blackPiece = new WritableImage(reader, 389, 68, 56, 56);
			final WritableImage whiteKing = new WritableImage(reader, 4, 131, 58, 61);
			final WritableImage blackKing = new WritableImage(reader, 132, 131, 58, 61);
			piecesArr.add(blackPiece);
			piecesArr.add(whitePiece);
			piecesArr.add(whiteKing);
			piecesArr.add(blackKing);
			blank = new WritableImage(getLargestWidth(), getLargestHeight());
		} catch (IllegalArgumentException e) {
			assetLoadExceptionAlert(e);
			Platform.exit();
		}
		// load optional elements
		try {
			final Image noa = new Image("noa.png", photosMaxSize, photosMaxSize, true, true);
			final Image shiner = new Image("shiner.png", photosMaxSize, photosMaxSize, true, true);
			final Image roi = new Image("roi.png", photosMaxSize, photosMaxSize, true, true);
			final Image yossi = new Image("yossi.png", photosMaxSize, photosMaxSize, true, true);

			final Image aboutImg = new Image("weekendgone.jpg", 150, 150, true, true);
			final Image icon = new Image("reversi-stabilne.png");

			appImages.put("icon", icon);
			appImages.put("aboutImg", aboutImg);
			piecesArr.add(noa);
			piecesArr.add(shiner);
			piecesArr.add(roi);
			piecesArr.add(yossi);

			// init blankimage to be the maximum size of the pieces
			blank = new WritableImage(getLargestWidth(), getLargestHeight());
		} catch (IllegalArgumentException e) {
			assetLoadExceptionAlert(e);
		}
	}

	/**
	 * @return the instance of Assets
	 */
	public static Assets getInstance() {
		if(instance == null) {
			instance = new Assets();
		}
		return instance;
	}

	/**
	 * get a piece image from the container
	 * @param index index of the piece to get
	 * @return Image of the piece, null if that index doesn't exist
	 */
	public Image getPieceImg(int index) {
		if (index<0 || index>getPiecesListSize()-1)
			return null;
		return piecesArr.get(index);
	}

	/**
	 * get app image
	 * @param key key of the app image to get
	 * @return the corresponding image, null if not found
	 */
	public Image getAppImage(String key) {
		return appImages.get(key);
	}

	/**
	 * @return amount of pieces in the pieces container
	 */
	public int getPiecesListSize() {
		return piecesArr.size();
	}

	/**
	 * @param index piece index
	 * @return the next available index of a piece in the container
	 */
	public int getNextIndex(int index) {
		if (index == getPiecesListSize()-1)
			return 0;
		return index+1;
	}

	// handle method to get the largest width of piece image in the container
	private int getLargestWidth() {
		int largest = -1;
		for (Image img: piecesArr) {
			if (img.getWidth()>largest)
				largest = (int)img.getWidth();
		}
		return largest;
	}
	// handle method to get the largest height of piece image in the container
	private int getLargestHeight() {
		int largest = -1;
		for (Image img: piecesArr) {
			if (img.getHeight()>largest)
				largest = (int)img.getHeight();
		}
		return largest;
	}

	// open an alert if there's an exception loading images
	private void assetLoadExceptionAlert(IllegalArgumentException e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("No game for you");
		alert.setHeaderText("Sorry, couldn't load assets of the game");
		alert.setContentText("probably couldn't reach one of the files.");

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();
	}
}