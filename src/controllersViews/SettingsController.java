package controllersViews;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.*;
import assets.*;
import settingsFromFile.*;

/**
 * controls the game settings view.
 * uses settingsFromFile.SettingsLoader to load/save settings for the next game.
 */
public class SettingsController {
	@FXML
	private Button cancelBtn;
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

	//defaults and constants
	private final int MAX_BOARD_SIZE = 20;
	private final int MIN_BOARD_SIZE = 4;
	private final int DEFAULT_BOARD_SIZE = 8;
	private final int DEFAULT_PIECE_P1 = 0;
	private final int DEFAULT_PIECE_P2 = 1;

	//temporary settings for user selection
	private int boardSize;
	private int player1PieceIndex;
	private int player2PieceIndex;

	/**
	 * initialize the view according to the save configuration
	 * if no saved file is found the default configuration will be loaded
	 */
	public void initialize() {
		// load configuration
		loadSettings();

		//init slider max/min values
		boardSizeSlider.setMax(MAX_BOARD_SIZE);
		boardSizeSlider.setMin(MIN_BOARD_SIZE);

		//set image and slider actions
		player1PieceImg.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			nextPieceIndex(Player.PLAYER1);
			player1PieceImg.setImage(Assets.getInstance().getPieceImg(player1PieceIndex));
			event.consume();
		});
		player2PieceImg.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			nextPieceIndex(Player.PLAYER2);
			player2PieceImg.setImage(Assets.getInstance().getPieceImg(player2PieceIndex));
			event.consume();
		});
		boardSizeSlider.valueProperty().addListener((obs, oldval, newVal) -> {
			boardSizeSlider.setValue(Math.round(newVal.doubleValue()));
			boardSize = (int) boardSizeSlider.getValue();
			boardSizelbl.setText(String.valueOf(boardSize));
		});

		//set button actions
		saveBtn.setOnAction((event -> {
			saveSettings();
			Stage stage = (Stage) saveBtn.getScene().getWindow();
			stage.close();
		}));
		cancelBtn.setOnAction((event -> {
			cancelChanges();
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		}));

		refreshView();
	}

	/**
	 * refresh the view according to loaded settings
	 */
	private void refreshView() {
		// update slider
		boardSizeSlider.setValue(boardSize);
		boardSizelbl.setText(String.valueOf(boardSize));

		//update piece images
		if (player1PieceIndex < Assets.getInstance().getPiecesListSize())
			player1PieceImg.setImage(Assets.getInstance().getPieceImg(player1PieceIndex));
		else
			player1PieceImg.setImage(Assets.getInstance().getPieceImg(DEFAULT_PIECE_P1));
		if (player2PieceIndex < Assets.getInstance().getPiecesListSize())
			player2PieceImg.setImage(Assets.getInstance().getPieceImg(player2PieceIndex));
		else
			player2PieceImg.setImage(Assets.getInstance().getPieceImg(DEFAULT_PIECE_P2));

	}

	/**
	 * load settings from a file
	 * if no settings file was found the default settings will be loaded
	 * @see SettingsLoader
	 */
	private void loadSettings() {
		try {
			SettingsLoader.loadSettings();
			boardSize = SettingsLoader.getSetting("boardSize");
			player1PieceIndex = SettingsLoader.getSetting("p1piece");
			player2PieceIndex = SettingsLoader.getSetting("p2piece");
		} catch (FileNotFoundException e) {
			// settings file not found, load defaults
			boardSize = DEFAULT_BOARD_SIZE;
			player1PieceIndex = DEFAULT_PIECE_P1;
			player2PieceIndex = DEFAULT_PIECE_P2;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * save current user choice to the settings file
	 * @see SettingsLoader
	 */
	private void saveSettings() {
		try {
			SettingsLoader.setSetting("boardSize", boardSize);
			SettingsLoader.setSetting("p1piece", player1PieceIndex);
			SettingsLoader.setSetting("p2piece", player2PieceIndex);
			SettingsLoader.saveSettings();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * cancel made changes by the user. note that this method is publid
	 * to make sure settings go back to previous state if user closes game/window unexpectedly
	 */
	public void cancelChanges() {
		refreshView();
	}

	/**
	 * advance piece image selection by 1.
	 * will show image if it has not been selected yet by the other player
	 * @param player player to advance the picture index of
	 * @see Assets
	 */
	private void nextPieceIndex(Player player) {
		if (player == Player.PLAYER1) {
			player1PieceIndex = Assets.getInstance().getNextIndex(player1PieceIndex);
			if (player1PieceIndex == player2PieceIndex)
				player1PieceIndex = Assets.getInstance().getNextIndex(player1PieceIndex);
		} else {
			player2PieceIndex = Assets.getInstance().getNextIndex(player2PieceIndex);
			if (player1PieceIndex == player2PieceIndex)
				player2PieceIndex = Assets.getInstance().getNextIndex(player2PieceIndex);
		}
	}

	/**
	 * getter for the saved board size
	 * @return wanted size of board for the next game
	 */
	public int getBoardSize() {
		return boardSize;
	}

	/**
	 * get the index of selected piece image by player 1
	 * @return index of that piece in the Assets container
	 * @see Assets
	 */
	public int getPlayer1PieceIndex() {
		return player1PieceIndex;
	}
	/**
	 * get the index of selected piece image by player 2
	 * @return index of that piece in the Assets container
	 * @see Assets
	 */
	public int getPlayer2PieceIndex() {
		return player2PieceIndex;
	}
}
