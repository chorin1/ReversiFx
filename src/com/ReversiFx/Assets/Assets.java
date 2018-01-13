package com.ReversiFx.assets;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import java.util.ArrayList;

public class Assets {
	private static Assets instance = null;
	private final ArrayList<Image> piecesArr = new ArrayList<Image>();

	private final Image spriteImage = new Image("com/ReversiFx/Assets/img/agk_spritesheet_gamebits3.png");
	private final PixelReader reader = spriteImage.getPixelReader();
	private final WritableImage whitePiece = new WritableImage(reader, 389, 4, 56, 56);
	private final WritableImage blackPiece = new WritableImage(reader, 389, 68, 56, 56);
	private final WritableImage whiteKing = new WritableImage(reader, 4, 131, 58, 61);
	private final WritableImage blackKing = new WritableImage(reader, 132, 131, 58, 61);

	private final Image noa = new Image("com/ReversiFx/Assets/img/noa.png", 120, 120, true, true);
	private final Image shiner = new Image("com/ReversiFx/Assets/img/shiner.png", 120, 120, true, true);
	private final Image roi = new Image("com/ReversiFx/Assets/img/roi.png", 120, 120, true, true);
	private final Image yossi = new Image("com/ReversiFx/Assets/img/yossi.png", 120, 120, true, true);

	public final Image aboutImg = new Image
			("com/ReversiFx/Assets/img/weekendgone.jpg", 100, 100, true, true);

	public final WritableImage blank;

	// singleton class
	private Assets() {
		piecesArr.add(blackPiece);
		piecesArr.add(whitePiece);
		piecesArr.add(whiteKing);
		piecesArr.add(blackKing);
		piecesArr.add(noa);
		piecesArr.add(shiner);
		piecesArr.add(roi);
		piecesArr.add(yossi);
		blank = new WritableImage(getLargestWidth(), getLargestHeight());
	}
	public static Assets getInstance() {
		if(instance == null) {
			instance = new Assets();
		}
		return instance;
	}

	public ArrayList<Image> getPiecesList() {
		return piecesArr;
	}
	public int getPiecesListSize() {
		return piecesArr.size();
	}
	public int getNextIndex(int index) {
		if (index == getPiecesListSize()-1)
			return 0;
		return index+1;
	}
	private int getLargestWidth() {
		int largest = -1;
		for (Image img: piecesArr) {
			if (img.getWidth()>largest)
				largest = (int)img.getWidth();
		}
		return largest;
	}
	private int getLargestHeight() {
		int largest = -1;
		for (Image img: piecesArr) {
			if (img.getHeight()>largest)
				largest = (int)img.getHeight();
		}
		return largest;
	}
}