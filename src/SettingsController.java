import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.IOException;

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

	private final static int MAX_BOARD_SIZE = 20;
	private final static int MIN_BOARD_SIZE = 4;
	private final static int DEFAULT_BOARD_SIZE = 8;
	private final static int DEFAULT_PIECE_P1 = 0;
	private final static int DEFAULT_PIECE_P2 = 1;

	//SETTINGS TO LOAD
	public static int boardSize = DEFAULT_BOARD_SIZE;
	public static int player1PieceIndex = 0;
	public static int player2PieceIndex = 1;

	public void initialize() {
		// load configuration
		loadSettings();

		//init slider
		boardSizeSlider.setMax(MAX_BOARD_SIZE);
		boardSizeSlider.setMin(MIN_BOARD_SIZE);
		boardSizeSlider.setValue(boardSize);
		//init slider label
		boardSizelbl.setText(String.valueOf(boardSize));

		//init images
		if (player1PieceIndex < Assets.getPiecesListSize())
			player1PieceImg.setImage(Assets.getPiecesList().get(player1PieceIndex));
		else
			player1PieceImg.setImage(Assets.getPiecesList().get(DEFAULT_PIECE_P1));
		if (player2PieceIndex < Assets.getPiecesListSize())
			player2PieceImg.setImage(Assets.getPiecesList().get(player2PieceIndex));
		else
			player2PieceImg.setImage(Assets.getPiecesList().get(DEFAULT_PIECE_P2));

		//set image and slider actions
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

		//set button actions
		saveBtn.setOnAction((event -> {
			saveSettings();
			Stage stage = (Stage) saveBtn.getScene().getWindow();
			stage.close();
		}));
		cancelBtn.setOnAction((event -> {
			loadSettings();
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		}));

		//TODO: close window with X fix


	}

	public static void loadSettings() {
		try {
			SettingsLoader.loadSettings();
			boardSize = SettingsLoader.getSetting("boardSize");
			player1PieceIndex = SettingsLoader.getSetting("p1piece");
			player2PieceIndex = SettingsLoader.getSetting("p2piece");
		} catch (FileNotFoundException e) {
			System.out.println("settings file not found");
			boardSize = DEFAULT_BOARD_SIZE;
			player1PieceIndex = DEFAULT_PIECE_P1;
			player2PieceIndex = DEFAULT_PIECE_P2;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
