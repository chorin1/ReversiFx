import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

public class SettingsController {
	@FXML
	private Button saveBtn;
	@FXML
	private Label boardSizelbl;
	@FXML
	private Slider boardSizeSlider;
	@FXML
	private ImageView player1PieceImg;
	@FXML
	private ImageView player2PieceImg;

	public final static int MAX_BOARD_SIZE = 20;
	public final static int MIN_BOARD_SIZE = 4;
	public final static int DEFAULT_BOARD_SIZE = 8;

	//SETTINGS TO LOAD
	// TODO: change values only on save btn click
	public static int boardSize = DEFAULT_BOARD_SIZE;
	public static int player1PieceIndex = 0;
	public static int player2PieceIndex = 1;

	//make in main on load
	public static Image player1Piece = Assets.getPiecesList().get(player1PieceIndex);
	public static Image player2Piece = Assets.getPiecesList().get(player2PieceIndex);

	public void initialize() {
		//init slider
		boardSizeSlider.setMax(MAX_BOARD_SIZE);
		boardSizeSlider.setMin(MIN_BOARD_SIZE);
		boardSizeSlider.setValue(DEFAULT_BOARD_SIZE);

		//init image
		player1PieceImg.setImage(player1Piece);
		player2PieceImg.setImage(player2Piece);

		// load configuration

		player1PieceImg.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			nextPiece(Player.PLAYER1);
			player1PieceImg.setImage(Assets.getPiecesList().get(player1PieceIndex));
			event.consume();
		});
		player2PieceImg.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			nextPiece(Player.PLAYER2);
			player2PieceImg.setImage(Assets.getPiecesList().get(player2PieceIndex));
			event.consume();
		});

		boardSizeSlider.valueProperty().addListener((obs, oldval, newVal) -> {
			boardSizeSlider.setValue(Math.round(newVal.doubleValue()));
			boardSize = (int) boardSizeSlider.getValue();
			boardSizelbl.setText(String.valueOf(boardSize));
		});
	}

	public void loadSettings() {
		try {
			SettingsLoader.loadSettings();
			boardSize = SettingsLoader.getSetting("boardSize");
			player1PieceIndex = SettingsLoader.getSetting("p1piece");
			player2PieceIndex = SettingsLoader.getSetting("p2piece");
		} catch (FileNotFoundException e) {
			System.out.println("settings file not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveSettings() {
		try {
			//TODO: update settings in class?
			SettingsLoader.setSetting("boardSize", boardSize);
			SettingsLoader.setSetting("p1piece", player1PieceIndex);
			SettingsLoader.setSetting("p2piece", player2PieceIndex);
			SettingsLoader.saveSettings();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void nextPiece(Player player) {
		if (player == Player.PLAYER1) {
			player1PieceIndex = Assets.getNextIndex(player1PieceIndex);
			if (player1PieceIndex==player2PieceIndex)
				player1PieceIndex = Assets.getNextIndex(player1PieceIndex);
		} else {
			player2PieceIndex = Assets.getNextIndex(player2PieceIndex);
			if (player1PieceIndex==player2PieceIndex)
				player2PieceIndex = Assets.getNextIndex(player2PieceIndex);
		}
	}
}
